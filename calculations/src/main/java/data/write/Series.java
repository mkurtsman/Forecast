package data.write;

import java.math.BigDecimal;

public class Series {
    private String name;
    private GraphType type;
    private int color[];
    private BigDecimal[][] points;

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

    public BigDecimal[][] getPoints() {
        return points;
    }

    public void setPoints(BigDecimal[][] points) {
        this.points = points;
    }
}
