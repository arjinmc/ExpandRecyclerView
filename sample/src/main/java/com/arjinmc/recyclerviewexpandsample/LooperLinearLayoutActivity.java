package com.arjinmc.recyclerviewexpandsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewSingleTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.recyclerviewdecoration.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eminem Lo on 2018/12/12.
 * email: arjinmc@hotmail.com
 */
public class LooperLinearLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView rvList = findViewById(R.id.rv_list);
        rvList.addItemDecoration(new RecyclerViewItemDecoration.Builder(this)
                .color(Color.GRAY)
                .thickness(5)
                .create());
        RecyclerViewStyleHelper.toLinearLayout(rvList, RecyclerView.VERTICAL);

        List<String> titles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            titles.add("item:" + i);
        }
        RecyclerViewAdapter adapter = new RecyclerViewAdapter<>(this, titles
                , R.layout.item_main_list
                , new RecyclerViewSingleTypeProcessor<String>() {
            @Override
            public void onBindViewHolder(RecyclerViewViewHolder holder, final int position, String str) {
                TextView textView = holder.getView(R.id.tv_content);
                textView.setText(str);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(LooperLinearLayoutActivity.this
                                , "Click item " + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
        rvList.setAdapter(adapter);
    }
}
