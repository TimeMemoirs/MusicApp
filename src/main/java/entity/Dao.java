package entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库music_player的增、删、改、查
 * Created by timem on 2015/11/18.
 */
public class Dao {
    private DBHelper dbHelper;

    public Dao(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public void insert(MusicInfo musicInfo){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("musicId", musicInfo.getMusicId());
        values.put("music_name", musicInfo.getMusicName());
        values.put("music_path", musicInfo.getMusicPath());
        values.put("music_icon", musicInfo.getMusicicon());
        db.insert(DBHelper.TABLE_NAME, null, values);
        db.close();
    }

    public void delect(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DBHelper.TABLE_NAME, "musicId = " + id, null);
        db.close();
    }

    public void upData(MusicInfo musicInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("music_name", musicInfo.getMusicName());
        values.put("music_path", musicInfo.getMusicPath());
        values.put("music_icon", musicInfo.getMusicicon());
        db.update(DBHelper.TABLE_NAME, values, "musicId = " + musicInfo.getMusicId(), null);
        db.close();
    }

    public List<MusicInfo> search() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<MusicInfo> musicInfoList = new ArrayList<>();
        MusicInfo musicInfo = null;
        Cursor cursor = db.query(true, DBHelper.TABLE_NAME, new String[]{"musicId", "music_name", "music_path", "music_icon"}, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            musicInfo = new MusicInfo();
            musicInfo.setMusicId(cursor.getInt(0));
            musicInfo.setMusicName(cursor.getString(1));
            musicInfo.setMusicPath(cursor.getString(2));
            musicInfo.setMusicicon(cursor.getInt(3));
            musicInfoList.add(musicInfo);
        }
        db.close();
        return musicInfoList;
    }

    public void deleTable(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "delete from " + DBHelper.TABLE_NAME;
        db.execSQL(sql);


        db.close();
    }



    public void insertMusic(MusicPlayer musicPlayer){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("musicId", musicPlayer.getPlayMusicId());
        values.put("music_name", musicPlayer.getPlayMusicName());
        values.put("music_path", musicPlayer.getPlayMusicPath());
        db.insert(DBHelper.MUSIC_PLAYER_TABLE_NAME, null, values);
        db.close();
    }

    public void deleteMusic(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DBHelper.MUSIC_PLAYER_TABLE_NAME, "musicId=?", new String[]{"" + id});
        db.close();
    }

    public void upDataMusic(MusicPlayer musicPlayer){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("music_name", musicPlayer.getPlayMusicName());
        values.put("music_path", musicPlayer.getPlayMusicPath());
        db.update(DBHelper.MUSIC_PLAYER_TABLE_NAME, values, "musicId=?", new String[]{"" + musicPlayer.getPlayMusicId()});
        db.close();
    }

    public List<MusicPlayer> searchMusic(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        List<MusicPlayer> musicPlayersList = new ArrayList<>();
        Cursor cursor = db.query(true, DBHelper.MUSIC_PLAYER_TABLE_NAME, new String[]{"musicId", "music_name", "music_path"}, null, null, null, null, null, null);
        MusicPlayer musicPlayer = null;
        while (cursor.moveToNext()) {
            musicPlayer = new MusicPlayer();
            musicPlayer.setPlayMusicId(cursor.getInt(0));
            musicPlayer.setPlayMusicName(cursor.getString(1));
            musicPlayer.setPlayMusicPath(cursor.getString(2));
            musicPlayersList.add(musicPlayer);
        }
        db.close();
        return musicPlayersList;
    }

    public void deleteTableMusic(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql = "delete from " + DBHelper.MUSIC_PLAYER_TABLE_NAME;
        db.execSQL(sql);

        db.close();
    }

    public MusicPlayer searchMusicId(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        MusicPlayer musicPlayer = null;
        Cursor cursor = db.query(true, DBHelper.MUSIC_PLAYER_TABLE_NAME, new String[]{"musicId", "music_name", "music_path"}, "musicId = ?", new String[]{id + ""}, null, null, null, null);
        while (cursor.moveToNext()){
            musicPlayer = new MusicPlayer();
            musicPlayer.setPlayMusicId(cursor.getInt(0));
            musicPlayer.setPlayMusicName(cursor.getString(1));
            musicPlayer.setPlayMusicPath(cursor.getString(2));
        }
        db.close();
        return musicPlayer;
    }

    public MusicPlayer searchMusicName(String musicName){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("=====================musicName_service", musicName);
        Cursor cursor = db.query(true, DBHelper.MUSIC_PLAYER_TABLE_NAME, new String[]{"musicId", "music_name", "music_path"}, "music_name=?", new String[]{musicName}, null, null, null, null);
        MusicPlayer musicPlayer = null;
        while (cursor.moveToNext()) {
            musicPlayer = new MusicPlayer();
            musicPlayer.setPlayMusicId(cursor.getInt(0));
            musicPlayer.setPlayMusicName(cursor.getString(1));
            musicPlayer.setPlayMusicPath(cursor.getString(2));
        }
        db.close();
        return musicPlayer;
    }

    public List<MusicPlayer> searchMusicNameLike(String musicName){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d("=====================musicName_service", musicName);
        Cursor cursor = db.query(true, DBHelper.MUSIC_PLAYER_TABLE_NAME, new String[]{"musicId", "music_name", "music_path"}, "music_name like ?", new String[]{"%" + musicName + "%"}, null, null, null, null);
        MusicPlayer musicPlayer = null;
        List<MusicPlayer> musicPlayers = new ArrayList<>();
        while (cursor.moveToNext()) {
            musicPlayer = new MusicPlayer();
            musicPlayer.setPlayMusicId(cursor.getInt(0));
            musicPlayer.setPlayMusicName(cursor.getString(1));
            musicPlayer.setPlayMusicPath(cursor.getString(2));
            musicPlayers.add(musicPlayer);
        }
        db.close();
        return musicPlayers;
    }


    public List<MusicInfo> searchNameLike(String musicName){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(true, DBHelper.TABLE_NAME, new String[]{"musicId", "music_name", "music_path", "music_icon"}, "music_name like ?", new String[]{"%" + musicName + "%"}, null, null, null, null);
        MusicInfo musicInfo = null;
        List<MusicInfo> musicInfos = new ArrayList<>();
        while (cursor.moveToNext()) {
            musicInfo = new MusicInfo();
            musicInfo.setMusicId(cursor.getInt(0));
            musicInfo.setMusicName(cursor.getString(1));
            musicInfo.setMusicPath(cursor.getString(2));
            musicInfo.setMusicicon(cursor.getInt(3));
            musicInfos.add(musicInfo);
        }
        db.close();
        return musicInfos;

    }
}
