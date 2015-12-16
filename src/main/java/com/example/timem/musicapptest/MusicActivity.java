package com.example.timem.musicapptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.MusicAdapter;
import adapter.ShouCangAdapter;
import adapter.TuijianAdapter;
import adapter.ViewAdapter;
import entity.Dao;
import entity.Fangfa;
import entity.MusicPlayer;
import entity.Shoucang;
import entity.Tuijian;
import service.MusicService;
import util.JavaScriptCall;
import util.RoundImageView;

public class MusicActivity extends Activity implements View.OnClickListener{
    private ViewPager viewPager;
    private RoundImageView roundImageView;
    private TextView empfehlen,music_index;
    public static TextView music_activity_name;
    private ImageView einrichten;
    public static ImageButton play_music_two;
    private View /*empfehlen_view*/web_layout,music_index_view;
    private List<View> viewList = new ArrayList<>();
    private ViewAdapter viewAdapter;
//    private EditText music_name;
    private LinearLayout linearLayout,show_play,find_music;
    private SharedPreferences sharedPreferences;
    private Dao dao = new Dao(this);
    public static int no  = 0;
    private ListView fangfa,shoucang,tuijian;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        getActionBar().hide();
        InitView();
        sharedPreferences = getSharedPreferences("PlayingStatus", Context.MODE_PRIVATE);
        MusicPlayer musicPlayer = dao.searchMusicId(sharedPreferences.getInt("position", 0));
        if (musicPlayer == null) {

        } else {
            music_activity_name.setText(musicPlayer.getPlayMusicName());
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("why", false);
        editor.putInt("status", 0);
        editor.commit();

        if (sharedPreferences.getInt("status", 0) == 0) {
            Drawable drawable = this.getResources().getDrawable(R.drawable.desk_play, null);
            play_music_two.setBackground(drawable);

        } else {
            Drawable drawable = this.getResources().getDrawable(R.drawable.desk_pause, null);
            play_music_two.setBackground(drawable);
        }

        ELInfo();


        ViewListener();
    }

    private void ELInfo() {

    }

    private void ViewListener() {
//        find_music.setOnClickListener(this);
        empfehlen.setOnClickListener(this);
        music_index.setOnClickListener(this);
//        linearLayout.setOnClickListener(this);
        show_play.setOnClickListener(this);
        play_music_two.setOnClickListener(this);

        fangfa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0 :
                        intent = new Intent(MusicActivity.this, LocalMusicActivity.class);
                        startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(MusicActivity.this, ShowPlayMusicActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    empfehlen.setTextColor(0xfff9ff4e);
                    music_index.setTextColor(0xffffffff);
                } else {
                    empfehlen.setTextColor(0xffffffff);
                    music_index.setTextColor(0xfff9ff4e);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void InitView() {
        play_music_two = (ImageButton) findViewById(R.id.play_music_two);
        music_activity_name = (TextView) findViewById(R.id.music_activity_name);
        show_play = (LinearLayout) findViewById(R.id.show_play);
        //主布局
        viewPager = (ViewPager) findViewById(R.id.show_view);

        empfehlen = (TextView) findViewById(R.id.empfehlen);
        music_index = (TextView) findViewById(R.id.music_index);
        roundImageView = (RoundImageView) findViewById(R.id.head_image);
        einrichten = (ImageView) findViewById(R.id.einrichten);
        //推荐    乐库      两个布局     定义
        //TODO
        web_layout = LayoutInflater.from(this).inflate(R.layout.web_layout, null);
        music_index_view = LayoutInflater.from(this).inflate(R.layout.main_music_index_layout,null);
        //添加viewpager
        viewList.add(web_layout);
        viewList.add(music_index_view);
        viewAdapter = new ViewAdapter(viewList);
        viewPager.setAdapter(viewAdapter);

        webView = (WebView) web_layout.findViewById(R.id.web_tuijain);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new JavaScriptCall(getApplicationContext()), "Android");
        webView.loadUrl("file:///android_asset/WebInfoToApp/index.html");
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_MOVE){
                    Log.d("=====","x:"+event.getX()+"Y:"+event.getY());
                    if (event.getY() < 750) { //有内容，多于1个时
                        // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                        viewPager.requestDisallowInterceptTouchEvent(true);
                    }
                }
                return false;
            }
        });

        //推荐
