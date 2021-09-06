package data;

import com.google.gson.Gson;
import data.write.DataConfig;
import data.write.GraphType;
import data.write.Series;
import timerow.DoubleRow;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataWriter {
    private DoubleRow data;
    private DoubleRow data1;
    private String filePath;

    DecimalFormat df = new DecimalFormat("0.#####");


    public DataWriter(DoubleRow data, DoubleRow data1, String filePath){
        this.data = data;
        this.data1 = data1;
        this.filePath = filePath;
    }

    public void write() throws IOException {
        DataConfig config = new DataConfig();
        config.setLabelX(IntStream.range(0, 13).mapToObj(Integer::toString).collect(Collectors.toList()).toArray(new String[]{}));
        var min = data.getYes().stream().min(Double::compareTo).orElse(0.0);
        var max = data.getYes().stream().max(Double::compareTo).orElse(0.0);
        config.setLabelY(IntStream.range(0, 11).mapToObj(Integer::toString).collect(Collectors.toList()).toArray(new String[]{}));
        String y[] = new String[11];
        for(int i = 0; i <= y.length -1; i++){
            var d = min + (max - min)*i/(y.length -1);
            y[i] = df.format(d);
        }
        config.setLabelY(y);

        Series s = new Series();
        s.setColor(new int[]{255, 0,0});
        s.setName("init data");
        s.setType(GraphType.line);
        s.setPoints(data.getPoints());
        config.setSeriesList(new Series[]{s});

        Series s1 = new Series();
        s1.setColor(new int[]{0, 0,255});
        s1.setName("approximation");
        s1.setType(GraphType.line);
        s1.setPoints(data1.getPoints());

        config.setSeriesList(new Series[]{s, s1});

        FileWriter writer = new FileWriter(filePath);
        new Gson().toJson(config, writer);
        writer.close();

    }

}
