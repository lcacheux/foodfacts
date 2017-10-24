package com.cacheux.foodfacts.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.cacheux.foodfacts.api.FoodFactsServer;
import com.cacheux.foodfacts.database.AppDatabase;
import com.cacheux.foodfacts.model.Ingredient;
import com.cacheux.foodfacts.model.Product;
import com.cacheux.foodfacts.pictures.PictureStore;
import com.cacheux.foodfacts.utils.Ln;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Base class to link all data sources together (REST API, database, pictures)
 *
 * All tasks are executed in AsyncTasks and results are sent to listeners
 */
public class DataAccess {

    private AppDatabase database;
    private FoodFactsServer server;
    private PictureStore pictureStore;

    private static DataAccess instance;

    private DataAccess(Context context) {
        server = new FoodFactsServer();
        database = Room.databaseBuilder(context, AppDatabase.class, "foodfacts.db").build();
        pictureStore = new PictureStore(context);
    }

    /**
     * Return the static instance of DataAccess
     * @param context
     * @return
     */
    public static DataAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DataAccess(context);
        }

        return instance;
    }

    /**
     * Access the {@link PictureStore} instance
     * @return
     */
    public PictureStore getPictureStore() {
        return pictureStore;
    }

    /**
     * Retrieve the list of all products from the database
     * @param listener
     */
    public void getAllProducts(AllProductsQueryListener listener) {
        new AllProductsQueryTask(listener).execute();
    }

    /**
     * Retrieve the list of ingredients for a product
     * @param product
     * @param listener
     */
    public void getIngredients(Product product, IngredientsQueryListener listener) {
        new IngredientsQueryTask(listener).execute(product.getUid());
    }

    /**
     * Retrieve a single product from the database
     * @param barcode
     * @param listener
     */
    public void getProduct(String barcode, ProductQueryListener listener) {
        new ProductQueryTask(listener).execute(barcode);
    }

    /**
     * Request the product infos from the API server and save it in the database
     * @param barcode
     * @param listener
     */
    public void queryAndSaveProduct(String barcode, AddProductListener listener) {
        Ln.v("queryAndSaveProduct " + barcode);
        new AddProductQuery(barcode, listener);
    }

    private class AddProductQuery implements FoodFactsServer.Listener {
        private AddProductListener listener;
        private Product product;

        public AddProductQuery(String barcode, AddProductListener listener) {
            this.listener = listener;
            server.getProduct(barcode, this);
        }

        @Override
        public void requestCompleted(Product product1) {
            Ln.v("requestCompleted " + product1);
            this.product = product1;
            if (product != null) {
                new InsertTask(new InsertListener() {
                    @Override
                    public void productAdded() {
                        if (listener != null) {
                            listener.productAdded(product);
                        }
                    }
                }).execute(product);
            } else {
                Ln.w("Product not found on server");
                if (listener != null) {
                    listener.productNotFound();
                }
            }
        }

        @Override
        public void requestFailure(Throwable t) {
            if (listener != null) {
                listener.networkRequestError(t.getMessage());
            }
        }
    }

    private class InsertTask extends AsyncTask<Product, Void, Void> {
        private InsertListener listener;

        InsertTask(InsertListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Product...products) {
            for (Product product : products) {
                product.setTimestamp(new Date().getTime());
                long id = database.productDao().insertProduct(product);
                for (Ingredient ingredient : product.getIngredients()) {
                    ingredient.setProductId(id);
                }
                database.productDao().insertIngredients(product.getIngredients());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (listener != null) {
                listener.productAdded();
            }
        }
    }

    private class AllProductsQueryTask extends AsyncTask<Void, Void, List<Product>> {
        private AllProductsQueryListener listener;

        public AllProductsQueryTask(AllProductsQueryListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {
            return database.productDao().getAllProducts();
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            if (listener != null) {
                listener.allProductsLoaded(result);
            }
        }
    }

    private class ProductQueryTask extends AsyncTask<String, Void, Product> {
        private ProductQueryListener listener;

        public ProductQueryTask(ProductQueryListener listener) {
            this.listener = listener;
        }

        @Override
        protected Product doInBackground(String... strings) {
            for (String barcode : strings) {
                return database.productDao().getProduct(barcode);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Product result) {
            if (listener != null) {
                listener.productLoaded(result);
            }
        }
    }

    private class IngredientsQueryTask extends AsyncTask<Long, Void, List<Ingredient>> {
        private IngredientsQueryListener listener;

        public IngredientsQueryTask(IngredientsQueryListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Ingredient> doInBackground(Long... longs) {
            for (Long value : longs) {
                return database.productDao().getIngredients(value);
            }
            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Ingredient> result) {
            if (listener != null) {
                listener.allIngredientsLoaded(result);
            }
        }
    }

    /**
     * Internal listener to link the REST API request and the database insert request
     */
    private interface InsertListener {
        void productAdded();
    }
}
