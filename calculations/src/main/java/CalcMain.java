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

import static timerow.DoubleRowOperations.subtract;

public class CalcMain {
    public static void main(String[] args) throws IOException {

        var path = /*"C:/work/Forecast/calculations/src/main/resources/AUDUSD240.csv" */"/home/misha/IdeaProjects/Forecast/calculations/src/main/resources";
        DataLoader dataLoader = new DataLoader(path + "/AUDUSD240.csv") ;
        LocalDateTime dt = LocalDateTime.now();
        int week = 0;
        DoubleRow dr = dataLoader.get(dt.minusMonths(2), dt);
        DoubleRow dr1 = dataLoader.get(dt.minusMonths(2), dt.minusWeeks(1));
        CalculatorL calculator = new CalculatorL(dr1, dr.size() - dr1.size());
        calculator.calculate();

        DataWriter dw = new DataWriter( path + "/AUDUSD240.json");
//        dw.addSeries(dr, new int[]{155, 155,23}, "init", GraphType.line);
//        dw.addSeries(dr1, new int[]{255, 0,0}, "init", GraphType.line);
//        dw.addSeries(calculator.getLineOptimizedRowExtrapolated(), new int[]{0, 0,255}, "forecast", GraphType.line);
//        dw.addSeries(calculator.getResultRowExtrapolated(), new int[]{155, 155,0}, "result", GraphType.line);

        dw.addSeries(subtract(calculator.getResultRow(), dr1), new int[]{155, 155,0}, "result", GraphType.line);
        dw.write();

        System.out.println("end");
    }
}
