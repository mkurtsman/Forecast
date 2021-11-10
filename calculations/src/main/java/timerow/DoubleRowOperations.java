package timerow;

import functions.ParametricFunction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DoubleRowOperations {

    public static final BigDecimal TWO = BigDecimal.valueOf(2.0);

    private static Collector<Integer, ?, Map<Integer, BigDecimal>> getIntegerMapCollector(Function<Integer, BigDecimal> func) {
        Collector<Integer, ?, Map<Integer,BigDecimal>> collector = Collectors.toMap(Function.identity(), func);
        return collector;
    }

    private static Stream<Integer> getRange(DoubleRow source) {
        return getRange(source.points.firstKey(),  source.points.lastKey()+1);
    }

    private static Stream<Integer> getRange( Integer from, Integer to) {
        return IntStream.range(from, to).boxed();
    }

    public static DoubleRow add(DoubleRow source, DoubleRow added){
        if(!source.getXes().equals(added.getXes())){
            throw new RuntimeException("size of rows mismatch");
        }

        Function<Integer, BigDecimal> func = x -> source.get(x).add(added.get(x));
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }


    public static  DoubleRow subtract(DoubleRow source, DoubleRow substracted){
        if(source.size() != substracted.size()){
            throw new RuntimeException("size of rows mismatch");
        }

        Function<Integer, BigDecimal> func = x -> source.get(x).subtract(substracted.get(x));
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow add(DoubleRow source, BigDecimal val){
        Function<Integer, BigDecimal> func = x -> source.get(x).add(val);
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow subtract(DoubleRow source, BigDecimal val){
        Function<Integer, BigDecimal> func = x -> source.get(x).subtract(val);
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }


    public static  DoubleRow mutiply(DoubleRow source, BigDecimal multiplyer){
        Function<Integer, BigDecimal> func = x -> source.get(x).multiply(multiplyer);
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow divide(DoubleRow source, BigDecimal devider){
        if(devider.compareTo(BigDecimal.ZERO) == 0){
            throw new RuntimeException("invalid devider");
        }

        Function<Integer, BigDecimal> func = x -> source.get(x).divide(devider, MathContext.DECIMAL128);
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow copy(DoubleRow source){
        Function<Integer, BigDecimal> func = x -> source.get(x);
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow subRow(DoubleRow source, int from){
        if(source.points.firstKey() > from){
            throw new RuntimeException("incorrect from parameter");
        }
        Function<Integer, BigDecimal> func = x -> source.get(x);
        Map<Integer, BigDecimal> points = getRange(from, source.size()).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow apply(DoubleRow source, ParametricFunction function){
        Function<Integer, BigDecimal> func = x -> function.apply(x);
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow ln(DoubleRow source){
        Function<Integer, BigDecimal> func = x -> BigDecimal.valueOf(Math.log(source.get(x).doubleValue()));
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow diff(DoubleRow source){
        Function<Integer, BigDecimal> func = x -> source.get(x).subtract(source.get(x-1));
        Map<Integer, BigDecimal> points = getRange(source.points.firstKey()+1, source.points.lastKey()+1).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow interg(DoubleRow source, DoubleRow diff){
        Function<Integer, BigDecimal> func = x -> source.get(x-1).add(diff.get(x));
        Map<Integer, BigDecimal> points = getRange(source.points.firstKey()+1, source.points.lastKey()+1).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
    }

    public static  DoubleRow exp(DoubleRow source){
        Function<Integer, BigDecimal> func = x -> {
            var y = source.get(x);
            var p = Math.exp(y.doubleValue());
            return BigDecimal.valueOf(p);
        };
        Map<Integer, BigDecimal> points = getRange(source).collect(getIntegerMapCollector(func));
        return new DoubleRow(points);
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
        Map<Integer, BigDecimal> points = getRange(source.points.size()+1, source.points.size() + 1 + extrapolationCount).collect(getIntegerMapCollector(function));
        var result = copy(source);
        result.addPoints(points);
        return result;

    }

    public static  DoubleRow extrapolateWithEmpty(DoubleRow source, int extrapolationCount) {
        Function<Integer, BigDecimal> func = x -> BigDecimal.ZERO;
        Map<Integer, BigDecimal> points = getRange(source.points.size(), source.points.size() + extrapolationCount).collect(getIntegerMapCollector(func));
        var result = copy(source);
        result.addPoints(points);
        return result;
    }

    public static BigDecimal minSquare(DoubleRow source, ParametricFunction function) {
        BigDecimal sum = getRange(source).map(i -> source.get(i).subtract(function.apply(i)).pow(2)).reduce((a, b) -> a.add(b) ).orElse(BigDecimal.ZERO);
        return sum.divide(BigDecimal.valueOf(source.size()), MathContext.DECIMAL128).sqrt(MathContext.DECIMAL128);
    }

    public static BigDecimal minSquare(DoubleRow source, DoubleRow row) {
        if(source.size() != row.size()){
            throw new RuntimeException("different row size");
        }
        BigDecimal sum = getRange(source).map(i -> source.get(i).subtract(row.get(i)).pow(2)).reduce((a, b) -> a.add(b) ).orElse(BigDecimal.ZERO);
        return sum.sqrt(MathContext.DECIMAL128).divide(BigDecimal.valueOf(source.size()), MathContext.DECIMAL128);
    }


}
