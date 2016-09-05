package com.yxy.zlp.dailyzh.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.yxy.zlp.dailyzh.util.httpUtil.HttpUtils;
import com.yxy.zlp.dailyzhi.R;

import java.io.File;

public class SplashActivity extends Activity {
    private File appCacheDir;
    private File imgFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCacheDir = getCacheDir();
        setContentView(R.layout.splash);
        ImageView startIV = (ImageView) findViewById(R.id.start);
        initImage(startIV);
    }

    private void initImage(ImageView imageView) {
        imgFile = new File(appCacheDir, "start.jpg");
        if (imgFile.exists()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        } else {
            imageView.setImageResource(R.drawable.start);
        }
        startScaleAnim(imageView);
    }

    private void startScaleAnim(ImageView imageView) {
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);

        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!HttpUtils.isOnline(SplashActivity.this)) {
                    Toast.makeText(SplashActivity.this, R.string.offline, Toast.LENGTH_SHORT);
                }
                startMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(scaleAnim);
    }

    private void startMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
