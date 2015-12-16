package com.example.timem.musicapptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.FindMusicAdapter;
import entity.Dao;
import entity.MusicInfo;
import entity.MusicPlayer;

public class FindMusicActivity extends Activity implements View.OnClickListener{
    private EditText find_music;
    private ImageView back, suche;
    private Intent intent;
    private ListView show_find_info;
    private Dao dao = new Dao(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_music);
        intent = getIntent();
        getActionBar().hide();
        InitView();
        GetInfo();
        ViewListener();
    }

    private void GetInfo() {
        String musicName = intent.getStringExtra("music_name");
        find_music.setText(musicName);


    }

    private void ViewListener() {
        back.setOnClickListener(this);
        suche.setOnClickListener(this);
        //EditText 的回车监听
        find_music.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    List<MusicInfo> musicInfos = dao.searchNameLike(find_music.getText().toString());

                    if (musicInfos.size() == 0) {
                        Toast.makeText(FindMusicActivity.this, "没有找到相应的歌曲", Toast.LENGTH_SHORT).show();
                    } else {
                        FindMusicAdapter findMusicAdapter = new FindMusicAdapter(FindMusicActivity.this, musicInfos);
                        show_find_info.setAdapter(findMusicAdapter);
                    }
                    return true;
                }
                return false;
                //return true   回车不换行，直接执行，手机自带的返回按钮无效；
                //return false  执行，并且回车换行；
                //只有当以上情况的时候才能使    键盘输入回车不换行，并执行搜索功能，并且手机的返回按钮也可以使用
            }
        });
    }

    private void InitView() {
        find_music = (EditText) findViewById(R.id.find_music_text);
        back = (ImageView) findViewById(R.id.back);
        suche = (ImageView) findViewById(R.id.suche);

        show_find_info = (ListView) findViewById(R.id.show_find_info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_music, menu);
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
        switch (v.getId()) {
            case R.id.back :
                //back.setImageResource(R.drawable.back_g);
                finish();
                break;
            case R.id.suche :
                List<MusicInfo> musicInfos = dao.searchNameLike(find_music.getText().toString());

                if (musicInfos.size() == 0) {
                    Toast.makeText(FindMusicActivity.this, "没有找到相应的歌曲", Toast.LENGTH_SHORT).show();
                } else {
                    FindMusicAdapter findMusicAdapter = new FindMusicAdapter(this, musicInfos);
                    show_find_info.setAdapter(findMusicAdapter);
                }

                break;
        }
    }
}
