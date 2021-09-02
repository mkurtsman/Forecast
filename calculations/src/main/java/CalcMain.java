import functions.LineFunction;
import functions.LineFunctionNoise;
import optimizer.ParamFuncOptimizer;
import timerow.DoubleRow;
import timerow.TimeRow;

import java.util.List;

public class CalcMain {
    public static void main(String[] args) {

        Integer[] x = new Integer[1000];
        for(int i = 0 ; i <x.length; i++){
            x[i] = i;
        }

        LineFunctionNoise lineFunctionNoise = new LineFunctionNoise(5, 34, 12, 5);

        TimeRow<Integer, Double> row = new DoubleRow(x, lineFunctionNoise);

        LineFunction lineFunction = new LineFunction(5.5, 30, 13);
        ParamFuncOptimizer<Integer, Double> optimizer = new ParamFuncOptimizer<>(lineFunction, List.of(100.0,100.0,100.0),0.000001, row);
        optimizer.optimize();


        System.out.println("end");
    }
}
