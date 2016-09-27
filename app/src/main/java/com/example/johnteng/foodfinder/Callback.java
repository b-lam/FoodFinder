package com.example.johnteng.foodfinder;

/**
 * Created by Brandon on 9/27/2016.
 */
public abstract class Callback <T extends Object> {
    public abstract void onResult(T result);
}
