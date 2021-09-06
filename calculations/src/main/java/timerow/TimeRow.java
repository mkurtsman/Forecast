package timerow;

import functions.ParametricFunction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

abstract public class
TimeRow<X, Y extends Number> {

    SortedMap<X, Y> points = new TreeMap<>();

    protected TimeRow(){
    }

    protected TimeRow(SortedMap<X, Y> points ){
        this.points.putAll(points);
    }

    public TimeRow(X[] xes, Y[] ys){
        for(int i = 0; i< xes.length; i++){
            points.put(xes[i], ys[i]);
        }
    }

    public TimeRow(X[] xes, ParametricFunction<X, Y> function){
        for(int i = 0; i< xes.length; i++){
            points.put(xes[i], function.apply(xes[i]));
        }
    }

    public TimeRow(X[] xes, Y[] ys, ParametricFunction<X, Y> function){
        for(int i = 0; i< xes.length; i++){

            if(i < ys.length){
                points.put(xes[i], ys[i]);
            } else {
                points.put(xes[i], function.apply(xes[i]));
            }
        }
    }

    public void set(X i, Y sum) {
        this.points.put(i, sum);
    }

    public Y get(X x){
        return points.get(x);
    }

    public List<X> getXes(){
        return new ArrayList<>(points.keySet());
    }

    public List<Y> getYes(){
        return points.keySet().stream().map(k -> points.get(k)).collect(Collectors.<Y>toList());
    }


    public TimeRow<X, Y> add(TimeRow<X, Y> added){
        TimeRow<X, Y> newRow = newEmpty();
        points.forEach((x, y) -> newRow.points.put(x, add(y, added.points.get(x))));
        return newRow;
    }

    public TimeRow<X, Y> subtract(TimeRow<X, Y> added){
        TimeRow<X, Y> newRow = newEmpty();
        points.forEach((x, y) -> newRow.points.put(x, subtract(y, added.points.get(x))));
        return newRow;
    }

    public TimeRow<X, Y> mutiply(Double multiplyer){
        TimeRow<X, Y> newRow = newEmpty();
        points.forEach((x, y) -> newRow.points.put(x, (Y) Double.valueOf(points.get(x).doubleValue() * multiplyer)));
        return newRow;
    }

    public TimeRow<X, Y> devide(Double devider){
        TimeRow<X, Y> newRow = newEmpty();
        points.forEach((x, y) -> newRow.points.put(x, (Y) Double.valueOf(points.get(x).doubleValue() / devider)));
        return newRow;
    }

    public TimeRow<X, Y> copy(){
        TimeRow<X, Y> newRow = newEmpty();
        newRow.points.putAll(this.points);
        return newRow;
    }

    public void apply(ParametricFunction<X, Y> function){
        List<X> xes = getXes();
        for(int i = 0; i< xes.size(); i++){
            set(xes.get(i), function.apply(xes.get(i)));
        }
    }

    public Double maxAbs(){
       return getYes().stream().mapToDouble(Number::doubleValue).map(Math::abs).max().orElse(0.0);
    }

    abstract public Double minSquare(ParametricFunction<X, Y> function);

    abstract protected Y add(Y v1, Y v2);

    abstract protected Y subtract(Y v1, Y v2);

    abstract protected TimeRow<X, Y> newEmpty();



}
