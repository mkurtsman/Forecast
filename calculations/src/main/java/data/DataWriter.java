package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.write.DataConfig;
import data.write.GraphType;
import data.write.Series;
import timerow.DoubleRow;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static timerow.DoubleRowOperations.max;

public class DataWriter {
    private DataConfig config = new DataConfig();
    private String filePath;

    private DecimalFormat df = new DecimalFormat("0.#####");
    private Double minY = Double.MAX_VALUE;
    private Double maxY = Double.MIN_VALUE;
    private LinkedList<Series> series =  new LinkedList<>();

    public DataWriter(String filePath){
        this.filePath = filePath;
        config.setSeriesList(new LinkedList<>());
    }

    public void write() throws IOException {
        DataConfig config = new DataConfig();
        config.setLabelX(IntStream.range(0, 13).mapToObj(Integer::toString).collect(Collectors.toList()).toArray(new String[]{}));
        String y[] = new String[11];
        for(int i = 0; i <= y.length -1; i++){
            var d = minY + (maxY - minY)*i/(y.length -1);
            y[i] = df.format(d);
        }
        config.setSeriesList(series);
        FileWriter writer = new FileWriter(filePath);
        new Gson().toJson(config, writer);
        writer.close();

    }

    public void addSeries(DoubleRow row, int[] color, String name, GraphType type){
        Series s = new Series();
        s.setColor(color);
        s.setName(name);
        s.setPoints(row.getPoints());
        s.setType(type);
        series.add(s);

        var m = max(row);
        maxY = BigDecimal.valueOf(maxY).compareTo(m) < 0 ? m.doubleValue() : maxY;
        minY = BigDecimal.valueOf(minY).compareTo(m) > 0 ? m.doubleValue() : minY;
    }

}
