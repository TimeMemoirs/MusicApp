package com.example.timem.musicapptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.ShowPlayMusicAdapter;
import entity.Dao;
import entity.MusicPlayer;
import service.MusicService;
import util.LyricView;

public class MainActivity extends Activity implements View.OnClickListener{
    private ImageButton show_music_list,play_music_style,play_music,next_music,prs_music;
    private TextView music_name;
    public static LyricView lyricView;
    private ImageView back,funktion;
    public static SeekBar music_bar;
    public static TextView now_time,gesamtzeit;
    private static SharedPreferences sharedPreferences;
    private Intent intent;
    private WebView webView;
    private static final String MAINMUSIC = "MainActivity";
    private int INTERVAL=20;//歌词每行的间隔
    private SharedPreferences.Editor editor;
    private Dao dao = new Dao(this);
//    private ListView listView;
    private List<MusicPlayer> musicPlayerList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        InitView();
        back.setImageResource(R.drawable.back_image_change);
        funktion.setImageResource(R.drawable.little_bar);
        MusicPlayer musicPlayer= dao.searchMusicId(sharedPreferences.getInt("position", 0));
        Log.d("++++++++++++++", "0");
        if (musicPlayer != null) {
            music_name.setText(musicPlayer.getPlayMusicName());
        }
        intent = new Intent(MainActivity.this, MusicService.class);
//        intent.putExtra("musicWhere", MAINMUSIC);
        if (MusicActivity.no != 0) {
            gesamtzeit.setText(ShowTime(MusicService.mediaPlayer.getDuration()));
            //TODO  画面

            if (sharedPreferences.getInt("status", 0) == 0) {
                Drawable drawable = this.getResources().getDrawable(R.drawable.desk_play, null);
                play_music.setBackground(drawable);
            } else {
                Drawable drawable = this.getResources().getDrawable(R.drawable.desk_pause, null);
                play_music.setBackground(drawable);
            }
            music_bar.setMax(MusicService.mediaPlayer.getDuration());
        }

        Log.d("++++++++++++++++", "1");
        lyricView.setSelected(true);        //歌词滚动

        SerchLrc();


