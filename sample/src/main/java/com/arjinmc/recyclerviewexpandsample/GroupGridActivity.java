package com.arjinmc.recyclerviewexpandsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewGroupAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewGroupTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.recyclerviewdecoration.RecyclerViewGroupItemDecoration;
import com.arjinmc.recyclerviewexpandsample.model.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample for group list
 * Created by Eminem Lo on 2017/9/11.
 * email: arjinmc@hotmail.com
 */

public class GroupGridActivity extends AppCompatActivity {

    private List<Car> mDataList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setSubtitle("GroupGrid");

        RecyclerView rvList = findViewById(R.id.rv_list);
        RecyclerViewStyleHelper.toGridView(rvList, 4);
        rvList.addItemDecoration(new RecyclerViewGroupItemDecoration.Builder(this)
                .color(Color.GRAY)
                .thickness(3)
                .groupType(0)
//                .groupStartVisible(true)
//                .groupEndVisible(true)
//                .groupLeftVisible(true)
//                .groupTopVisible(true)
//                .groupRightVisible(true)
//                .groupBottomVisible(true)
                .create());

        mDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Car car = new Car();
            car.setBrand("Car Band" + i);
            car.setTypeName("Car Type" + i * i);

            switch (i) {
                case 0:
                case 2:
                case 8:
                case 9:
                case 11:
                case 16:
                case 28:
                case 33:
                case 65:
                case 84:
                case 87:
                case 99:
                    car.setGroup("Group " + (i / 10));
            }
            mDataList.add(car);
        }

        rvList.setAdapter(new RecyclerViewGroupAdapter<>(this, mDataList
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
                if (mDataList.get(position).getGroup() != null)
                    return 0;
                return 1;
            }
        }));

    }

}
