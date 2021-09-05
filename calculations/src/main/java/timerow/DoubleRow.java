package timerow;

import functions.ParametricFunction;

import java.util.List;
import java.util.SortedMap;

public class DoubleRow extends TimeRow<Integer, Double> {

    public DoubleRow() {
    }

    public DoubleRow(Integer[] integers, Double[] doubles) {
        super(integers, doubles);
    }

    public DoubleRow(Integer[] integers, ParametricFunction<Integer, Double> function) {
        super(integers, function);
    }

    @Override
    public Double minSquare(ParametricFunction<Integer, Double> function) {
        double sum = this.points.entrySet().stream().mapToDouble(p -> Math.pow(p.getValue() - function.apply(p.getKey()),2)).sum();
        return Math.sqrt(sum/points.size());
    }

    @Override
    protected Double add(Double v1, Double v2) {
        return v1 + v2;
    }

    @Override
    protected Double subtract(Double v1, Double v2) {
        return v1 - v2;
    }

    @Override
    protected TimeRow<Integer, Double> newEmpty() {
        return new DoubleRow();
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

}
