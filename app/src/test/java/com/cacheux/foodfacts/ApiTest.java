package com.cacheux.foodfacts;

import static junit.framework.Assert.assertEquals;

import android.util.Log;

import com.cacheux.foodfacts.api.FoodFactsApi;
import com.cacheux.foodfacts.api.FoodFactsApiResponse;
import com.cacheux.foodfacts.api.FoodFactsServer;
import com.cacheux.foodfacts.model.Product;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class ApiTest {

    @Test
    public void testGetProduct() {
        FoodFactsServer server = new FoodFactsServer();
        FoodFactsApi api = server.getApi();

        // https://world.openfoodfacts.org/api/v0/product/3329770057258.json
        Call<FoodFactsApiResponse> call = api.getProduct("3329770057258");

        try {
            Response<FoodFactsApiResponse> response = call.execute();
            Product product = response.body().getProduct();
            assertEquals("Petits Filous Tub's Goût Fraise, Pêche, Framboise", product.getProductName());
            assertEquals(15, product.getIngredients().size());
            assertEquals("kJ", product.getNutriments().getEnergyUnit());
            assertEquals("414", product.getNutriments().getEnergyValue());
        } catch (IOException e) {
            Log.e("FoodFactsTest", "IOException", e);
        }
    }
}
