package com.planet.avocado.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.planet.avocado.R;
import com.planet.avocado.data.Product;
import com.planet.avocado.repos.ProductRepo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CompositeDisposable mProductDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
    }

    private void loadData() {
        Log.d(TAG, "loadData: ");

        Observable<List<Product>> productList = ProductRepo.getInstance()
                .getProductList();
        changeProductSource(productList);
    }

    private void changeProductSource(Observable<List<Product>> productList) {
        Log.d(TAG, "changeProductSource: ");

        mProductDisposable.dispose();
        mProductDisposable = new CompositeDisposable();
        mProductDisposable.add(
                productList.subscribe(this::updateProductList)
        );
    }

    private void updateProductList(List<Product> productList) {
        Log.d(TAG, "updateProductList() called with: productList = [" + productList + "]");
    }
}
