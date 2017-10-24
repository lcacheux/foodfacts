package com.cacheux.foodfacts.data;

import com.cacheux.foodfacts.model.Ingredient;

import java.util.List;

public interface IngredientsQueryListener {
    void allIngredientsLoaded(List<Ingredient> ingredients);
}
