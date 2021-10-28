package optimizer;

import java.math.BigDecimal;

public class Range {
    private BigDecimal first;
    private BigDecimal second;

    public static Range of(BigDecimal first, BigDecimal second){
        return new Range(first, second);
    }

    protected Range(BigDecimal first, BigDecimal second) {
        this.first = first;
        this.second = second;
    }

    public BigDecimal getFirst() {
        return first;
    }

    public BigDecimal getSecond() {
        return second;
    }

    public void setFirst(BigDecimal first) {
        this.first = first;
    }

    public void setSecond(BigDecimal second) {
        this.second = second;
    }

    public BigDecimal getCenter() {
        return (second.add(first)).divide(BigDecimal.valueOf(2.0)) ;
    }

    public BigDecimal getSize() {
        return second.subtract(first).abs();
    }
}
