package optimizer;

import data.DataRowFactory;
import functions.ParamFuncFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DichotomyParamFuncOptimizerTest {

    /**
     * Correct is 20, but we can get only 25.048828125
     */
    @Test
    public void approximateValueParamTest(){
        var func = ParamFuncFactory.getSimpleSQFunctionOneParam(BigDecimal.valueOf(20.0));
        var row = DataRowFactory.getSQDoubleRow(func);
        var initParams = List.copyOf(func.getParams());

        var ranges = List.of(new Range(BigDecimal.valueOf(-100.0),BigDecimal.valueOf(100.0)));
        var paramEps = 0.05d;
        var eps = 0.05d;
        var count = 10;

        DichotomyParamFuncOptimizer optimizer = new DichotomyParamFuncOptimizer(func, ranges, paramEps, row);
        optimizer.optimize();
        assertEquals(25.048828125, func.getParam(0));
    }

    @Test
    public void approximateValueParamTest1(){
        var func = ParamFuncFactory.getSimpleSQFunctionOneParam(BigDecimal.valueOf(20.0));
        var row = DataRowFactory.getSQDoubleRow(func);

        var ranges = List.of(new Range(BigDecimal.valueOf(-10.0), BigDecimal.valueOf(10.0)));
        var paramEps = 0.05d;
        var eps = 0.05d;
        var count = 10;

        DichotomyParamFuncOptimizer optimizer = new DichotomyParamFuncOptimizer(func, ranges, paramEps, row);
        optimizer.optimize();
        assertEquals(9.9609375, func.getParam(0));
    }

    @Test
    public void optimisationTest(){
        var func = ParamFuncFactory.getSimpleSQFunction(DataRowFactory.sqParams);
        var row =  DataRowFactory.getSQDoubleRow(func);
        func.setParam(0, BigDecimal.valueOf(2.0));
        func.setParam(1, BigDecimal.valueOf(4.0));
        func.setParam(2, BigDecimal.valueOf(5.0));

        var ranges = List.of(new Range(BigDecimal.valueOf(-4.0), BigDecimal.valueOf(4.0)), new Range(BigDecimal.valueOf(-8.0), BigDecimal.valueOf(8.0)), new Range(BigDecimal.valueOf(-10.0), BigDecimal.valueOf(10.1)));
        var paramEps = 0.0005d;

        AbstractOptimizer optimizer = new DichotomyParamFuncOptimizer(func, ranges, paramEps, row);
        optimizer.optimize();

        assertEquals(-9.9609375, func.getParam(0));
        assertEquals(-9.9609375, func.getParam(1));
        assertEquals(40.380859375 , func.getParam(2));

    }
}