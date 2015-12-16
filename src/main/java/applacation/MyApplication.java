package applacation;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
//
//import entity.DaoMaster;
//import entity.DaoSession;


/**
 * Created by Administrator on 2015/9/24.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;
   /* private static DaoMaster daoMaster;
    private static DaoSession daoSession;*/
    private RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
      //  cacheDir = StorageUtils.getOwnCacheDirectory(this, "imageLodar/cache");//缓存路径

    }

    public static MyApplication getIntance(){
        return myApplication;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    /**
     * GreenDao
     * 主要用  DaoSession  ；
     * @param context
     * @return
     */
   /* public static DaoMaster getDaoMaster(Context context){
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,"school.db",null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }
    public static DaoSession getDaoSession(Context context){
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }*/
}
