package calculator;

import functions.LineFunction;
import functions.ParametricFunction;
import optimizer.ParamFuncOptimizer;
import timerow.DoubleRow;
import timerow.TimeRow;

import java.util.ArrayList;
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


    }
}
