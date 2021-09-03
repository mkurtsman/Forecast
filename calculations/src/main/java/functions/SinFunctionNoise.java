package functions;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class SinFunctionNoise extends SinFunction{

    private final double kNoise;
    private Random random;

    public SinFunctionNoise(int count, List<Double> params, double kNoise){
        super(count, params);
        this.kNoise = kNoise;
        random = new Random();
    }
    @Override
    public Double apply(Integer arg) {
        return super.apply(arg) + kNoise * (0.5 - random.nextDouble());
    }
}
