package com.yxy.zlp.dailyzh.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yxy.zlp.dailyzh.activity.MainActivity;
import com.yxy.zlp.dailyzh.model.Theme;
import com.yxy.zlp.dailyzh.model.ThemeList;
import com.yxy.zlp.dailyzh.request.DailyService;
import com.yxy.zlp.dailyzh.request.RetrofitManager;
import com.yxy.zlp.dailyzh.util.httpUtil.HttpUtils;
import com.yxy.zlp.dailyzhi.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ThemeListFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout mDrawerLayout;
    private ListView lvThemeList;
    private ThemeList mThemeList;
    private List<Theme> mThemes;

    private TextView downloadTV;
    private TextView indexTV;
    private TextView collectTV;
    private TextView loginTV;

    private ThemeAdapter mAdapter;

    private DailyService mService = RetrofitManager.getService();

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_item_list, container, false);
        mDrawerLayout = (LinearLayout) view.findViewById(R.id.drawer_header_layout);
        indexTV = (TextView) view.findViewById(R.id.index);
        indexTV.setOnClickListener(this);
        lvThemeList = (ListView) view.findViewById(R.id.item_list);
        lvThemeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                getFragmentManager().beginTransaction().replace(R.id.main_content,
                        new ThemeFragment(mThemes.get(position).getId(), mThemes.get(position).getName()),
                        "theme").commit();
                ((MainActivity) mActivity).closeDrawer();
            }
        });
        return view;
    }

    @Override
    protected void initContent() {
        super.initContent();
        mThemes = new ArrayList<>();
        Call<ThemeList> themeList = mService.getThemeList();
        

        if (HttpUtils.isOnline(mActivity)) {
            themeList.enqueue(new Callback<ThemeList>() {
                @Override
                public void onResponse(Call<ThemeList> call, Response<ThemeList> response) {
                    mThemeList = response.body();
                    updateThemeList();
                }

                @Override
                public void onFailure(Call<ThemeList> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(mActivity, R.string.offline, Toast.LENGTH_SHORT);
        }
    }

    private void updateThemeList() {
        if (mThemeList != null) {
            mThemes = mThemeList.getOthers();
            mAdapter = new ThemeAdapter();
            lvThemeList.setAdapter(mAdapter);
        }
    }

    public class ThemeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mThemes.size();
        }

        @Override
        public Object getItem(int position) {
            return mThemes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        @SuppressWarnings("deprecation")
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mActivity).inflate(
                        R.layout.theme, parent, false);
            }
            TextView tv_item = (TextView) convertView
                    .findViewById(R.id.tv_item);
            tv_item.setTextColor(getResources().getColor(android.R.color.black));
            tv_item.setText(mThemes.get(position).getName());
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
         mActivity.setTitle("首页");
        ((MainActivity) mActivity).loadContent();
        ((MainActivity) mActivity).closeDrawer();
    }

}
