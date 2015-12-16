package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timem.musicapptest.R;

import java.util.List;

import entity.MusicInfo;
import entity.MusicPlayer;

/**
 * Created by timem on 2015/11/21.
 */
public class ShowPlayMusicAdapter extends BaseAdapter {
    private Context context ;
    private List<MusicPlayer> list;

    public ShowPlayMusicAdapter(Context context, List<MusicPlayer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShowMusic showMusic = null;
        if (convertView == null) {
            showMusic = new ShowMusic();
            convertView = LayoutInflater.from(context).inflate(R.layout.music_name_text,null);
            showMusic.musicName = (TextView) convertView.findViewById(R.id.music_name_showInfo);
            convertView.setTag(showMusic);
        } else {
            showMusic = (ShowMusic) convertView.getTag();
        }
        MusicPlayer musicPlayer = list.get(position);
        showMusic.musicName.setText(musicPlayer.getPlayMusicName());
        return convertView;
    }
    private class ShowMusic{
        private TextView musicName;
    }
}
