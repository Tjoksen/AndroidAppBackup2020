package com.talent.posdat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageSliderActivity extends AppCompatActivity {
        private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        viewPager=findViewById(R.id.view_pager_browse);
        ImageAdapter adapter=new ImageAdapter(this);
        viewPager.setAdapter(adapter);

    }

    public  class  ImageAdapter extends PagerAdapter{
        private Context mContext;
        private int[] GalImages=new int[]{
        R.drawable.fbphoto,R.drawable.fbphoto2,R.drawable.presentation,R.drawable.justice
        };
        ImageAdapter(Context context){
            mContext=context;
        }
        @Override
        public int getCount() {
            return GalImages.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView=new ImageView(mContext);
        int padding=mContext.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        imageView.setPadding(padding,padding,padding,padding);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(GalImages[position]);
        container.addView(imageView,0);
        return  imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((ImageView)object);
        }
    }
}
