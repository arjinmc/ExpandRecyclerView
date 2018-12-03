package com.arjinmc.expandrecyclerview.util;

import java.util.List;

/**
 * Created by Eminem Lo on 2018/12/3.
 * email: arjinmc@hotmail.com
 */
public final class ListUtil {

    public static <E> List<E> move(List<E> dataList, int fromPosition, int toPosition) {
        if (dataList == null) {
            return dataList;
        }
        int dataSize = dataList.size();
        if (isBeyondListSize(dataSize, fromPosition) || isBeyondListSize(dataSize, toPosition)) {
            return dataList;
        }
        E data = dataList.get(fromPosition);
        if (fromPosition > toPosition) {
            dataList.remove(fromPosition);
            dataList.add(toPosition, data);
        } else {
            dataList.add(toPosition, data);
            dataList.remove(fromPosition);
        }
        return dataList;
    }

    /**
     * check if position is beyond the range of list index
     *
     * @param listSize
     * @param position
     * @return
     */
    public static boolean isBeyondListSize(int listSize, int position) {
        if (listSize == 0) {
            return true;
        }
        if (position < 0 || position >= listSize) {
            return true;
        }
        return false;
    }
}
