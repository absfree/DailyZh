package com.yxy.zlp.dailyzh.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.widget.FrameLayout;

import com.yxy.zlp.dailyzh.fragment.MainFragment;
import com.yxy.zlp.dailyzhi.R;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mContentLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private long firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        loadContent();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                replaceFragment();
                mRefreshLayout.setRefreshing(false);
            }
        });

        mContentLayout = (FrameLayout) findViewById(R.id.main_content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public void loadContent() {
        replaceFragment();
    }

    private void replaceFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content,
                new MainFragment(), MainFragment.TAG).commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeDrawer();
        }
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 3000) {
            Snackbar sb = Snackbar.make(mContentLayout, R.string.prompt, Snackbar.LENGTH_SHORT);
            sb.show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

    public void setToolbarTitle(String text) {
        toolbar.setTitle(text);
    }

    public void setRefreshEnable(boolean enable) {
        mRefreshLayout.setEnabled(enable);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

}
