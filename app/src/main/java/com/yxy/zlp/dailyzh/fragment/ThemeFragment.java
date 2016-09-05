package com.yxy.zlp.dailyzh.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yxy.zlp.dailyzh.activity.MainActivity;
import com.yxy.zlp.dailyzh.activity.NewsContentActivity;
import com.yxy.zlp.dailyzh.adapter.NewsItemAdapter;
import com.yxy.zlp.dailyzh.model.Story;
import com.yxy.zlp.dailyzh.model.ThemeNews;
import com.yxy.zlp.dailyzh.request.DailyService;
import com.yxy.zlp.dailyzh.request.RetrofitManager;
import com.yxy.zlp.dailyzh.util.httpUtil.HttpUtils;
import com.yxy.zlp.dailyzhi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class ThemeFragment extends BaseFragment {
    private ListView mNewsList;
    private ImageView titleIV;
    private TextView titleTV;
    private String themeId;
    private String themeName;
    private ThemeNews news;
    private NewsItemAdapter mAdapter;
    private SharedPreferences mSP;

    private DailyService mService = RetrofitManager.getService();

    public ThemeFragment(String id, String name) {
        themeId = id;
        themeName = name;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSP = PreferenceManager.getDefaultSharedPreferences(mActivity);
        ((MainActivity) mActivity).setToolbarTitle(themeName);
        View view = inflater.inflate(R.layout.theme_content, container, false);
        mNewsList = (ListView) view.findViewById(R.id.news_list);
        View listHeader = LayoutInflater.from(mActivity).inflate(
                R.layout.theme_content_header, mNewsList, false);
        titleIV = (ImageView) listHeader.findViewById(R.id.title_img);
        titleTV = (TextView) listHeader.findViewById(R.id.title);
        mNewsList.addHeaderView(listHeader);
        mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            @SuppressWarnings("deprecation")
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Story currentStory = (Story) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mActivity, NewsContentActivity.class);
                intent.putExtra(MainFragment.CURRENT_STORY, currentStory);

                String newsRead = mSP.getString("newsRead", "");
                if (!newsRead.contains(currentStory.getId() + "")) {
                    newsRead += currentStory.getId();
                }
                mSP.edit().putString("newsRead", newsRead);
                TextView newsTitle = (TextView) view.findViewById(R.id.title);
                newsTitle.setTextColor(getResources().getColor(R.color.read_textcolor));
                startActivity(intent);
            }
        });
        mNewsList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (mNewsList != null && mNewsList.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setRefreshEnable(enable);
                }
            }
        });
        return view;
    }

    @Override
    protected void initContent() {
        super.initContent();
        if (HttpUtils.isOnline(mActivity)) {
            Call<ThemeNews> themeNews = mService.getThemeNews(themeId);

            themeNews.enqueue(new Callback<ThemeNews>() {
                @Override
                public void onResponse(Call<ThemeNews> call, Response<ThemeNews> response) {
                    news = response.body();
                    updateThemeNews();
                }

                @Override
                public void onFailure(Call<ThemeNews> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(mActivity, R.string.offline, Toast.LENGTH_SHORT);
        }
    }

    private void updateThemeNews() {
        if (news != null) {
            titleTV.setText(news.getDescription());
            Picasso.with(mActivity).load(news.getImage()).into(titleIV);
            mAdapter = new NewsItemAdapter(mActivity, news.getStories());
            mNewsList.setAdapter(mAdapter);
        }
    }

}
