package optimizer;

import functions.ParametricFunction;
import timerow.DoubleRow;

import java.text.MessageFormat;
import java.util.List;

import static timerow.DoubleRowOperations.minSquare;

public class StepFuncOptimizer implements AbstractOptimizer {

    protected static MessageFormat mf = new MessageFormat("iterrations {0}, error {1}, func {2}");
    private List<Double> steps;
    protected ParametricFunction function;
    protected DoubleRow timeRow;
    protected Double paramEps;
    protected Double eps;
    protected Integer maxCount;
    protected Integer stepCount;

    public StepFuncOptimizer(ParametricFunction function, List<Double> steps, Double paramEps, Double eps, Integer maxCount, Integer stepCount, DoubleRow timeRow) {
        this.steps = steps;
        this.function = function;
        this.timeRow = timeRow;
        this.paramEps = paramEps;
        this.eps = eps;
        this.maxCount = maxCount;
        this.stepCount = stepCount;
    }

    @Override
    public void optimize(){
        var count = 0;
        var error = Double.MAX_VALUE;
        while (count < maxCount && error > eps) {
            for (int i = 0; i < function.getParamsCount(); i++) {
                 optimizeByParam(i, function, 0);
            }
            error = minSquare(timeRow, function);
            count++;
            System.out.println(mf.format(new Object[]{count, error, function}));
        }
    }

    private void optimizeByParam(int i, ParametricFunction function, int req) {
        if(req > stepCount){
            return;
        }

        Double step = steps.get(i);
        Double param = function.getParam(i);

        Double v = minSquare(timeRow, function);

        function.setParam(i, param + step);
        Double v1 = minSquare(timeRow, function);

        function.setParam(i, param - step);
        Double v2 = minSquare(timeRow, function);

        System.out.println("(" + (param + step) + ";" + (param - step) + ")");
        System.out.println("(" + v1 + ";" + v + ";" + v2);

        if (v1 > v && v > v2) {
            function.setParam(i, param - step);
        } else if (v2 > v && v > v1){
            function.setParam(i, param + step);
        } else {

            function.setParam(i, param);
            steps.set(i, step / 2);
            optimizeByParam(i, function, req +1);
        }

    }
}