//        find_music = (LinearLayout) empfehlen_view.findViewById(R.id.main_find_music);
//        music_name = (EditText) empfehlen_view.findViewById(R.id.main_music_name);
        //乐库

        View fangfa_buju = LayoutInflater.from(this).inflate(R.layout.fangfa_buju_layout, null);

        shoucang = (ListView) music_index_view.findViewById(R.id.shoucang);

        List<Shoucang> shoucangList = new ArrayList<>();

        Shoucang shoucang0 = new Shoucang();
        shoucang0.setIcon(R.drawable.my_no1);
        shoucang0.setName("我喜欢的音乐0");
        shoucang0.setMore("0首");
        shoucangList.add(shoucang0);

        Shoucang shoucang1 = new Shoucang();
        shoucang1.setIcon(R.drawable.my_no1);
        shoucang1.setName("我喜欢的音乐1");
        shoucang1.setMore("1首");
        shoucangList.add(shoucang1);

        Shoucang shoucang2 = new Shoucang();
        shoucang2.setIcon(R.drawable.my_no1);
        shoucang2.setName("我喜欢的音乐2");
        shoucang2.setMore("2首");
        shoucangList.add(shoucang2);

        ShouCangAdapter shouCangAdapter = new ShouCangAdapter(this, shoucangList);
        shoucang.setAdapter(shouCangAdapter);


//        tuijian = (ListView) empfehlen_view.findViewById(R.id.tuijian);

      /*  List<Tuijian> tuijianList = new ArrayList<>();

        Tuijian tuijian0 = new Tuijian();
        tuijian0.setIcon_no1(R.drawable.music_head_no1);
        tuijian0.setName_no1("推荐1");
        tuijian0.setIcon_no2(R.drawable.music_head_no1);
        tuijian0.setName_no2("推荐2");
        tuijianList.add(tuijian0);

        TuijianAdapter tuijianAdapter = new TuijianAdapter(this, tuijianList);
        tuijian.setAdapter(tuijianAdapter);
*/

        shoucang.addHeaderView(fangfa_buju, null, false);




        fangfa = (ListView) fangfa_buju.findViewById(R.id.fangfa);

        List<Fangfa> fangfaList = new ArrayList<>();

        Fangfa fangfa0 = new Fangfa();
        fangfa0.setIcon(R.drawable.bendi);
        fangfa0.setText("本地歌曲");
        fangfaList.add(fangfa0);

        Fangfa fangfa1 = new Fangfa();
        fangfa1.setIcon(R.drawable.zuijin);
        fangfa1.setText("最近播放");
        fangfaList.add(fangfa1);

        Fangfa fangfa2 = new Fangfa();
        fangfa2.setIcon(R.drawable.xiazai);
        fangfa2.setText("下载管理");
        fangfaList.add(fangfa2);

        Fangfa fangfa3 = new Fangfa();
        fangfa3.setIcon(R.drawable.diantai);
        fangfa3.setText("我的电台");
        fangfaList.add(fangfa3);

        MusicAdapter musicAdapter = new MusicAdapter(this, fangfaList);
        fangfa.setAdapter(musicAdapter);



        //TODO
        int totalHeight = 0;        //listView高度初始化
        for (int i = 0, len = musicAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = musicAdapter.getView(i, null, fangfa);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = fangfa.getLayoutParams();
        params.height = totalHeight+ (fangfa.getDividerHeight() * (musicAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        fangfa.setLayoutParams(params);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (v.getId()) {
            case R.id.main_find_music :
                Intent intent = new Intent(MusicActivity.this, FindMusicActivity.class);
//                intent.putExtra("music_name",music_name.getText().toString());
                startActivity(intent);
                break;
            case R.id.empfehlen :
                viewPager.setCurrentItem(0);
                break;
            case R.id.music_index :
                viewPager.setCurrentItem(1);
                break;
            case R.id.show_play :
                Intent show_play_intent = new Intent(MusicActivity.this, MainActivity.class);

                startActivity(show_play_intent);
                break;
            case R.id.play_music_two :
                Intent serviveIntent = new Intent(MusicActivity.this, MusicService.class);
                if (no == 0) {
                    serviveIntent.putExtra("run", true);
                    no = 1;
                } else {
                    serviveIntent.putExtra("run", false);
                }

                if (sharedPreferences.getInt("status", 0) == 0) {
                    serviveIntent.putExtra("status", 0);
                    Drawable drawable = this.getResources().getDrawable(R.drawable.desk_pause, null);
                    play_music_two.setBackground(drawable);
                    editor.putInt("status", 1);

                } else {
                    serviveIntent.putExtra("status", 1);
                    Drawable drawable = this.getResources().getDrawable(R.drawable.desk_play, null);
                    play_music_two.setBackground(drawable);
                    editor.putInt("status", 0);
                }

                MusicPlayer musicPlayer = dao.searchMusicId(sharedPreferences.getInt("position", 0));
                serviveIntent.putExtra("address", musicPlayer.getPlayMusicPath());
                editor.commit();

                startService(serviveIntent);
                break;
        }
    }


}
