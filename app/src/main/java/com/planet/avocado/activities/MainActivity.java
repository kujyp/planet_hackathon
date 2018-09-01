package com.planet.avocado.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.planet.avocado.R;
import com.planet.avocado.data.Product;
import com.planet.avocado.repos.ProductRepo;

import java.util.ArrayList;
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

        Product product = new Product();
        product.type = "과자";
        product.name = "갸또";
        product.imgPath = "http://dsakfjld";
        product.avg = 1.0d;
        product.companyName = "1";
        product.commentList = new ArrayList<>();
        product.commentList.add("abcd");
        product.commentList.add("abcde");
        product.userId = "admin";
        ProductRepo.getInstance().insert(product);

//        loadData();
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
