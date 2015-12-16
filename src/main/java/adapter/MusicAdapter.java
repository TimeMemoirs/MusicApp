package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.timem.musicapptest.R;

import java.util.List;

import entity.Fangfa;

/**
 * Created by timem on 2015/12/2.
 */
public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<Fangfa> list;

    public MusicAdapter(Context context, List<Fangfa> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fangfa_layout, null);
            info.imageView = (ImageView) convertView.findViewById(R.id.fangfa_icon);
            info.textView = (TextView) convertView.findViewById(R.id.fangfa_name);
            convertView.setTag(info);
        } else {
            info = (Info) convertView.getTag();
        }
        Fangfa fangfa = list.get(position);
        info.imageView.setImageResource(fangfa.getIcon());
        info.textView.setText(fangfa.getText());
        return convertView;
    }

    public class Info{
        private ImageView imageView;
        private TextView textView;
    }
}
