package com.arjinmc.recyclerviewexpandsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewSingleTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.expandrecyclerview.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Drag Item Activity
 * Created by Eminem Lo on 2018/12/3.
 * email: arjinmc@hotmail.com
 */
public class DragItemActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private List<String> mDataList;
    private RecyclerViewAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_list);
        RecyclerViewStyleHelper.toLinearLayout(mRecyclerView, LinearLayoutManager.VERTICAL);

        mDataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDataList.add("Item " + i);
        }
        mAdapter = new RecyclerViewAdapter<>(this, mDataList, R.layout.item_main_list
                , new RecyclerViewSingleTypeProcessor<String>() {
            @Override
            public void onBindViewHolder(RecyclerViewViewHolder holder, int position, String str) {
                TextView tvContent = holder.getView(R.id.tv_content);
                tvContent.setText(str);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                mDataList = ListUtil.move(mDataList, fromPosition, toPosition);
                mAdapter.notifyDataItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
}
