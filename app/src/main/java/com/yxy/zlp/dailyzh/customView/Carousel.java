package com.yxy.zlp.dailyzh.customView;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yxy.zlp.dailyzh.model.LatestNews;
import com.yxy.zlp.dailyzhi.R;

import java.util.ArrayList;
import java.util.List;

public class Carousel extends FrameLayout implements View.OnClickListener {
    private static final int DELAY = 3000;
    private Context mContext;
    private List<LatestNews.TopStory> mTopStories;
    private List<View> mViews;
    private List<ImageView> mDotsIV;
    private ViewPager mVP;
    private boolean isAutoPlay;
    private int currentItem;
    private OnNewsClickListener mOnNewsClickListener;
    private Handler mHandler = new Handler();

    public Carousel(Context context) {
        this(context, null);
    }

    public Carousel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Carousel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mTopStories = new ArrayList<>();
        mViews = new ArrayList<>();
        mDotsIV = new ArrayList<>();
    }

    public void setTopStories(List<LatestNews.TopStory> topStories) {
        mTopStories = topStories;
        initContent();
    }

    private void initContent() {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.carousel_content, this, true);
        mVP = (ViewPager) view.findViewById(R.id.view_pager);
        LinearLayout mDotsLayout = (LinearLayout) view.findViewById(R.id.dots);

        int len = mTopStories.size();
        //initialize mDotsLayout and mDotsIV.
        for (int i = 0; i < len; i++) {
            ImageView dotIV = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = params.rightMargin = 4;
            mDotsLayout.addView(dotIV, params);
            mDotsIV.add(dotIV);
        }
        //initialize mViews
        for (int i = 0; i < len; i++) {
            View itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.carousel_item, null);
            ImageView titleIV = (ImageView) itemView.findViewById(R.id.title_img);
            TextView title = (TextView) itemView.findViewById(R.id.title);
            Picasso.with(mContext).load(mTopStories.get(i).getImage()).into(titleIV);
            title.setText(mTopStories.get(i).getTitle());
            itemView.setOnClickListener(this);
            mViews.add(itemView);
        }

        mVP.setAdapter(new TopPagerAdapter());
        mVP.setFocusable(true);
        mVP.setCurrentItem(currentItem);
        mVP.addOnPageChangeListener(new TopOnPageChangeListener());
        startCarousel();
    }

    private void startCarousel() {
        isAutoPlay = true;
        mHandler.postDelayed(task, DELAY);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (isAutoPlay) {
                currentItem = (currentItem + 1) % (mTopStories.size());
                mVP.setCurrentItem(currentItem);
                mHandler.postDelayed(task, DELAY);
            } else {
                mHandler.postDelayed(task, DELAY);
            }
        }
    };

    class TopPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    class TopOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mDotsIV.size(); i++) {
                if (i == position) {
                    mDotsIV.get(i).setImageResource(R.drawable.dot_focus);
                } else {
                    mDotsIV.get(i).setImageResource(R.drawable.dot_blur);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                //SCROLL_STATE_DRAGGING
                case 1:
                    isAutoPlay = false;
                    break;
                //SCROLL_STATE_SETTLING
                case 2:
                    isAutoPlay = true;
                    break;
                default:
                    break;
            }
        }
    }

    public void setItemClickListener(OnNewsClickListener onNewsClickListener) {
        this.mOnNewsClickListener = onNewsClickListener;
    }

    public interface OnNewsClickListener {
        void onNewsClick(View v, LatestNews.TopStory topStory);
    }

    @Override
    public void onClick(View v) {
        if (mOnNewsClickListener != null) {
            LatestNews.TopStory topStory = mTopStories.get(mVP.getCurrentItem() - 1);
            mOnNewsClickListener.onNewsClick(v, topStory);
        }
    }

}




















