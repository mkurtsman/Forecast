package functions;

import timerow.TimeRow;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.IntStream;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class MovingAverageFunction extends ParametricFunction<Integer, Double>{


    private final TimeRow<Integer, Double> timeRow;
    private final TimeRow<Integer, Double> values;

    public MovingAverageFunction(TimeRow<Integer, Double> timeRow, List<Double> params){
        this.params.addAll(params);
        this.timeRow = timeRow;
        values = timeRow.copy();

        update(params);

    }

    @Override
    public void setParam(Integer num, Double val) {
        super.setParam(num, val);
        update(params);
    }

    private void update(List<Double> params) {
        int order = params.size();
        List<Integer> args = values.getXes();
        for(int i = order; i < args.size(); i++ ){
            double sum = params.get(0);
            for(int j = 1; j <order; j++){
                sum += params.get(j) *values.get((i - order)+j);
            }
            values.set(i, sum);
        }
    }

    @Override
    public Double apply(Integer x) {
        Double val = values.get(x);
        return  val == null ? timeRow.get(x) : val;
    }

    @Override
    public int from() {
        return params.size();
    }
}
