package com.jlshix.wlife_v03.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jlshix.wlife_v03.R;
import com.jlshix.wlife_v03.adapter.EnvirAdapter;
import com.jlshix.wlife_v03.data.EnvirData;
import com.jlshix.wlife_v03.tool.BaseFragment;
import com.jlshix.wlife_v03.tool.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2016/6/14.
 * 环境 Fragment
 */

@ContentView(R.layout.fragment_device)
public class Envir extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "ENVIR_FRAG";

    @ViewInject(R.id.scroll)
    NestedScrollView scroll;

    @ViewInject(R.id.recycler)
    private RecyclerView recycler;

    @ViewInject(R.id.swipe)
    private SwipeRefreshLayout swipe;

    // 准备的数据
    private List<EnvirData> list;

    // Adapter
    private EnvirAdapter adapter;

    // 分布图部分
    @ViewInject(R.id.container)
    CardView container;

    CardView layout;
    LinearLayout[] layouts;


    public static final int REFRESH = 0x01;
    public static final int FOCUS_UP = 0x03;
    public static final int REFRESHING = 0x04;
    public static final int REF_SINGLE = 0x05;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    swipe.setRefreshing(true);
                    getData();
                    swipe.setRefreshing(false);
                    break;
                case FOCUS_UP:
                    scroll.fullScroll(View.FOCUS_UP);
                    break;
                case REFRESHING:
                    swipe.setRefreshing(true);
                    break;
                case REF_SINGLE:
                    getData();
                    swipe.setRefreshing(false);
                    break;
            }
        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipe.setOnRefreshListener(this);
        list = new ArrayList<>();
        list.add(new EnvirData());
        initView();
    }

    private void initView() {
        scroll.setNestedScrollingEnabled(true);
        scroll.setSmoothScrollingEnabled(true);
        adapter = new EnvirAdapter(getContext(), list, handler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        recycler.setNestedScrollingEnabled(false);
        recycler.setItemAnimator(new DefaultItemAnimator());
        adapter.notifyItemRangeChanged(0, list.size());
        scroll.fullScroll(View.FOCUS_UP);

        //分布图初始化
        switch (L.getLayout()) {
            case 1:
                layouts = L.initGraph(getActivity(), 1, container);
                break;
        }
        handler.sendEmptyMessage(REFRESH);
    }

    /**
     * 获取数据并显示
     */
    private void getData() {
        list.clear();
        RequestParams params = new RequestParams(L.URL_GET);
        params.addParameter("gate", L.getGateImei());
        params.addParameter("type", "02");
        x.http().post(params,
                new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject object = new JSONObject(result);
                            if (!object.optString("code").equals("1")) {
                                L.toast(getContext(), "ENVIR_CODE_ERR");
                                return;
                            }
                            json2List(object.optJSONArray("info"));
                            setGraph();
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    private void json2List(JSONArray info) {
                        list.clear();
                        for (int i = 0; i < info.length(); i++) {
                            JSONObject object = info.optJSONObject(i);
                            String name = object.optString("name");
                            String state = object.optString("state");
                            int sign = object.optInt("sign");
                            int placeNo = object.optInt("place");
                            int no = object.optInt("no");
                            list.add(new EnvirData(name, state, sign, placeNo, no));
                        }
                    }

                    // 刷新数据时用于更新分布图
                    private void setGraph() {
                        for (LinearLayout layout :
                                layouts) {
                            int count = layout.getChildCount();
                            if (count > 1) {
                                layout.removeViews(1, count - 1);
                            }
                        }
                        for (EnvirData data :
                                list) {
                            int place = data.getPlaceNo();
                            int sign = data.getSign();
                            ImageView img = new ImageView(getContext());
                            img.setImageResource(R.drawable.blue_selector);
                            img.setPadding(5, 5, 5, 5);
                            img.setColorFilter(L.signs[sign]);
                            layouts[place].addView(img);
                        }
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        L.toast(getContext(), ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(REFRESH);
    }

    public Handler getHandler() {
        return handler;
    }

}