package functions;

import java.math.BigDecimal;

public class LineFunction extends ParametricFunction {

    public LineFunction(BigDecimal a, BigDecimal b){
        params.add(a);
        params.add(b);
    }

    @Override
    public BigDecimal apply(Integer arg) {
        return params.get(0).add(params.get(1).multiply(BigDecimal.valueOf(arg)));
    }

    @Override
    public int from() {
        return 0;
    }
}
