package com.glodblock.github.dmeblood.common.data;

public class DataSet {

    public static TierData tierData = new TierData();
    public static ModelData modelData = new ModelData();

    public static void init() {
        JSONLoader.deserialize(tierData, TierData.id);
        JSONLoader.deserialize(modelData, ModelData.id);
    }

}
