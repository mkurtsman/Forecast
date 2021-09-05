package data;

import data.read.Model;
import timerow.DoubleRow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataLoader {

    private final String filePath;

    public DataLoader(String filePath) {
        this.filePath = filePath;
    }

    public SortedMap<LocalDateTime, Model> getModelList() throws IOException {
        Path path =  Paths.get(filePath);
        SortedMap<LocalDateTime, Model> data = new TreeMap<LocalDateTime, Model>();
        try(BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            for(String line; (line = br.readLine()) != null; ) {
                var d = line.split(",");
                var ld = LocalDate.parse(d[0], DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                var lt = LocalTime.parse(d[1], DateTimeFormatter.ofPattern("HH:mm"));

                var model = new Model(LocalDateTime.of(ld, lt),
                        Double.valueOf(d[2]),
                        Double.valueOf(d[3]),
                        Double.valueOf(d[4]),
                        Double.valueOf(d[5])
                        );
                data.put(model.getDate(),model);
            }
        }
        return data;
    }

    public DoubleRow getFromDate(LocalDateTime from) throws IOException {

        SortedMap<LocalDateTime, Model> models = getModelList().tailMap(from);

        List<Integer> iList = new LinkedList<>();
        List<Double> dList = new LinkedList<>();

        int i = 0;
        var it = models.entrySet().iterator();
        while (it.hasNext()){
            iList.add(i);
            dList.add(it.next().getValue().getClose());
            i++;
        }

        return new DoubleRow(iList.toArray(new Integer[]{}), dList.toArray(new Double[]{}));
    }

    public static void main(String[] args) {
        try {
            DataLoader dataLoader = new DataLoader("/home/misha/IdeaProjects/Forecast/calculations/src/main/resources/AUDUSD240.csv");
            SortedMap<LocalDateTime, Model> models =  dataLoader.getModelList();
            DoubleRow dr = dataLoader.getFromDate(LocalDateTime.now().minusYears(1));
            DataWriter dw = new DataWriter( dr,"/home/misha/IdeaProjects/Forecast/calculations/src/main/resources/AUDUSD240.json");
            dw.write();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
