package com.cacheux.foodfacts.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cacheux.foodfacts.R;
import com.cacheux.foodfacts.barcode.BarcodeHandler;
import com.cacheux.foodfacts.barcode.BarcodeMethod;
import com.cacheux.foodfacts.data.AddProductListener;
import com.cacheux.foodfacts.data.DataAccess;
import com.cacheux.foodfacts.data.ProductQueryListener;
import com.cacheux.foodfacts.model.Product;

import java.util.List;

public class HistoryActivity extends Activity implements HistoryFragment.Listener,
        PopupMenu.OnMenuItemClickListener, AddProductListener {

    private FloatingActionButton fab;
    private PopupMenu popupMenu;
    private Toolbar toolbar;

    private List<BarcodeMethod> methods;
    private BarcodeMethod currentMethod;

    private DataAccess dataAccess;

    private HistoryFragment historyFragment;
    private DetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        toolbar = findViewById(R.id.toolbar);

        historyFragment = (HistoryFragment) getFragmentManager().findFragmentById(R.id.fragment);

        methods = BarcodeHandler.getAvailableMethods();

        fab = findViewById(R.id.fab);
        popupMenu = new PopupMenu(this, fab);
        for (int i = 0; i < methods.size(); i++) {
            BarcodeMethod method = methods.get(i);
            if (method.isAvailable(this)) {
                popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, method.getDisplayName());
            }
        }
        popupMenu.setOnMenuItemClickListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

        dataAccess = DataAccess.getInstance(getApplicationContext());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        currentMethod = methods.get(item.getItemId());
        currentMethod.requestBarcode(this);
        return true;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (currentMethod != null) {
            final String barcode = currentMethod.parseResult(requestCode, resultCode, data);
            if (barcode != null) {
                dataAccess.getProduct(barcode, new ProductQueryListener() {
                    @Override
                    public void productLoaded(Product product) {
                        if (product != null) {
                            openDetails(product);
                        } else {
                            dataAccess.queryAndSaveProduct(barcode, HistoryActivity.this);
                        }
                    }
                });
            }

            currentMethod = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (detailsFragment != null) {
            closeDetails();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void networkRequestError(String message) {
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void productNotFound() {
        Toast.makeText(this, R.string.product_not_found, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void productAdded(Product product) {
        if (product != null) {
            Toast.makeText(this, R.string.product_added, Toast.LENGTH_SHORT).show();
            historyFragment.refresh();
            openDetails(product);
        }
    }

    @Override
    public void productClicked(Product product) {
        openDetails(product);
    }

    private void openDetails(Product product) {
        detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DetailsFragment.ARGUMENT_BARCODE, product.getBarcode());
        detailsFragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment, detailsFragment);
        ft.commit();

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDetails();
            }
        });
    }

    private void closeDetails() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(detailsFragment);
        ft.commit();
        toolbar.setNavigationIcon(null);
        detailsFragment = null;
    }
}
