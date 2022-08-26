package com.shenby.launcher;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shenby.launcher.databinding.ItemAppBinding;


public class AppHolder extends RecyclerView.ViewHolder {
    private final ItemAppBinding mBinding;
    private final AppItemViewModel mItemViewModel;

    public AppHolder(@NonNull ItemAppBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
        final PackageManager packageManager = itemView.getContext().getPackageManager();
        mItemViewModel = new AppItemViewModel(packageManager);
        mBinding.setViewModel(mItemViewModel);
    }

    public void bind(@NonNull ResolveInfo info) {
        mItemViewModel.setResolveInfo(info);
        mBinding.executePendingBindings();
    }
}
