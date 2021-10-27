package functions;

import timerow.DoubleRow;

import java.math.BigDecimal;
import java.util.List;

import static timerow.DoubleRowOperations.copy;

public class MovingAverageFunction extends ParametricFunction{

    private final DoubleRow timeRow;
    private final DoubleRow values;

    public MovingAverageFunction(DoubleRow timeRow, List<BigDecimal> params){
        this.params.addAll(params);
        this.timeRow = timeRow;
        values = copy(timeRow);
        update(params);

    }

    @Override
    public void setParam(Integer num, BigDecimal val) {
        super.setParam(num, val);
        update(params);
    }

    private void update(List<BigDecimal> params) {
        int order = params.size();
        List<Integer> args = values.getXes();
        for(int i = order; i < args.size(); i++ ){
            BigDecimal sum = params.get(0);
            for(int j = 1; j <order; j++){
                sum = sum.add(params.get(j).multiply(values.get((i - order)+j)));
            }
            values.set(i, sum);
        }
    }

    @Override
    public BigDecimal apply(Integer x) {
        BigDecimal val = values.get(x);
        return  val == null ? timeRow.get(x) : val;
    }

    @Override
    public int from() {
        return params.size();
    }
}
