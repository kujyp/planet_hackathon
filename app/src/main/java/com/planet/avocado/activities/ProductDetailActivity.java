package com.planet.avocado.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.planet.avocado.R;
import com.planet.avocado.consts.Consts;
import com.planet.avocado.data.Product;
import com.planet.avocado.repos.ProductRepo;

import java.util.Random;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProductDetailActivity";
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        String id = checkIntent();
        loadWithTemporalId();
//        loadData(id);
    }

    private void loadWithTemporalId() {
        ProductRepo.getInstance()
                .getListOnce()
                .subscribe(productList -> {
                    int idx = new Random().nextInt(productList.size());
                    loadData(productList.get(idx).id);
                });
    }

    private void loadData(String id) {
        Single<Product> productSingle = ProductRepo.getInstance()
                .getOnce(id);

        mDisposable.add(
                productSingle.subscribe(product -> {
                    showProduct(product);
                })
        );
    }

    private void showProduct(Product product) {
        Log.d(TAG, "showProduct() called with: product = [" + product.toString() + "]");

    }

    private String checkIntent() {
        Intent intent = getIntent();
        return intent.getStringExtra(Consts.Bundle.PRODUCT_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        mDisposable.dispose();
    }
}
