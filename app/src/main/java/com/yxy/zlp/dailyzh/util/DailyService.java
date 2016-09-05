package com.yxy.zlp.dailyzh.request;

import com.yxy.zlp.dailyzh.model.LatestNews;
import com.yxy.zlp.dailyzh.model.NewsContent;
import com.yxy.zlp.dailyzh.model.PrevNews;
import com.yxy.zlp.dailyzh.model.ThemeList;
import com.yxy.zlp.dailyzh.model.ThemeNews;
import com.yxy.zlp.dailyzh.util.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2016/9/3.
 */
public interface DailyService {
    @GET(Constant.LATEST_NEWS)
    Call<LatestNews> getLatest();

    @GET(Constant.PREV_NEWS + "{date}")
    Call<PrevNews> getPrev(@Path("date") String date);

    @GET(Constant.NEWS_CONTENT + "{id}")
    Call<NewsContent> getNewsContent(@Path("id") int id);

    @GET(Constant.THEME + "{id}")
    Call<ThemeNews> getThemeNews(@Path("id") String id);

    @GET(Constant.THEME_LIST)
    Call<ThemeList> getThemeList();
}
