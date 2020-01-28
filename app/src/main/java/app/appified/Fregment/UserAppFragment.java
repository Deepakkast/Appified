package app.appified.Fregment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.activeandroid.query.Select;
import com.apollographql.apollo.ApolloClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.appified.Activity.AppHideUnhide;
import app.appified.Adapter.AppsAdapter;
import app.appified.AppUninstall;
import app.appified.Config.ApolloClientService;
import app.appified.Config.App;
import app.appified.Database.AppModel;
import app.appified.R;
import app.appified.Utils.LogUtil;

public class UserAppFragment extends Fragment {
    private static final String TAG = "MyActivity";
    public static ApolloClient apolloClient;
    static AppsAdapter appsAdapter;
    static AppsAdapter appsAdapterPending;
    static List<AppModel> appModelList = new ArrayList<>();
    static List<AppModel> decsion_pending = new ArrayList<>();
    static List<AppModel> decision_confirm = new ArrayList<>();
    private static int index;
    private static String packageName;
    public CardView cardViewPendingApps;
    ImageButton filter;
    AppHideUnhide appHideUnhide = new AppHideUnhide();
    RecyclerView recyclerViewPendingApps;
    RecyclerView recyclerViewApps;
    EditText searchText;
    ImageView clearSearchText;
    CardView searchCard;
    AppModel appModel = new AppModel();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_app, container, false);

        //uninstall broadcast reciver

        if (getActivity() != null) {
            LogUtil.debug("Broad cast starting................. ");
            IntentFilter uninstallIntent = new IntentFilter();
            uninstallIntent.addAction(Intent.ACTION_PACKAGE_REMOVED);
            uninstallIntent.addDataScheme("package");
            getActivity().registerReceiver(new UserAppFragment.AppUninstallReciver(), uninstallIntent);
        }

        cardViewPendingApps = view.findViewById(R.id.card_view_pending_apps);
        recyclerViewApps = view.findViewById(R.id.my_recycler_view);
        searchText = view.findViewById(R.id.et_search_user_app);
        clearSearchText = view.findViewById(R.id.clear_search_user_app);
        filter = view.findViewById(R.id.filter_image);
        recyclerViewApps = (RecyclerView) view.findViewById(R.id.my_recycler_view);
//          for all apps
        recyclerViewApps = view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewApps.setLayoutManager(layoutManager);

//        for pending apps
        recyclerViewPendingApps = view.findViewById(R.id.recycler_view_pending_apps);
        LinearLayoutManager layoutManagerPendingApps = new LinearLayoutManager(requireContext());
        layoutManagerPendingApps.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewPendingApps.setLayoutManager(layoutManagerPendingApps);
        appModelList.clear();
        appModelList = new Select().from(AppModel.class).execute();
        for (int i = 0; i < appModelList.size(); i++) {
            LogUtil.debug(appModelList.get(i).app_name);
            LogUtil.debug(appModelList.get(i).is_hide);

        }
//              for search box

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchText.setVisibility(View.VISIBLE);
            }
        });
        clearSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });
        searchText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z]+")) { // here no space character
                            return cs;
                        }
                        return "";
                    }
                }
        });
        searchText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for space ignore
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z]+")) { // here no space character
                            return cs;
                        }
                        return "";
                    }
                }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


//        passing list for pending apps


        appsAdapterPending = new AppsAdapter(requireContext(), decsion_pending, new AppsAdapter.Callback() {
            @Override
            public void onClickUninstall(AppModel object) {


            }

            @Override
            public void onClick(int index, boolean isChecked) {
                decsion_pending.get(index).setIs_hide(isChecked);
                decsion_pending.get(index).setServer_sync(1);
                AppModel appModel = new AppModel();
                appModel.findServerSyncStatus(decsion_pending.get(index).getPackage_name(), 1);
                LogUtil.debug("before check value is : " + decsion_pending.get(index).getPackage_name() + decsion_pending.get(index).is_hide);
                appModel.findPackageNameAndHideAndShow(decsion_pending.get(index).getPackage_name(), !decsion_pending.get(index).is_hide);
                hideDetail(decsion_pending.get(index).package_name, decsion_pending.get(index).is_hide);
                LogUtil.debug("after check value is : " + decsion_pending.get(index).getPackage_name() + decsion_pending.get(index).is_hide);
                decision_confirm.add(decsion_pending.get(index));
                decsion_pending.remove(index);
                appsAdapter.notifyDataSetChanged();
                appsAdapterPending.notifyDataSetChanged();
            }
        });
        LogUtil.debug("pending list is : " + decsion_pending);
        recyclerViewPendingApps.setAdapter(appsAdapterPending);
        // sortList();
        appsAdapterPending.notifyDataSetChanged();

