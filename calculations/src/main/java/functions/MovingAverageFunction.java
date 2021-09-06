package functions;

import timerow.DoubleRow;

import java.util.List;

public class MovingAverageFunction extends ParametricFunction{


    private final DoubleRow timeRow;
    private final DoubleRow values;

    public MovingAverageFunction(DoubleRow timeRow, List<Double> params){
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
