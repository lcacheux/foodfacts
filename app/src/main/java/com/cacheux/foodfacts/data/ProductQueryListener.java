package com.cacheux.foodfacts.data;

import com.cacheux.foodfacts.model.Product;

public interface ProductQueryListener {
    void productLoaded(Product product);
}
