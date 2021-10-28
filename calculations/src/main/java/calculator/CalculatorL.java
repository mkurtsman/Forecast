package calculator;

import functions.LineFunction;
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

public class CalculatorL {

    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);

    private final DoubleRow timeRow;
    private final int extrapolationCount;

    private DoubleRow resultRow;

    private DoubleRow lineOptimizedRow;

    private DoubleRow lineOptimizedRowExtrapolated;
    private DoubleRow resultRowExtrapolated;


    private LineFunction lineFunction;

    private Double error;
    private Double lineError;

    public CalculatorL(DoubleRow timeRow, int extrapolationCount) {
        this.timeRow = timeRow;
        this.extrapolationCount = extrapolationCount;
    }

    public void calculate(){
        // 1. remove trend
        var minLine = min(timeRow);
        var maxLine = max(timeRow);

        lineFunction = new LineFunction((maxLine.subtract(minLine)).divide(TWO) , (maxLine.subtract(minLine)).divide(BigDecimal.valueOf(timeRow.size()), MathContext.DECIMAL128) );

        List<Range> steps = List.of(Range.of(maxLine,minLine) , Range.of(maxLine,minLine));
        Double eps = 0.000001;
        AbstractOptimizer optimizer = new DichotomyParamFuncOptimizer(lineFunction, steps, eps, timeRow);
        optimizer.optimize();

        lineOptimizedRow = apply(timeRow, lineFunction);
        lineError = minSquare(lineOptimizedRow, timeRow).doubleValue();

        // 4. add cyclic and trend
        resultRow =  lineOptimizedRow;
        // 5. calc noise
        error = minSquare(timeRow,resultRow).doubleValue();

        lineOptimizedRowExtrapolated = extrapolate(lineOptimizedRow, extrapolationCount, lineFunction);
        resultRowExtrapolated = lineOptimizedRowExtrapolated;
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
}
