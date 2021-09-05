package data.write;

public class Series {
    private String name;
    private GraphType type;
    private int color[];
    private Double[][] points;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GraphType getType() {
        return type;
    }

    public void setType(GraphType type) {
        this.type = type;
    }

    public int[] getColor() {
        return color;
    }

    public void setColor(int[] color) {
        this.color = color;
    }

    public Double[][] getPoints() {
        return points;
    }

    public void setPoints(Double[][] points) {
        this.points = points;
    }
}
