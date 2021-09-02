package functions;

import java.util.Random;

public class LineFunctionNoise extends LineFunction {

    private final double kNoise;

    public LineFunctionNoise(double a, double b, double kNoise){
        super(a, b);
        this.kNoise = kNoise;
    }

    @Override
    public Double apply(Integer arg) {
        return super.apply(arg) + kNoise * new Random().nextInt(100);
    }

}
