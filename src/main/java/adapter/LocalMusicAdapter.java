package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timem.musicapptest.R;

import java.util.List;

import entity.MusicInfo;

/**
 * Created by timem on 2015/11/18.
 */
public class LocalMusicAdapter extends BaseAdapter {
    private List<MusicInfo> list ;
    private Context context;

    public LocalMusicAdapter(List<MusicInfo> list, Context context) {
        this.list = list;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        MusicListShowInfo musicListShowInfo = null;
        if (convertView == null) {
            musicListShowInfo = new MusicListShowInfo();
            convertView = LayoutInflater.from(context).inflate(R.layout.local_music_list_layout,null);
            musicListShowInfo.music_name_local = (TextView) convertView.findViewById(R.id.music_name_local);
            convertView.setTag(musicListShowInfo);
        } else {
            musicListShowInfo = (MusicListShowInfo) convertView.getTag();
        }
        MusicInfo musicInfo = list.get(position);
        musicListShowInfo.music_name_local.setText(musicInfo.getMusicName());
       /* musicListShowInfo.music_name_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("====================adapter", "" + position);
            }
        });*/
        return convertView;
    }

    public class MusicListShowInfo{
        private TextView music_name_local;
    }
}
