package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.*;
import android.util.Log;

import com.example.timem.musicapptest.MainActivity;
import com.example.timem.musicapptest.MusicActivity;
import com.example.timem.musicapptest.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import entity.DBHelper;
import entity.Dao;
import entity.MusicPlayer;
import util.LyricView;
import util.Timer;

public class MusicService extends Service implements  Runnable{
    private static final String LOCALMUSIC = "LocalMusic";

    private Thread thread ;

    public static MediaPlayer mediaPlayer ;
    private Dao dao = new Dao(this);
    private DBHelper dbHelper ;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;
    private List<MusicPlayer> musicPlayerList = new ArrayList<>();
    private String musicOldName = "";

    private  ExecutorService executorService;
    private int musicPlayerId = 0;
    public static int tempI;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (sharedPreferences.getBoolean("why", false)) {
                MainActivity.lyricView.invalidate();
                MainActivity.lyricView.setOffsetY(MainActivity.lyricView.getOffsetY() - MainActivity.lyricView.SpeedLrc() + 10);
                MainActivity.lyricView.SelectIndex(tempI);

                if (msg.what == 1) {
//                Log.d("==============tempI", "" + tempI);
//                msg.arg1 = tempI;
                /*int a = msg.arg1 / ( 1000 * 60 );
                long b =  Long.parseLong(msg.arg1+"") % (60 * 1000) / 1000;*/

                    MainActivity.now_time.setText(ShowTime(tempI));
//                MainActivity.music_bar.setProgress(msg.arg1);
                } else {
//                int a = msg.arg1 / ( 1000 * 60 );
//                long b =  Long.parseLong(msg.arg1+"") % (60 * 1000) / 1000;
                    MainActivity.gesamtzeit.setText(ShowTime(msg.arg1));
                }
            }


        }
    };
    public MusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        executorService = Executors.newSingleThreadExecutor();
        sharedPreferences = getSharedPreferences("PlayingStatus", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        /*mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               *//* switch (sharedPreferences.getInt("howToPlay", 0)) {
                    case 0 :
//                        MainActivity mainActivity = new MainActivity();
                        MainActivity.xunhuan();
                        break;
                }*//*

                *//*if (sharedPreferences.getInt("position", 0) == dao.searchMusic().size() - 1) {
                    playMusic(dao.searchMusicId(0).getPlayMusicPath());
                } else {
                    playMusic(dao.searchMusicId(sharedPreferences.getInt("position", 0) + 1).getPlayMusicPath());
                    editor.putInt("position", sharedPreferences.getInt("position", 0) + 1);
                    editor.commit();
                }*//*
            }*/
//        });


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /**
         *
         * 所应用到的数据，（数据库中读取）
         *  position
         *  status
         *
         *
         *  并且想办法实现暂停播放（来自MainActivity.class和LocalMusicActivity.class的音乐）
         *
         *
         * 一、首先判断时哪里过来的数据，如果是LocalMusicActivity中过来的数据则运行下面的程序
         *
         * 根据传过来的position来查询本地的数据           （√）
         *
         *
         * 当点击相同名字的歌曲的时候暂停，当点击不同名字的歌曲的时候继续播放
         *          如果当前播放的名字个点击名字相同，则暂停播放。
         *          如果不相同，则直接播放点击的那一首。
         *          并将当前播放的名字设置为播放的那首歌。
         *
         *          问题： 当前问题：点击第一遍后，能正常播放。但是点击第二下后，重新播放，后面的则正常
         *          原因： 首次播放的时候没有设置状态
         *          （已解决）
         *
         * 判断当前是否在播放：
         *      如果当先是播放状态（sharedPreferences.getInt("status", 0) == 1） 则置  1
         *      如果是暂停状态   （sharedPreferences.getInt("status", 0) == 0）  则置  0
         *              播放      status      1       （√）
         *              暂停      status      0       （√）
         *
         * 二、如果是点击AllPlay过来的，则运行下面的方法
         *
         *      播放列表中的第一首歌
         *          并记住position 和 musicOldName
         *
         * 三、如果是MainActivity中过来的数据则调用则运行下面的方法
         *          1、查询列表musicPlayerList中的歌曲(数据库中的歌曲)。
         *          2、点击下一首歌/上一首歌
         *                  首先判断点击的是上一首歌还是下一首歌。     (√)
         *                  其次获得当前歌曲在列表中position
         *                      获得position(musicPlayerId)：获得当前播放的歌曲名称。
         *                                   根据歌曲名称，查询id来确定position
         *                  根据position来播放上一首或者下一首；
         *                          上一首     若 当前position == 0；则播放musicPlayerList.size() -1 ；
         *                                    否则position = i - 1;
         *                          下一首     若 当前position == musicPlayerList.size -1;则播放position = 0；
         *                                    否则position = i + 1；
         *                      问题： 播放、前进、后退  无法选择其他的歌曲
         *                      解决： 点击之后重启service使position初始化，使position恒等于0，
         *                            故点击播放、前进、后退  无法按照想要的结果运行
         *
         *
         *
         *
         */
/**
 * 判断是播放还是暂停
 * 如果是暂停状态，则运行下面的状态；
 * 如果是运行状态，则运行另外一个
 *     TODO: 2015/11/24 没有实现
 */


        /**
         * 播放、暂停
         */
//        Log.d("====================service_run", "" + intent.getBooleanExtra("run", true));
        if (intent.getBooleanExtra("run", true)) {

            playMusic(intent.getStringExtra("address"));
            Log.d("=======================address", intent.getStringExtra("address"));
        }

//        Log.d("=============== status", "" + intent.getIntExtra("status", 0));
        if (intent.getIntExtra("status", 0) == 0) {
            Log.d("===================play", "start");
            if (sharedPreferences.getBoolean("why", false)) {
                MainActivity.music_bar.setMax(mediaPlayer.getDuration());
            }

            Drawable drawable = this.getResources().getDrawable(R.drawable.desk_pause, null);
            MusicActivity.play_music_two.setBackground(drawable);
            MusicActivity.music_activity_name.setText(dao.searchMusicId(sharedPreferences.getInt("position", 0)).getPlayMusicName());


            mediaPlayer.start();
        } else {
            Log.d("===================play", "pause");
            mediaPlayer.pause();

            Drawable drawable = this.getResources().getDrawable(R.drawable.desk_play, null);
            MusicActivity.play_music_two.setBackground(drawable);
//            MusicActivity.music_activity_name.setText(dao.searchMusicId(sharedPreferences.getInt("position", 0)).getPlayMusicName());

        }

        switch (sharedPreferences.getInt("howToPlay", 0)) {
            case 1 :
                mediaPlayer.setLooping(true);
                break;
            default:
                mediaPlayer.setLooping(false);
                break;
        }


       /* if (intent.getStringExtra("musicWhere").equals("LocalMusicActivity")) {
            //获得musicPlayer
            MusicPlayer musicPlayer = (MusicPlayer) intent.getSerializableExtra("musicPlayer");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            *//*if (sharedPreferences.getInt("status", 0) == 0) {
                Log.d("=============测试", "2");
                playMusicTwo(musicPlayer.getPlayMusicPath());
            } else {
                Log.d("=============测试", "1");
                playMusic(musicPlayer.getPlayMusicPath());
            }*//*
            playMusic(musicPlayer.getPlayMusicPath());
            musicPlayerList = dao.searchMusicName(musicPlayer.getPlayMusicName());
            musicPlayerId = musicPlayerList.get(0).getPlayMusicId();
            if (!musicPlayer.getPlayMusicName().equals(musicOldName)) {
                musicOldName = musicPlayer.getPlayMusicName();
                editor.putInt("status", 1);
                editor.putInt("position", musicPlayerId);
                editor.commit();
                MainActivity.music_bar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
            } else {
                if (sharedPreferences.getInt("status",0) == 0) {
                    editor.putInt("status", 1);
                    editor.putInt("position", musicPlayerId);
                    editor.commit();
                    MainActivity.music_bar.setMax(mediaPlayer.getDuration());
                    mediaPlayer.start();
                } else {
                    editor.putInt("status", 0);
                    editor.putInt("position", musicPlayerId);
                    editor.commit();
                    mediaPlayer.pause();
                }
            }


        } else if (intent.getStringExtra("musicWhere").equals("MainActivity")) {
            int no = 0;
            musicPlayerList = dao.searchMusic();

            if (musicPlayerList.size() == 0) {
                Toast.makeText(this, "列表中没有歌曲！", Toast.LENGTH_SHORT).show();
            } else {
                if (intent.getBooleanExtra("play", true)) {
                    musicPlayerId = sharedPreferences.getInt("position", 0);

                    //TODO 断点播放完全做不出来，之后再说
                    if (no == 0) {
                        Log.d("=====no", "" + no);
                        no = no + 1;
                        playMusic(musicPlayerList.get(musicPlayerId).getPlayMusicPath());
                    } else {
                        Log.d("=====no", "" + no);
                        playMusicTwo(musicPlayerList.get(musicPlayerId).getPlayMusicPath());
                    }

                    *//*if (sharedPreferences.getInt("status", 0) == 0) {
                        Log.d("=============测试", "2");
                        playMusicTwo(musicPlayerList.get(musicPlayerId).getPlayMusicPath());
                    } else {
                        Log.d("=============测试", "1");
                        playMusic(musicPlayerList.get(musicPlayerId).getPlayMusicPath());
                    }*//*
                    *//*if (!mediaPlayer.isPlaying()) {
                        Log.d("=============测试", "1");*//*
//                        playMusic(musicPlayerList.get(musicPlayerId).getPlayMusicPath());
                    *//*} else {
                        Log.d("=============测试", "2");
                        playMusicTwo(musicPlayerList.get(musicPlayerId).getPlayMusicPath());
                    }*//*
//                    playMusic(musicPlayerList.get(musicPlayerId).getPlayMusicPath());
                    musicOldName = musicPlayerList.get(musicPlayerId).getPlayMusicName();
                    statusChange();
                } else {
                    if (intent.getStringExtra("nextOrPrs").equals("next")) {
                        for (int i = 0; i < musicPlayerList.size(); i++) {
                            if (musicPlayerList.get(i).getPlayMusicName().equals(musicOldName)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                musicPlayerId = sharedPreferences.getInt("position", 0);
                                if (musicPlayerList.size() - 1 == sharedPreferences.getInt("position", 0)) {
                                    musicPlayerId = 0;
                                    editor.putInt("position", musicPlayerId);
                                    editor.commit();
                                    playMusic(musicPlayerList.get(musicPlayerId).getPlayMusicPath());
                                } else {
                                    editor.putInt("position", musicPlayerId + 1);
                                    editor.commit();
                                    playMusic(musicPlayerList.get(musicPlayerId + 1).getPlayMusicPath());

                                }
                                MainActivity.music_bar.setMax(mediaPlayer.getDuration());
                                mediaPlayer.start();
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < musicPlayerList.size(); i++) {
                            if (musicPlayerList.get(i).getPlayMusicName().equals(musicOldName)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                musicPlayerId = sharedPreferences.getInt("position", 0);
                                if (sharedPreferences.getInt("position", 0) == 0) {
                                    musicPlayerId = musicPlayerList.size() - 1;
                                    editor.putInt("position", musicPlayerId);
                                    editor.commit();
                                    playMusic(musicPlayerList.get(musicPlayerId).getPlayMusicPath());

                                } else {
                                    musicPlayerId = musicPlayerList.get(musicPlayerId - 1).getPlayMusicId();
                                    editor.putInt("position", musicPlayerId);
                                    editor.commit();
                                    playMusic(musicPlayerList.get(musicPlayerId).getPlayMusicPath());

                                }
                                MainActivity.music_bar.setMax(mediaPlayer.getDuration());
                                mediaPlayer.start();
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "还未完成", Toast.LENGTH_SHORT).show();
            // TODO: 2015/11/24 全部播放
        }
*/

        // TODO: 2015/11/23 没有写完





        return super.onStartCommand(intent, flags, startId);
    }

    private void playMusic(String address) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(address);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(false);
            executorService.execute(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void run() {


        android.os.Process.setThreadPriority(TRIM_MEMORY_BACKGROUND);           //设置优先级，告诉系统这是一个子线程
        Message message1 = new Message();
        int currentPos = 0;                             //初始化
        int total = mediaPlayer.getDuration();          //歌曲总长
        message1.arg1 = total;
        message1.what = 2;
        handler.sendMessage(message1);
        while (mediaPlayer != null && currentPos <= total) {
            currentPos = mediaPlayer.getCurrentPosition();      //当前进度
            tempI = currentPos;
            if (sharedPreferences.getBoolean("why", false)) {
                MainActivity.music_bar.setProgress(currentPos);
            }
            handler.sendEmptyMessage(1);
        }
    }





    /*public class MyAsy extends AsyncTask<Void, Integer, String>{

        @Override
        protected String doInBackground(Void... params) {
            int currentPos = 0;
            int total = mediaPlayer.getDuration();
            while (mediaPlayer != null && currentPos <= total) {
                currentPos = mediaPlayer.getCurrentPosition();      //当前进度
                tempI = currentPos;
                publishProgress(tempI);

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {        //
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {             //开始前
            super.onPreExecute();
            int total = mediaPlayer.getDuration();          //歌曲总长
            int a = total / ( 1000 * 60 );
            long b =  Long.parseLong(String.valueOf(total % (60 * 1000))) / 1000;
            MainActivity.gesamtzeit.setText(a + ":" + b);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("==============tempI", "" + tempI);
            int a = values[values.length - 1] / ( 1000 * 60 );
            long b =  Long.parseLong(String.valueOf(values[values.length - 1] % (60 * 1000))) / 1000;
           // MainActivity.now_time.setText(a + ":" + b);
            MainActivity.music_bar.setProgress(values[values.length - 1]);

        }
    }*/

    public static String ShowTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
}
