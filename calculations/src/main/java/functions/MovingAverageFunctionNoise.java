package functions;

import timerow.TimeRow;

import java.util.List;
import java.util.Random;

public class MovingAverageFunctionNoise extends MovingAverageFunction{

    private final double kNoise;
    private Random random;

    public MovingAverageFunctionNoise(TimeRow<Integer, Double> timeRow, List<Double> params, double kNoise) {
        super(timeRow, params);
        this.kNoise = kNoise;
        random = new Random();
    }

    @Override
    public Double apply(Integer arg) {
        return super.apply(arg) + kNoise * (0.5 - random.nextDouble());
    }
}
