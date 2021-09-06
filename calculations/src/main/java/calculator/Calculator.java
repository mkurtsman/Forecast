package calculator;

import functions.LineFunction;
import optimizer.ParamFuncOptimizer;
import timerow.DoubleRow;
import java.util.List;

public class Calculator<X, Y extends Number>{

    private final DoubleRow timeRow;

    public Calculator(DoubleRow timeRow) {
        this.timeRow = timeRow;
    }

    public void calculate(){
        // 1. remove trend
        LineFunction function = new LineFunction(0.5, 0.5);
        List<Double> steps = List.of(5.0, 5.0);
        Double eps = 0.000001;
        ParamFuncOptimizer<X,Y> optimizer = new ParamFuncOptimizer(function, steps, eps, timeRow);
        optimizer.optimize();

        DoubleRow lineOpimizedRow = timeRow.copy();
        lineOpimizedRow.apply(function);

        DoubleRow cyclic = timeRow.copy();
        cyclic.subtract(lineOpimizedRow);

       // 2. remove cyclic

        Double devider = cyclic.maxAbs();
        DoubleRow normalizedCyclic = (DoubleRow) cyclic.devide(devider);

        ParamFuncOptimizer<X,Y> optimizer = new ParamFuncOptimizer(function, steps, eps, timeRow);

        cyclic.apply();
        cyclic.mutiply(devider);

        // 3. remove MA

        // 4. calc noise

        // 5. extrapolate

        // 6. add cyclic
        // 7. add trend



    }
}