       /* new Runnable() {
            @Override
            public void run() {
                howToPlayWay();
            }
        };*/
        ViewListener();

    }

    public void SerchLrc() {
        String musicNameTemp =  dao.searchMusicId(sharedPreferences.getInt("position", 0)).getPlayMusicPath();

        String lrc = musicNameTemp.substring(0, musicNameTemp.length() - 4).trim() + ".lrc".trim();
        Log.d("================lrc", lrc);
        LyricView.read(lrc);
        lyricView.setSIZEWORD(50);
        lyricView.setOffsetY(350);
        Log.d("+++++++++++++++++++", "2");
    }

    private void ViewListener() {

        //button
        show_music_list.setOnClickListener(this);
        play_music.setOnClickListener(this);
        play_music_style.setOnClickListener(this);
        next_music.setOnClickListener(this);
        prs_music.setOnClickListener(this);
        //title
        back.setOnClickListener(this);
        funktion.setOnClickListener(this);


        //SeekBar
        music_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicService.mediaPlayer.seekTo(progress);
                    lyricView.setOffsetY(760 - lyricView.SelectIndex(progress) * (lyricView.getSIZEWORD() + INTERVAL - 1));
                }
                Log.d("++++++++++++++++++++++", "3");
                Log.d("====================", "总时间为" + MusicService.mediaPlayer.getDuration());
                Log.d("====================", "当前时间" + progress);
                if (MusicService.mediaPlayer.getDuration() - progress < 50) {
                    Log.d("============测试一下", "测试");

                    switch (sharedPreferences.getInt("howToPlay", 0)) {
                        case 0 :
                            intent.putExtra("run", true);
                            int a = sharedPreferences.getInt("position", 0);
                            if (a == dao.searchMusic().size() - 1) {
                                a = 0;
                            } else {
                                a = a + 1;
                            }
                            editor.putInt("position", a);
                            intent.putExtra("status", 0);
                            editor.putInt("status", 1);
                            intent.putExtra("address", dao.searchMusicId(a).getPlayMusicPath());
                            editor.commit();
                            break;
                        case 1 :

                            break;
                        case 2 :
                            intent.putExtra("run", true);
                            int b = (int) (Math.random() * (dao.searchMusic().size() - 1));
                            editor.putInt("position", b);
                            intent.putExtra("status", 0);
                            editor.putInt("status", 1);
                            intent.putExtra("address", dao.searchMusicId(b).getPlayMusicPath());
                            editor.commit();
                            break;
                        default:
                            break;
                    }
                    music_name.setText(dao.searchMusicId(sharedPreferences.getInt("position", 0)).getPlayMusicName());
                    Drawable drawable = MainActivity.this.getResources().getDrawable(R.drawable.desk_pause, null);
                    play_music.setBackground(drawable);
                    SerchLrc();
                    startService(intent);
                    return;
                }
                Log.d("=================", "  " + sharedPreferences.getInt("position", 0));
                Log.d("=================", dao.searchMusicId(sharedPreferences.getInt("position", 0)).getPlayMusicName());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                MusicService.mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicService.mediaPlayer.start();
                Drawable drawable = MainActivity.this.getResources().getDrawable(R.drawable.desk_pause);
                play_music.setBackground(drawable);
                if (sharedPreferences.getInt("status", 0) == 0) {
                    editor.putInt("status", 1);
                    editor.commit();
                }
            }
        });
    }

    private void InitView() {
        //button
        play_music = (ImageButton) findViewById(R.id.play_music);                   //播放
        show_music_list = (ImageButton) findViewById(R.id.music_list);              //播放列表
        play_music_style = (ImageButton) findViewById(R.id.play_music_style);       //播放方式
        next_music = (ImageButton) findViewById(R.id.next_music);                   //下一首
        prs_music = (ImageButton) findViewById(R.id.prs_music);                     //上一首
        lyricView = (LyricView) findViewById(R.id.lyric_main);                      //歌词
        //title
//        View view = LayoutInflater.from(this).inflate(R.layout.title_layouut, null);
        music_name = (TextView) findViewById(R.id.title);                           //音乐名字
        back = (ImageView) findViewById(R.id.back);                                 //返回
        funktion = (ImageView) findViewById(R.id.funktion);                         //设置？更多
        //SeekBar
        now_time = (TextView) findViewById(R.id.now_time);                          //当前时间
        music_bar = (SeekBar) findViewById(R.id.music_bar);                         //进度条
        gesamtzeit = (TextView) findViewById(R.id.gesamtzeit);                      //总时间

        sharedPreferences = getSharedPreferences("PlayingStatus", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("why", true);
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        int a = 0;
        musicPlayerList = dao.searchMusic();
        MusicPlayer musicPlayer = null;
        Drawable drawable = null;
//        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (v.getId()) {
            case R.id.play_music :
                /**
                 * 确定状态
                 * 确定是否进行换歌
                 * position是否要变
                 *      只读取position不改变position；（已修改完成）
                 */
                if (MusicActivity.no == 0) {
                    intent.putExtra("run", true);
                    MusicActivity.no = 1;

                } else {
                    intent.putExtra("run", false);
                }

                if (sharedPreferences.getInt("status", 0) == 0) {
                    intent.putExtra("status", sharedPreferences.getInt("status", 0));
                    editor.putInt("status", 1);
                    drawable = this.getResources().getDrawable(R.drawable.desk_pause, null);
                    play_music.setBackground(drawable);
                } else {
                    intent.putExtra("status", sharedPreferences.getInt("status", 0));
                    editor.putInt("status", 0);
                    drawable = this.getResources().getDrawable(R.drawable.desk_play, null);
                    play_music.setBackground(drawable);
                }
                editor.commit();
                musicPlayer = musicPlayerList.get(sharedPreferences.getInt("position", 0));
                intent.putExtra("address", musicPlayer.getPlayMusicPath());
                startService(intent);
                break;
            case R.id.music_list :
                showPlayMusicListView();
                break;
            case R.id.play_music_style :

                if (sharedPreferences.getInt("howToPlay", 0) == 0) {
                    drawable = this.getResources().getDrawable(R.drawable.desk_loop, null);
                    play_music_style.setBackground(drawable);
                    editor.putInt("howToPlay", 1);
                    editor.commit();
                    xunhuan();
                } else if (sharedPreferences.getInt("howToPlay", 1) == 1) {
                    drawable = this.getResources().getDrawable(R.drawable.desk_one, null);
                    play_music_style.setBackground(drawable);
                    editor.putInt("howToPlay", 2);
                    editor.commit();
                    danqu();
                } else if (sharedPreferences.getInt("howToPlay", 2) == 2) {
                    drawable = this.getResources().getDrawable(R.drawable.desk_shuffle, null);
                    play_music_style.setBackground(drawable);
                    editor.putInt("howToPlay", 0);
                    editor.commit();
                    suiji();
                }
                break;
            case R.id.next_music :
                intent.putExtra("run", true);
                MusicActivity.no = 1;
                intent.putExtra("status", 0);
                editor.putInt("status", 1);
                /**
                 * 这里需要判断，下一首歌，当是列表最后一首歌的时候；
                 * 判断 a == musicPlayerList.size()
                 *          是：则下一首的position = 0；
                 *          否：position ++；
                 */
                a = sharedPreferences.getInt("position", 0);
                Log.d("==============Main_position", "" + a);
                if (a == musicPlayerList.size() - 1) {
                    editor.putInt("position", 0);
                } else {
                    editor.putInt("position", a + 1);
                }
                editor.commit();
                musicPlayer = musicPlayerList.get(sharedPreferences.getInt("position", 0));
                music_name.setText(musicPlayer.getPlayMusicName());
                intent.putExtra("address", musicPlayer.getPlayMusicPath());
                drawable = this.getResources().getDrawable(R.drawable.desk_pause, null);
                play_music.setBackground(drawable);
                SerchLrc();
                startService(intent);

                break;
            case R.id.prs_music :
//                MusicPlayer musicPlayer= dao.searchMusicId(sharedPreferences.getInt("position", 0));
                intent.putExtra("run", true);
                MusicActivity.no = 1;
                intent.putExtra("status", 0);
                editor.putInt("status", 1);
                /**
                 * 这里需要判断，上一首歌，当是列表的第一首歌的时候；
                 * 判断 a == 0
                 *          是：则上一首的position = musicPlayerList.size() - 1；
                 *          否：position --；
                 */
                a = sharedPreferences.getInt("position", 0);
                Log.d("==============Main_position", "" + a);
                if (a == 0) {
                    editor.putInt("position", musicPlayerList.size() - 1);
                    Log.d("=========music_size", "" + ( musicPlayerList.size() - 1 ));
                } else {
                    editor.putInt("position", a - 1);
                }
                editor.commit();
                musicPlayer = musicPlayerList.get(sharedPreferences.getInt("position", 0));
                music_name.setText(musicPlayer.getPlayMusicName());
                intent.putExtra("address", musicPlayer.getPlayMusicPath());
                SerchLrc();
                drawable = this.getResources().getDrawable(R.drawable.desk_pause, null);
                play_music.setBackground(drawable);
                startService(intent);
                break;
            case R.id.back :
                finish();
                break;
            case R.id.funktion :
                showPlayMusicListView();
                break;

        }
    }
    public void showPlayMusicListView(){
        final Intent intentTemp = new Intent(MainActivity.this, MusicService.class);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        LinearLayout show_show_show = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_show_play_music, null);
        ListView listView = (ListView) show_show_show.findViewById(R.id.show_play_list);
        final List<MusicPlayer> musicPlayerList = dao.searchMusic();
        ShowPlayMusicAdapter showPlayMusicAdapter = new ShowPlayMusicAdapter(this, musicPlayerList);
        listView.setAdapter(showPlayMusicAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intentTemp.putExtra("run", true);
                intentTemp.putExtra("status", 0);
                editor.putInt("status", 1);
                intentTemp.putExtra("address", musicPlayerList.get(position).getPlayMusicPath());
                editor.putInt("position", position);
                editor.commit();
                music_name.setText(musicPlayerList.get(position).getPlayMusicName());
                Drawable drawable = MainActivity.this.getResources().getDrawable(R.drawable.desk_pause, null);
                play_music.setBackground(drawable);
                MusicActivity.no = 1;
                startService(intentTemp);
            }
        });
        new AlertDialog.Builder(MainActivity.this).setTitle("播放列表").setView(show_show_show).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }
    public static void simulateKey(final int KeyCode) {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void howToPlayWay(){
        switch (sharedPreferences.getInt("howToPlay", 0)) {
            case 1 :
                danqu();
                break;
            case 2 :
                suiji();
                break;
            case 0 :
               xunhuan();
                break;
            default:
                break;
        }
    }

    public void danqu(){
        MusicService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MusicService.mediaPlayer.setLooping(true);
            }
        });
    }

    public void suiji(){
        MusicService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
                Intent intent = new Intent(MainActivity.this, MusicService.class);
                intent.putExtra("run", true);
                intent.putExtra("status", 0);
                editor.putInt("status", 1);
                /**
                 * 这里需要判断，下一首歌，当是列表最后一首歌的时候；
                 * 判断 a == musicPlayerList.size()
                 *          是：则下一首的position = 0；
                 *          否：position ++；
                 */
                int a = (int) (Math.random() * musicPlayerList.size());
                Log.d("==============Main_position", "" + a);
                if (a == musicPlayerList.size() - 1) {
                    editor.putInt("position", 0);
                } else {
                    editor.putInt("position", a + 1);
                }
                editor.commit();
                MusicPlayer musicPlayer = musicPlayerList.get(sharedPreferences.getInt("position", 0));
                music_name.setText(musicPlayer.getPlayMusicName());
                intent.putExtra("address", musicPlayer.getPlayMusicPath());
                startService(intent);
            }
        });
    }

    public void xunhuan(){
        MusicService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
                Intent intent = new Intent(MainActivity.this, MusicService.class);
                intent.putExtra("run", true);
                intent.putExtra("status", 0);
                editor.putInt("status", 1);
                /**
                 * 这里需要判断，下一首歌，当是列表最后一首歌的时候；
                 * 判断 a == musicPlayerList.size()
                 *          是：则下一首的position = 0；
                 *          否：position ++；
                 */
                int a = sharedPreferences.getInt("position", 0);
                Log.d("==============Main_position", "" + a);
                if (a == musicPlayerList.size() - 1) {
                    editor.putInt("position", 0);
                } else {
                    editor.putInt("position", a + 1);
                }
                editor.commit();
                MusicPlayer musicPlayer = musicPlayerList.get(sharedPreferences.getInt("position", 0));
                music_name.setText(musicPlayer.getPlayMusicName());
                intent.putExtra("address", musicPlayer.getPlayMusicPath());
                startService(intent);
            }
        });
    }
    public static String ShowTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
}
