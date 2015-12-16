package entity;

import java.io.Serializable;

/**
 * Created by timem on 2015/12/2.
 */
public class Tuijian implements Serializable {
    private int icon_no1;
    private int icon_no2;
    private String name_no1;
    private String name_no2;

    public int getIcon_no1() {
        return icon_no1;
    }

    public void setIcon_no1(int icon_no1) {
        this.icon_no1 = icon_no1;
    }

    public int getIcon_no2() {
        return icon_no2;
    }

    public void setIcon_no2(int icon_no2) {
        this.icon_no2 = icon_no2;
    }

    public String getName_no1() {
        return name_no1;
    }

    public void setName_no1(String name_no1) {
        this.name_no1 = name_no1;
    }

    public String getName_no2() {
        return name_no2;
    }

    public void setName_no2(String name_no2) {
        this.name_no2 = name_no2;
    }
}
