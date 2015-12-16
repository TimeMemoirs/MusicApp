package entity;

import java.io.Serializable;

/**
 * Created by timem on 2015/12/2.
 */
public class Shoucang implements Serializable {
    private int icon;
    private String name;
    private String more;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }
}
