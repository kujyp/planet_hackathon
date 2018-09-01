package com.planet.avocado.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.planet.avocado.R;
import com.planet.avocado.adapter.ReviewRecyclerAdapter;
import com.planet.avocado.consts.Consts;
import com.planet.avocado.data.Product;
import com.planet.avocado.repos.ProductRepo;

import java.util.Random;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProductDetailActivity";
    private CompositeDisposable mDisposable = new CompositeDisposable();

    ImageView mainStuffImage;
    TextView companyTextView;
    TextView stuffTextView;
    TextView pointTextView;
    RecyclerView reviewRecyclerView;
    ReviewRecyclerAdapter reviewRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mainStuffImage = (ImageView) findViewById(R.id.main_stuff_image);
        companyTextView = (TextView) findViewById(R.id.company_tv);
        stuffTextView = (TextView) findViewById(R.id.stuff_tv);
        pointTextView = (TextView) findViewById(R.id.point_tv);

        reviewRecyclerView = (RecyclerView)findViewById(R.id.review_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        reviewRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.divider));
        reviewRecyclerView.addItemDecoration(dividerItemDecoration);

        String id = checkIntent();
        loadWithTemporalId();
        //loadData(id);
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
        Glide.with(this).load(product.imgPath).into(mainStuffImage);
        companyTextView.setText(product.companyName);
        stuffTextView.setText(product.name);

        double per = product.avg;
        double cuttedAvg = Double.parseDouble(String.format("%.2f",per));
        pointTextView.setText(String.valueOf(cuttedAvg));

        //리사이클러 어댑터..
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
