package com.planet.avocado.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import com.planet.avocado.R;
import com.planet.avocado.adapters.ReviewRecyclerAdapter;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    ImageView mainStuffImage;
    TextView companyTextView;
    TextView stuffTextView;
    TextView pointTextView;
    RecyclerView reviewRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mainStuffImage = (ImageView) findViewById(R.id.main_stuff_image);
        companyTextView = (TextView) findViewById(R.id.company_tv);
        stuffTextView = (TextView) findViewById(R.id.stuff_tv);
        pointTextView = (TextView) findViewById(R.id.point_tv);


        ArrayList<cardview_item> items = new ArrayList<>();

        for(int i = 0 ; i< 5 ;i++){
            cardview_item item = new cardview_item();
            item.setUserId("yoonchul");
            item.setPoint("2.3");
            item.setReviewText("너무너무 맛잇어요!!!!!!!!!!!!");
            items.add(item);
        }


        reviewRecyclerView = (RecyclerView)findViewById(R.id.review_recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        reviewRecyclerView.setLayoutManager(linearLayoutManager);

        ReviewRecyclerAdapter reviewRecyclerAdapter = new ReviewRecyclerAdapter(getApplicationContext(),items);
        reviewRecyclerView.setAdapter(reviewRecyclerAdapter);


    }
}
