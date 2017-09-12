package com.arjinmc.recyclerviewexpandsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewGroupAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewGroupTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.recyclerviewdecoration.RecyclerViewItemDecoration;
import com.arjinmc.recyclerviewexpandsample.model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Sample for group list
 * Created by Eminem Lo on 2017/9/11.
 * email: arjinmc@hotmail.com
 */

public class GroupGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setSubtitle("GroupGrid");

        RecyclerView rvList = (RecyclerView) findViewById(R.id.rv_list);
        RecyclerViewStyleHelper.toGridView(rvList, 3);

        final List<Car> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Car car = new Car();
            car.setBrand("Car Band" + i);
            car.setTypeName("Car Type" + i * i);
            car.setUuid(UUID.randomUUID().toString());
            if (i % 10 == 0) {
                car.setGroup("Group " + (i / 10));
            }
            dataList.add(car);
        }

        rvList.setAdapter(new RecyclerViewGroupAdapter<>(this, dataList
                , new int[]{R.layout.item_group_type, R.layout.item_list_type2}
                , new RecyclerViewGroupTypeProcessor<Car>() {

            @Override
            public void onBindGroupViewHolder(RecyclerViewViewHolder holder, int groupPosition, Car car) {
                TextView tvGroup = holder.getView(R.id.tv_group);
                tvGroup.setText(car.getGroup());
            }

            @Override
            public void onBindItemViewHolder(RecyclerViewViewHolder holder, final int groupPosition, final int itemPosition, Car car) {
                TextView tvContent = holder.getView(R.id.tv_content);
                String str = "Car brand:" + car.getBrand() + " / type: " + car.getTypeName();
                if (itemPosition % 2 == 0) {
                    str += car.getUuid();
                }
                tvContent.setText(str);

                tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(GroupGridActivity.this
                                , "Group: " + groupPosition + "\titemPosition: " + itemPosition
                                , Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public int getItemViewType(int position) {
                if (position % 10 == 0)
                    return 0;
                return 1;
            }
        }));

    }

}
