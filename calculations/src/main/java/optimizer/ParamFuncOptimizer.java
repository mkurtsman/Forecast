package optimizer;

import functions.ParametricFunction;
import timerow.DoubleRow;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class ParamFuncOptimizer {
    private ParametricFunction function;
    private List<Double> initSteps;

    private DoubleRow timeRow;

    private Double eps;

    public ParamFuncOptimizer(ParametricFunction function, List<Double> initSteps, Double eps, DoubleRow timeRow) {
        this.function = function;
        this.initSteps = new ArrayList<>(initSteps);
        this.timeRow = timeRow;
        this.eps = eps;
    }

    public void optimize() {
        Double error = timeRow.minSquare(function);
        Double prewError = Double.MAX_VALUE;
        int cnt = 0;
        while (abs(prewError - error) > eps) {
            for (int i = 0; i < function.getParamsCount(); i++) {
                optimizeByParam(i, function, 0);
            }
            prewError = error;
            error = timeRow.minSquare(function);
            System.out.println("Itteration count: " + cnt++ + " error " + error +" "+function.paramsString());
        }
        System.out.println(function.paramsString());
    }

    private void optimizeByParam(int i, ParametricFunction function, int req) {
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


