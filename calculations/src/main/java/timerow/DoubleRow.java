package timerow;

import functions.LineFunction;
import functions.ParametricFunction;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DoubleRow {

    private List<BigDecimal> points = new ArrayList<>();

    public DoubleRow() {
    }


    public DoubleRow(List<BigDecimal> points){
        this.points.addAll(points);
    }

    public DoubleRow(int size, ParametricFunction function){
        for(int i = 0; i< size; i++){
            points.add(function.apply(i));
        }
    }

    public DoubleRow(int size, BigDecimal[] ys, ParametricFunction function){
        for(int i = 0; i< size; i++){

            if(i < ys.length){
                points.add(ys[i]);
            } else {
                points.add(function.apply(i));
            }
        }
    }

    public BigDecimal[][] getPoints(){
        BigDecimal[][] ret = new BigDecimal[points.size()][2];
        List<Integer> xes = getXes();
        List<BigDecimal> yes = getYes();

        for(int i = 0; i < points.size(); i++){
            ret[i][0] = BigDecimal.valueOf(xes.get(i));
            ret[i][1] = yes.get(i);
        }
        return ret;
    }

    public void set(Integer x, BigDecimal sum) {
        this.points.set(x, sum);
    }

    public BigDecimal get(Integer x){
        return points.get(x);
    }

    public List<Integer> getXes(){
        return IntStream.range(0, points.size()).boxed().toList();
    }

    public int size() {
        return  points.size();
    }

        public List<BigDecimal> getYes(){
        return new ArrayList<>(points);
    }

}
