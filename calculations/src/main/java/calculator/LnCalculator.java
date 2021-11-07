package calculator;

import functions.SinFunction;
import optimizer.AbstractOptimizer;
import optimizer.DichotomyParamFuncOptimizer;
import optimizer.Range;
import timerow.DoubleRow;
import timerow.DoubleRowOperations;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.IntStream;

import static timerow.DoubleRowOperations.*;

public class LnCalculator {

    private final DoubleRow timeRow;
    private DoubleRow ln;
    private DoubleRow diff;
    private DoubleRow lnInt;
    private DoubleRow timeRow1;
    private DoubleRow cyclicOptimizedRow;



    public LnCalculator(DoubleRow timeRow, int extrapolationCount) {
        this.timeRow = timeRow;

    }

    public void calculate() {
        ln = exp(timeRow);
        diff = DoubleRowOperations.diff(ln);
        var min = min(diff);
        var max = max(diff);
        var center = min.add(max).divide(TWO);
        var normalizedDiff = divide(subtract(diff, center), max.subtract(min).divide(TWO));

        // 2. remove cyclic
        int sinOrder = 50;

        List<BigDecimal> sinParams = IntStream.range(0, sinOrder).mapToObj(i -> BigDecimal.ONE.divide(BigDecimal.valueOf(sinOrder), MathContext.DECIMAL128)).toList();
        List<Range> sinSteps = IntStream.range(0, sinOrder).mapToObj(i -> Range.of(-1.0, 1.0)).toList();

        var sinFunction = new SinFunction(normalizedDiff.size()*2, sinParams);

        Double sinEps = 0.0000001;
        AbstractOptimizer cyclicOptimizer = new DichotomyParamFuncOptimizer(sinFunction, sinSteps, sinEps, normalizedDiff);
        cyclicOptimizer.optimize();
        cyclicOptimizedRow = apply(normalizedDiff,sinFunction);
        cyclicOptimizedRow = extrapolate(cyclicOptimizedRow, 3, sinFunction);
        cyclicOptimizedRow = add(mutiply(cyclicOptimizedRow, max.subtract(min).divide(TWO)), center);

        ///////////////

        lnInt = DoubleRowOperations.interg(ln, cyclicOptimizedRow);
        timeRow1 = ln(lnInt);
    }

    public DoubleRow getLn() {
        return ln;
    }

    public DoubleRow getDiff() {
        return diff;
    }

    public DoubleRow getTimeRow1() {
        return timeRow1;
    }

    public DoubleRow getTimeRow() {
        return timeRow;
    }

    public DoubleRow getCyclicOptimizedRow() {
        return cyclicOptimizedRow;
    }

    public DoubleRow getLnInt() {
        return lnInt;
    }
}
