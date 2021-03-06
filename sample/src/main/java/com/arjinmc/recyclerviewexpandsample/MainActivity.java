package com.arjinmc.recyclerviewexpandsample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewSingleTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.recyclerviewdecoration.RecyclerViewLinearItemDecoration;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvList = findViewById(R.id.rv_list);
        rvList.addItemDecoration(new RecyclerViewLinearItemDecoration.Builder(this)
                .color(Color.GRAY)
                .thickness(5)
                .create());
        RecyclerViewStyleHelper.toLinearLayout(rvList, RecyclerView.VERTICAL);
        String[] titles = getResources().getStringArray(R.array.best_practices);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter<>(this, Arrays.asList(titles)
                , R.layout.item_main_list
                , new RecyclerViewSingleTypeProcessor<String>() {
            @Override
            public void onBindViewHolder(RecyclerViewViewHolder holder, final int position, String str) {
                TextView textView = holder.getView(R.id.tv_content);
                textView.setText(str);
//                textView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        clickPosition(position);
//                    }
//                });
            }

        });
        rvList.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                clickPosition(position);
            }
        });

    }

    private void clickPosition(int position) {
        switch (position) {
            case 0:
                jumpToActivity(ListActivity.class);
                break;
            case 1:
                jumpToActivity(ListStickyActivity.class);
                break;
            case 2:
                jumpToActivity(ViewPagerDemoActivity.class);
                break;
            case 3:
                jumpToActivity(GridDiverActivity.class);
                break;
            case 4:
                jumpToActivity(GroupListActivity.class);
                break;
            case 5:
                jumpToActivity(GroupGridActivity.class);
                break;
            case 6:
                jumpToActivity(GroupStickyActivity.class);
                break;
            case 7:
                jumpToActivity(DragItemActivity.class);
                break;
            case 8:
                jumpToActivity(LooperLinearLayoutActivity.class);
                break;
            default:
                break;
        }
    }

    private void jumpToActivity(Class clz) {
        startActivity(new Intent(this, clz));
    }

}
