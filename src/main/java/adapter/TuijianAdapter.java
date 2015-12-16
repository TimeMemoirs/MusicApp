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

import entity.Tuijian;

/**
 * Created by timem on 2015/12/2.
 */
public class TuijianAdapter extends BaseAdapter{
    private Context context;
    private List<Tuijian> list;

    public TuijianAdapter(Context context, List<Tuijian> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.tuijian_layout, null);
            info.icon_no1 = (ImageView) convertView.findViewById(R.id.tuijian_no1_icon);
            info.icon_no2 = (ImageView) convertView.findViewById(R.id.tuijian_no2_icon);
            info.name_no1 = (TextView) convertView.findViewById(R.id.tuijian_no1_name);
            info.name_no2 = (TextView) convertView.findViewById(R.id.tuijian_no2_name);
            convertView.setTag(info);
        } else {
            info = (Info) convertView.getTag();
        }
        Tuijian tuijian = list.get(position);
        info.icon_no1.setImageResource(tuijian.getIcon_no1());
        info.name_no1.setText(tuijian.getName_no1());
        info.icon_no2.setImageResource(tuijian.getIcon_no2());
        info.name_no2.setText(tuijian.getName_no2());
        return convertView;
    }

    public class Info{
        private ImageView icon_no1,icon_no2;
        private TextView name_no1,name_no2;
    }
}
