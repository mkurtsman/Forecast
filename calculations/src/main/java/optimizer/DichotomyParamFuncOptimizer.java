package optimizer;

import functions.ParametricFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timerow.DoubleRow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.List;

import static timerow.DoubleRowOperations.minSquare;


public class DichotomyParamFuncOptimizer implements AbstractOptimizer {

    private static Logger logger = LoggerFactory.getLogger(DichotomyParamFuncOptimizer.class);

    public static final BigDecimal DIVISOR_2 = BigDecimal.valueOf(2);
    protected List<Range> ranges;
    protected static MessageFormat mf = new MessageFormat("iterrations {0}, error {1}, func {2}");
    protected ParametricFunction function;
    protected DoubleRow timeRow;
    protected Double paramEps;

    public DichotomyParamFuncOptimizer(ParametricFunction function, List<Range> initRanges, Double paramEps, DoubleRow timeRow) {
        this.ranges = initRanges;
        this.function = function;
        this.timeRow = timeRow;
        this.paramEps = paramEps;
    }

    public void optimize() {
        var error = Double.MAX_VALUE;
        for (int i = 0; i < function.getParamsCount(); i++) {
            optimizeByParam(i);
        }
        error = minSquare(timeRow, function).doubleValue();
        logger.info("params {} error {}", function.toString(), error);

    }

    private void optimizeByParam(int i) {

        Range range = ranges.get(i);
        var a = range.first;
        var b = range.second;
        var sigma = BigDecimal.valueOf(paramEps).divide(BigDecimal.valueOf(3), MathContext.DECIMAL128);

        while (b.subtract(a).abs().compareTo(BigDecimal.valueOf(paramEps)) > 0) {

            var x1 = a.add(b).subtract(sigma).divide(DIVISOR_2);
            var x2 = a.add(b).add(sigma).divide(DIVISOR_2);

            function.setParam(i, x1);
            BigDecimal v1 = minSquare(timeRow, function);

            function.setParam(i, x2);
            BigDecimal v2 = minSquare(timeRow, function);

            if (v1.compareTo(v2) < 0) {
                b = x2;
            } else {
                a = x1;
            }

            logger.debug("param{} f(x1) = {} {} f(x2) = {} {}, ", i, v1, x1, v2, x2);
            logger.debug("param{} a = {} b = {}, ", i, a, b);
        }

        function.setParam(i, a.add(b).divide(DIVISOR_2));
    }
}


