package optimizer;

import functions.ParametricFunction;
import timerow.DoubleRow;

import java.text.MessageFormat;

abstract public class AbstractOptimizer {

    protected static MessageFormat mf = new MessageFormat("iterrations {0}, error {1}, func {2}");
    protected ParametricFunction function;
    protected DoubleRow timeRow;
    protected Double paramEps;
    protected Double eps;
    protected Integer maxCount;


    public AbstractOptimizer(ParametricFunction function, Double dichotomyEps, Double eps, Integer maxCount, DoubleRow timeRow) {
        this.function = function;
        this.timeRow = timeRow;
        this.paramEps = dichotomyEps;
        this.eps = eps;
        this.maxCount = maxCount;
    }

    abstract public void optimize();

    abstract void optimizeByParam(int i, ParametricFunction function);

}
