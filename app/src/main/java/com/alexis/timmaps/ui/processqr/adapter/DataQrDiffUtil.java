package com.alexis.timmaps.ui.processqr.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.alexis.timmaps.domain.processqr.model.DataQr;

import java.util.Objects;

public class DataQrDiffUtil extends DiffUtil.ItemCallback<DataQr> {

    @Override
    public boolean areItemsTheSame(@NonNull DataQr oldItem, @NonNull DataQr newItem) {
        return Objects.equals(oldItem.getLat(), newItem.getLat())
                && Objects.equals(oldItem.getLon(), newItem.getLon());
    }

    @Override
    public boolean areContentsTheSame(@NonNull DataQr oldItem, @NonNull DataQr newItem) {
        return Objects.equals(oldItem.getLabel(), newItem.getLabel()) &&
                Objects.equals(oldItem.getObservation(), newItem.getObservation());
    }
}
