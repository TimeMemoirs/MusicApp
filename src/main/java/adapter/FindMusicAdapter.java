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

import static com.example.timem.musicapptest.R.id.music_activity_name;
import static com.example.timem.musicapptest.R.id.music_name_local;

/**
 * Created by timem on 2015/12/3.
 */
public class FindMusicAdapter extends BaseAdapter {
    private Context context;
    private List<MusicInfo> list;

    public FindMusicAdapter(Context context, List<MusicInfo> list) {
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
        Info info = null;
        if (convertView == null) {
            info = new Info();
            convertView = LayoutInflater.from(context).inflate(R.layout.local_music_list_layout, null);
            info.textView = (TextView) convertView.findViewById(R.id.music_name_local);
            convertView.setTag(info);
        } else {
            info = (Info) convertView.getTag();
        }
        MusicInfo musicInfo = list.get(position);
        info.textView.setText(musicInfo.getMusicName());
        return convertView;
    }

    public class Info{
        private TextView textView;
    }
}
