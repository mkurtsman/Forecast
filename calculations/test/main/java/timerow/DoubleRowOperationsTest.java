package timerow;

import data.DataRowFactory;
import functions.ParamFuncFactory;
import functions.ParametricFunction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static timerow.DoubleRowOperations.*;
import static org.junit.jupiter.api.Assertions.*;

class DoubleRowOperationsTest {


    @Test
    public void minSquareIdentityTest(){
        List<BigDecimal> params = new ArrayList<>();
        params.add(BigDecimal.valueOf(2));
        params.add(BigDecimal.valueOf(4));
        params.add(BigDecimal.valueOf(5));
        ParametricFunction func = ParamFuncFactory.getSimpleSQFunction(params);
        DoubleRow row = new DoubleRow(10, func);

        assertTrue(minSquare(row, func).compareTo(BigDecimal.valueOf(0.0001)) < 0);
    }

    @Test
    public void minSquareTest(){
        List<BigDecimal> params = new ArrayList<>();
        params.add(BigDecimal.valueOf(2));
        params.add(BigDecimal.valueOf(4));
        params.add(BigDecimal.valueOf(5));
        ParametricFunction func = ParamFuncFactory.getSimpleSQFunction(params);
        DoubleRow row = new DoubleRow(10, func);

        func.setParam(0, BigDecimal.valueOf(3));
        func.setParam(1, BigDecimal.valueOf(5));
        func.setParam(2, BigDecimal.valueOf(6));


        assertTrue(minSquare(row, func).subtract(BigDecimal.valueOf(654.6597)).compareTo(BigDecimal.valueOf(0.0001)) < 0);
    }

    @Test
    public void maxAbsTest(){
        assertEquals( BigDecimal.valueOf(43.1), maxAbs(DataRowFactory.getDoubleRow()));
    }

    @Test
    public void minTest(){
        assertEquals( BigDecimal.valueOf(43.1), max(DataRowFactory.getDoubleRow()));
    }

    @Test
    public void maxTest(){
        assertEquals( BigDecimal.valueOf(1.4), min(DataRowFactory.getDoubleRow()));
    }

}