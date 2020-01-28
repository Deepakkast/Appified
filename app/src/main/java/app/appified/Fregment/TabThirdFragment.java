package app.appified.Fregment;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import app.appified.Adapter.ThirdFragmentAdapter;
import app.appified.R;
import app.appified.modelclass.AppList;
/**
 * A simple {@link Fragment} subclass.
 */
public class TabThirdFragment extends Fragment {
    private List<AppList> itemLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private ThirdFragmentAdapter tfAdapter;
    private TextView search;
    private ImageView clear;
    private CardView cardSearch;
    public TabThirdFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_third, container, false);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        tfAdapter = new ThirdFragmentAdapter(itemLists,getContext());
        recyclerView.setHasFixedSize(true);
        search = view.findViewById(R.id.et_search);
        clear = view.findViewById(R.id.clear_search);
        cardSearch = view.findViewById(R.id.card_view_search);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tfAdapter);
        prepareData();
     /*   if (search.length()>=1){
            clear.setVisibility(View.VISIBLE);
        }else {
            clear.setVisibility(View.INVISIBLE);
        }
*/
       search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear.setVisibility(View.VISIBLE);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });
        search.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("")){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[a-zA-Z]+")){ // here no space character
                            return cs;
                        }
                        return "";
                    }
                }
        });

        search.addTextChangedListener(new TextWatcher() {
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
        return view;
    }
    private void filter(String text){
        ArrayList<AppList> filteredlist = new ArrayList<>();
        for (AppList list : itemLists){
            if (list.getAppName().toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(list);
            }
        }
        tfAdapter.filterList(filteredlist);
    }
    private void prepareData() {
        ArrayList<AppList> apps = getInstalledApps(false);
        itemLists.clear();
        itemLists.addAll(apps);
        tfAdapter.notifyDataSetChanged();
        final int max = apps.size();
        for (int i = 0; i < max; i++) {
            apps.get(i);
        }
    }
    private ArrayList<AppList> getInstalledApps(boolean getSysPackages) {
        ArrayList<AppList> res = new ArrayList<>();
        List<PackageInfo> packs = requireContext().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) == true) {                 // or   ((!getSysPackages) && (p.versionName == null))
                continue;
            }
            AppList newInfo = new AppList(p.applicationInfo.loadLabel(requireContext().getPackageManager()).toString(),
                    p.applicationInfo.loadIcon(getContext().getPackageManager()),
                    p.lastUpdateTime);
            res.add(newInfo);
        }
        return res;
    }
}
