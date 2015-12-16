package adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by timem on 2015/11/17.
 */
public class ViewAdapter extends PagerAdapter {
    List<View> list ;

    public ViewAdapter(List<View> list) {
        Log.d("==========================", "222222222222222");
        this.list = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = list.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
