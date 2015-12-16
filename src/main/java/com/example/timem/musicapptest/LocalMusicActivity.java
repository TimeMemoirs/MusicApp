package com.example.timem.musicapptest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import adapter.LocalMusicAdapter;
import entity.DBHelper;
import entity.Dao;
import entity.MusicInfo;
import entity.MusicPlayer;
import service.MusicService;

public class LocalMusicActivity extends Activity implements View.OnClickListener{
    private TextView title,music_activity_name;
    private ImageView back,find;
    private LinearLayout find_local_music,play_all_music,show_play_two;
    private ListView listView;

    private static final String MUSIC_NO = "LocalMusicActivity";

    private SharedPreferences sharedPreferences;
    private File path_SDcard,musicFile;
    public static File[] musicPath;
    private List<String> musicName,oneMusicPath;
    private List<MusicInfo> musicInfoList = new ArrayList<>();
    private List<MusicPlayer> musicPlayerList = new ArrayList<>();
    private Dao dao = new Dao(this);
    private MusicPlayer musicPlayer;
    private Intent intent;
    private MusicInfo musicInfo;
    private ImageButton play_music_san;
    private int no = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        getActionBar().hide();
        InitView();
        title.setText("本地音乐");
        if (dao.searchMusicId(sharedPreferences.getInt("position", 0)) != null) {
            music_activity_name.setText(dao.searchMusicId(sharedPreferences.getInt("position", 0)).getPlayMusicName());
        }
        back.setImageResource(R.drawable.back_image_change);
        find.setImageResource(R.drawable.find_music_image_change);
        ViewListener();
        AddMusicInfo();

    }

    private void AddMusicInfo() {
        musicInfoList = dao.search();
        if (musicInfoList.size() != 0) {
            LocalMusicAdapter localMusicAdapter = new LocalMusicAdapter(musicInfoList, this);
            listView.setAdapter(localMusicAdapter);
        }

    }

    private void ViewListener() {
        back.setOnClickListener(this);
        find.setOnClickListener(this);

        find_local_music.setOnClickListener(this);
        play_all_music.setOnClickListener(this);

        show_play_two.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 2015/11/20 重新写！
                /**
                 * 首先要实现点击标题播放音乐                                （√）
                 * 播放列表中只能有一个相同的音乐名称：
                 *          判断数据库中是否有数据
                 *              用musicPlayerList 得到数据库中的信息，
                 *              如果musicPlayList == 0 则说明数据库中没有数据，反之数据库中有数据
                 *                  有：则判断有没有相同的音乐名称：
                 *                          有：则直接播放。                （√）
                 *                          无：则添加到播放列表再播放       （√）
                 *                  无：则添加到列表后再播放                 （√）
                 *     问题：现在情况当上一首是与下一首name相同时不会添加
                 *          单是与上一首name不同的时候，就会添加到列表
                 *     原因：break是跳出当前循环，continue是跳过本次，执行后面的循环；
                 *          （已解决）
                 * 判断当前是否在播放：
                 *      如果当先是播放状态（sharedPreferences.getInt("status", 0) == 1） 则置  1
                 *      如果是暂停状态   （sharedPreferences.getInt("status", 0) == 0）  则置  0
                 *              播放      status      1
                 *              暂停      status      0
                 *
                 *   temp = 0 ;     写入数据库
                 *   temp = 1 ;     不写入数据库
                 *
                 *   运行
                 *
                 */
                /*int temp = 0;
                sharedPreferences = getSharedPreferences("PlayingStatus", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                musicInfo = musicInfoList.get(position);
                musicPlayerList = dao.searchMusic();
                if (musicPlayerList.size() == 0) {
                    temp = 0;
                } else {
                    for (int i = 0; i < musicPlayerList.size(); i++) {
                        String musicPath_db = musicPlayerList.get(i).getPlayMusicPath();
                        String musicPath_local = musicInfo.getMusicPath();
                        if (musicPath_db.equals(musicPath_local)) {
                            temp = 1;
                            break;
                        } else {
                            temp = 0;
                        }
                    }
                }


                if (temp == 0) {
                    intent = new Intent(LocalMusicActivity.this, MusicService.class);
                    intent.putExtra("run", true);
                    musicPlayer = new MusicPlayer();
                    musicPlayer.setPlayMusicId(musicPlayerList.size());
                    musicPlayer.setPlayMusicName(musicInfo.getMusicName());
                    musicPlayer.setPlayMusicPath(musicInfo.getMusicPath());
                    dao.insertMusic(musicPlayer);
                    intent.putExtra("status", sharedPreferences.getInt("status", 0));
                    editor.putInt("status", 1);
                    intent.putExtra("address", musicInfo.getMusicPath());
                } else {
                    intent = new Intent(LocalMusicActivity.this, MusicService.class);
                    if (no == 0) {
                        Log.d("===============local_no", "" + no);
                        intent.putExtra("run", true);
                        no = 1;
                    } else {
                        Log.d("===============local_no", "" + no);
                        intent.putExtra("run", false);
                    }

                    if (sharedPreferences.getInt("status", 0) == 0) {

                        intent.putExtra("status", sharedPreferences.getInt("status", 0));
                        editor.putInt("status", 1);
                    } else {
                        intent.putExtra("status", sharedPreferences.getInt("status", 0));
                        editor.putInt("status", 0);
                    }

                    intent.putExtra("status", sharedPreferences.getInt("status", 0));
                    intent.putExtra("address", musicInfo.getMusicPath());
                    editor.putInt("status", 1);
                }*/

                //TODO ---------------------------------分割线----------------------------------

                /**
                 *
                 *          当这首歌不在播放列表中的时候  即  temp = 0；
                 *          点击添加到数据库
                 *          并且播放
                 *                  播放条件：
                 *                            run = true;
                 *                            status = 0;
                 *                            address = musicInfo.getMusicPath();
                 *                  必须添加到本地的数据：
                 *                            position = musicPlayerList.size();
                 *                            status = 1；
                 *
                 *       当这首歌在在列表中的时候  即  temp = 1；
                 *              不添加到数据库
                 *              一、当点击的名字和当前position的歌曲名字相同的时候
                 *                  1.首次点击播放：
                 *                      播放条件：
                 *                              run = true；
                 *                              status = 0；
                 *                              address = musicInfo.getMusicPath();
                 *                      必须添加到数据库的数据：
                 *                              position = dao.searchMusicName(musicInfo.getMusicName()).getMusicId;
                 *                              status = 1;
                 *                  2.第二次点击暂停：
                 *                      暂停条件:
                 *                              run = false ;
                 *                              status = 1;
                 *                              address = musicInfo.getMusicPath();
                 *                      必须添加到数据库的数据：
                 *                              position = dao.searchMusicName(musicInfo.getMusicName()).getPlayMusicId;不变
                 *                              status = 0；
                 *                  3、第三地点击播放：
                 *                      播放条件：
                 *                              run = false；
                 *                              status = 0；
                 *                              address = musicInfo.getMusicPath();
                 *                      必须添加到数据库的数据：
                 *                              position = dao.searchMusicName(musicInfo.getMusicName()).getPlayMusicId;不变
                 *                              status = 1;
                 *              二、当点击的名字和当前position的歌曲名字不相同的时候
                 *                  点击播放：
                 *                      播放条件：
                 *                              run = true;
                 *                              status = 0;
                 *                              address = musicInfo.getMusicPath();
                 *                      必须添加到数据库中的数据：
                 *                              position = dao.searchMusicName(musicInfo.getMusicName()).getPlayMusicId;
                 *                              status = 1;
                 */
//                sharedPreferences = getSharedPreferences("PlayingStatus", Context.MODE_PRIVATE);

                //TODO  baoliu
                //TODO  baoliu
                //TODO  baoliu
                //TODO  baoliu
                //TODO  baoliu
                //TODO  baoliu
                /*SharedPreferences.Editor editor = sharedPreferences.edit();
                intent = new Intent(LocalMusicActivity.this, MusicService.class);
                *//**
                 * 判斷是這首歌是否播放列表
                 *//*
                musicInfo = musicInfoList.get(position);
                String music_name_temp = musicInfo.getMusicName();
                MusicPlayer musicPlayer = dao.searchMusicName(music_name_temp);
//                Log.d("=================Local  demo", "1" + musicPlayer.getPlayMusicName());
                if (musicPlayer == null) {
                    Log.d("==============local   demo", "1");
                    MusicPlayer musicPlayer_temp = new MusicPlayer();
                    musicPlayer_temp.setPlayMusicId(dao.searchMusic().size());
                    musicPlayer_temp.setPlayMusicName(musicInfo.getMusicName());
                    musicPlayer_temp.setPlayMusicPath(musicInfo.getMusicPath());
                    dao.insertMusic(musicPlayer_temp);
                    editor.putInt("position", dao.search().size());
                    intent.putExtra("run", true);
                    intent.putExtra("status", 0);
                    editor.putInt("status", 1);
                    Drawable drawable = LocalMusicActivity.this.getResources().getDrawable(R.drawable.desk_pause, null);
                    play_music_san.setBackground(drawable);
                } else {
                    Log.d("=================local    demo", "2");
                    if (sharedPreferences.getInt("position", 0) == dao.searchMusicName(musicInfo.getMusicName()).getPlayMusicId()) {
                        if (no == 0) {
                            intent.putExtra("run", true);
                            no = 1;
                        } else {
                            intent.putExtra("run", false);
                        }
                        if (sharedPreferences.getInt("status", 0) == 0) {
                            intent.putExtra("status", 0);
                            editor.putInt("status", 1);
                            Drawable drawable = LocalMusicActivity.this.getResources().getDrawable(R.drawable.desk_pause, null);
                            play_music_san.setBackground(drawable);
                        } else {
                            intent.putExtra("status", 1);
                            editor.putInt("status", 0);
                            Drawable drawable = LocalMusicActivity.this.getResources().getDrawable(R.drawable.desk_play, null);
                            play_music_san.setBackground(drawable);
                        }
                    } else {
                        intent.putExtra("run", true);
                        editor.putInt("position", musicPlayer.getPlayMusicId());
                        intent.putExtra("status", 0);
                        editor.putInt("status", 1);
                        Drawable drawable = LocalMusicActivity.this.getResources().getDrawable(R.drawable.desk_pause, null);
                        play_music_san.setBackground(drawable);
                    }


                }


                intent.putExtra("address", musicInfo.getMusicPath());
                editor.commit();
                startService(intent);

                music_activity_name.setText(musicInfo.getMusicName());
*/

                //TODO  ------------------------------分割线----------------------------------------
                SharedPreferences.Editor editor = sharedPreferences.edit();
                intent = new Intent(LocalMusicActivity.this, MusicService.class);
                musicInfoList = dao.search();
                musicInfo = musicInfoList.get(position);
                Log.d("===============music", musicInfo.getMusicName());

                musicPlayer = dao.searchMusicName(musicInfo.getMusicName());
                if (musicPlayer == null) {
                    intent.putExtra("run", true);
                    MusicActivity.no = 1;
                    intent.putExtra("status", 0);
                    editor.putInt("status", 1);
                    editor.putInt("position", dao.searchMusic().size());
                    MusicPlayer musicPlayerTemp = new MusicPlayer();
                    musicPlayerTemp.setPlayMusicId(dao.searchMusic().size());
                    musicPlayerTemp.setPlayMusicName(musicInfo.getMusicName());
                    musicPlayerTemp.setPlayMusicPath(musicInfo.getMusicPath());
                    dao.insertMusic(musicPlayerTemp);
                    Log.d("===============================musicPlayer", "没有数据");
                    Drawable drawable = LocalMusicActivity.this.getResources().getDrawable(R.drawable.desk_pause, null);
                    play_music_san.setBackground(drawable);
                    music_activity_name.setText(musicInfo.getMusicName());
                } else {
                    Log.d("=======hehe", musicInfo.getMusicName());
                    Log.d("=======haha", dao.searchMusicId(sharedPreferences.getInt("position", 0)).getPlayMusicName());
                    if (dao.searchMusicId(sharedPreferences.getInt("position", 0)).getPlayMusicName().equals(musicInfo.getMusicName())) {
                        Log.d("=======s", "shuchu ");

                        if (MusicActivity.no == 0) {
                            intent.putExtra("run", true);
                            MusicActivity.no = 1;
                        } else {
                            intent.putExtra("run", false);
                        }
                        Log.d("======", "输出1" + sharedPreferences.getInt("status", 0));
                        if (sharedPreferences.getInt("status", 0) == 0) {
                            Log.d("======", "输出1");
                            intent.putExtra("status", 0);
                            editor.putInt("status", 1);
                            Drawable drawable = LocalMusicActivity.this.getResources().getDrawable(R.drawable.desk_pause, null);
                            play_music_san.setBackground(drawable);
                        } else {
                            Log.d("======", "输出2");
                            intent.putExtra("status", 1);
                            editor.putInt("status", 0);
                            Drawable drawable = LocalMusicActivity.this.getResources().getDrawable(R.drawable.desk_play, null);
                            play_music_san.setBackground(drawable);
                        }
                    } else {
                        Log.d("======g", "shuchu");
                        intent.putExtra("status", 0);
                        editor.putInt("status", 1);
                        intent.putExtra("run", true);
                        MusicActivity.no = 1;
                        editor.putInt("position", dao.searchMusicName(musicInfo.getMusicName()).getPlayMusicId());
                        Drawable drawable = LocalMusicActivity.this.getResources().getDrawable(R.drawable.desk_pause, null);
                        play_music_san.setBackground(drawable);
                        music_activity_name.setText(musicInfo.getMusicName());
                    }
                }




                editor.commit();
                intent.putExtra("address", musicInfo.getMusicPath());
                startService(intent);
                //TODO  ------------------------------分割线----------------------------------------
                /*int temp = 0;
                intent = new Intent(LocalMusicActivity.this, MusicService.class);
                sharedPreferences = getSharedPreferences("PlayingStatus", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                musicInfo = musicInfoList.get(position);
                musicPlayerList = dao.searchMusic();
                if (musicPlayerList.size() == 0) {
                    Log.d("==================local", "壹");
                    temp = 0;
                } else {
                    Log.d("==================local", "貳");
                    for (int i = 0; i < musicPlayerList.size(); i++) {
                        String musicPath_db = musicPlayerList.get(i).getPlayMusicPath();
                        String musicPath_local = musicInfo.getMusicPath();
                        if (musicPath_db.equals(musicPath_local)) {
                            Log.d("==================local", "叁");
                            temp = 1;
                            break;
                        } else {
                            Log.d("==================local", "肆");
                            temp = 0;
                        }
                    }
                }

                //TODO    测试测试！！！！有问题    多查几次
                if (temp == 0) {
                    //添加数据库
                    Log.d("===========local    LocalMusicActivity", "添加数据库,");
                    musicPlayer = new MusicPlayer();
                    musicPlayer.setPlayMusicId(musicPlayerList.size());
                    musicPlayer.setPlayMusicName(musicInfo.getMusicName());
                    musicPlayer.setPlayMusicPath(musicInfo.getMusicPath());
                    dao.insertMusic(musicPlayer);
                    //播放条件
                    intent = new Intent(LocalMusicActivity.this, MusicService.class);
                    intent.putExtra("run", true);
                    //状态 传 0 置 1
                    intent.putExtra("status", 0);
                    editor.putInt("position", musicPlayerList.size());
                    editor.putInt("status", 1);
                    editor.commit();
                    //地址
                    intent.putExtra("address", musicInfo.getMusicPath());

                } else {
                    if (musicInfo.getMusicName().equals(dao.searchMusicId(sharedPreferences.getInt("position", 0)))) {

                        if (no == 0) {
                            Log.d("==================local", "伍");

                            Log.d("===============Local    no", "" + no);

                            intent.putExtra("run", true);
                            no = 1;
                        } else {
                            Log.d("==================local", "陸");

                            intent.putExtra("run", false);
                        }
                        intent.putExtra("status", sharedPreferences.getInt("status", 0));
                        if (sharedPreferences.getInt("status", 0) == 0) {
                            editor.putInt("status", 1);
                        } else {
                            editor.putInt("status", 0);
                        }
                        intent.putExtra("address", musicInfo.getMusicPath());
                        editor.putInt("position", dao.searchMusicName(musicInfo.getMusicName()).getPlayMusicId());
                        editor.commit();
                    } else {
                        Log.d("==================local", "柒");

                        intent.putExtra("run", true);
                        intent.putExtra("status", 0);
                        editor.putInt("status", 1);
                        editor.putInt("position", dao.searchMusicName(musicInfo.getMusicName()).getPlayMusicId());
                        editor.commit();
                        intent.putExtra("address", musicInfo.getMusicPath());
                    }
                }
*/


               /* intent = new Intent(LocalMusicActivity.this, MusicService.class);
                if (no == 0) {
                    Log.d("===============local_no", "" + no);
                    intent.putExtra("run", true);
                    no = 1;
                } else {
                    Log.d("===============local_no", "" + no);
                    intent.putExtra("run", false);
                }

                if (sharedPreferences.getInt("status", 0) == 0) {

                    intent.putExtra("status", sharedPreferences.getInt("status", 0));
                    editor.putInt("status", 1);
                } else {
                    intent.putExtra("status", sharedPreferences.getInt("status", 0));
                    editor.putInt("status", 0);
                }

                intent.putExtra("status", sharedPreferences.getInt("status", 0));
                intent.putExtra("address", musicInfo.getMusicPath());
                editor.putInt("status", 1);*/
                //TODO ---------------------------------分割线----------------------------------

               /* int temp = 0;
                sharedPreferences = getSharedPreferences("PlayingStatus", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                intent =  new Intent(LocalMusicActivity.this, MusicService.class);
                MusicInfo musicInfo = musicInfoList.get(position);
                musicPlayerList = dao.searchMusic();
                if (musicPlayerList.size() == 0) {
                    temp = 1;
                } else {
                    for (int i = 0; i < musicPlayerList.size(); i++) {
                        if (musicPlayerList.get(i).getPlayMusicPath().equals(musicInfo.getMusicPath())) {
                            *//**
                 * 这首歌在列表中，但是不是当前播放的歌曲
                 *          run     ==     true
                 *
                 *          首先  获得当前播放的歌曲的名字
                 *               通过position获得正在播放的歌曲的名字
                 *
                 *          其次  判断：如果点击的歌曲名字和当前播放的名字相同
                 *                              运行暂停  即  run = false；
                 *                     如果点击的歌曲名字和当前的名字不相同
                 *                              运行播放  即  run = true；
                 *
                 *
                 *    点击的名字形同， 则暂停
                 *              若 当前状态为正在播放  即  status = 1  传  status = 1；
                 *              若 当前状态为暂停  即 传  status = 0 ；
                 *    点击的名字不同， 则播放  传  status = 0；
                 *//*
                            Log.d("=====position_temp = 1", "" + sharedPreferences.getInt("position", 0));
                            int music_now_position = sharedPreferences.getInt("position", 0);
                            String music_now_name = musicPlayerList.get(music_now_position).getPlayMusicName();
                            if (music_now_name.equals(musicInfo.getMusicName())) {
                                intent.putExtra("run", false);
                                Log.d("==================local_run", "false");
                                if (sharedPreferences.getInt("status", 0) == 0) {
                                    intent.putExtra("status", sharedPreferences.getInt("status", 0));
                                    editor.putInt("status", 1);
                                } else {
                                    intent.putExtra("status", sharedPreferences.getInt("status", 0));
                                    editor.putInt("status", 0);
                                }
                            } else {
                                *//**
                 * position 则要更换
                 *      要跳转到当前正在播放的歌曲的Id
                 *//*

                                MusicPlayer musicPlayer = dao.searchMusicName(musicInfo.getMusicName());
                                intent.putExtra("run",true);
                                Log.d("==================local_run", "true");
                                intent.putExtra("status", 0);
                                editor.putInt("position", musicPlayer.getPlayMusicId());
                                editor.putInt("status", 1);

                            }
                            break;
                        } else {
                            temp = 1;
                        }
                    }
                }

                if (temp == 1) {
                    *//**
                 * 若要添加到数据库
                 *  必须要运行  即  run = true；
                 *
                 * 当前情况下   状态为0  即  status = 0；
                 *
                 * 若添加数据库 则将position 装换成数据库最后的面号，即  musicPlayerList.size()
                 *//*
                    Log.d("=====position_temp = 1", "" + sharedPreferences.getInt("position", 0));
                    intent.putExtra("run", true);
                    Log.d("==================local_run_temp = 1", "true");
                    intent.putExtra("status", 0);
                    editor.putInt("status", 1);
                    editor.putInt("position", musicPlayerList.size() - 1);
                    MusicPlayer musicPlayer = new MusicPlayer();
                    musicPlayer.setPlayMusicId(musicPlayerList.size());
                    musicPlayer.setPlayMusicName(musicInfo.getMusicName());
                    musicPlayer.setPlayMusicPath(musicInfo.getMusicPath());
                    dao.insertMusic(musicPlayer);
                }




                intent.putExtra("status", sharedPreferences.getInt("status",0));
                intent.putExtra("address", musicInfo.getMusicPath());
                editor.commit();*/


//                startService(intent);


            }
        });
    }


    /**
     * 暂时不知道做什么用
     */
