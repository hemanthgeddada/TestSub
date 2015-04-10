package com.example.dell.mavride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
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
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(reqContext).inflate(
                    R.layout.activity_driver_home, null);
            holder = new ViewHolder();
            holder.DvrTxtReqStatus = (TextView) convertView.findViewById(R.id.dvrTxtSts);
            holder.DvrTxtReqLoc = (TextView) convertView.findViewById(R.id.dvrTxtLoc);
            holder.DvrTxtReqDes = (TextView) convertView.findViewById(R.id.dvrTxtDes);
            // remove after homework holder.DvrTxtReqName = (TextView) convertView.findViewById(R.id.dvrTxtName);
            holder.DvrTxtReqName = (TextView) convertView.findViewById(R.id.dvrTxtName);
            holder.DvrTxtReqRiders = (TextView) convertView.findViewById(R.id.dvrTxtRidCnt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ParseObject Requests = reqObjects.get(Position);

        //String Id = Requests.getString("objectId");

        String Status = Requests.getString("Status");
        holder.DvrTxtReqStatus.setText(Status);

        String Loc = Requests.getString("Source");
        holder.DvrTxtReqLoc.setText(Loc);

        String Des = Requests.getString("Destination");
        holder.DvrTxtReqDes.setText(Des);

        int Riders = Requests.getInt("NoRiders");
        String Rid = String.valueOf(Riders);
        holder.DvrTxtReqRiders.setText(Rid);

        String FirstName = Requests.getString("RiderId");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");
        query.whereEqualTo("UserId",FirstName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if(e==null){
                    final String FName = parseObject.getString("First_Name");
                    holder.DvrTxtReqName.setText(FName);
                }
            }
        });

        return convertView;
    }

    public static class ViewHolder{
        TextView DvrTxtReqStatus;
        TextView DvrTxtReqLoc;
        TextView DvrTxtReqDes;
        TextView DvrTxtReqName;
        TextView DvrTxtReqRiders;
    }
}