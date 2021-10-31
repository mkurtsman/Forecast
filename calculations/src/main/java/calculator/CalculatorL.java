package calculator;

import functions.LineFunction;
import functions.SinFunction;
import optimizer.AbstractOptimizer;
import optimizer.DichotomyParamFuncOptimizer;
import optimizer.Range;
import optimizer.SimpleParamFuncOptimizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timerow.DoubleRow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.IntStream;

import static timerow.DoubleRowOperations.*;

public class CalculatorL {

    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);

    private final DoubleRow timeRow;
    private final int extrapolationCount;

    private DoubleRow resultRow;

    private DoubleRow lineOptimizedRow;
    private DoubleRow lineOptimizedRowExtrapolated;
    private DoubleRow resultRowExtrapolated;

    private DoubleRow cyclic;
    private DoubleRow cyclicOptimizedRow;
    private DoubleRow cyclicOptimizedRowExtrapolated;

    private SinFunction sinFunction;

    private LineFunction lineFunction;

    private Double error;
    private Double lineError;
    private Double sinError;

    public CalculatorL(DoubleRow timeRow, int extrapolationCount) {
        this.timeRow = timeRow;
        this.extrapolationCount = extrapolationCount;
    }

    public void calculate(){
        // 1. remove trend
        var minLine = min(timeRow);
        var maxLine = max(timeRow);
        var center = (maxLine.add(minLine)).divide(TWO);
        var length = maxLine.subtract(minLine);

        lineFunction = new LineFunction( center, length.divide(BigDecimal.valueOf(timeRow.size()), MathContext.DECIMAL128) );

        var range = Range.of(BigDecimal.valueOf(-10), BigDecimal.valueOf(10));

        List<Range> steps = List.of(range, range);
        Double eps = 0.00000001;
        AbstractOptimizer optimizer = new DichotomyParamFuncOptimizer(lineFunction, steps, eps, timeRow);
//        AbstractOptimizer optimizer = new SimpleParamFuncOptimizer(lineFunction, steps, 1000000, timeRow);
        optimizer.optimize();

        lineOptimizedRow = apply(timeRow, lineFunction);
        lineError = minSquare(lineOptimizedRow, timeRow).doubleValue();
// 2. remove cyclic
        cyclic = subtract(timeRow,lineOptimizedRow);
        BigDecimal cyclicDivider =  maxAbs(cyclic);
        DoubleRow normalizedCyclic = divide(cyclic,cyclicDivider);
        int sinOrder = 5;

        List<BigDecimal> sinParams = IntStream.range(0, sinOrder).mapToObj(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(sinOrder), MathContext.DECIMAL128)).toList();
        List<Range> sinSteps = IntStream.range(0, sinOrder).mapToObj(i -> Range.of(-10.0, 10.0)).toList();

        sinFunction = new SinFunction(normalizedCyclic.size()*3, sinParams);

        Double sinEps = 0.0000001;
        AbstractOptimizer cyclicOptimizer = new DichotomyParamFuncOptimizer(sinFunction, sinSteps, sinEps, normalizedCyclic);
        cyclicOptimizer.optimize();
        cyclicOptimizedRow = apply(normalizedCyclic,sinFunction);

        sinError = minSquare(normalizedCyclic,cyclicOptimizedRow).multiply(cyclicDivider).doubleValue();

        // 4. add cyclic and trend
        // 4. add cyclic and trend
        cyclicOptimizedRowExtrapolated = extrapolate(cyclicOptimizedRow, 5, sinFunction);
        lineOptimizedRowExtrapolated = extrapolate(lineOptimizedRow, 5, lineFunction);

        resultRow = add(mutiply(cyclicOptimizedRow,cyclicDivider), lineOptimizedRow);
        // 5. calc noise

        error = minSquare(timeRow,resultRow).doubleValue();
        logger.info("error = {}", error);


        resultRowExtrapolated = add(mutiply(cyclicOptimizedRowExtrapolated,cyclicDivider), lineOptimizedRowExtrapolated);
    }

    public DoubleRow getResultRow() {
        return resultRow;
    }

    public Double getError() {
        return error;
    }
    public DoubleRow getTimeRow() {
        return timeRow;
    }

    public DoubleRow getLineOptimizedRow() {
        return lineOptimizedRow;
    }

    public LineFunction getLineFunction() {
        return lineFunction;
    }

    public Double getLineError() {
        return lineError;
    }

    public DoubleRow getLineOptimizedRowExtrapolated() {
        return lineOptimizedRowExtrapolated;
    }

    public DoubleRow getResultRowExtrapolated() {
        return resultRowExtrapolated;
    }

    public DoubleRow getCyclic() {
        return cyclic;
    }

    public DoubleRow getCyclicOptimizedRowExtrapolated() {
        return cyclicOptimizedRowExtrapolated;
    }

    public DoubleRow getCyclicOptimizedRow() {
        return cyclicOptimizedRow;
    }
}
