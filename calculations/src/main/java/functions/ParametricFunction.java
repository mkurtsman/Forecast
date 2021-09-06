package functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class ParametricFunction implements Function<Integer, Double> {

    protected List<Double> params = new ArrayList<>();

    public void setParam(Integer num, Double val){
        params.set(num, val);
    }

    public Double getParam(Integer num){
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
}
