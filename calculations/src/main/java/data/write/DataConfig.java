package data.write;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataConfig {
    @SerializedName("lx")
    private String[] labelX;
    @SerializedName("series")
    private Series[] seriesList;

    public String[] getLabelX() {
        return labelX;
    }

    public void setLabelX(String[] labelX) {
        this.labelX = labelX;
    }

    public Series[] getSeriesList() {
        return seriesList;
    }

    public void setSeriesList(List<Series> seriesList){
        this.seriesList = seriesList.toArray(Series[]::new);
    }
}
