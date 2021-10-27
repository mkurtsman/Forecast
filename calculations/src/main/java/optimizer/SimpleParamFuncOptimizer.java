package optimizer;

import functions.ParametricFunction;
import timerow.DoubleRow;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static timerow.DoubleRowOperations.minSquare;


public class SimpleParamFuncOptimizer implements AbstractOptimizer {

    public static final BigDecimal DIVISOR_2 = BigDecimal.valueOf(2);
    protected List<Range> ranges;
    protected static MessageFormat mf = new MessageFormat("iterrations {0}, error {1}, func {2}");
    protected ParametricFunction function;
    protected DoubleRow timeRow;
    protected Double paramEps;
    protected Double eps;
    protected Integer maxCount;

    public SimpleParamFuncOptimizer(ParametricFunction function, List<Range> initRanges, Double paramEps, Double eps, Integer maxCount, DoubleRow timeRow) {
        this.ranges = initRanges;
        this.function = function;
        this.timeRow = timeRow;
        this.paramEps = paramEps;
        this.eps = eps;
        this.maxCount = maxCount;
    }

    public void optimize(){
        var count = 0;
        var error = Double.MAX_VALUE;
        double COUNT = 100.0;

        for(int k = ranges.size() -1; k >=0; k--){
            var range = ranges.get(k);
            var r = range.getSize().divide(BigDecimal.valueOf(COUNT));
            var rr = range.getFirst();

            BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
            BigDecimal point = range.getFirst();

            for(int i = 0; i < COUNT; i++){
                function.setParam(k, rr);
                var val = minSquare(timeRow, function);
                System.out.println(val);
                if(val.compareTo(min) < 0){
                    System.out.println(val +" ---" +rr);
                    min = val;
                    point = rr;
                }
                rr = rr.add(r);
            }
            function.setParam(k, point);
        }
        function.getParams().forEach(System.out::println);

    }

}


