import calculator.LnCalculator;
import data.DataLoader;
import data.DataWriter;
import data.write.GraphType;
import timerow.DoubleRow;

import java.io.IOException;
import java.time.LocalDateTime;

public class CalcMain {
    public static void main(String[] args) throws IOException {

        var path = /*"C:/work/Forecast/calculations/src/main/resources/AUDUSD240.csv" */"/home/misha/IdeaProjects/Forecast/calculations/src/main/resources";
        DataLoader dataLoader = new DataLoader(path + "/AUDUSD240.csv") ;
        LocalDateTime dt = LocalDateTime.now();
        int week = 0;
        DoubleRow dr = dataLoader.get(dt.minusYears(1), dt);
        DoubleRow dr1 = dataLoader.get(dt.minusYears(1), dt);
        LnCalculator calculator = new LnCalculator(dr1, dr.size() - dr1.size());
        calculator.calculate();

        DataWriter dw = new DataWriter( path + "/AUDUSD240.json");
        dw.addSeries(dr, new int[]{155, 155,23}, "init", GraphType.line);
//        dw.addSeries(dr1, new int[]{255, 0,0}, "init", GraphType.line);
//        dw.addSeries(calculator.getLineOptimizedRowExtrapolated(), new int[]{0, 0,255}, "forecast", GraphType.line);
//        dw.addSeries(calculator.getResultRowExtrapolated(), new int[]{155, 155,0}, "result", GraphType.line);
//        dw.addSeries(calculator.getLn(), new int[]{55, 0,0}, "LN", GraphType.line);

//        dw.addSeries(subtract(calculator.getResultRow(), dr1), new int[]{155, 155,0}, "result", GraphType.line);
        dw.addSeries(calculator.getLn(), new int[]{255, 0,0}, "ln", GraphType.line);
        dw.addSeries(calculator.getDiff(), new int[]{0, 0,255}, "diff", GraphType.line);
        dw.addSeries(calculator.getCyclicOptimizedRow(), new int[]{33, 33,133}, "cyclic", GraphType.line);

        dw.addSeries(calculator.getLnInt(), new int[]{0, 55,55}, "ln diff", GraphType.line);
        dw.addSeries(calculator.getTimeRow1(), new int[]{23, 25,150}, "new tr", GraphType.line);

        dw.write();

        System.out.println("end");
    }
}
