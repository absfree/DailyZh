package com.yxy.zlp.dailyzh.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yxy.zlp.dailyzh.activity.MainActivity;
import com.yxy.zlp.dailyzh.activity.NewsContentActivity;
import com.yxy.zlp.dailyzh.adapter.MainNewsAdapter;
import com.yxy.zlp.dailyzh.customView.Carousel;
import com.yxy.zlp.dailyzh.model.LatestNews;
import com.yxy.zlp.dailyzh.model.PrevNews;
import com.yxy.zlp.dailyzh.model.Story;
import com.yxy.zlp.dailyzh.request.DailyService;
import com.yxy.zlp.dailyzh.request.RetrofitManager;
import com.yxy.zlp.dailyzh.util.Constant;
import com.yxy.zlp.dailyzh.util.httpUtil.HttpUtils;
import com.yxy.zlp.dailyzhi.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends BaseFragment {
    public static final String TAG = MainFragment.class.getSimpleName();
    public static final String CURRENT_STORY = "currentStory";

    private DailyService mService = RetrofitManager.getService();

    private SharedPreferences mSP;
    private ListView mNewsList;
    private MainNewsAdapter mMainNewsAdapter;
    private Carousel mCarousel;
    private String mDate;
    private boolean isLoading;
    private Handler mHandler = new Handler();
    private LatestNews mLatestNews;
    private PrevNews mPrevNews;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSP = PreferenceManager.getDefaultSharedPreferences(mActivity);
        View rootView = inflater.inflate(R.layout.main_news, container, false);

        mNewsList = (ListView) rootView.findViewById(R.id.news_list);
        View listHeader = inflater.inflate(R.layout.carousel, null);
        mCarousel = (Carousel) listHeader.findViewById(R.id.carousel);
        mCarousel.setItemClickListener(new Carousel.OnNewsClickListener() {
            @Override
            public void onNewsClick(View v, LatestNews.TopStory topStory) {
                Story story = new Story();
                story.setId(topStory.getId());
                story.setTitle(topStory.getTitle());

                Intent intent = new Intent(mActivity, NewsContentActivity.class);
                intent.putExtra(MainFragment.CURRENT_STORY, story);
                startActivity(intent);
            }
        });
        mNewsList.addHeaderView(listHeader);
        mMainNewsAdapter = new MainNewsAdapter(mActivity);
        mNewsList.setAdapter(mMainNewsAdapter);
        mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 1) {
                    Story currentStory = (Story) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(mActivity, NewsContentActivity.class);
                    intent.putExtra(MainFragment.CURRENT_STORY, currentStory);
                    String newsRead = mSP.getString("newsRead", "");
                    if (!newsRead.contains(currentStory.getId() + "")) {
                        newsRead += currentStory.getId();
                    }
                    mSP.edit().putString("newsRead", newsRead);
                    TextView newsTitle = (TextView) view.findViewById(R.id.news_title);
                    newsTitle.setTextColor(getResources().getColor(R.color.read_textcolor));
                    startActivity(intent);
                } else {
                    Log.d("TAG", "do nothing.");
                }
            }
        });
        mNewsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mNewsList.getChildCount() > 0) {
                    boolean refreshEnable = (firstVisibleItem == 0) &&
                            (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setRefreshEnable(refreshEnable);
                    if ((firstVisibleItem + visibleItemCount == totalItemCount) && (!isLoading)) {
                        //int prevDate = Integer.parseInt(mDate);
                        loadPrevNews(mDate);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    protected void initContent(){
        isLoading = true;

        Call<LatestNews> latest = mService.getLatest();
        if (HttpUtils.isOnline(mActivity)) {
            latest.enqueue(new Callback<LatestNews>() {
                @Override
                public void onResponse(Call<LatestNews> call, Response<LatestNews> response) {
                    mLatestNews = response.body();
                    updateLatestList();
                }

                @Override
                public void onFailure(Call<LatestNews> call, Throwable t) {
                    Toast.makeText(mActivity, "首页数据加载失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(mActivity, "没有网络连接", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateLatestList() {
//        mLatestNews = (new Gson()).fromJson(jsonString, LatestNews.class);
//        mDate = mLatestNews.getDate();
        mCarousel.setTopStories(mLatestNews.getTop_stories());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                List<Story> stories = mLatestNews.getStories();
                Story topic = new Story();
                topic.setType(Constant.TOPIC);
                topic.setTitle("今日热闻");
                stories.add(0, topic);
                mMainNewsAdapter.addList(stories);
                isLoading = false;
            }
        });
    }

    private void loadPrevNews(String date) {
        isLoading = true;
        if (HttpUtils.isOnline(mActivity)) {
            Call<PrevNews> prevNews = mService.getPrev(date);

            prevNews.enqueue(new Callback<PrevNews>() {
                @Override
                public void onResponse(Call<PrevNews> call, Response<PrevNews> response) {
                    mPrevNews = response.body();
                    updatePrevList();
                }

                @Override
                public void onFailure(Call<PrevNews> call, Throwable t) {

                }
            });
        }
    }

    private void updatePrevList() {
        if (mPrevNews != null) {
            mDate = mPrevNews.getDate();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    List<Story> stories = mPrevNews.getStories();
                    Story topic = new Story();
                    topic.setType(Constant.TOPIC);
                    topic.setTitle(formatDate(mDate));
                    stories.add(0, topic);
                    mMainNewsAdapter.addList(stories);
                }
            });
        }
        isLoading = false;
    }

    private String formatDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String result = null;
        try {
            Date  date = dateFormat.parse(dateString);
            SimpleDateFormat finalDateFormat = new SimpleDateFormat("MMM dd EEE");
            result = finalDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

}
