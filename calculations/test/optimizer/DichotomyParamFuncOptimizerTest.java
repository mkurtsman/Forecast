package optimizer;

import data.DataRowFactory;
import functions.ParamFuncFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Math.abs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class DichotomyParamFuncOptimizerTest {

    /**
     * Correct is 20, but we can get only 25.048828125
     */
    @Test
    public void approximateValueParamTest(){
        var func = ParamFuncFactory.getSimpleSQFunctionOneParam(20.0);
        var row = DataRowFactory.getSQDoubleRow(func);
        var initParams = List.copyOf(func.getParams());

        var ranges = List.of(new Range(-100.0, +100.0));
        var paramEps = 0.05d;
        var eps = 0.00005d;
        var count = 100;

        DichotomyParamFuncOptimizer optimizer = new DichotomyParamFuncOptimizer(func, ranges, paramEps, eps, count, row);
        optimizer.optimize();
        assertEquals(25.048828125, func.getParam(0));
    }

    @Test
    public void approximateValueParamTest1(){
        var func = ParamFuncFactory.getSimpleSQFunctionOneParam(20.0);
        var row = DataRowFactory.getSQDoubleRow(func);

        var ranges = List.of(new Range(-10.0, +10.0));
        var paramEps = 0.05d;
        var eps = 0.00005d;
        var count = 100;

        DichotomyParamFuncOptimizer optimizer = new DichotomyParamFuncOptimizer(func, ranges, paramEps, eps, count, row);
        optimizer.optimize();
        assertEquals(9.9609375, func.getParam(0));
    }

    @Test
    public void optimisationTest(){
        var func = ParamFuncFactory.getSimpleSQFunction(DataRowFactory.sqParams);
        var row =  DataRowFactory.getSQDoubleRow(func);
        var initParams = List.copyOf(func.getParams());
        func.setParam(0, 2.0);
        func.setParam(1, 3.0);
        func.setParam(2, 4.0);

        var ranges = List.of(new Range(-2.0, 4.0), new Range(-1.0, 5.0), new Range(-34.0, 100.1));
        var paramEps = 0.05d;
        var eps = 0.00005d;
        var count = 1000;

        DichotomyParamFuncOptimizer optimizer = new DichotomyParamFuncOptimizer(func, ranges, paramEps, eps, count, row);
        optimizer.optimize();

        assertEquals(-9.9609375, func.getParam(0));
        assertEquals(-9.9609375, func.getParam(1));
        assertEquals(40.380859375 , func.getParam(2));

    }
}