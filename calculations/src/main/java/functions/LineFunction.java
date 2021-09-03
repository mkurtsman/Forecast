package functions;

public class LineFunction extends ParametricFunction<Integer, Double> {

    public LineFunction(double a, double b){
        params.add(a);
        params.add(b);
    }

    @Override
    public Double apply(Integer arg) {
        return params.get(0) + params.get(1) * arg;
    }

}
