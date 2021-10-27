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

        return new DoubleRow(IntStream.range(0, 1000).mapToObj(x -> func.apply(x)).toList());
    }
}
