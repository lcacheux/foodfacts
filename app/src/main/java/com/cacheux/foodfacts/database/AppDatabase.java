package com.cacheux.foodfacts.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.cacheux.foodfacts.model.Ingredient;
import com.cacheux.foodfacts.model.Product;

@Database(entities = {Product.class, Ingredient.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
