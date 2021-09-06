package timerow;

import functions.ParametricFunction;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DoubleRow {

    private List<Double> points = new ArrayList<>();

    public DoubleRow() {
    }


    public DoubleRow(List<Double> points){
        this.points.addAll(points);
    }

    public DoubleRow(int size, ParametricFunction function){
        for(int i = 0; i< size; i++){
            points.add(function.apply(i));
        }
    }

    public DoubleRow(int size, Double[] ys, ParametricFunction function){
        for(int i = 0; i< size; i++){

            if(i < ys.length){
                points.add(ys[i]);
            } else {
                points.add(function.apply(i));
            }
        }
    }

    public Double minSquare(ParametricFunction function) {
        double sum = IntStream.range(0, points.size()).mapToDouble(i -> Math.pow(points.get(i) - function.apply(i),2)).sum();
        return Math.sqrt(sum/points.size());
    }

    public Double[][] getPoints(){
        Double[][] ret = new Double[points.size()][2];
        List<Integer> xes = getXes();
        List<Double> yes = getYes();

        for(int i = 0; i < points.size(); i++){
            ret[i][0] = Double.valueOf(xes.get(i));
            ret[i][1] = yes.get(i);
        }
        return ret;
    }

    public void set(Integer x, Double sum) {
        this.points.set(x, sum);
    }

    public Double get(Integer x){
        return points.get(x);
    }

    public List<Integer> getXes(){
        return IntStream.range(0, points.size()).boxed().toList();
    }

    public Integer size() {
        return  points.size();
    }

        public List<Double> getYes(){
        return new ArrayList<>(points);
    }


    public DoubleRow add(DoubleRow added){
        DoubleRow newRow = new DoubleRow();
        IntStream.range(0, points.size()).forEach(x -> newRow.points.add(this.points.get(x) + added.points.get(x)));
        return newRow;
    }

    public DoubleRow subtract(DoubleRow substracted){
        DoubleRow newRow = new DoubleRow();
        IntStream.range(0, points.size()).forEach(x -> newRow.points.add(this.points.get(x) - substracted.points.get(x)));
        return newRow;
    }

    public DoubleRow mutiply(Double multiplyer){
        DoubleRow newRow = new DoubleRow();
        IntStream.range(0, points.size()).forEach(x -> newRow.points.add(this.points.get(x)* multiplyer));
        return newRow;
    }

    public DoubleRow devide(Double devider){
        DoubleRow newRow = new DoubleRow();
        IntStream.range(0, points.size()).forEach(x -> newRow.points.add(this.points.get(x)/ devider));
        return newRow;
    }

    public DoubleRow copy(){
        DoubleRow newRow = new DoubleRow();
        newRow.points.addAll(this.points);
        return newRow;
    }

    public DoubleRow apply(ParametricFunction function){
        DoubleRow newRow = new DoubleRow();
        IntStream.range(0, points.size()).forEach(x -> newRow.points.add(function.apply(x)));
        return newRow;
    }

    public Double maxAbs(){
        return getYes().stream().mapToDouble(Number::doubleValue).map(Math::abs).max().orElse(0.0);
    }

}
