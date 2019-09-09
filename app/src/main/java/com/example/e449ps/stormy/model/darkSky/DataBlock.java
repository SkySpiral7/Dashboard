package com.example.e449ps.stormy.model.darkSky;

import java.io.Serializable;
import java.util.List;

public class DataBlock implements Serializable {
    private static final long serialVersionUID = 0;

    private List<DataPoint> data;

    public List<DataPoint> getData() {
        return data;
    }

    public void setData(List<DataPoint> data) {
        this.data = data;
    }
}
