package optimizer;

import static java.lang.Math.abs;

public class Range {
    protected Double first;
    protected Double second;

    public Range(Double first, Double second) {
        this.first = first;
        this.second = second;
    }

    public Double getFirst() {
        return first;
    }

    public Double getSecond() {
        return second;
    }

    public void setFirst(Double first) {
        this.first = first;
    }

    public void setSecond(Double second) {
        this.second = second;
    }

    public Double getCenter() {
        return (second + first) / 2;
    }

    public Double getSize() {
        return abs(second - first);
    }
}
