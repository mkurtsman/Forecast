package functions;

import data.DataRowFactory;
import org.junit.jupiter.api.Test;

import timerow.DoubleRowOperations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovingAverageFunctionTest {

    private ParametricFunction func;

    public MovingAverageFunctionTest(){
        List<BigDecimal> params = List.of(BigDecimal.valueOf(2), BigDecimal.valueOf(3), BigDecimal.valueOf(4));
        func = new MovingAverageFunction(DataRowFactory.getMADoubleRow(), params);
    }

    @Test
    public void testMa(){
        var row = DataRowFactory.getMADoubleRow();
        var result = DoubleRowOperations.apply(row, func);
        assertEquals(1, result.get(1).intValue());
        assertEquals(2, result.get(2).intValue());
        assertEquals(12, result.get(3).intValue());
        assertEquals(19, result.get(4).intValue());
        assertEquals(26, result.get(5).intValue());
        assertEquals(33, result.get(6).intValue());
        assertEquals(40, result.get(7).intValue());
        assertEquals(47, result.get(8).intValue());
        assertEquals(54, result.get(9).intValue());
        assertEquals(61, result.get(10).intValue());
        assertEquals(10, result.size());
    }




}