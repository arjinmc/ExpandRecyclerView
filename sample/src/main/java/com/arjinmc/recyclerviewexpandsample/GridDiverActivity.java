package com.arjinmc.recyclerviewexpandsample;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewSingleTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.recyclerviewdecoration.RecyclerViewGridSpaceItemDecoration;
import com.arjinmc.recyclerviewexpandsample.model.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eminem Lo on 2019-09-24.
 * email: arjinmc@hotmail.com
 */
public class GridDiverActivity extends AppCompatActivity {


    private GridView mGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setSubtitle("Grid Diver");


        List<Car> mDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Car car = new Car();
            car.setBrand("Car Brand" + i);
            car.setTypeName("Car Type" + i * i);
            mDataList.add(car);
        }

        RecyclerView rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new GridLayoutManager(this, 7, RecyclerView.VERTICAL, false));
//        rvList.setLayoutManager(new GridDiverLayoutManager(this, 3, 1, 1));
//        rvList.addItemDecoration(new RecyclerViewItemDecoration.Builder(this)
//                .color(Color.GRAY)
//                .thickness(3)
//                .create());

//        rvList.addItemDecoration(new SpacingDecoration(20,10,true));
//        rvList.addItemDecoration(new RecyclerViewGridSpaceItemDecoration(20, 10, true));
//        rvList.addItemDecoration(new RecyclerViewGridSpaceItemDecoration(2, 2, true));
        rvList.addItemDecoration(new RecyclerViewGridSpaceItemDecoration.Builder(this).marginHorizontal(10).marginVertical(4).create());

        RecyclerViewAdapter adapter = new RecyclerViewAdapter<>(this, mDataList
                , R.layout.item_list_type1
                , new RecyclerViewSingleTypeProcessor<Car>() {
            @Override
            public void onBindViewHolder(RecyclerViewViewHolder holder, int position, Car object) {
                TextView textView = holder.getView(R.id.tv_content);
                textView.setText(object.getBrand() + "/" + object.getTypeName());
            }
        });
        rvList.setAdapter(adapter);
    }
}
