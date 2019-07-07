//package com.hse.yatoufang;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.hse.yatoufang.PersonalCenter.GuideAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
//    private ViewPager viewPager;
//    private GuideAdapter guideAdapter;
//    private ImageView[] imageViews;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.guide_layout);
//
//        viewPager = findViewById(R.id.viewPage);
//        initView();
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        for (int i = 0; i < 3; i++) {
//            if(position == i){
//                imageViews[i].setImageResource(R.drawable.circle_used);
//            }else{
//                imageViews[i].setImageResource(R.drawable.circle_unused);
//            }
//        }
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    private void initView(){
//        LayoutInflater inflater = getLayoutInflater();
//        List<View> views = new ArrayList<>(3);
//        views.add(inflater.inflate(R.layout.guide_item_1,null));
//        views.add(inflater.inflate(R.layout.guide_item_2,null));
//        views.add(inflater.inflate(R.layout.guide_item_3,null));
//
//        guideAdapter = new GuideAdapter(views);
//        viewPager.setAdapter(guideAdapter);
//
//        viewPager.addOnPageChangeListener(this);
//
//        imageViews = new ImageView[3];
//        imageViews[0] = findViewById(R.id.iv_circle1);
//        imageViews[1] = findViewById(R.id.iv_circle2);
//        imageViews[2] = findViewById(R.id.iv_circle3);
//
//        views.get(2).findViewById(R.id.btn_in).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"进入首页",Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//}
