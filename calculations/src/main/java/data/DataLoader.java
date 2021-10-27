package data;

import data.read.Model;
import timerow.DoubleRow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public DoubleRow get(LocalDateTime from, LocalDateTime to) throws IOException {

        SortedMap<LocalDateTime, Model> models = getModelList().subMap(from, to);
        return new DoubleRow(models.entrySet().stream().map(e -> BigDecimal.valueOf(e.getValue().getClose())).collect(Collectors.<BigDecimal>toList()));
    }

}
