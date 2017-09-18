package com.arjinmc.recyclerviewexpandsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewSingleTypeProcessor;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eminem Lo on 2017/8/18.
 * email: arjinmc@hotmail.com
 */

public class ViewPagerDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setSubtitle("ViewPager");

        RecyclerView rvList = (RecyclerView) findViewById(R.id.rv_list);
        RecyclerViewStyleHelper.toViewPager(rvList, LinearLayout.HORIZONTAL);
        List<String> dataList = new ArrayList<>();
        for(int i=0;i<10;i++){
            dataList.add(""+i);
        }
        rvList.setAdapter(new RecyclerViewAdapter<>(this,dataList,R.layout.item_viewpager
                ,new RecyclerViewSingleTypeProcessor<String>(){
            @Override
            public void onBindViewHolder(RecyclerViewViewHolder holder, int position, String object) {
                TextView tvContent = holder.getView(R.id.tv_content);
                tvContent.setText("item"+object);
            }
        }));


    }
}
