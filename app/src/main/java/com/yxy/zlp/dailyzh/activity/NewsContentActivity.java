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

import com.google.gson.Gson;
import com.yxy.zlp.dailyzh.fragment.MainFragment;
import com.yxy.zlp.dailyzh.model.NewsContent;
import com.yxy.zlp.dailyzh.model.Story;
import com.yxy.zlp.dailyzh.util.Constants;
import com.yxy.zlp.dailyzh.util.httpUtil.AsyncHttpUtils;
import com.yxy.zlp.dailyzh.util.httpUtil.HttpUtils;
import com.yxy.zlp.dailyzh.util.httpUtil.ResponseHandler;
import com.yxy.zlp.dailyzh.util.imageLoader.FreeImageLoader;
import com.yxy.zlp.dailyzhi.R;

public class NewsContentActivity extends AppCompatActivity {
    private WebView mWebView;
    private ImageView mImageView;
    private FreeImageLoader mFreeImageLoader;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private Story mStory;
    private NewsContent mContent;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        mFreeImageLoader = FreeImageLoader.getInstance(this);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mStory = (Story) getIntent().getSerializableExtra(MainFragment.CURRENT_STORY);
        mImageView = (ImageView) findViewById(R.id.image);
        mFreeImageLoader.displayImage(mStory.getImages().get(0), mImageView);
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
            AsyncHttpUtils.get(Constants.NEWS_CONTENT + mStory.getId(), new ResponseHandler() {
                @Override
                public void onSuccess(byte[] result) {
                    String jsonString = new String(result);
                    parseContentJson(jsonString);
                }

                @Override
                public void onFailure() {

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

    private void parseContentJson(String jsonString) {
        mContent = (new Gson()).fromJson(jsonString, NewsContent.class);

        initWebViewContent();
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
