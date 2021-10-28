import calculator.Calculator;
import calculator.CalculatorL;
import calculator.CalculatorLS;
import data.DataLoader;
import data.DataWriter;
import data.write.GraphType;
import timerow.DoubleRow;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;

public class CalcMain {
    public static void main(String[] args) throws IOException {

        DataLoader dataLoader = new DataLoader("C:/work/Forecast/calculations/src/main/resources/AUDUSD240.csv" /*"/home/misha/IdeaProjects/Forecast/calculations/src/main/resources/AUDUSD240.csv"*/);
        LocalDateTime dt = LocalDateTime.of(2021, Month.SEPTEMBER, 3, 0, 0);
        DoubleRow dr = dataLoader.get(dt.minusWeeks(120), dt.minusWeeks(5));
        DoubleRow dr1 = dataLoader.get(dt.minusWeeks(120), dt.minusWeeks(6));
        CalculatorL calculator = new CalculatorL(dr1, dr.size() - dr1.size());
        calculator.calculate();

        DataWriter dw = new DataWriter( "C:/work/Forecast/calculations/src/main/resources/AUDUSD240.json"/*"/home/misha/IdeaProjects/Forecast/calculations/src/main/resources/AUDUSD240.json"*/);
        dw.addSeries(dr, new int[]{255, 0,0}, "init", GraphType.line);
//        dw.addSeries(calculator.getLineOptimizedRowExtrapolated(), new int[]{0, 0,255}, "forecast", GraphType.line);
        dw.addSeries(calculator.getLineOptimizedRow(), new int[]{0, 255,0}, "approximation", GraphType.line);
//        dw.addSeries(calculator.getResultRowExtrapolated(), new int[]{155, 155,0}, "result", GraphType.line);
        dw.write();

        System.out.println("end");
    }
}
