package calculator;

import functions.LineFunction;
import functions.MovingAverageFunction;
import functions.SinFunction;
import optimizer.AbstractOptimizer;
import optimizer.DichotomyParamFuncOptimizer;
import optimizer.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timerow.DoubleRow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.IntStream;

import static timerow.DoubleRowOperations.*;

public class Calculator{

    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);

    private final DoubleRow timeRow;
    private final int extrapolationCount;

    private DoubleRow resultRow;

    private DoubleRow lineOptimizedRow;
    private DoubleRow cyclic;
    private DoubleRow cyclicOptimizedRow;
    private DoubleRow maRow;
    private DoubleRow maOptimizedRow;

    private DoubleRow lineOptimizedRowExtrapolated;
    private DoubleRow cyclicOptimizedRowExtrapolated;
    private DoubleRow maOtimizedRowExtrapolated;
    private DoubleRow resultRowExtrapolated;


    private LineFunction lineFunction;
    private SinFunction sinFunction;
    private MovingAverageFunction movingAverageFunction;

    private Double error;
    private Double lineError;
    private Double sinError;
    private Double maError;

    public Calculator(DoubleRow timeRow, int extrapolationCount) {
        this.timeRow = timeRow;
        this.extrapolationCount = extrapolationCount;
    }

    public void calculate(){
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

        // 3. remove MA

        maRow = subtract(normalizedCyclic, cyclicOptimizedRow);
        BigDecimal maDivider = maxAbs(maRow);
        DoubleRow normalizedMaRow = divide(maRow,maDivider);
        int maOrder = 10;
        List<BigDecimal> maParams = IntStream.range(0, maOrder).mapToObj(i -> BigDecimal.valueOf (i).divide(BigDecimal.valueOf(maOrder))).toList();
        List<Range> maSteps = IntStream.range(0, maOrder).mapToObj(i -> Range.ofSizeOne()).toList();

        movingAverageFunction = new MovingAverageFunction(normalizedMaRow, maParams);

        Double maEps = 0.0000001;
        AbstractOptimizer maOptimizer = new DichotomyParamFuncOptimizer(movingAverageFunction, maSteps, maEps, normalizedMaRow);
        maOptimizer.optimize();
        maOptimizedRow = apply(normalizedMaRow, movingAverageFunction);
        maError = minSquare(maRow,maOptimizedRow).multiply(maDivider).multiply(cyclicDivider).doubleValue();

        // 4. add cyclic and trend
        var cyclicAndMa = add(mutiply(maOptimizedRow,maDivider), cyclic);
        resultRow = add(mutiply(cyclicAndMa,cyclicDivider), lineOptimizedRow);
        // 5. calc noise
        error = minSquare(timeRow,resultRow).doubleValue();

        lineOptimizedRowExtrapolated = extrapolate(lineOptimizedRow, extrapolationCount, lineFunction);
        cyclicOptimizedRowExtrapolated = extrapolate(cyclicOptimizedRow, extrapolationCount, sinFunction);
        maOtimizedRowExtrapolated = extrapolate(cyclicOptimizedRow, extrapolationCount, movingAverageFunction);

        var cyclicAndMaExtrapolated = add(mutiply(maOtimizedRowExtrapolated,maDivider), cyclicOptimizedRowExtrapolated);
        resultRowExtrapolated = add(mutiply(cyclicAndMaExtrapolated,cyclicDivider), lineOptimizedRowExtrapolated);
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

    public DoubleRow getMaRow() {
        return maRow;
    }

    public DoubleRow getMaOptimizedRow() {
        return maOptimizedRow;
    }

    public LineFunction getLineFunction() {
        return lineFunction;
    }

    public SinFunction getSinFunction() {
        return sinFunction;
    }

    public MovingAverageFunction getMovingAverageFunction() {
        return movingAverageFunction;
    }

    public Double getLineError() {
        return lineError;
    }

    public Double getSinError() {
        return sinError;
    }

    public Double getMaError() {
        return maError;
    }

    public DoubleRow getLineOptimizedRowExtrapolated() {
        return lineOptimizedRowExtrapolated;
    }

    public DoubleRow getCyclicOptimizedRowExtrapolated() {
        return cyclicOptimizedRowExtrapolated;
    }

    public DoubleRow getMaOptimizedRowExtrapolated() {
        return maOtimizedRowExtrapolated;
    }

    public DoubleRow getResultRowExtrapolated() {
        return resultRowExtrapolated;
    }
}
