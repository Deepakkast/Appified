package app.appified.Fregment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import app.appified.Adapter.PagfAdapter;
import app.appified.Adapter.ThirdFragmentAdapter;
import app.appified.Config.ApolloClientService;
import app.appified.Config.App;
import app.appified.Database.AppModel;
import app.appified.GetPagfData;
import app.appified.R;
import app.appified.Utils.LogUtil;
import app.appified.modelclass.AppList;
import app.appified.modelclass.FriendDetailDto;
import app.appified.modelclass.PagfListModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagfFragment extends Fragment {
    private List<PagfListModel> pagfItemLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private PagfAdapter pgfAdapter;
    private EditText search;
    private ImageView clear;
    private CardView cardSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pagf_tab, container, false);
        recyclerView = view.findViewById(R.id.pgf_recycler_view);
        sortByDate();
        pgfAdapter = new PagfAdapter(pagfItemLists,getContext());
        recyclerView.setHasFixedSize(true);
        search = view.findViewById(R.id.et_pgf_search);
        clear = view.findViewById(R.id.pgf_clear_search);
        cardSearch = view.findViewById(R.id.card_view_search_pgf);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(pgfAdapter);
        //prepareData();
        Log.d("TAG","lists"+pagfItemLists);
        getPagfData(App.getPreferences().getUserId());
        // for visibilty of clear sign
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear.setVisibility(View.VISIBLE);
            }
        });

        // for clear text in search view
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });

        // search view for searching given list by name
        search.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("")){ // for space ignore
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
        ArrayList<PagfListModel> filteredlist = new ArrayList<>();
        for (PagfListModel list : pagfItemLists){
            if (list.getAppName().toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(list);
            }
        }
        pgfAdapter.filterList(filteredlist);
    }

    private void getPagfData(String user_id) {

        ApolloClientService.getPagfData(user_id, new ApolloClientService.OnRequestComplete() {
            @Override
            public void onSuccess(Object object) {
                final GetPagfData.Data  data=(GetPagfData.Data) object;
                LogUtil.debug("list size is" + data.getPagfData().size());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data.getPagfData()!=null)
                        {
                            pagfItemLists.clear();
                            for (int i = 0; i < data.getPagfData().size(); i++) {
                                PagfListModel pagf = new PagfListModel();
                                pagf.setAppName(data.getPagfData().get(i).appName());
                                pagf.setOnOfferPrice(data.getPagfData().get(i).discountPrice());
                                pagf.setIcon(data.getPagfData().get(i).icon());
                                pagf.setTimer(data.getPagfData().get(i).endOfSale());
                                pagf.setOriginalPrice(data.getPagfData().get(i).actualPrice());
                                pagf.setIsFree(data.getPagfData().get(i).isFree());
                                pagf.setPackageName(data.getPagfData().get(i).packageName());
                                // Log.d(TAG, String.valueOf(data.friendList().get(i)));
                                pagfItemLists.add(pagf);
                            }
                            pgfAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }

            @Override
            public void onFailure(String errorMessage, int errorCode) {

                LogUtil.debug("you are in failure");
            }
        });
        }

    public void sortByDate()
    {
        Collections.sort(pagfItemLists, new Comparator<PagfListModel>() {
            @Override
            public int compare(PagfListModel o1, PagfListModel o2) {
                return o1.getTimer().compareToIgnoreCase(o2.getTimer());
            }
        });
    }

}
