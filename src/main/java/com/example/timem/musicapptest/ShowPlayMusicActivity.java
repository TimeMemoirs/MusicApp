package com.example.timem.musicapptest;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapter.LocalMusicAdapter;
import adapter.ShowPlayMusicAdapter;
import entity.Dao;
import entity.MusicPlayer;

public class ShowPlayMusicActivity extends Activity {
    private ListView show_play_music_list;
    private Button clear;
    private Dao dao = new Dao(this);
    private List<MusicPlayer> musicPlayerList = new ArrayList<>();
    private ShowPlayMusicAdapter showPlayMusicAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_play_music);
        getActionBar().setTitle("最近播放");
//        Drawable drawable = this.getResources().getDrawable(0x000000, null);
//        getActionBar().setBackgroundDrawable(drawable);
        InitView();

        AddInfo();

        ViewListener();
    }

    private void ViewListener() {

    }

    private void AddInfo() {
        musicPlayerList = dao.searchMusic();
        showPlayMusicAdapter = new ShowPlayMusicAdapter(this, musicPlayerList);
        show_play_music_list.setAdapter(showPlayMusicAdapter);
    }

    private void InitView() {
        show_play_music_list = (ListView) findViewById(R.id.show_play_list);
//        clear = (Button) findViewById(R.id.clear);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_play_music, menu);
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
}
