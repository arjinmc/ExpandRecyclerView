package com.arjinmc.recyclerviewexpandsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewGroupAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewGroupTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.recyclerviewdecoration.RecyclerViewGroupItemDecoration;
import com.arjinmc.recyclerviewexpandsample.model.Car;
import com.arjinmc.recyclerviewexpandsample.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Group ItemDecoration
 * Created by Eminem Lo on 2018/4/8.
 * email: arjinmc@hotmail.com
 */
public class GroupItemDecorationActivity extends AppCompatActivity {

    private RecyclerViewGroupAdapter mGroupAdapter;
    private List<Car> mDataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_itemdecoration);
        getSupportActionBar().setSubtitle("Group ItemDecoration");

        RecyclerView rvList = (RecyclerView) findViewById(R.id.rv_list);
//        RecyclerViewStyleHelper.toLinearLayout(rvList, LinearLayout.HORIZONTAL);
        RecyclerViewStyleHelper.toGridView(rvList, 3);
        rvList.addItemDecoration(new RecyclerViewGroupItemDecoration.Builder(this)
                .color(Color.GRAY)
                .thickness(3)
                .groupType(0)
                .groupStartVisible(true)
                .groupEndVisible(true)
                .create());

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
            public void onBindGroupViewHolder(RecyclerViewViewHolder holder, final int groupPosition, Car car) {
                TextView tvGroup = holder.getView(R.id.tv_group);
                tvGroup.setText(car.getGroup());

                tvGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(GroupItemDecorationActivity.this
                                , "Group: " + groupPosition);
                    }
                });
            }

            @Override
            public void onBindItemViewHolder(RecyclerViewViewHolder holder, final int groupPosition, final int itemPosition, Car car) {
                TextView tvContent = holder.getView(R.id.tv_content);
                tvContent.setText("Car brand:" + car.getBrand() + " / type: " + car.getTypeName());

                tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(GroupItemDecorationActivity.this
                                , "Group: " + groupPosition + "\titemPosition: " + itemPosition);
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
