package data;

import functions.ParamFuncFactory;
import functions.ParametricFunction;
import timerow.DoubleRow;

import java.util.List;
import java.util.stream.IntStream;

public class DataRowFactory {

    public static List<Double> sqParams = List.of(2.0, 3.0, 50.0);

    public static DoubleRow getSQDoubleRow(ParametricFunction func){
        return new DoubleRow(IntStream.range(0, 100).mapToDouble(x -> func.apply(x)).boxed().toList());
    }
}
