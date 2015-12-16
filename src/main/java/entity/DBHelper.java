package entity;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by timem on 2015/11/18.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "music_player";
    public static final String TABLE_NAME = "local_music_info";
    public static final String MUSIC_PLAYER_TABLE_NAME = "music_player";
    private static final String CREATE_TABLE = "create table if not exists `" + TABLE_NAME +"`(musicId int primary key, music_name varchar(255), music_path varchar(255), music_icon int(255))";
    private static final String MUSIC_PLAYER_CERATE_TABLE = "create table if not exists `" + MUSIC_PLAYER_TABLE_NAME + "`(musicId int primary key, music_name varchar(255), music_path varchar(255))";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 3);//数据库的名字以及数据库的版本

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);   //创建表
        db.execSQL(MUSIC_PLAYER_CERATE_TABLE);

    }

    /**
     * 当数据库进行升级的时候调用
     * @param db            数据库
     * @param oldVersion    旧版本号？？
     * @param newVersion    新版本？？
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}
