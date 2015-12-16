package entity;

import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by timem on 2015/12/2.
 */
public class Fangfa implements Serializable {
    private int icon;
    private String text;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
