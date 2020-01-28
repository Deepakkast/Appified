package app.appified.Fregment;


import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import app.appified.Adapter.AppUsageAdapter;
import app.appified.R;
import app.appified.modelclass.AppList;
import app.appified.modelclass.AppUsageAppList;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import static android.content.Context.APP_OPS_SERVICE;
import static android.content.Context.USAGE_STATS_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;




public class AppUsageFragment extends Fragment {
    List<AppUsageAppList> appModelList = new ArrayList<>();
    Context context;
    private RecyclerView recyclerView;
    private AppUsageAdapter mAdapter;
    private PieChart pieChart;
    public UsageStatsManager usageStatsManager;
    private PackageManager packageManager;
    private List<Integer> color1;
    PieDataSet dataSet;
    int i ;

    private static final int flags = PackageManager.GET_META_DATA |
            PackageManager.GET_SHARED_LIBRARY_FILES |
            PackageManager.GET_UNINSTALLED_PACKAGES;



    public AppUsageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_usage, container, false);
            color1 = new ArrayList<Integer>();
        //getcontext = this;
        recyclerView = view.findViewById(R.id.my_recycler_view);
        usageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        packageManager = getActivity().getPackageManager();


        color1 = new ArrayList<Integer>();
        pieChart = view.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        mAdapter = new AppUsageAdapter(appModelList, getContext());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (!checkUsageStatsAllowedOrNot()) {
            Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            usageAccessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(usageAccessIntent);
            if (checkUsageStatsAllowedOrNot()) {
                new BackgroundService().execute("");

            } else {
                Toast.makeText(getApplicationContext(), "Please give Access", Toast.LENGTH_SHORT).show();
            }
        } else {
            new BackgroundService().execute("");
        }

            return view;
    }
    public boolean checkUsageStatsAllowedOrNot() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getActivity().getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getActivity().getSystemService(APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error cannot find any usage stats manager", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onDestroy() {
        if (checkUsageStatsAllowedOrNot()) {
            getActivity().startService(new Intent(getActivity(), BackgroundService.class));
        }
        super.onDestroy();
    }

    private class BackgroundService extends AsyncTask<String, Void, List<AppUsageAppList>> {
        @Override
        protected List<AppUsageAppList> doInBackground(String... strings) {
            List<AppUsageAppList> list = new ArrayList<>();
//            for finding used time of particular app in given interval
            UsageStatsManager usageStatsManager = (UsageStatsManager) getActivity().getSystemService(USAGE_STATS_SERVICE);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            now.set(Calendar.HOUR, 0);
            now.set(Calendar.MINUTE, 0);
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.HOUR_OF_DAY, 0);
            Date date = now.getTime();
            long startTime = date.getTime();

            calendar.add(Calendar.DAY_OF_WEEK, -1);
            long endTime = System.currentTimeMillis();
            long beginTime = (startTime + 25200000) ;

            //  List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
           /* if (usageStats != null) {
                for (UsageStats usageStat : usageStats) {
                    // List<AppList> appList = new ArrayList<>();
                    AppUsageAppList appList1 = new AppUsageAppList(usageStat.getPackageName(), usageStat.getTotalTimeInForeground());
                    //Log.d(com.example.myapplication.BackgroundService.class.getSimpleName(), "app active or not " + appList1);
                    list.add(appList1);
                }
            }
            return list;

*/
//              second method

            List<String> installedApps = getInstalledAppList();
//            Map<String, UsageStats> usageStats = usageStatsManager.queryAndAggregateUsageStats(beginTime, System.currentTimeMillis());
            List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginTime, endTime);
            List<UsageStats> stats = new ArrayList<>();
            stats.addAll(usageStats);

            List<AppUsageAppList> finalList = buildUsageStatsWrapper(installedApps, stats);

            return finalList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<AppUsageAppList> list) {
            super.onPostExecute(list);
            appModelList.clear();
            appModelList.addAll(list);
            mAdapter.notifyDataSetChanged();
            chart();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private List<String> getInstalledAppList(){
        List<ApplicationInfo> infos = packageManager.getInstalledApplications(flags);
        List<String> installedApps = new ArrayList<>();
        for (ApplicationInfo info : infos){
            if (!isSystemPackage(info)) installedApps.add(info.packageName);
        }
        return installedApps;
    }
    //for getting only installed apps
    private boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private List<AppUsageAppList> buildUsageStatsWrapper(List<String> packageNames, List<UsageStats> usageStatses) {
        List<AppUsageAppList> list = new ArrayList<>();
        for (String name : packageNames) {
            boolean added = false;
            for (UsageStats stat : usageStatses) {
                if (name.equals(stat.getPackageName())) {
                    added = true;
                    list.add(fromUsageStat(stat));
                }
            }
            if (!added) {
                list.add(fromUsageStat(name));
            }
        }
        Collections.sort(list);
        return list;
    }
    private AppUsageAppList fromUsageStat(String packageName) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            return new AppUsageAppList(null, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private AppUsageAppList fromUsageStat(UsageStats usageStats) throws IllegalArgumentException {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(usageStats.getPackageName(), 0);
            return new AppUsageAppList(usageStats, packageManager.getApplicationIcon(ai), packageManager.getApplicationLabel(ai).toString());

        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void chart(){
        ArrayList foregroundTime = new ArrayList();
        long topFive =0;
        long totalTimeOfAllApps=0;
        for ( i =0;i<5;i++){
            //for icon color in slice
            Drawable icon = appModelList.get(i).getIcon() ;
            Bitmap bitmap1 = ((BitmapDrawable) icon).getBitmap();


            Palette.from(bitmap1).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    //work with the palette here   R.drawable.wallpaper
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null){
                        int rgb = swatch.getRgb();
                        color1.add(new Integer(swatch.getRgb()));
                    }
                }
            });
            long count = appModelList.get(i).getUsageStats().getTotalTimeInForeground();

            Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));

            foregroundTime.add(new PieEntry(count, d));
            topFive =topFive +count;
        }
        for (AppUsageAppList app : appModelList){
            if (app.getUsageStats() != null) {
                long count1 = app.getUsageStats().getTotalTimeInForeground();
                totalTimeOfAllApps = totalTimeOfAllApps + count1;
            }
        }
        Drawable icon = getResources().getDrawable(R.drawable.other_apps) ;
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
        long otherApps = totalTimeOfAllApps - topFive;
        foregroundTime.add(new PieEntry(otherApps, d));

        dataSet = new PieDataSet(foregroundTime, "App Stats");
        setChart();

        long second = (totalTimeOfAllApps/1000)%60;
        long minute = (totalTimeOfAllApps/(1000*60))%60;
        long hour = (totalTimeOfAllApps/(1000*60*60))%24;
        String appUsage1 = hour + " h " + minute + "m" + second +"s";
        pieChart.setCenterText(String.valueOf(appUsage1));
    }
    public void setChart(){
        dataSet.setSliceSpace(3f);
       // dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieChart.animateX(2000);
        dataSet.setColors(color1);
        dataSet.setValueLineColor(Color.WHITE);
        dataSet.setSelectionShift(10f);
        PieData data = new PieData( dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.RED);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawRoundedSlices(true);
        pieChart.getLegend().setEnabled(false);                 //for disabling bottom description
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setHoleRadius(60f);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setData(data);
    }
}


