package functions;

import functions.ParametricFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ParamFuncFactory {

    public static ParametricFunction getSimpleSQFunction(List<BigDecimal> params){

        var func = new ParametricFunction(params) {

            @Override
            public int from() {
                return 0;
            }

            @Override
            public BigDecimal apply(Integer x) {
                var params = getParams();
                var s1 = params.get(0);
                var s2 = params.get(1).multiply(BigDecimal.valueOf(x).subtract(BigDecimal.valueOf(30.)));
                var s3 = params.get(2).multiply(BigDecimal.valueOf(x).subtract(BigDecimal.valueOf(50.)).pow(2));
                return s1.add(s2).add(s3);
            }
        };

        for(int i = 0; i < params.size(); i++){
            func.setParam(i, params.get(i));
        }

        return func;
    }


    public static ParametricFunction getSimpleSQFunctionOneParam(BigDecimal param){

        var func = new ParametricFunction(List.of(param)) {

            @Override
            public int from() {
                return 0;
            }

            @Override
            public BigDecimal apply(Integer x) {
                var params = getParams();
                return BigDecimal.valueOf(x).subtract(params.get(0)).pow(2);
            }
        };

        return func;
    }
}