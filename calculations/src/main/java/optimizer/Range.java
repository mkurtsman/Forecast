package optimizer;

import java.math.BigDecimal;
import java.math.MathContext;

public class Range {
    private BigDecimal first;
    private BigDecimal second;

    public static Range of(double first, double second){
        return new Range(BigDecimal.valueOf(first), BigDecimal.valueOf(second));
    }

    public static Range of(BigDecimal first, BigDecimal second){
        return new Range(first, second);
    }

    public static Range ofSizeOne(){
        return new Range(BigDecimal.valueOf(-1), BigDecimal.ONE);
    }

    public static Range ofSizeOne(int order){
        Range range = ofSizeOne();
        return new Range(range.first.divide(BigDecimal.valueOf(order), MathContext.DECIMAL128), range.second.divide(BigDecimal.valueOf(order), MathContext.DECIMAL128));
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
