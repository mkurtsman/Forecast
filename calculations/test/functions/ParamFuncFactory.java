package functions;

import functions.ParametricFunction;

import java.util.ArrayList;
import java.util.List;


public class ParamFuncFactory {

    public static ParametricFunction getSimpleSQFunction(List<Double> params){

        var func = new ParametricFunction(params) {

            @Override
            public int from() {
                return 0;
            }

            @Override
            public Double apply(Integer x) {
                var params = getParams();
                return params.get(0) + (x * params.get(1)) + (x - params.get(2)) * (x - params.get(2));
            }
        };

        for(int i = 0; i < params.size(); i++){
            func.setParam(i, params.get(i));
        }

        return func;
    }


    public static ParametricFunction getSimpleSQFunctionOneParam(Double param){

        var func = new ParametricFunction(List.of(param)) {

            @Override
            public int from() {
                return 0;
            }

            @Override
            public Double apply(Integer x) {
                var params = getParams();
                return (x - params.get(0)) * (x - params.get(0));
            }
        };

        return func;
    }
}