import functions.LineFunction;
import functions.LineFunctionNoise;
import functions.SinFunction;
import functions.SinFunctionNoise;
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

//        LineFunctionNoise lineFunctionNoise = new LineFunctionNoise(5, 34, 12);
        SinFunctionNoise sinFunctionNoise = new SinFunctionNoise(1000, List.of(2.0, 3.1, 5.8, 3.1, 2.1), 10);
        TimeRow<Integer, Double> row = new DoubleRow(x, sinFunctionNoise);

        SinFunction sinFunction = new SinFunction(1000, List.of(10., 10., 10., 10., 10.));
        ParamFuncOptimizer<Integer, Double> optimizer = new ParamFuncOptimizer<>(sinFunction, List.of(100.0,100.0,100.0,100.0,100.0),0.0000001, row);
        optimizer.optimize();


        System.out.println("end");
    }
}
