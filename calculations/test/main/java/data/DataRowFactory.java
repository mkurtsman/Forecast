package data;

import functions.ParametricFunction;
import timerow.DoubleRow;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataRowFactory {

    public static List<BigDecimal> sqParams = List.of( 2.0, 4.0, 5.0).stream().map(BigDecimal::valueOf).toList();

    public static DoubleRow getSQDoubleRow(ParametricFunction func){
        var map = IntStream.range(0, 100).boxed().collect(Collectors.toMap(Function.identity(), x -> func.apply(x)));
        return new DoubleRow(map);
    }

    public static DoubleRow getDoubleRow(){
        var map = Map.of(0, BigDecimal.valueOf(23.4), 1, BigDecimal.valueOf(1.4), 2, BigDecimal.valueOf(43.1));
        return new DoubleRow(map);
    }
}
