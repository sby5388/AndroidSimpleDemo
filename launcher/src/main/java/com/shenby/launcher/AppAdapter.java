package com.shenby.launcher;

import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.shenby.launcher.databinding.ItemAppBinding;


public class AppAdapter extends ListAdapter<ResolveInfo, AppHolder> {
    public AppAdapter() {
        super(new DiffUtil.ItemCallback<ResolveInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull ResolveInfo oldItem, @NonNull ResolveInfo newItem) {
                return TextUtils.equals(oldItem.activityInfo.packageName, newItem.activityInfo.packageName)
                        && TextUtils.equals(oldItem.activityInfo.name, newItem.activityInfo.name);
            }

            @Override
            public boolean areContentsTheSame(@NonNull ResolveInfo oldItem, @NonNull ResolveInfo newItem) {
                return TextUtils.equals(oldItem.toString(), newItem.toString());
            }
        });
    }

    @NonNull
    @Override
    public AppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemAppBinding binding = ItemAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AppHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppHolder holder, int position) {
        final ResolveInfo item = getItem(position);
        holder.bind(item);
    }
}