//        passing appmodelist for all apps

        appsAdapter = new AppsAdapter(requireContext(), decision_confirm,
                new AppsAdapter.Callback() {
                    @Override
                    public void onClickUninstall(AppModel object) {
                        Intent intent = new Intent(Intent.ACTION_DELETE);
                        intent.setData(Uri.parse("package:" + object.package_name));
                        startActivity(intent);
                        index = decision_confirm.indexOf(object);
                        Log.d("my activity", "Fregment uninstal : " + index);
                        packageName = object.package_name;
                        Log.d("my activity", "package name  : " + packageName);
                        decision_confirm.remove(packageName);
                    }

                    @Override
                    public void onClick(int index, boolean isChecked) {
                        if (App.getPreferences().getSortingFilter() == 1)
                            sortList();
                        else
                            sortByDate();

                        appsAdapter.notifyDataSetChanged();

                    }
                });

        recyclerViewApps.setAdapter(appsAdapter);
        appsAdapter.notifyItemChanged(0, appsAdapter.getItemCount());

        sortList();
        if (!decsion_pending.isEmpty()) {
            cardViewPendingApps.setVisibility(View.VISIBLE);
        } else {
            cardViewPendingApps.setVisibility(View.GONE);
        }
        appsAdapter.notifyDataSetChanged();
        appsAdapterPending.notifyDataSetChanged();
        //      hideDetail(appModel.package_name, appModel.is_hide);


        //show dialoge when user come first time
        if (!App.getPreferences().getIsPresent()) {
            //if user present but not select any option
            if (App.getPreferences().getUserAppStatusFlag() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setCancelable(false);
                builder.setTitle("Hide your app here");
                View view1 = View.inflate(getContext(), R.layout.dialoge_permission, null);
                builder.setView(view1);
                final RadioGroup radioGroup = view1.findViewById(R.id.permission_radio);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getPreferences().setIsShowDialog(true);
                        App.getPreferences().setUserAppStatusFlag(getSelectedItemPosition(radioGroup.getCheckedRadioButtonId()));
                        LogUtil.debug(App.getPreferences().getUserAppStatusFlag() + " is :");
                        dialog.cancel();
                    }


                });
                builder.show();
            }
        }

        filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setCancelable(false);
                    builder.setTitle("Filter");
                    View view1 = View.inflate(getContext(), R.layout.filter_layout, null);
                    builder.setView(view1);
                    final RadioGroup radioGroup = view1.findViewById(R.id.filter_permission);
                    if (App.getPreferences().getSortingFilter() == 1)
                        radioGroup.check(R.id.alphabetically_sort);
                    else
                        radioGroup.check(R.id.sort_by_install_date);
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.getPreferences().setSortingFilter(getSelectedItemPostionOfFilter(radioGroup.getCheckedRadioButtonId()));
                            LogUtil.debug(App.getPreferences().getSortingFilter() + " is :");
                            dialog.cancel();
                            final int statusOfRadio = App.getPreferences().getSortingFilter();
                            if (statusOfRadio == 1) {
                                sortList();
                            } else {
                                sortByDate();
                            }


                        }


                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


                    builder.show();
                }


            });
        return view;
        }



        //searching filter

    private void filter(String text) {
        ArrayList<AppModel> filteredlist = new ArrayList<>();
        for (AppModel list : decision_confirm) {
            if (list.getApp_name().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(list);
            }
        }
        appsAdapter.filterList(filteredlist);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.getPreferences().getSortingFilter() == 1) {
            sortList();
        } else {
            sortByDate();
        }

        appsAdapter.notifyItemChanged(0, appsAdapter.getItemCount());
        // hideDetail(appModel.package_name, appModel.is_hide);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //hideDetail(appModel.package_name, appModel.is_hide);
    }

    private int getSelectedItemPosition(int checkedRadioButtonId) {
        if (checkedRadioButtonId == R.id.let_me_choose_radio_button) {
            App.getPreferences().setUserAppStatusFlag(1);
            return 1;
        } else if (checkedRadioButtonId == R.id.show_radio_button) {
            App.getPreferences().setUserAppStatusFlag(2);
            return 2;
        } else if (checkedRadioButtonId == R.id.hide_radio_button) {
            App.getPreferences().setUserAppStatusFlag(3);
            return 3;
        }
        return 0;
    }


    private int getSelectedItemPostionOfFilter(int filterId) {
        if (filterId == R.id.alphabetically_sort) {
            return 1;
        } else {
            return 2;
        }
    }

    private void hideDetail(final String packageName, boolean ishide) {
        ApolloClientService.isHide(packageName, ishide, new ApolloClientService.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                try {
                    if (packageName != null) {
                        Log.d("TAG", "Your package name is: " + packageName);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage, int errorCode) {
            }
        });
    }

    public void sortList() {
        decsion_pending.clear();
        decision_confirm.clear();
        for (AppModel app : appModelList) {
            if (app.isServer_sync() == 0)
                decsion_pending.add(app);
            else {
                decision_confirm.add(app);
            }
        }
        Collections.sort(decision_confirm, new Comparator<AppModel>() {
            @Override
            public int compare(AppModel o1, AppModel o2) {
                String name = o2.getApp_name();
                return o1.getApp_name().compareToIgnoreCase(name);
            }
        });

        Collections.sort(decsion_pending, new Comparator<AppModel>() {
            @Override
            public int compare(AppModel o1, AppModel o2) {
                String name = o2.getApp_name();
                return o1.getApp_name().compareToIgnoreCase(name);
            }
        });

        appsAdapter.notifyDataSetChanged();
        appsAdapterPending.notifyDataSetChanged();

    }


    public void sortByDate() {
        decsion_pending.clear();
        decision_confirm.clear();
        for (AppModel app : appModelList) {
            if (app.isServer_sync() == 0) {
                decsion_pending.add(app);
            } else {
                decision_confirm.add(app);
            }

            Collections.sort(decision_confirm, new Comparator<AppModel>() {
                @Override
                public int compare(AppModel o1, AppModel o2) {
                    return o1.getInsatll_date().compareToIgnoreCase(o2.getInsatll_date());
                }
            });

            Collections.sort(decsion_pending, new Comparator<AppModel>() {
                @Override
                public int compare(AppModel o1, AppModel o2) {
                    return o1.getInsatll_date().compareToIgnoreCase(o2.getInsatll_date());
                }
            });

        }
        appsAdapter.notifyDataSetChanged();
        appsAdapterPending.notifyDataSetChanged();

    }


    public class AppUninstallReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.debug("IN Broadcast  ................. ");
            Log.d("appified receiver", "in on recive method : ");
            try {
                final String packageNames = intent.getData().getEncodedSchemeSpecificPart();
                if (packageNames != null) {
                    if (decision_confirm.size() != 0) {
                        decision_confirm.remove(index);
                        //  Log.d("my activity", "removeing index is " + appModelList.remove(index));
                        appsAdapter.notifyItemRemoved(index);
                        AppModel appModel = new AppModel();
                        appModel.deleteAppByPackage(packageNames);
                        LogUtil.debug("your deleted app is : " + packageNames);
                        appUninstall(packageNames);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        public void appUninstall(String packagename) {
            App.getAppContext().getApolloClient();
            ApolloClientService.isuninstall(packagename, new ApolloClientService.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    AppUninstall.Data data = (AppUninstall.Data) object;
                    if (data.unInstall() != null) {
                        LogUtil.debug("uninstall finish : " + data.unInstall());
                    }

                }

                @Override
                public void onFailure(String errorMessage, int errorCode) {

                    LogUtil.debug("you are in failure ");
                }
            });
        }

    }
}
