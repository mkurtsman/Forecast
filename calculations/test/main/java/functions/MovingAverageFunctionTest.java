package functions;

import data.DataRowFactory;
import org.junit.jupiter.api.Test;
import timerow.DoubleRow;
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
        assertEquals(11, result.get(3).intValue());
        assertEquals(17, result.get(4).intValue());
        assertEquals(23, result.get(5).intValue());
        assertEquals(29, result.get(6).intValue());
        assertEquals(35, result.get(7).intValue());
        assertEquals(41, result.get(8).intValue());
        assertEquals(47, result.get(9).intValue());
        assertEquals(53, result.get(10).intValue());
        assertEquals(10, result.size());
    }




}