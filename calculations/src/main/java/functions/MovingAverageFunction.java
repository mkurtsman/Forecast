package functions;

import timerow.DoubleRow;

import java.math.BigDecimal;
import java.util.List;

public class MovingAverageFunction extends ParametricFunction{

    private final DoubleRow timeRow;

    public MovingAverageFunction(DoubleRow timeRow, List<BigDecimal> params){
        this.params.addAll(params);
        this.timeRow = timeRow;
    }

    @Override
    public void setParam(Integer num, BigDecimal val) {
        super.setParam(num, val);
    }

    @Override
    public BigDecimal apply(Integer x) {
        return  x < params.size() ? timeRow.get(x) : getValue(x);
    }

    private BigDecimal getValue(Integer x) {
        int order = params.size();
        if(x < order){
            return timeRow.get(x);
        }

        BigDecimal sum = params.get(0);
        for(int i = 1 ; i < order; i++ ){
            sum = sum.add(params.get(i).multiply( timeRow.get(x - 1)));
        }

        return sum;
    }


    @Override
    public int from() {
        return params.size();
    }

}
