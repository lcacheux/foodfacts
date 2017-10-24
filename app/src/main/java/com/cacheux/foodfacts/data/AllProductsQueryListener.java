package com.cacheux.foodfacts.data;

import com.cacheux.foodfacts.model.Product;

import java.util.List;

public interface AllProductsQueryListener {
    void allProductsLoaded(List<Product> products);
}
