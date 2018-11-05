package com.justtennis.plugin.shared.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    private T item;

    public RecyclerViewHolder(View view) {
        super(view);
    }

    public void set(T item) {
        this.item = item;
    }

    public T get() {
        return item;
    }
}
