package optimizer;

import functions.ParametricFunction;
import timerow.DoubleRow;

import java.util.List;

import static timerow.DoubleRowOperations.minSquare;

public class StepFuncOptimizer extends AbstractOptimizer {

    private List<Double> steps;

    public StepFuncOptimizer(ParametricFunction function, List<Double> steps, Double dichotomyEps, Double eps, Integer maxCount, DoubleRow timeRow) {
        super(function, dichotomyEps, eps, maxCount, timeRow);
        this.steps = steps;
    }

    @Override
    public void optimize(){
        var count = 0;
        var error = Double.MAX_VALUE;
        while (count < maxCount && error > eps) {
            for (int i = 0; i < function.getParamsCount(); i++) {
                 optimizeByParam(i, function);
            }
            error = minSquare(timeRow, function);
            count++;
            System.out.println(mf.format(new Object[]{count, error, function}));
        }
    }

    @Override
    protected void optimizeByParam(int i, ParametricFunction function) {

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
        }

    }
}
