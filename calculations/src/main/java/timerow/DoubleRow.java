package timerow;

import functions.ParametricFunction;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DoubleRow {

    protected TreeMap<Integer, BigDecimal> points = new TreeMap<>();

    public DoubleRow() {
    }


    public DoubleRow(Map<Integer, BigDecimal> points){
        this.points.putAll(points);
    }

    public DoubleRow(int size, ParametricFunction function){
        for(int i = 0; i< size; i++){
            points.put(i, function.apply(i));
        }
    }

    public DoubleRow(int size, BigDecimal[] ys, ParametricFunction function){
        for(int i = 0; i< size; i++){

            if(i < ys.length){
                points.put(i, ys[i]);
            } else {
                points.put(i, function.apply(i));
            }
        }
    }

    public BigDecimal[][] getPoints(){
        BigDecimal[][] ret = new BigDecimal[points.size()][2];
        AtomicInteger counter = new AtomicInteger(0);

        points.forEach((k, v) -> {
            var i = counter.getAndIncrement();
            ret[i][0] = BigDecimal.valueOf(k);
            ret[i][1] = v;

        });

        for(int i = 0; i < points.size(); i++){
        }
        return ret;
    }

    public void set(Integer x, BigDecimal val) {
        this.points.put(x, val);
    }

    public BigDecimal get(Integer x){
        return points.get(x);
    }

    public Collection<Integer> getXes(){
        return points.keySet();
    }

    public int size() {
        return  points.size();
    }

    public Collection<BigDecimal> getYes(){
        return points.values();
    }

    public void addPoints(Map<Integer, BigDecimal> map){
        points.putAll(map);
    }

    public  int maxX(){
        return points.lastKey();
    }

}
