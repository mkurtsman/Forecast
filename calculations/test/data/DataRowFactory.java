package data;

import functions.ParamFuncFactory;
import functions.ParametricFunction;
import timerow.DoubleRow;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

public class DataRowFactory {

    public static List<BigDecimal> sqParams = List.of( 2.0, 4.0, 5.0).stream().map(BigDecimal::valueOf).toList();

    public static DoubleRow getSQDoubleRow(ParametricFunction func){
        return new DoubleRow(IntStream.range(0, 100).mapToObj(x -> func.apply(x)).toList());
    }

    public static DoubleRow getDoubleRow(){
        List<BigDecimal> list = List.of(BigDecimal.valueOf(23.4), BigDecimal.valueOf(1.4), BigDecimal.valueOf(43.1));
        return new DoubleRow(list);
    }
}
