package com.glodblock.github.dmeblood.common.data;

public class DataSet {

    public static TierData tierData = new TierData();
    public static ModelData modelData = new ModelData();
    public static CatalystData catalystData = new CatalystData();
    public static WillData willData = new WillData();
    public static WillTierData willTierData = new WillTierData();

    public static void init() {
        JSONLoader.deserialize(tierData, TierData.id);
        JSONLoader.deserialize(modelData, ModelData.id);
        JSONLoader.deserialize(catalystData, CatalystData.id);
        JSONLoader.deserialize(willData, WillData.id);
        JSONLoader.deserialize(willTierData, WillTierData.id);
    }

}
