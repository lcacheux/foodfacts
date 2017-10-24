package com.cacheux.foodfacts.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.cacheux.foodfacts.model.Ingredient;
import com.cacheux.foodfacts.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert
    public long insertProduct(Product product);

    @Insert
    public void insertIngredients(List<Ingredient> ingredients);

    @Query("SELECT * FROM products ORDER BY timestamp DESC")
    public List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE barcode = :barcode")
    public Product getProduct(String barcode);

    @Query("SELECT * FROM ingredients WHERE product_id = :productId ORDER BY rank")
    public List<Ingredient> getIngredients(long productId);
}
