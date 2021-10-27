package functions;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.*;

public class SinFunction extends ParametricFunction{

    private final int count;

    public SinFunction(int count, List<BigDecimal> params){
        this.count = count;
        this.params.addAll(params);
    }
    @Override
    public BigDecimal apply(Integer x) {
        double a = 2 * PI * x / count;
        return IntStream.range(0,params.size()).mapToObj(i -> params.get(i).multiply(BigDecimal.valueOf(sin(a*(i+1))))).reduce((aa, b) -> aa.add(b)).orElse(BigDecimal.ZERO);
    }

    @Override
    public int from() {
        return 0;
    }
}
