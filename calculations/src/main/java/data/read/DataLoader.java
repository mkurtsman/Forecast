package data.read;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataLoader {

    public static List<Model> getModelList() throws IOException {
        Path path =  Paths.get("/home/misha/IdeaProjects/Forecast/calculations/src/main/resources/AUDUSD240.csv");
        List<Model> data = new ArrayList<>();
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
                data.add(model);
            }
        }
        return data;
    }

    getLastDate(Date from)

    public static void main(String[] args) {
        try {
            List<Model> models = DataLoader.getModelList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
