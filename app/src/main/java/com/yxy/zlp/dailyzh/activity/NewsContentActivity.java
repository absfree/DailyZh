package com.yxy.zlp.dailyzh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yxy.zlp.dailyzh.fragment.MainFragment;
import com.yxy.zlp.dailyzh.model.NewsContent;
import com.yxy.zlp.dailyzh.model.Story;
import com.yxy.zlp.dailyzh.request.DailyService;
import com.yxy.zlp.dailyzh.request.RetrofitManager;
import com.yxy.zlp.dailyzh.util.httpUtil.HttpUtils;
import com.yxy.zlp.dailyzhi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsContentActivity extends AppCompatActivity {;
    private WebView mWebView;
    private ImageView mImageView;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private Story mStory;
    private NewsContent mContent;
    private ShareActionProvider mShareActionProvider;

    private DailyService mService = RetrofitManager.getService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mStory = (Story) getIntent().getSerializableExtra(MainFragment.CURRENT_STORY);
        mImageView = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(mStory.getImages().get(0)).into(mImageView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(mStory.getTitle());

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //mWebView.getSettings().setDomStorageEnabled(true);
        //mWebView.getSettings().setDatabaseEnabled(true);
        //mWebView.getSettings().setAppCacheEnabled(true);

        if (HttpUtils.isOnline(this)) {
            Call<NewsContent> newsContent = mService.getNewsContent(mStory.getId());

            newsContent.enqueue(new Callback<NewsContent>() {
                @Override
                public void onResponse(Call<NewsContent> call, Response<NewsContent> response) {
                    mContent = response.body();
                    initWebViewContent();
                }

                @Override
                public void onFailure(Call<NewsContent> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(NewsContentActivity.this, R.string.offline, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_content_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressWarnings("deprecation")
    private Intent createShareNewsIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mContent.getShare_url());
        return shareIntent;
    }

    private void initWebViewContent() {
        //String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><body>" + mContent.getBody() + "</body></html>";
       // html = html.replace("<div class=\"img-place-holder\">", "");
       // html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
        mShareActionProvider.setShareIntent(createShareNewsIntent());
    }

}
