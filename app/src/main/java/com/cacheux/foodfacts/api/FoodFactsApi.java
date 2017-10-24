package com.cacheux.foodfacts.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FoodFactsApi {
    @GET("product/{barcode}.json")
    Call<FoodFactsApiResponse> getProduct(@Path("barcode") String barcode);
}
