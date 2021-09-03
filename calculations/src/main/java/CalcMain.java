import functions.*;
import optimizer.ParamFuncOptimizer;
import timerow.DoubleRow;
import timerow.TimeRow;

import java.util.List;
import java.util.Random;

public class CalcMain {
    public static void main(String[] args) {


        List<Double> params = List.of(0.1,0.2,-0.1,0.1,0.2,-0.1,0.1,0.2,-0.1,0.1,0.2,-0.1);

        Integer[] x = new Integer[1000];
        Double[] y = new Double[1000];


        for(int i = 0 ; i <x.length; i++){
            x[i] = i;
        }


        var rnd = new Random();
        for (int i = 0; i < params.size(); i++){
            y[i] = rnd.nextDouble() * 10;
        }

        for (int i = params.size(); i < y.length; i++){
            y[i] = 0.;
        }

//        LineFunctionNoise lineFunctionNoise = new LineFunctionNoise(5, 34, 12);
        TimeRow<Integer, Double> tmpRow = new DoubleRow(x, y);
        MovingAverageFunction movingAverageFunction = new MovingAverageFunctionNoise(tmpRow, params, 30);

        TimeRow<Integer, Double> row = new DoubleRow(x, movingAverageFunction);

        MovingAverageFunction movingAverageFunction1 = new MovingAverageFunction(tmpRow, List.of(0.2,0.2,-0.3,7.,0.2,-0.1,0.4,0.2,-0.1,0.1,0.2,-0.1));

        ParamFuncOptimizer<Integer, Double> optimizer = new ParamFuncOptimizer<>(movingAverageFunction1, List.of(1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.,1.),0.000000001, row);
        optimizer.optimize();



        System.out.println("end");
    }
}
