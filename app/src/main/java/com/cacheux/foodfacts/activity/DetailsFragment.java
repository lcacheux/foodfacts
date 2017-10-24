package com.cacheux.foodfacts.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cacheux.foodfacts.R;
import com.cacheux.foodfacts.data.DataAccess;
import com.cacheux.foodfacts.data.IngredientsQueryListener;
import com.cacheux.foodfacts.data.ProductQueryListener;
import com.cacheux.foodfacts.model.Ingredient;
import com.cacheux.foodfacts.model.Nutriments;
import com.cacheux.foodfacts.model.Product;
import com.cacheux.foodfacts.utils.Markdown;

import java.util.List;

public class DetailsFragment extends Fragment implements ProductQueryListener {

    static final String ARGUMENT_BARCODE = "barcode";

    private DataAccess dataAccess;

    private ImageView picture;
    private TextView productName;
    private TextView energyValue;
    private ListView ingredientsList;

    private ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataAccess = DataAccess.getInstance(getActivity());

        String barcode = getArguments().getString(ARGUMENT_BARCODE);
        if (barcode != null) {
            dataAccess.getProduct(barcode, this);
        }

        View root = inflater.inflate(R.layout.fragment_details, container, false);
        picture = root.findViewById(R.id.picture);
        productName = root.findViewById(R.id.product_name);
        energyValue = root.findViewById(R.id.energy_value);
        ingredientsList = root.findViewById(R.id.ingredients_list);

        adapter = new IngredientsAdapter(getActivity());
        ingredientsList.setAdapter(adapter);

        return root;
    }

    @Override
    public void productLoaded(Product product) {
        if (product != null) {
            dataAccess.getPictureStore().loadImage(product, picture);
            productName.setText(product.getProductName());
            Nutriments nutriments = product.getNutriments();
            if (nutriments != null) {
                energyValue.setText(nutriments.getEnergyValue() + " " + nutriments.getEnergyUnit());
            }
            dataAccess.getIngredients(product, (IngredientsQueryListener) adapter);
        }
    }

    private class IngredientsAdapter extends ArrayAdapter<Ingredient> implements IngredientsQueryListener {

        public IngredientsAdapter(Context context) {
            super(context, R.layout.item_ingredient);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final Ingredient ingredient = getItem(position);

            if (view == null) {
                view = getActivity().getLayoutInflater()
                        .inflate(R.layout.item_ingredient, parent, false);
            }
            ViewHolder holder;
            if (view.getTag() != null) {
                holder = (ViewHolder) view.getTag();
            } else {
                holder = new ViewHolder();
                holder.name = view.findViewById(R.id.name);
                view.setTag(holder);
            }

            holder.name.setText(Markdown.parseMarkdown(ingredient.getText()));
            return view;
        }

        @Override
        public void allIngredientsLoaded(List<Ingredient> products) {
            clear();
            addAll(products);
        }

        private class ViewHolder {
            TextView name;
        }
    }
}
