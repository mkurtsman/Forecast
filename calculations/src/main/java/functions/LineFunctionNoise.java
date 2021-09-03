package functions;

import java.util.Random;

public class LineFunctionNoise extends LineFunction {

    private final double kNoise;
    private Random random;

    public LineFunctionNoise(double a, double b,  double kNoise){
        super(a, b);
        this.kNoise = kNoise;
        random = new Random();
    }

    @Override
    public Double apply(Integer arg) {
        return super.apply(arg) + kNoise * (0.5 - random.nextDouble());
    }

}
