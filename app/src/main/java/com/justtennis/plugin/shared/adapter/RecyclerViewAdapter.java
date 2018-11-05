package com.justtennis.plugin.shared.adapter;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("WeakerAccess")
public abstract class RecyclerViewAdapter<T, H extends RecyclerViewHolder<T>> extends RecyclerView.Adapter<H> {

    public interface Comparator<T> {
        boolean areItemsTheSame(T oldItem, T newItem);
        boolean areContentsTheSame(T oldItem, T newItem);
    }

    @Nullable
    protected final Consumer<T> listener;

    private List<T> items = new ArrayList<>();

    public RecyclerViewAdapter(@Nullable Consumer<T> listener) {
        this.listener = listener;
    }

    /**
     * Override this method in order to speed up list refresh
     */
    protected Comparator<T> getComparator() {
        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull H holder, int position) {
        holder.set(get(position));
        if (null != listener) {
            // Notify the active callbacks interface that an item has been selected.
            holder.itemView.setOnClickListener( view -> listener.accept(holder.get()));
        }
    }

    public List<T> getList() {
        return items;
    }

    public T get(int index) {
        return items.get(index);
    }

    public void setList(List<T> newItems) {

        if (getComparator() == null || items.isEmpty()) {
            int itemCount = getItemCount();
            items = newItems == null ? new ArrayList<>() : newItems;
            // Fix : java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder
            notifyItemRangeRemoved(0, itemCount);
            notifyItemRangeInserted(0, items.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return getItemCount();
                }

                @Override
                public int getNewListSize() {
                    return newItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    T oldItem = items.get(oldItemPosition);
                    T newItem = newItems.get(newItemPosition);
                    return getComparator().areItemsTheSame(oldItem, newItem);
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    T oldItem = items.get(oldItemPosition);
                    T newItem = newItems.get(newItemPosition);
                    return getComparator().areContentsTheSame(oldItem, newItem);
                }
            });
            items = newItems;
            result.dispatchUpdatesTo(this);
        }
    }
}