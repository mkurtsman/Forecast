package optimizer;

import functions.ParametricFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timerow.DoubleRow;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

import static timerow.DoubleRowOperations.minSquare;


public class SimpleParamFuncOptimizer implements AbstractOptimizer {
    private static Logger logger = LoggerFactory.getLogger(SimpleParamFuncOptimizer.class);

    protected List<Range> ranges;
    protected static MessageFormat mf = new MessageFormat("iterrations {0}, error {1}, func {2}");
    protected ParametricFunction function;
    protected DoubleRow timeRow;
    protected BigDecimal maxCount;

    public SimpleParamFuncOptimizer(ParametricFunction function, List<Range> initRanges, Integer maxCount, DoubleRow timeRow) {
        this.ranges = initRanges;
        this.function = function;
        this.timeRow = timeRow;
        this.maxCount = BigDecimal.valueOf(maxCount);
    }

    public void optimize(){
        var count = 0;
        var error = Double.MAX_VALUE;

        for(int k = 0; k < ranges.size(); k++){
            var range = ranges.get(k);
            var step = range.getSize().divide(this.maxCount);
            var valX = range.getFirst();
            BigDecimal paramValue = range.getFirst();
            BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
            for(int i = 0; i <= this.maxCount.intValue(); i++){
                function.setParam(k, valX);
                var val = minSquare(timeRow, function);
                if(val.compareTo(min) < 0){
                    logger.debug("iteration {}, arg {}, min {}", k,val,valX);
                    min = val;
                    paramValue = valX;
                }
                valX = valX.add(step);
            }
            function.setParam(k, paramValue);
        }

        logger.info("params {}", function.toString());

    }

}


