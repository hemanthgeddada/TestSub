package com.example.dell.mavride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Hemanth on 2/25/2015.
 */
public class RequestAdapter extends ArrayAdapter<ParseObject> {

    protected Context reqContext;
    protected List<ParseObject> reqObjects;

    public RequestAdapter(Context context, List<ParseObject> request) {
        super(context, R.layout.activity_driver_home, request);
        reqContext = context;
        reqObjects = request;
    }
    @Override
    public View getView(final int Position, View convertView, ViewGroup Parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(reqContext).inflate(
                    R.layout.activity_driver_home, null);
            holder = new ViewHolder();
            holder.DvrTxtReqId = (TextView) convertView.findViewById(R.id.dvrTxtId);
            holder.DvrTxtReqLoc = (TextView) convertView.findViewById(R.id.dvrTxtLoc);
            holder.DvrTxtReqDes = (TextView) convertView.findViewById(R.id.dvrTxtDes);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ParseObject Requests = reqObjects.get(Position);

        String Id = Requests.getString("objectId");
        holder.DvrTxtReqId.setText(Id);

        String Loc = Requests.getString("Source");
        holder.DvrTxtReqLoc.setText(Loc);

        String Des = Requests.getString("Destination");
        holder.DvrTxtReqDes.setText(Des);


        return convertView;
    }

    public static class ViewHolder{
        TextView DvrTxtReqId;
        TextView DvrTxtReqLoc;
        TextView DvrTxtReqDes;
    }
}