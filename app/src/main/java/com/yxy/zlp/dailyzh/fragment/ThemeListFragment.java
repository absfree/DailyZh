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

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yxy.zlp.dailyzh.activity.MainActivity;
import com.yxy.zlp.dailyzh.model.Theme;
import com.yxy.zlp.dailyzh.util.Constants;
import com.yxy.zlp.dailyzh.util.HttpUtils;
import com.yxy.zlp.dailyzhi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class ThemeListFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout mDrawerLayout;
    private ListView mThemeList;
    private List<Theme> mThemes;

    private TextView downloadTV;
    private TextView indexTV;
    private TextView collectTV;
    private TextView loginTV;

    private ThemeAdapter mAdapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_item_list, container, false);
        mDrawerLayout = (LinearLayout) view.findViewById(R.id.drawer_layout);
        loginTV = (TextView) view.findViewById(R.id.login);
        collectTV = (TextView) view.findViewById(R.id.collect);
        downloadTV = (TextView) view.findViewById(R.id.download);
        downloadTV.setOnClickListener(this);
        indexTV = (TextView) view.findViewById(R.id.index);
        indexTV.setOnClickListener(this);
        mThemeList = (ListView) view.findViewById(R.id.item_list);
        mThemeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                getFragmentManager().beginTransaction().replace(R.id.main_content,
                        new ThemeFragment(mThemes.get(position).getId(), mThemes.get(position).getTitle()),
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
        if (HttpUtils.isOnline(mActivity)) {
           HttpUtils.getJson(Constants.THEME_LIST, new AsyncHttpResponseHandler() {
               @Override
               public void onSuccess(int i, Header[] headers, byte[] bytes) {
                   String jsonString = new String(bytes);
                   parseThemesJson(jsonString);
               }

               @Override
               public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

               }
           });

        } else {
            Toast.makeText(mActivity, R.string.offline, Toast.LENGTH_SHORT);
        }
    }

    private void parseThemesJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray themesArray = jsonObject.getJSONArray("others");
            for (int i = 0; i < themesArray.length(); i++) {
                Theme theme = new Theme();
                JSONObject themeObject = themesArray.getJSONObject(i);
                theme.setTitle(themeObject.getString("name"));
                theme.setId(themeObject.getString("id"));
                mThemes.add(theme);
            }
            mAdapter = new ThemeAdapter();
            mThemeList.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
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
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.theme, parent, false);
            }
            TextView tv_item = (TextView) convertView
                    .findViewById(R.id.tv_item);
            tv_item.setTextColor(getResources().getColor(android.R.color.black));
            tv_item.setText(mThemes.get(position).getTitle());
            return convertView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index:
                ((MainActivity) mActivity).loadContent();
                ((MainActivity) mActivity).closeDrawer();
                break;
        }
    }

}
