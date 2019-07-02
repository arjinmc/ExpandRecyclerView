package com.arjinmc.recyclerviewexpandsample;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.arjinmc.expandrecyclerview.adapter.RecyclerViewAdapter;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewMultipleTypeProcessor;
import com.arjinmc.expandrecyclerview.adapter.RecyclerViewViewHolder;
import com.arjinmc.expandrecyclerview.style.RecyclerViewStyleHelper;
import com.arjinmc.recyclerviewdecoration.RecyclerViewItemDecoration;
import com.arjinmc.recyclerviewexpandsample.exception.OutOfRangeException;
import com.arjinmc.recyclerviewexpandsample.model.Car;
import com.arjinmc.recyclerviewexpandsample.model.Mode;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample for Vertical List
 * Created by Eminem Lo on 2017/8/18.
 * email: arjinmc@hotmail.com
 */

public class ListActivity extends AppCompatActivity {

    private int mNewDataPosition = -1;
    private RecyclerView mRvList;
    private RecyclerViewAdapter mAdapter;
    private List<Car> mDataList = new ArrayList<>();

    private int mCurrentMode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setSubtitle("List");

        RadioGroup rgMode = findViewById(R.id.rg_mode);
        rgMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_add:
                        mCurrentMode = Mode.ADD;
                        break;
                    case R.id.rb_change:
                        mCurrentMode = Mode.CHANGE;
                        break;
                    case R.id.rb_remove:
                        mCurrentMode = Mode.REMOVE;
                        break;
                    case R.id.rb_move:
                        mCurrentMode = Mode.MOVE;
                        break;
                    default:
                        break;
                }
            }
        });

        mRvList = findViewById(R.id.rv_list);
        RecyclerViewStyleHelper.toLinearLayout(mRvList, RecyclerView.VERTICAL);
        mRvList.addItemDecoration(new RecyclerViewItemDecoration.Builder(this)
                .color(Color.GREEN)
                .thickness(1)
                .create());

        for (int i = 0; i < 100; i++) {
            Car car = new Car();
            car.setBrand("Car Brand" + i);
            car.setTypeName("Car Type" + i * i);
            mDataList.add(car);
        }

        mAdapter = new RecyclerViewAdapter<>(this, mDataList
                , new int[]{R.layout.item_list_type0, R.layout.item_list_type1}
                , new RecyclerViewMultipleTypeProcessor<Car>() {
            @Override
            public void onBindViewHolder(RecyclerViewViewHolder holder, int position, Car object) {
                TextView textView = holder.getView(R.id.tv_content);
                textView.setText(object.getBrand() + "/" + object.getTypeName());
            }

            @Override
            public int getItemViewType(int position) {
                //define two viewTypes
                if (position % 2 == 0) {
                    return 1;
                }
                return 0;
            }
        });
        mRvList.setAdapter(mAdapter);

    }

    private void addCar(Integer position) {

        mNewDataPosition++;

        if (isOutOfRange(position)) {
            try {
                throw new OutOfRangeException();
            } catch (OutOfRangeException e) {
                e.printStackTrace();
                return;
            }
        }
        int index = (position == null ? 0 : position);
        Car car = new Car();
        car.setBrand("Car Brand Add" + mNewDataPosition);
        car.setTypeName("Car Type Add" + mNewDataPosition * mNewDataPosition);
        mDataList.add(index, car);

//        mAdapter.notifyDataItemInserted(index, car);
        //or
        mAdapter.notifyDataChanged(mDataList);

    }

    private void addCarList(Integer startPosition, int itemCount) {

        if (isOutOfRange(startPosition)) {
            throw new IllegalArgumentException("The position is out of the range of data set");
        }

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            mNewDataPosition++;
            Car car = new Car();
            car.setBrand("Car Brand Add" + mNewDataPosition);
            car.setTypeName("Car Type Add" + mNewDataPosition * mNewDataPosition);
            cars.add(car);
        }

        int index = (startPosition == null ? 0 : startPosition);
        mDataList.addAll(index, cars);

