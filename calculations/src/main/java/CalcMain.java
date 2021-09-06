import data.DataLoader;
import data.DataWriter;
import data.read.Model;
import functions.*;
import optimizer.ParamFuncOptimizer;
import timerow.DoubleRow;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class CalcMain {
    public static void main(String[] args) throws IOException {

        DataLoader dataLoader = new DataLoader("/home/misha/IdeaProjects/Forecast/calculations/src/main/resources/AUDUSD240.csv");
        SortedMap<LocalDateTime, Model> models =  dataLoader.getModelList();
        DoubleRow dr = dataLoader.getFromDate(LocalDateTime.now().minusWeeks(3));

        List<Double> coefs = new ArrayList<>();
        List<Double> steps = new ArrayList<>();

        int order = 100;
        for(int i = 0; i < order; i++){
            coefs.add(1./ order);
            steps.add(100.);
        }

        SinFunction sinFunction = new SinFunction(300,coefs);

        ParamFuncOptimizer optimizer = new ParamFuncOptimizer(sinFunction, steps,0.000005, dr);
        optimizer.optimize();

        DoubleRow dr1 = new DoubleRow(dr.size(), sinFunction);

        DataWriter dw = new DataWriter( dr, dr1,"/home/misha/IdeaProjects/Forecast/calculations/src/main/resources/AUDUSD240.json");
        dw.write();

        System.out.println("end");
    }
}
