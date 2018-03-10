package com.asif.linkkin.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asif.linkkin.R;
import com.asif.linkkin.utils.Font;

import java.util.ArrayList;

/**
 * Created by kanchan on 11/24/2015.
 */
public class OfficialInfoAdapter extends RecyclerView.Adapter<OfficialInfoAdapter.OfficialViewHolder> {

    public static Activity mContext;
    ArrayList<String> titles=new ArrayList<>();
    ArrayList<String>values=new ArrayList<>();


    public OfficialInfoAdapter(Activity mContext,ArrayList<String>titles,ArrayList<String>values)
    {
        this.mContext = mContext;
        this.titles=titles;
        this.values=values;
    }


    @Override
    public OfficialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        return new OfficialViewHolder(mInflater.inflate(R.layout.row_official, parent, false));
    }

    @Override
    public void onBindViewHolder(OfficialViewHolder holder, int position) {
        Font.LATO_Regular.apply(mContext, holder.txtTitle);
        Font.LATO_Regular.apply(mContext, holder.txtValue);

        holder.txtTitle.setText(titles.get(position));
        holder.txtValue.setText(values.get(position));
       // if(position==0) holder.txtSeparator.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    class OfficialViewHolder extends RecyclerView.ViewHolder
    {

        TextView txtTitle,txtValue;
        public OfficialViewHolder(View itemView) {
            super(itemView);
            txtTitle=(TextView)itemView.findViewById(R.id.txtTitle);
            txtValue=(TextView)itemView.findViewById(R.id.txtValue);
           // txtSeparator=(TextView)itemView.findViewById(R.id.txtSeparator);

        }



    }


}
