package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class ParametricFunction<T, R> implements Function<T, R> {

    protected List<Double> params = new ArrayList<>();

    public void setParam(Integer num, Double val){
        params.set(num, val);
    }

    public Double getParam(Integer num){
        return params.get(num);
    }

}
