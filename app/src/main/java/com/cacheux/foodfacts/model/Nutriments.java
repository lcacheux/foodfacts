package com.cacheux.foodfacts.model;

import com.google.gson.annotations.SerializedName;

public class Nutriments {

    @SerializedName("energy_unit")
    private String energyUnit;

    @SerializedName("energy_value")
    private String energyValue;

    public String getEnergyUnit() {
        return energyUnit;
    }

    public void setEnergyUnit(String energyUnit) {
        this.energyUnit = energyUnit;
    }

    public String getEnergyValue() {
        return energyValue;
    }

    public void setEnergyValue(String energyValue) {
        this.energyValue = energyValue;
    }
}
