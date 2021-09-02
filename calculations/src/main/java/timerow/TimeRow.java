package timerow;

import functions.ParametricFunction;

import java.util.*;
import java.util.stream.Collectors;

abstract public class TimeRow<X, Y extends Number> {

    private SortedMap<X, Y> points = new TreeMap<>();

    protected TimeRow(){
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

    public List<X> getXes(){
        return new ArrayList<>(points.keySet());
    }

    public List<Y> getYes(){
        return points.keySet().stream().map(k -> points.get(k)).collect(Collectors.<Y>toList());
    }


    public TimeRow<X, Y> add(TimeRow<X, Y> added){
        TimeRow<X, Y> newRow = newEmpty(this);
        points.forEach((x, y) -> newRow.points.put(x, add(y, added.points.get(x))));
        return newRow;
    }

    public TimeRow<X, Y> subtract(TimeRow<X, Y> added){
        TimeRow<X, Y> newRow = newEmpty(this);
        points.forEach((x, y) -> newRow.points.put(x, subtract(y, added.points.get(x))));
        return newRow;
    }

    abstract protected Y add(Y v1, Y v2);

    abstract protected Y subtract(Y v1, Y v2);

    abstract protected TimeRow<X, Y> newEmpty(TimeRow<X, Y> original);

}
