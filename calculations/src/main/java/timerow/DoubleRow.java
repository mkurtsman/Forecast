package timerow;

import functions.LineFunction;
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

}
