package com.alexis.timmaps.ui.processqr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.alexis.timmaps.databinding.ItemDataQrBinding;
import com.alexis.timmaps.domain.processqr.model.DataQr;

import java.util.function.Consumer;

public class DataQrAdapter extends ListAdapter<DataQr, DataQrViewHolder> {

    private final Runnable onMapClick;
    private final Consumer<DataQr> onCardClick;

    public DataQrAdapter(Runnable onMapClick, Consumer<DataQr> onCardClick) {
        super(new DataQrDiffUtil());
        this.onMapClick = onMapClick;
        this.onCardClick = onCardClick;
    }

    @NonNull
    @Override
    public DataQrViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDataQrBinding binding = ItemDataQrBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new DataQrViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull DataQrViewHolder holder, int position) {
        DataQr currentDataQr = getItem(position);
        holder.bind(currentDataQr, onMapClick, onCardClick);
    }
}
