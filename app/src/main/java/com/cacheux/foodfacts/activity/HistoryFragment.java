package com.cacheux.foodfacts.activity;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cacheux.foodfacts.R;
import com.cacheux.foodfacts.data.AllProductsQueryListener;
import com.cacheux.foodfacts.data.DataAccess;
import com.cacheux.foodfacts.model.Product;
import com.cacheux.foodfacts.utils.Ln;

import java.util.List;

public class HistoryFragment extends ListFragment {

    private HistoryAdapter adapter;
    private DataAccess dataAccess;
    private Listener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof Listener) {
            this.listener = (Listener) activity;
        } else {
            Ln.e("HistoryFragment parent activity must implement Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataAccess = DataAccess.getInstance(getActivity().getApplicationContext());
        adapter = new HistoryAdapter(getActivity());
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void refresh() {
        dataAccess.getAllProducts(adapter);
    }

    private class HistoryAdapter extends ArrayAdapter<Product> implements AllProductsQueryListener {

        HistoryAdapter(Context context) {
            super(context, R.layout.item_product, R.id.product_name);
            dataAccess.getAllProducts(this);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final Product product = getItem(position);

            if (view == null) {
                view = getActivity().getLayoutInflater()
                        .inflate(R.layout.item_product, parent, false);
            }
            ViewHolder holder;
            if (view.getTag() != null) {
                holder = (ViewHolder) view.getTag();
            } else {
                holder = new ViewHolder();
                holder.picture = view.findViewById(R.id.picture);
                holder.productName = view.findViewById(R.id.product_name);
                holder.productBarcode = view.findViewById(R.id.product_barcode);
                view.setTag(holder);
            }

            dataAccess.getPictureStore().loadThumbnail(product, holder.picture);
            holder.productName.setText(product.getProductName());
            holder.productBarcode.setText(product.getBarcode());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.productClicked(product);
                    }
                }
            });
            return view;
        }

        @Override
        public void allProductsLoaded(List<Product> products) {
            clear();
            addAll(products);
        }

        private class ViewHolder {
            ImageView picture;
            TextView productName;
            TextView productBarcode;
        }
    }

    interface Listener {
        void productClicked(Product product);
    }
}
