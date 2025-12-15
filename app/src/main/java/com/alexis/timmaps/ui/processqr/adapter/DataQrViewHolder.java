package com.alexis.timmaps.ui.processqr.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexis.timmaps.databinding.ItemDataQrBinding;
import com.alexis.timmaps.domain.processqr.model.DataQr;

public class DataQrViewHolder extends RecyclerView.ViewHolder {

    private final ItemDataQrBinding binding;

    public DataQrViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemDataQrBinding.bind(itemView);
    }

    public void bind(DataQr dataQr) {
        binding.tvLabel1d.setText(dataQr.getLabel());
        binding.tvObservationValue.setText(dataQr.getObservation());
    }
}
