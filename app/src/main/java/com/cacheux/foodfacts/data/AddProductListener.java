package com.cacheux.foodfacts.data;

import com.cacheux.foodfacts.model.Product;

public interface AddProductListener {
    void networkRequestError(String message);
    void productNotFound();
    void productAdded(Product product);
}
