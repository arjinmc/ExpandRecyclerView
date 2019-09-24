package com.arjinmc.recyclerviewdecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Grid mode ItemDecoration
 * Created by Eminem Lo on 2019-09-24.
 * email: arjinmc@hotmail.com
 */
public class RecyclerViewGridItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * default decoration color
     */
    private static final String DEFAULT_COLOR = "#bdbdbd";
    /**
     * default thickness for diver
     */
    private static final int DEFAULT_THICKNESS = 2;

    /**
     * the color for the diver
     */
    private int mColor;
    /**
     * the thickness for the diver
     */
    private int mThickness;
    /**
     * horizontalSpacing
     */
    private int mHorizontalSpacing = 0;
    /***
     * the horzontailSpacing that user wanted
     */
    private int mRequestHorizontalSpacing = 0;
    /**
     * VerticalSpacing
     */
    private int mVerticalSpacing = 0;

    public void setParams(Param param) {

        mColor = param.color;
        mThickness = param.thickness;
        mHorizontalSpacing = param.horizontalSpacing;
        mVerticalSpacing = param.verticalSpacing;
    }

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    public static class Builder {

        private Param params;

        public Builder(Context context) {

            params = new Param();
        }

        public RecyclerViewGridItemDecoration create() {
            RecyclerViewGridItemDecoration recyclerViewItemDecoration = new RecyclerViewGridItemDecoration();
            recyclerViewItemDecoration.setParams(params);
            return recyclerViewItemDecoration;
        }


        public Builder color(@ColorInt int color) {
            params.color = color;
            return this;
        }

        public Builder color(String color) {
            if (RVItemDecorationUtil.isColorString(color)) {
                params.color = Color.parseColor(color);
            }
            return this;
        }

        public Builder thickness(int thickness) {
            if (thickness % 2 != 0) {
                thickness += 1;
            }
            if (thickness <= 2) {
                thickness = 2;
            }
            params.thickness = thickness;
            return this;
        }

        public Builder horizontalSpacing(int spacing) {
            if (spacing < 0) {
                spacing = 0;
            }
            if (spacing % 2 != 0) {
                spacing += 1;
            }
            params.horizontalSpacing = spacing;
            return this;
        }

        public Builder verticalSpacing(int spacing) {
            if (spacing < 0) {
                spacing = 0;
            }
            if (spacing % 2 != 0) {
                spacing += 1;
            }
            params.verticalSpacing = spacing;
            return this;
        }
    }

    private static class Param {

        public int color = Color.parseColor(DEFAULT_COLOR);
        public int thickness = DEFAULT_THICKNESS;
        public int horizontalSpacing = 0;
        public int verticalSpacing = 0;
    }
}
