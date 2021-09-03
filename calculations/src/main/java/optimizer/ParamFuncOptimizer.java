package optimizer;

import functions.ParametricFunction;
import timerow.TimeRow;

import java.util.ArrayList;
import java.util.List;

public class ParamFuncOptimizer<X, Y extends Number> {
    private ParametricFunction<X, Y> function;
    private List<Double> initSteps;

    private TimeRow<X, Y> timeRow;

    private Double eps;

    public ParamFuncOptimizer(ParametricFunction<X, Y> function, List<Double> initSteps, Double eps, TimeRow<X, Y> timeRow) {
        this.function = function;
        this.initSteps = new ArrayList<>(initSteps);
        this.timeRow = timeRow;
        this.eps = eps;
    }

    public ParametricFunction<X, Y> optimize() {
        Double error = timeRow.minSquare(function);
        Double prewError = Double.MAX_VALUE;
        int cnt = 0;
        while (prewError - error > eps) {
            for (int i = 0; i < function.getParamsCount(); i++) {
                optimizeByParam(i, function, 0);
            }
            prewError = error;
            error = timeRow.minSquare(function);
            System.out.println("Itteration count: " + cnt++ + " error " + error +" "+function.paramsString());
        }
        System.out.println(function.paramsString());
        return function;
    }

    private void optimizeByParam(int i, ParametricFunction<X, Y> function, int req) {
        if(req > 100){
            return;
        }

        Double step = initSteps.get(i);
        Double initVal = function.getParam(i);

        Double v0 = timeRow.minSquare(function);

        function.setParam(i, initVal + step);
        Double v1 = timeRow.minSquare(function);

        function.setParam(i, initVal - step);
        Double v2 = timeRow.minSquare(function);

        if (v1 > v0 && v0 > v2) {
            // function.setParam(i, initVal - step);
        } else if (v1 < v0 && v0 < v2) {
            function.setParam(i, initVal + step);
        } else {
            function.setParam(i, initVal);
            initSteps.set(i, step / 2.0);
            optimizeByParam(i, function, req+1);
        }
    }
}


