package com.shenby.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;

import com.shenby.launcher.databinding.FragmentAppListBinding;

public class AppListFragment extends Fragment {
    public static final String TAG = "AppListFragment";
    private AppListViewModel mAppListViewModel;
    private AppAdapter mAppAdapter;
    private com.shenby.launcher.databinding.FragmentAppListBinding mBinding;
    private IntentFilter mIntentFilter;
    private BroadcastReceiver mBroadcastReceiver;
    private final LifecycleObserver mLifecycleObserver = new LifecycleObserver() {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void registerReceiver() {
            Log.d(TAG, "registerReceiver: ");
            requireActivity().registerReceiver(mBroadcastReceiver, mIntentFilter);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void unregisterReceiver() {
            Log.d(TAG, "unregisterReceiver: ");
            requireActivity().unregisterReceiver(mBroadcastReceiver);
        }

    };

    public static AppListFragment newInstance() {
        final AppListFragment fragment = new AppListFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mAppListViewModel = provider.get(AppListViewModel.class);
        mAppAdapter = new AppAdapter();
        mIntentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        mIntentFilter.addDataScheme("package");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: refresh application list");
                mAppListViewModel.loadPackageList();
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        mBinding = FragmentAppListBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
//        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL));
        mBinding.recyclerView.setAdapter(mAppAdapter);

        mAppListViewModel.getPackageList()
                .observe(getViewLifecycleOwner(), list -> mAppAdapter.submitList(list));
        mAppListViewModel.loadPackageList();
        getViewLifecycleOwner().getLifecycle().addObserver(mLifecycleObserver);
    }

    @Override
    public void onDestroyView() {
        getViewLifecycleOwner().getLifecycle().removeObserver(mLifecycleObserver);
        mBinding = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
