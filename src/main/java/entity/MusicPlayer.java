package entity;

import java.io.Serializable;

/**
 * Created by timem on 2015/11/19.
 */
public class MusicPlayer implements Serializable {
    private int playMusicId;
    private String playMusicName;
    private String playMusicPath;

    public MusicPlayer() {

    }

    public MusicPlayer(int playMusicId, String playMusicName, String playMusicPath) {
        this.playMusicId = playMusicId;
        this.playMusicName = playMusicName;
        this.playMusicPath = playMusicPath;
    }

    public int getPlayMusicId() {
        return playMusicId;
    }

    public void setPlayMusicId(int playMusicId) {
        this.playMusicId = playMusicId;
    }

    public String getPlayMusicName() {
        return playMusicName;
    }

    public void setPlayMusicName(String playMusicName) {
        this.playMusicName = playMusicName;
    }

    public String getPlayMusicPath() {
        return playMusicPath;
    }

    public void setPlayMusicPath(String playMusicPath) {
        this.playMusicPath = playMusicPath;
    }
}
