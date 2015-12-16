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

import entity.Shoucang;

/**
 * Created by timem on 2015/12/2.
 */
public class ShouCangAdapter extends BaseAdapter {
    private Context context;
    private List<Shoucang> list;

    public ShouCangAdapter(Context context, List<Shoucang> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.shoucang_layout, null);
            info.imageView = (ImageView) convertView.findViewById(R.id.shoucang_icon);
            info.name = (TextView) convertView.findViewById(R.id.shoucang_name);
            info.more = (TextView) convertView.findViewById(R.id.shoucang_more);
            convertView.setTag(info);
        } else {
            info = (Info) convertView.getTag();
        }

        Shoucang shoucang = list.get(position);
        info.imageView.setImageResource(shoucang.getIcon());
        info.name.setText(shoucang.getName());
        info.more.setText(shoucang.getMore());
        return convertView;
    }

    public class Info{
        private ImageView imageView;
        private TextView name, more;
    }
}
