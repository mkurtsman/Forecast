package calculator;

import main.java.functions.LineFunction;
import main.java.functions.MovingAverageFunction;
import main.java.functions.SinFunction;
import main.java.optimizer.AbstractOptimizer;
import main.java.optimizer.DichotomyParamFuncOptimizer;
import main.java.optimizer.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import main.java.timerow.DoubleRow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.IntStream;

import static main.java.timerow.DoubleRowOperations.*;
import static main.java.timerow.DoubleRowOperations.mutiply;

public class CalculatorLS {

    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);

    private final DoubleRow timeRow;
    private final int extrapolationCount;

    private DoubleRow resultRow;

    private DoubleRow ln;

    private DoubleRow lineOptimizedRow;
    private DoubleRow cyclic;
    private DoubleRow cyclicOptimizedRow;

    private DoubleRow lineOptimizedRowExtrapolated;
    private DoubleRow cyclicOptimizedRowExtrapolated;
    private DoubleRow resultRowExtrapolated;


    private LineFunction lineFunction;
    private SinFunction sinFunction;

    private Double error;
    private Double lineError;
    private Double sinError;

    public CalculatorLS(DoubleRow timeRow, int extrapolationCount) {
        this.timeRow = timeRow;
        this.extrapolationCount = extrapolationCount;
    }

    public void calculate(){

        ln = ln(timeRow);

        // 1. remove trend
        var minLine = min(timeRow);
        var maxLine = max(timeRow);

        lineFunction = new LineFunction((maxLine.subtract(minLine)).divide(TWO) , (maxLine.subtract(minLine)).divide(BigDecimal.valueOf(timeRow.size()), MathContext.DECIMAL128) );

        List<Range> steps = List.of(Range.of(maxLine,minLine) , Range.of(maxLine,minLine));
        Double eps = 0.0001;
        AbstractOptimizer optimizer = new DichotomyParamFuncOptimizer(lineFunction, steps, eps, timeRow);
        optimizer.optimize();

        lineOptimizedRow = apply(timeRow, lineFunction);
        lineError = minSquare(lineOptimizedRow, timeRow).doubleValue();

        // 2. remove cyclic
        cyclic = subtract(timeRow,lineOptimizedRow);
        BigDecimal cyclicDivider =  maxAbs(cyclic);
        DoubleRow normalizedCyclic = divide(cyclic,cyclicDivider);
        int sinOrder = 2;

        List<BigDecimal> sinParams = IntStream.range(0, sinOrder).mapToObj(i -> BigDecimal.valueOf(i).divide(BigDecimal.valueOf(sinOrder), MathContext.DECIMAL128)).toList();
        List<Range> sinSteps = IntStream.range(0, sinOrder).mapToObj(i -> Range.ofSizeOne(sinOrder)).toList();

        sinFunction = new SinFunction(normalizedCyclic.size()/2, sinParams);

        Double sinEps = 0.000001;
        AbstractOptimizer cyclicOptimizer = new DichotomyParamFuncOptimizer(sinFunction, sinSteps, sinEps, normalizedCyclic);
        cyclicOptimizer.optimize();
        cyclicOptimizedRow = apply(normalizedCyclic,sinFunction);

        sinError = minSquare(normalizedCyclic,cyclicOptimizedRow).multiply(cyclicDivider).doubleValue();


        // 4. add cyclic and trend
        resultRow = add(mutiply(cyclicOptimizedRow,cyclicDivider), lineOptimizedRow);
        // 5. calc noise
        error = minSquare(timeRow,resultRow).doubleValue();

        lineOptimizedRowExtrapolated = extrapolate(lineOptimizedRow, extrapolationCount, lineFunction);
        cyclicOptimizedRowExtrapolated = extrapolate(cyclicOptimizedRow, extrapolationCount, sinFunction);
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

    public DoubleRow getCyclic() {
        return cyclic;
    }

    public DoubleRow getCyclicOptimizedRow() {
        return cyclicOptimizedRow;
    }

    public LineFunction getLineFunction() {
        return lineFunction;
    }

    public SinFunction getSinFunction() {
        return sinFunction;
    }

    public Double getLineError() {
        return lineError;
    }

    public Double getSinError() {
        return sinError;
    }

    public DoubleRow getLineOptimizedRowExtrapolated() {
        return lineOptimizedRowExtrapolated;
    }

    public DoubleRow getCyclicOptimizedRowExtrapolated() {
        return cyclicOptimizedRowExtrapolated;
    }

    public DoubleRow getResultRowExtrapolated() {
        return resultRowExtrapolated;
    }

    public DoubleRow getLn() {
        return ln;
    }
}
