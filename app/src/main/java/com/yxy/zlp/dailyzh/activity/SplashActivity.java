package com.yxy.zlp.dailyzh.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yxy.zlp.dailyzh.util.Constants;
import com.yxy.zlp.dailyzh.util.HttpUtils;
import com.yxy.zlp.dailyzhi.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import cz.msebera.android.httpclient.Header;

public class SplashActivity extends Activity {
    private File appCacheDir;
    private File imgFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCacheDir = StorageUtils.getCacheDirectory(this,false);
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
                if (HttpUtils.isOnline(SplashActivity.this)) {
                    HttpUtils.getJson(Constants.START_IMG, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            try {
                                JSONObject jsonObject = new JSONObject(new String(bytes));
                                String imgUrl = jsonObject.getString("img");
                                HttpUtils.getImage(imgUrl, new BinaryHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        saveStartImage(bytes);
                                        startMainActivity();
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                        startMainActivity();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            startMainActivity();
                        }
                    });
                } else {
                    Toast.makeText(SplashActivity.this, R.string.offline, Toast.LENGTH_SHORT);
                    startMainActivity();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(scaleAnim);
    }

    private void saveStartImage(byte[] imgBytes) {
        try {
            FileOutputStream fos = new FileOutputStream(imgFile);
            fos.write(imgBytes);
            fos.flush();
            fos.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
