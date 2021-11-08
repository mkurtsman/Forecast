import calculator.LnCalculator;
import data.DataLoader;
import data.DataWriter;
import data.write.GraphType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import timerow.DoubleRow;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class CalcMainOptimize {
    private static Logger logger = LoggerFactory.getLogger(CalcMainOptimize.class);

    public static void main(String[] args) throws IOException {

        var path = /*"C:/work/Forecast/calculations/src/main/resources/AUDUSD240.csv" */"/home/misha/IdeaProjects/Forecast/calculations/src/main/resources";
        DataLoader dataLoader = new DataLoader(path + "/AUDUSD240.csv") ;
        LocalDateTime dt = LocalDateTime.now();
        DoubleRow dr = dataLoader.get(dt.minusYears(1), dt);

        double error = Double.MAX_VALUE;
        int errI = 0;
        DecimalFormat df = new DecimalFormat("#.###########");
//        for(int i = 40; i < 200; i+=5) {
//            LnCalculator calculator = new LnCalculator(dr, i, 2);
//            calculator.calculate();
//            if(error > calculator.getError()){
//                error = calculator.getError();
//                errI = i;
//                logger.info("{};{}", errI, df.format(error));
//            }
//        }

        double errD = 0;
        for(double i = 1.5; i < 3; i+=0.1) {
            LnCalculator calculator = new LnCalculator(dr, 50, i);
            calculator.calculate();
            if(error > calculator.getError()){
                error = calculator.getError();
                errD = i;
                logger.info("{};{}", errD, df.format(error));
            }
        }

        System.out.println("end");
    }
}