//        mAdapter.notifyDataItemRangeInserted(index, cars);
        //or
        mAdapter.notifyDataChanged(mDataList);
    }

    private void changeCar(Integer position) {

        mNewDataPosition++;
        if (isOutOfRange(position)) {
            try {
                throw new OutOfRangeException();
            } catch (OutOfRangeException e) {
                e.printStackTrace();
                return;
            }
        }
        int index = (position == null ? 0 : position);
        Car car = mDataList.get(index);
        car.setBrand("Car Brand Update" + mNewDataPosition);
        car.setTypeName("Car Type Update" + mNewDataPosition * mNewDataPosition);
        mDataList.set(index, car);

//        mAdapter.notifyDataItemChanged(index, car);
        //or
        mAdapter.notifyDataChanged(mDataList);

    }

    private void changeCarList(Integer startPosition, int itemCount) {

        if (startPosition != null
                && (isOutOfRange(startPosition) || (isOutOfRange(startPosition + itemCount)))) {
            try {
                throw new OutOfRangeException();
            } catch (OutOfRangeException e) {
                e.printStackTrace();
                return;
            }
        }

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            mNewDataPosition++;
            Car car = new Car();
            car.setBrand("Car Brand Update" + mNewDataPosition);
            car.setTypeName("Car Type Update" + mNewDataPosition * mNewDataPosition);
            cars.add(car);
        }

        int index = (startPosition == null ? 0 : startPosition);
        for (int i = 0; i < itemCount; i++) {
            mDataList.set(index + i, cars.get(i));
        }

        mAdapter.notifyDataItemRangeChanged(index, cars);
        //or
//        mAdapter.notifyDataChanged(mDataList);

    }

    private void removeCar(Integer position) {
        if (isOutOfRange(position)) {
            try {
                throw new OutOfRangeException();
            } catch (OutOfRangeException e) {
                e.printStackTrace();
                return;
            }
        }
        int index = (position == null ? 0 : position);

        mDataList.remove(index);

//        mAdapter.notifyDataItemRemoved(index);
        //or
        mAdapter.notifyDataChanged(mDataList);

    }

    private void removeCarList(Integer startPosition, int itemCount) {
        if (startPosition != null
                && (isOutOfRange(startPosition) || isOutOfRange(startPosition + itemCount))) {
            try {
                throw new OutOfRangeException();
            } catch (OutOfRangeException e) {
                e.printStackTrace();
                return;
            }
        }
        int index = (startPosition == null ? 0 : startPosition);
        for (int i = itemCount; i > 0; i--) {
            mDataList.remove(index + i - 1);
        }

//        mAdapter.notifyDataItemRangeRemoved(index,itemCount);
        //or
        mAdapter.notifyDataChanged(mDataList);

    }

    private void moveCar(int fromPosition, int toPosition) {
        if (isOutOfRange(fromPosition) || isOutOfRange(toPosition)) {
            try {
                throw new OutOfRangeException();
            } catch (OutOfRangeException e) {
                e.printStackTrace();
                return;
            }
        }

        if (fromPosition == toPosition) {
            return;
        }

        Car car = mDataList.get(fromPosition);
        if (fromPosition > toPosition) {
            mDataList.remove(fromPosition);
            mDataList.add(toPosition, car);
        } else {
            mDataList.add(toPosition, car);
            mDataList.remove(fromPosition);
        }

//        mAdapter.notifyDataItemMoved(fromPosition, toPosition);
        //or
        mAdapter.notifyDataChanged(mDataList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                switch (mCurrentMode) {
                    case Mode.ADD:
                        addCar(null);
//                        addCarList(null,3);
                        break;
                    case Mode.CHANGE:
                        changeCar(mNewDataPosition + 1);
//                        changeCarList(mNewDataPosition+1,3);
                        break;
                    case Mode.REMOVE:
//                        removeCar(mNewDataPosition + 1);
                        removeCarList(null, 3);
                        break;
                    case Mode.MOVE:
                        moveCar(1, 3);
                        break;
                    default:
                        break;
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isOutOfRange(Integer position) {
        if (position != null && (position < 0 || position > mDataList.size())) {
            return true;
        }
        return false;
    }

}
