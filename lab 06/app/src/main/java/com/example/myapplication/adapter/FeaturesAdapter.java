package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder> {
    
    private List<String> features;
    
    public FeaturesAdapter(List<String> features) {
        this.features = features;
    }
    
    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feature, parent, false);
        return new FeatureViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        String feature = features.get(position);
        holder.tvFeature.setText(feature);
    }
    
    @Override
    public int getItemCount() {
        return features != null ? features.size() : 0;
    }
    
    public void updateFeatures(List<String> newFeatures) {
        this.features = newFeatures;
        notifyDataSetChanged();
    }
    
    static class FeatureViewHolder extends RecyclerView.ViewHolder {
        TextView tvFeature;
        
        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFeature = itemView.findViewById(R.id.tvFeature);
        }
    }
}