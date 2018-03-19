package com.arjinmc.recyclerviewexpandsample.exception;

/**
 * OutOfRangeException
 * Created by Eminem Lo on 2018/3/15.
 * email: arjinmc@hotmail.com
 */

public class OutOfRangeException extends Exception {

    public OutOfRangeException(){
        super("The position is out of the range of the data set size");
    }
}
