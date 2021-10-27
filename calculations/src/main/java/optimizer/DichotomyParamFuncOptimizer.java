package optimizer;

import functions.ParametricFunction;
import timerow.DoubleRow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;

import static java.lang.Math.abs;
import static timerow.DoubleRowOperations.minSquare;


public class DichotomyParamFuncOptimizer implements AbstractOptimizer {

    public static final BigDecimal DIVISOR_2 = BigDecimal.valueOf(2);
    protected List<Range> ranges;
    protected static MessageFormat mf = new MessageFormat("iterrations {0}, error {1}, func {2}");
    protected ParametricFunction function;
    protected DoubleRow timeRow;
    protected Double paramEps;
    protected Double eps;
    protected Integer maxCount;

    public DichotomyParamFuncOptimizer(ParametricFunction function, List<Range> initRanges, Double paramEps, Double eps, Integer maxCount, DoubleRow timeRow) {
        this.ranges = initRanges;
        this.function = function;
        this.timeRow = timeRow;
        this.paramEps = paramEps;
        this.eps = eps;
        this.maxCount = maxCount;
    }

    public void optimize(){
        var count = 0;
        var error = Double.MAX_VALUE;
        while (error > eps) {
            for (int i = function.getParamsCount() - 1; i >= 0; i--) {
                optimizeByParam(i);
            }
            error = minSquare(timeRow, function).doubleValue();
            count++;
            System.out.println(mf.format(new Object[]{count, error, function}));
        }
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

            System.out.println("minsq: " + v1  + ";" + v2 + ")");
        }

        function.setParam(i, a.add(b).divide(DIVISOR_2));
        System.out.println("param" + i + "=" + function.getParam(i) + ")");
    }
}