//    private void musicInfoToMusicPlayer_no2(int position) {
//
//        musicPlayer = new MusicPlayer();
//        musicPlayer.setPlayMusicId(musicPlayerList.size());
//        musicPlayer.setPlayMusicName(musicInfo.getMusicName());
//        musicPlayer.setPlayMusicPath(musicInfo.getMusicPath());
//        dao.insertMusic(musicPlayer);
//        intent = new Intent(LocalMusicActivity.this, MusicService.class);
//        intent.putExtra("musicPlayer", musicPlayer);
//    }

    private void InitView() {
        show_play_two = (LinearLayout) findViewById(R.id.show_play_two);
        music_activity_name = (TextView) findViewById(R.id.music_activity_name);
        play_music_san = (ImageButton) findViewById(R.id.play_music_san);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        find = (ImageView) findViewById(R.id.funktion);
        find_local_music = (LinearLayout) findViewById(R.id.find_local_music);
        play_all_music = (LinearLayout) findViewById(R.id.play_all_music);
        listView = (ListView) findViewById(R.id.show_music);

        sharedPreferences = getSharedPreferences("PlayingStatus", Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_music, menu);
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
                finish();
                break;
            case R.id.funktion :
                Intent intent = new Intent(this,FindMusicActivity.class);
                startActivity(intent);
                break;
            case R.id.find_local_music :
                FildLocalMusices();
                break;
            case R.id.play_all_music :
                /**
                 * 点击按钮
                 * 将全部的本地歌曲全部添加到musicPlayerList中 并添加到数据库中
                 * 并要将原来的数据清空
                 * 并运行service
                 */
//                dao.deleteTableMusic();
                for (int i = 0; i < musicInfoList.size(); i++) {
                    MusicPlayer musicPlayer = new MusicPlayer();
                    musicPlayer.setPlayMusicId(musicPlayerList.size());
                    musicPlayer.setPlayMusicName(musicInfoList.get(i).getMusicName());
                    musicPlayer.setPlayMusicPath(musicInfoList.get(i).getMusicPath());
                    dao.insertMusic(musicPlayer);
                }
                intent = new Intent(LocalMusicActivity.this, MusicService.class);
                startService(intent);
                break;
            case R.id.show_play_two :
                intent = new Intent(LocalMusicActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void FildLocalMusices() {


        path_SDcard = Environment.getExternalStorageDirectory();        //获取SD card路径
        oneMusicPath = new ArrayList<>();
        musicName = new ArrayList<>();
        musicFile = new File(path_SDcard, "/m");
        Log.d("========musicFile", "" + musicFile);
        if (musicFile.exists()) {
            Log.d("==========", "1");
            if (musicFile.listFiles(new MyFilter()).length > 0) {
                Log.d("===========", "2");
                musicPath = musicFile.listFiles(new MyFilter());
                for (int i = 0; i < musicPath.length; i++) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setMusicId(i);
                    musicInfo.setMusicName(musicPath[i].getName());
                    musicInfo.setMusicPath(musicPath[i].getPath());
                    musicInfo.setMusicicon(R.drawable.icon_no);
                    if (musicInfoList.size() != musicPath.length) {
//                        dao.deleTable();
                        // TODO: 2015/11/20 当数据库为空的时候，只能插入一条数据！！！！
                        dao.insert(musicInfo);
                    } else {
                        if (!musicInfoList.get(i).getMusicName().equals(musicPath[i].getName())) {
                            dao.upData(musicInfo);
                            // TODO: 2015/11/19 感觉有问题，目前没有发现
                        }
                    }
                }
            } else {
                Toast.makeText(this, "『没有找到音乐』", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "~\\(—— ——ㄍ)ㄥ没有找到文件夹~~~~", Toast.LENGTH_SHORT).show();
        }
    }



    private class MyFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.toString().endsWith(".mp3");

        }
    }

}
