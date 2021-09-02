package functions;

public class LineFunction extends ParametricFunction<Integer, Double> {

    public LineFunction(double a, double b, double c){
        params.add(a);
        params.add(b);
        params.add(c);
    }

    @Override
    public Double apply(Integer arg) {
        return params.get(0) + params.get(1) * arg+ params.get(2) * Math.sin(arg);
    }

}
