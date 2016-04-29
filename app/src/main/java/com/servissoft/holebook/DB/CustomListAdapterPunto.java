package com.servissoft.holebook.DB;

/**
 * Created by iproject on 2/03/15.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.servissoft.holebook.R;
import com.servissoft.holebook.entidades.Punto;

import java.util.List;


public class CustomListAdapterPunto extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Punto> movieItems;

    public CustomListAdapterPunto(Activity activity, List<Punto> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);


        TextView x = (TextView) convertView.findViewById(R.id.x);
        TextView y = (TextView) convertView.findViewById(R.id.y);
        TextView t = (TextView) convertView.findViewById(R.id.t);
        TextView ax = (TextView) convertView.findViewById(R.id.ax);
        TextView ay = (TextView) convertView.findViewById(R.id.ay);
        TextView az = (TextView) convertView.findViewById(R.id.az);

        Punto m = movieItems.get(position);
        x.setText("" + String.valueOf(m.getX()) + "");
        y.setText("" + String.valueOf(m.getY() + ""));
        t.setText("" + String.valueOf(m.getT() + ""));
        ax.setText("" + String.valueOf(m.getAx() + ""));
        ay.setText("" + String.valueOf(m.getAy() + ""));
        az.setText("" + String.valueOf(m.getAz() + ""));

        return convertView;*/
        return null;
    }

}
