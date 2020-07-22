package com.arjinmc.recyclerviewexpandsample;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewMultipleTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.expandrecyclerview.item.RecyclerViewItemWrapper;
import com.arjinmc.expandrecyclerview.listener.OnRecyclerViewItemClickListener;
import com.arjinmc.expandrecyclerview.listener.OnRecyclerViewItemLongClickListener;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.recyclerviewdecoration.RecyclerViewLinearItemDecoration;
import com.arjinmc.recyclerviewdecoration.RecyclerViewStickyHeadItemDecoration;
import com.arjinmc.recyclerviewexpandsample.model.Car;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eminem Lo on 30/6/2020.
 * email: arjinmc@hotmail.com
 */
public class ListStickyActivity extends AppCompatActivity implements View.OnClickListener {

    private int mNewDataPosition = -1;
    private CheckBox mCbIsGroup;
    private EditText mEtPosition;
    private Button mBtnAdd, mBtnRemove, mBtnUpdate;
    private RecyclerView mRvList;
    private RecyclerViewAdapter mAdapter;
    private List<Car> mDataList = new ArrayList<>();

    /**
     * define item view for group
     */
    private RecyclerViewItemWrapper mRvGroup = new RecyclerViewItemWrapper<Car>() {
        @Override
        public void onBind(RecyclerViewViewHolder holder, int position, Car car) {
            TextView tvGroup = holder.getView(R.id.tv_group);
            tvGroup.setText(car.getGroup());
        }

        @Override
        public int getViewType() {
            return 0;
        }
    };

    /**
     * define item view for item
     */
    private RecyclerViewItemWrapper mRvItem = new RecyclerViewItemWrapper<Car>() {
        @Override
        public void onBind(RecyclerViewViewHolder holder, int position, Car car) {
            TextView tvContent = holder.getView(R.id.tv_content);
            tvContent.setText(car.getBrand());
        }

        @Override
        public int getViewType() {
            return 1;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sticky);

        mCbIsGroup = findViewById(R.id.cb_is_group);
        mEtPosition = findViewById(R.id.et_position);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnRemove = findViewById(R.id.btn_remove);
        mBtnUpdate = findViewById(R.id.btn_update);
        mRvList = findViewById(R.id.rv_list);

        mBtnAdd = findViewById(R.id.btn_add);
        mBtnRemove = findViewById(R.id.btn_remove);
        mBtnUpdate = findViewById(R.id.btn_update);
        mBtnAdd.setOnClickListener(this);
        mBtnRemove.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);

        RecyclerViewStyleHelper.toLinearLayout(mRvList, RecyclerView.VERTICAL);
        mRvList.addItemDecoration(new RecyclerViewLinearItemDecoration.Builder(this)
                .color(Color.GREEN)
                .thickness(1)
                .create());
        mRvList.addItemDecoration(new RecyclerViewStickyHeadItemDecoration.Builder().create());

        initData();
        initListener();
    }

    private void initData() {

        for (int i = 0; i < 100; i++) {
            Car car = new Car();
            if (i % 10 == 0) {
                car.setGroup("Group " + i / 10);
            } else {
                car.setBrand("Brand:" + i);
            }
            car.setTypeName("Type:" + i);
            mDataList.add(car);
        }

        mAdapter = new RecyclerViewAdapter<>(this, mDataList
                , new int[]{R.layout.item_group_type, R.layout.item_list_type1}
                , new RecyclerViewMultipleTypeProcessor<Car>() {
            @Override
            public void onBindViewHolder(RecyclerViewViewHolder holder, int position, Car car) {

                if (getItemViewType(position) == mRvGroup.getViewType()) {
                    mRvGroup.onBind(holder, position, car);
                } else {
                    mRvItem.onBind(holder, position, car);
                }
            }

            @Override
            public int getItemViewType(int position) {
                if (TextUtils.isEmpty(mDataList.get(position).getGroup())) {
                    return mRvItem.getViewType();
                }
                return mRvGroup.getViewType();
            }
        });

        mRvList.setAdapter(mAdapter);
    }

    private void initListener() {
        mBtnAdd.setOnClickListener(this);
        mBtnRemove.setOnClickListener(this);
        mBtnUpdate.setOnClickListener(this);

        mRvList.addOnItemTouchListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                Log.e("onItemClick", "position:" + position);
            }
        });
        mRvList.addOnItemTouchListener(new OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder, int position) {
                Log.e("onItemLongClick", "position:" + position);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                break;
            case R.id.btn_remove:
                break;
            case R.id.btn_update:
                break;
            default:
                break;
        }
    }

    private Integer getPosition() {
        Integer position = null;
        try {
            position = Integer.valueOf(mEtPosition.getText().toString());
        } catch (Exception e) {

        }
        return position;
    }

}
