package calculator;

import functions.SinFunction;
import optimizer.AbstractOptimizer;
import optimizer.DichotomyParamFuncOptimizer;
import optimizer.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timerow.DoubleRow;
import timerow.DoubleRowOperations;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static timerow.DoubleRowOperations.*;

public class LnCalculator {

    private static Logger log = LoggerFactory.getLogger(LnCalculator.class);

    private final DoubleRow timeRow;
    private DoubleRow ln;
    private DoubleRow diff;
    private DoubleRow lnInt;
    private DoubleRow timeRow1;
    private DoubleRow cyclicOptimizedRow;
    private int sinOrder; // 50 optimal
    private double error;
    private double phase; // normalizedDiff.size()*2

    public LnCalculator(DoubleRow timeRow) {
        this(timeRow, 5, 2.1);
    }

    public LnCalculator(DoubleRow timeRow, int sinOrder, double phase) {
        this.timeRow = timeRow;
        this.sinOrder = sinOrder;
        this.phase = phase;

    }

    public void calculate() {
        ln = ln(timeRow);
        diff = DoubleRowOperations.diff(ln);
        var min = min(diff);
        var max = max(diff);
        var center = min.add(max).divide(TWO);
        var normalizedDiff = divide(subtract(diff, center), max.subtract(min).divide(TWO));

        // 2. remove cyclic

        List<BigDecimal> sinParams = IntStream.range(0, sinOrder).mapToObj(i -> BigDecimal.ONE.divide(BigDecimal.valueOf(sinOrder), MathContext.DECIMAL128)).toList();
        List<Range> sinSteps = IntStream.range(0, sinOrder).mapToObj(i -> Range.of(-1.0, 1.0)).toList();

        var sinFunction = new SinFunction((int) (normalizedDiff.size()*phase), sinParams);

        Double sinEps = 0.0000001;
        AbstractOptimizer cyclicOptimizer = new DichotomyParamFuncOptimizer(sinFunction, sinSteps, sinEps, normalizedDiff);
        cyclicOptimizer.optimize();
        cyclicOptimizedRow = apply(normalizedDiff,sinFunction);
        cyclicOptimizedRow = extrapolate(cyclicOptimizedRow, 2, sinFunction);
        cyclicOptimizedRow = add(mutiply(cyclicOptimizedRow, max.subtract(min).divide(TWO)), center);

        ///////////////

        lnInt = DoubleRowOperations.interg(ln, cyclicOptimizedRow);
        timeRow1 = exp(lnInt);
        error = minSquare(subRow(timeRow, 1), timeRow1).doubleValue();

        var idx =  timeRow1.maxX();
        BigDecimal expected = timeRow1.get(idx).add(cyclicOptimizedRow.get(idx + 1));
        timeRow1.addPoints(Map.of(idx +1 , expected));
        log.info(" expected value1 {}", expected);

        idx++;
        expected = timeRow1.get(idx).add(cyclicOptimizedRow.get(idx + 1));
        timeRow1.addPoints(Map.of(idx +1 , expected));
        log.info(" expected value2 {}", expected);


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

    public double getError() {
        return error;
    }
}
