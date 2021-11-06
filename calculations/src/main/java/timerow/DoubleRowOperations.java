package timerow;

import functions.ParametricFunction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class DoubleRowOperations {

    public static final BigDecimal TWO = BigDecimal.valueOf(2.0);

    public static DoubleRow add(DoubleRow source, DoubleRow added){
        if(source.size() != added.size()){
            throw new RuntimeException("size of rows mismatch");
        }

        List<BigDecimal> values = IntStream.range(0, source.size()).mapToObj(x ->  source.get(x).add(added.get(x))).toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow subtract(DoubleRow source, DoubleRow substracted){
        if(source.size() != substracted.size()){
            throw new RuntimeException("size of rows mismatch");
        }

        List<BigDecimal> values = IntStream.range(0, source.size()).mapToObj(x -> source.get(x).subtract(substracted.get(x))).toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow mutiply(DoubleRow source, BigDecimal multiplyer){
        List<BigDecimal> values = IntStream.range(0, source.size()).mapToObj(x -> source.get(x).multiply(multiplyer)).toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow divide(DoubleRow source, BigDecimal devider){
        if(devider.compareTo(BigDecimal.ZERO) == 0){
            throw new RuntimeException("invalid devider");
        }
        List<BigDecimal> values = IntStream.range(0, source.size()).mapToObj(x -> source.get(x).divide(devider, MathContext.DECIMAL128)).toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow copy(DoubleRow source){
        return new DoubleRow(source.getYes());
    }

    public static  DoubleRow apply(DoubleRow source, ParametricFunction function){
        List<BigDecimal> values = IntStream.range(0, source.size()).mapToObj(x -> function.apply(x)).toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow ln(DoubleRow source){
        List<BigDecimal> values = IntStream.range(0, source.size()).mapToObj(x -> BigDecimal.valueOf(Math.log(source.get(x).doubleValue()))).toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow diff(DoubleRow source){
        List<BigDecimal> values = IntStream.range(1, source.size()).mapToObj(x -> source.get(x).subtract(source.get(x-1))).toList();
        return new DoubleRow(values);
    }

    public static  DoubleRow exp(DoubleRow source){
        List<BigDecimal> values = IntStream.range(0, source.size()).mapToObj(x -> {
            var y = source.get(x);
            var p = Math.exp(y.doubleValue());
            return BigDecimal.valueOf(p);
        }).toList();
        return new DoubleRow(values);
    }

    public static  BigDecimal maxAbs(DoubleRow source){
        return source.getYes().stream().map(BigDecimal::abs).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }
    public static  BigDecimal min(DoubleRow source){
        return source.getYes().stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    public static  BigDecimal max(DoubleRow source){
        return source.getYes().stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    public static  DoubleRow extrapolate(DoubleRow source, int extrapolationCount, Function<Integer, BigDecimal> function) {
        List<BigDecimal> list = new ArrayList<>(source.getYes());
        IntStream.range(list.size(), list.size() + extrapolationCount).forEach(i -> list.add(function.apply(i)));
        return  new DoubleRow(list);
    }

    public static  DoubleRow extrapolateWithEmpty(DoubleRow source, int extrapolationCount) {
        return  extrapolate(source, extrapolationCount, (p) -> BigDecimal.ZERO);
    }

    public static BigDecimal minSquare(DoubleRow source, ParametricFunction function) {
        BigDecimal sum = IntStream.range(0, source.size()).mapToObj(i -> source.get(i).subtract(function.apply(i)).pow(2)).reduce((a, b) -> a.add(b) ).orElse(BigDecimal.ZERO);
        return sum.sqrt(MathContext.DECIMAL128).divide(BigDecimal.valueOf(source.size()), MathContext.DECIMAL128);
    }

    public static BigDecimal minSquare(DoubleRow source, DoubleRow row) {
        if(source.size() != row.size()){
            throw new RuntimeException("different row size");
        }
        BigDecimal sum = IntStream.range(0, source.size()).mapToObj(i -> source.get(i).subtract(row.get(i)).pow(2)).reduce((a, b) -> a.add(b) ).orElse(BigDecimal.ZERO);
        return sum.sqrt(MathContext.DECIMAL128).divide(BigDecimal.valueOf(source.size()), MathContext.DECIMAL128);
    }


}
