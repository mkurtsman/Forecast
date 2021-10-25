package timerow;

import functions.ParametricFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DoubleRowOperations {

    public static DoubleRow add(DoubleRow source, DoubleRow added){
        if(source.size() != added.size()){
            throw new RuntimeException("size of rows mismatch");
        }

        List<Double> values = IntStream.range(0, source.size()).mapToDouble(x -> source.get(x) + added.get(x)).boxed().toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow subtract(DoubleRow source, DoubleRow substracted){
        if(source.size() != substracted.size()){
            throw new RuntimeException("size of rows mismatch");
        }

        List<Double> values = IntStream.range(0, source.size()).mapToDouble(x -> source.get(x) - substracted.get(x)).boxed().toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow mutiply(DoubleRow source, Double multiplyer){
        List<Double> values = IntStream.range(0, source.size()).mapToDouble(x -> source.get(x) * multiplyer).boxed().toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow divide(DoubleRow source, Double devider){
        if(devider == 0.0){
            throw new RuntimeException("invalid devider");
        }
        List<Double> values = IntStream.range(0, source.size()).mapToDouble(x -> source.get(x) / devider).boxed().toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow copy(DoubleRow source){
        return new DoubleRow(source.getYes());
    }

    public static  DoubleRow apply(DoubleRow source, ParametricFunction function){
        List<Double> values = IntStream.range(0, source.size()).mapToDouble(x -> function.apply(x)).boxed().toList();
        return new DoubleRow(values);
    }

    public static  Double maxAbs(DoubleRow source){
        return source.getYes().stream().mapToDouble(Number::doubleValue).map(Math::abs).max().orElse(0.0);
    }
    public static  Double min(DoubleRow source){
        return source.getYes().stream().mapToDouble(Number::doubleValue).min().orElse(0.0);
    }

    public static  Double max(DoubleRow source){
        return source.getYes().stream().mapToDouble(Number::doubleValue).max().orElse(0.0);
    }

    public static  DoubleRow extrapolate(DoubleRow source, int extrapolationCount, Function<Integer, Double> function) {
        List<Double> list = new ArrayList<>(source.getYes());
        IntStream.range(0, extrapolationCount).forEach(i -> list.add(function.apply(i)));
        return  new DoubleRow(list);
    }

    public static  DoubleRow extrapolateWithEmpty(DoubleRow source, int extrapolationCount) {
        return  extrapolate(source, extrapolationCount, (p) -> 0.0);
    }

    public static Double minSquare(DoubleRow source, ParametricFunction function) {
        double sum = IntStream.range(0, source.size()).mapToDouble(i -> Math.pow(source.get(i) - function.apply(i),2)).sum();
        return Math.sqrt(sum/source.size());
    }

}
