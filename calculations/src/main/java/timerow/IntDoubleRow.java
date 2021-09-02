package timerow;

public class IntDoubleRow extends TimeRow<Integer, Double> {
    @Override
    protected Double add(Double v1, Double v2) {
        return v1 + v2;
    }

    @Override
    protected Double subtract(Double v1, Double v2) {
        return v1 - v2;
    }

    @Override
    protected TimeRow<Integer, Double> newEmpty(TimeRow<Integer, Double> original) {
        return new IntDoubleRow();
    }
}
