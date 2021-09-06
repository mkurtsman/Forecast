package functions;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class SinFunction extends ParametricFunction<Integer, Double>{

    private final int count;

    public SinFunction(int count, List<Double> params){
        this.count = count;
        this.params.addAll(params);
    }
    @Override
    public Double apply(Integer x) {
        double a = 2 * PI * x / count;
        return IntStream.range(0,params.size()).mapToDouble(i -> params.get(i)*sin(a*(i+1))).sum();
    }

    @Override
    public int from() {
        return 0;
    }
}
