package com.arjinmc.recyclerviewexpandsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
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

//import com.arjinmc.recyclerviewdecoration.RecyclerViewStickyHeadItemDecoration;

/**
 * Sample for group list
 * Created by Eminem Lo on 2017/9/11.
 * email: arjinmc@hotmail.com
 */

public class GroupStickyActivity extends AppCompatActivity {

    private RecyclerViewGroupAdapter mGroupAdapter;
    private List<Car> mDataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setSubtitle("GroupSticky");


        RecyclerView rvList = findViewById(R.id.rv_list);
        RecyclerViewStyleHelper.toLinearLayout(rvList, LinearLayout.VERTICAL);
        rvList.addItemDecoration(new RecyclerViewItemDecoration.Builder(this)
                .color(Color.GRAY)
                .thickness(3)
                .create());
//        rvList.addItemDecoration(new RecyclerViewStickyHeadItemDecoration.Builder().isSmooth(true).create());
        mDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Car car = new Car();
            car.setBrand("Car Band" + i);
            car.setTypeName("Car Type" + i * i);
            if (i % 10 == 0) {
                car.setGroup("Group " + (i / 10));
            }
            mDataList.add(car);
        }

        mGroupAdapter = new RecyclerViewGroupAdapter<>(this, mDataList
                , new int[]{R.layout.item_group_type, R.layout.item_list_type1}
                , new RecyclerViewGroupTypeProcessor<Car>() {

            @Override
            public void onBindGroupViewHolder(RecyclerViewViewHolder holder, int groupPosition, Car car) {
                TextView tvGroup = holder.getView(R.id.tv_group);
                tvGroup.setText(car.getGroup());
            }

            @Override
            public void onBindItemViewHolder(RecyclerViewViewHolder holder, final int groupPosition, final int itemPosition, Car car) {
                TextView tvContent = holder.getView(R.id.tv_content);
                tvContent.setText("Car brand:" + car.getBrand() + " / type: " + car.getTypeName());

                tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(GroupStickyActivity.this
                                , "Group: " + groupPosition + "\titemPosition: " + itemPosition, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public int getItemViewType(int position) {
                if (mDataList.get(position).getGroup() != null)
                    return 0;
                return 1;
            }
        });

        rvList.setAdapter(mGroupAdapter);
    }

}
