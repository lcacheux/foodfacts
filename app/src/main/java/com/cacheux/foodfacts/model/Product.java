package com.cacheux.foodfacts.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "products", indices = {@Index(value = {"barcode"}, unique = true)})
public class Product extends BaseObject {

    @SerializedName("_id")
    private String barcode;

    private long timestamp;

    @Ignore
    private List<Ingredient> ingredients;

    @ColumnInfo(name = "image_front_url")
    @SerializedName("image_front_url")
    private String imageFrontUrl;

    @ColumnInfo(name = "image_front_small_url")
    @SerializedName("image_front_small_url")
    private String imageFrontSmallUrl;

    @ColumnInfo(name = "product_name")
    @SerializedName("product_name")
    private String productName;

    @Embedded
    private Nutriments nutriments;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageFrontUrl() {
        return imageFrontUrl;
    }

    public void setImageFrontUrl(String imageFrontUrl) {
        this.imageFrontUrl = imageFrontUrl;
    }

    public String getImageFrontSmallUrl() {
        return imageFrontSmallUrl;
    }

    public void setImageFrontSmallUrl(String imageFrontSmallUrl) {
        this.imageFrontSmallUrl = imageFrontSmallUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Nutriments getNutriments() {
        return nutriments;
    }

    public void setNutriments(Nutriments nutriments) {
        this.nutriments = nutriments;
    }

    @Override
    public String toString() {
        return barcode + " / " + productName;
    }
}
