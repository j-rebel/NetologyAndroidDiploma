package com.example.appdiploma;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class NoteDiffUtilCallback extends DiffUtil.Callback {
    private final List<Note> oldList;
    private final List<Note> newList;

    public NoteDiffUtilCallback(List<Note> oldList, List<Note> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldProduct = oldList.get(oldItemPosition);
        Note newProduct = newList.get(newItemPosition);
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Note oldProduct = oldList.get(oldItemPosition);
        Note newProduct = newList.get(newItemPosition);
        return oldProduct.getTitle().equals(newProduct.getTitle())
                && oldProduct.getText() == newProduct.getText()
                && oldProduct.getYear() == newProduct.getYear()
                && oldProduct.getMonth() == newProduct.getMonth()
                && oldProduct.getDay() == newProduct.getDay()
                && oldProduct.getState() == newProduct.getState();
    }
}
