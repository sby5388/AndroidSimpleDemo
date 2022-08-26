package com.shenby.launcher;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppListViewModel extends AndroidViewModel {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<ResolveInfo>> mPackageList = new MutableLiveData<>(new ArrayList<>());
    private final Comparator<ResolveInfo> mInfoComparator = (o1, o2) -> {
        final PackageManager pm = getApplication().getPackageManager();
        return String.CASE_INSENSITIVE_ORDER.compare(
                o1.loadLabel(pm).toString(), o2.loadLabel(pm).toString()
        );
    };

    private final Runnable mRunnable = () -> {
        final PackageManager pm = getApplication().getPackageManager();
        final Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> list = pm.queryIntentActivities(launcherIntent, PackageManager.MATCH_ALL);
        list.sort(mInfoComparator);
        final String packageName = getApplication().getApplicationInfo().packageName;
        for (ResolveInfo info : list) {
            if (info == null) {
                continue;
            }
            if (TextUtils.equals(packageName, info.activityInfo.packageName)) {
                list.remove(info);
                break;
            }
        }
        mPackageList.postValue(list);
    };

    public AppListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ResolveInfo>> getPackageList() {
        return mPackageList;
    }


    public void loadPackageList() {
        mExecutor.execute(mRunnable);
    }
}
