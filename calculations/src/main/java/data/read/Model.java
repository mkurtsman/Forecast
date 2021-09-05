package data.read;

import java.time.LocalDateTime;

public class Model {
    private final LocalDateTime date;
    private final Double high;
    private final Double low;
    private final Double open;
    private final Double close;

    public Model(LocalDateTime date, Double open, Double high, Double low,  Double close) {
        this.date = date;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Double getHigh() {
        return high;
    }

    public Double getLow() {
        return low;
    }

    public Double getOpen() {
        return open;
    }

    public Double getClose() {
        return close;
    }
}
