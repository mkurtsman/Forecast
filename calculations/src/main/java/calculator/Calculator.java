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
    private DoubleRow maptimizedRowExtrapolated;
    private DoubleRow resultRowExtrapolated;


    private LineFunction lineFunction;
    private SinFunction sinFunction;
    private MovingAverageFunction movingAverageFunction;

    private Double error;
    private BigDecimal lineError;
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

        lineFunction = new LineFunction((maxLine.subtract(minLine)).divide(TWO) , (maxLine.subtract(minLine)).divide(BigDecimal.valueOf(timeRow.size()), MathContext.DECIMAL64) );

        List<Range> steps = List.of(Range.of(maxLine,minLine) , Range.of(maxLine,minLine));
        Double eps = 0.0001;
        AbstractOptimizer optimizer = new DichotomyParamFuncOptimizer(lineFunction, steps, eps, timeRow);
        optimizer.optimize();

        lineOptimizedRow = apply(timeRow, lineFunction);
        lineError = minSquare(lineOptimizedRow, timeRow);

        // 2. remove cyclic
        cyclic = timeRow.subtract(lineOptimizedRow);
        Double cyclicDivider = cyclic.maxAbs();
        DoubleRow normalizedCyclic = cyclic.divide(cyclicDivider);
        int sinOrder = 2;
        List<Double> sinParams = IntStream.range(0, sinOrder).mapToDouble(i -> ((double) i)/sinOrder).boxed().toList();
        List<Double> sinSteps = IntStream.range(0, sinOrder).mapToDouble(i -> (1.0/normalizedCyclic.size())).boxed().toList();
        sinFunction = new SinFunction(normalizedCyclic.size()/2, sinParams);
        Double sinEps = 0.000001;
        ParamFuncOptimizer cyclicOptimizer = new ParamFuncOptimizer(sinFunction, sinSteps, sinEps, normalizedCyclic);
        cyclicOptimizer.optimize();
        cyclicOptimizedRow = normalizedCyclic.apply(sinFunction);
        sinError = cyclic.subtract(cyclicOptimizedRow).epsSquare() * cyclicDivider;

        // 3. remove MA

        maRow = normalizedCyclic.subtract(cyclicOptimizedRow);
        Double maDivider = maRow.maxAbs();
        DoubleRow normalizedMaRow = maRow.divide(maDivider);
        int maOrder = 20;
        List<Double> maParams = IntStream.range(0, sinOrder).mapToDouble(i -> ((double) i)/maOrder).boxed().toList();
        List<Double> maSteps = IntStream.range(0, sinOrder).mapToDouble(i -> (1.0)).boxed().toList();
        movingAverageFunction = new MovingAverageFunction(normalizedMaRow, maParams);

        Double maEps = 0.0000001;
        ParamFuncOptimizer maOptimizer = new ParamFuncOptimizer(movingAverageFunction, maSteps, maEps, normalizedMaRow);
        maOptimizer.optimize();
        maOptimizedRow = normalizedMaRow.apply(movingAverageFunction);
        maError = maRow.subtract(maOptimizedRow).epsSquare() * maDivider * cyclicDivider;

        // 4. add cyclic and trend
        resultRow = lineOptimizedRow.add(cyclicOptimizedRow/*.add(maOptimizedRow.mutiply(maDivider)*/.mutiply(cyclicDivider));
        // 5. calc noise
        error = timeRow.subtract(resultRow).epsSquare();

        lineOptimizedRowExtrapolated = lineOptimizedRow.extrapolate(extrapolationCount, lineFunction);
         cyclicOptimizedRowExtrapolated = cyclicOptimizedRow.extrapolate(extrapolationCount, sinFunction);

//        MovingAverageFunction f = new MovingAverageFunction(maOptimizedRow.extrapolateWithEmpty(extrapolationCount), movingAverageFunction.getParams());
//        maOptimizedRowExtrapolated = maOptimizedRow.extrapolate(extrapolationCount, f);
        resultRowExtrapolated = lineOptimizedRowExtrapolated.add(cyclicOptimizedRowExtrapolated/*.add(maOptimizedRowExtrapolated.mutiply(maDivider)*/.mutiply(cyclicDivider));


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
        return maOptimizedRowExtrapolated;
    }

    public DoubleRow getResultRowExtrapolated() {
        return resultRowExtrapolated;
    }
}
