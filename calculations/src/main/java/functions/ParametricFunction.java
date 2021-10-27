package functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class ParametricFunction implements Function<Integer, BigDecimal> {

    protected List<BigDecimal> params;

    public ParametricFunction(){
        this(new ArrayList<>());
    }

    public ParametricFunction(List<BigDecimal> params){
        this.params = new ArrayList<>(params);
    }

    public void setParam(Integer num, BigDecimal val){
        params.set(num, val);
    }

    public BigDecimal getParam(Integer num){
        return params.get(num);
    }

    public int getParamsCount(){
        return params.size();
    }


    public String paramsString() {
        StringBuilder builder = new StringBuilder("params: ");
        for (int i = 0 ; i < params.size(); i++){
            builder.append("p").append(i).append("=").append(params.get(i)).append(" ");
        }
        return builder.toString();
    }

    public abstract int from();

    public List<BigDecimal> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return paramsString();
    }
}
