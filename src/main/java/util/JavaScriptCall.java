package util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.timem.musicapptest.FindMusicActivity;

/**
 * Created by timem on 2015/12/9.
 */




public class JavaScriptCall {
    private Context context;

    public JavaScriptCall(Context context) {
        this.context = context;
    }

    @android.webkit.JavascriptInterface
    public void showToastMsg(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

    }

    @android.webkit.JavascriptInterface
    public void findMusic(){
        Intent intent = new Intent();
        intent.setClass(context, FindMusicActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /*@android.webkit.JavascriptInterface
    public void login(String name, String pwd) {
        if (name.equals("111") && pwd.equals("123")) {
            Log.d("", "***********");
            Intent it = new Intent();
            it.setClass(context, MainMusic.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);

            Toast.makeText(context, "登陆成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }

    }*/
}
