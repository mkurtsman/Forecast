package optimizer;

import functions.ParametricFunction;
import timerow.DoubleRow;

import java.text.MessageFormat;
import java.util.List;

import static timerow.DoubleRowOperations.minSquare;


public class DichotomyParamFuncOptimizer implements AbstractOptimizer {

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
        while (count < maxCount && error > eps) {
            for (int i = 0; i < function.getParamsCount(); i++) {
                while (ranges.get(i).getSize() > paramEps) {
                    optimizeByParam(i, function);
                }
            }
            error = minSquare(timeRow, function);
            count++;
            System.out.println(mf.format(new Object[]{count, error, function}));
        }
    }

    private void optimizeByParam(int i, ParametricFunction function) {

        Range range = ranges.get(i);

        var center = range.getCenter();

        function.setParam(i, range.getFirst());
        Double v1 = minSquare(timeRow, function);

        function.setParam(i, range.getSecond());
        Double v2 = minSquare(timeRow, function);

        System.out.println("(" + range.getFirst() + ";" + range.getSecond() + ")");
        System.out.println("(" + v1 + ";" + v2);

        if (v1 > v2) {
            range.setFirst(center);
        } else {
            range.setSecond(center);
        }

        System.out.println("--(" + range.getFirst() + ";" + range.getSecond() + ")");

        function.setParam(i, center);

    }
}


