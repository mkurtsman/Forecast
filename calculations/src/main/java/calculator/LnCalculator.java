package calculator;

import timerow.DoubleRow;
import timerow.DoubleRowOperations;

import static timerow.DoubleRowOperations.*;

public class LnCalculator {

    private final DoubleRow timeRow;
    private DoubleRow ln;
    private DoubleRow diff;
    private DoubleRow lnInt;
    private DoubleRow timeRow1;


    public LnCalculator(DoubleRow timeRow, int extrapolationCount) {
        this.timeRow = timeRow;

    }

    public void calculate() {
        ln = exp(timeRow);
        diff = DoubleRowOperations.diff(ln);
        lnInt = DoubleRowOperations.interg(ln, diff);
        timeRow1 = ln(lnInt);
    }

    public DoubleRow getLn() {
        return ln;
    }

    public DoubleRow getDiff() {
        return diff;
    }

    public DoubleRow getTimeRow1() {
        return timeRow1;
    }

    public DoubleRow getTimeRow() {
        return timeRow;
    }

    public DoubleRow getLnInt() {
        return lnInt;
    }
}
