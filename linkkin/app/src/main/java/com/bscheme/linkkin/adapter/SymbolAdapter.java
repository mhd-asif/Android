package com.bscheme.linkkin.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bscheme.linkkin.R;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ASUS on 10/18/2015.
 */
public class SymbolAdapter extends RecyclerView.Adapter<SymbolAdapter.SymbolViewHolder>{


    public static Activity mContext;
    ArrayList<String> details=new ArrayList<>();
    ArrayList<String>links=new ArrayList<>();
    int numRow,prevPos;
    ToolTipView prevView;


    public SymbolAdapter(Activity mContext, ArrayList<String> details, ArrayList<String>links)
    {
        this.mContext = mContext;
        this.details=details;
        this.links=links;
        int size=links.size();
        numRow=size/3;
        if(numRow*3<size) numRow+=1;
        prevPos=-1;
    }



    @Override
    public SymbolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        return new SymbolViewHolder(mInflater.inflate(R.layout.row_symbol, parent, false));
    }

    @Override
    public void onBindViewHolder(final SymbolViewHolder holder, int pos) {

        final int firstPos=pos*3;
        Picasso.with(mContext).load(links.get(firstPos)).fit().into(holder.img1, new Callback() {
            @Override
            public void onSuccess() {
                holder.img1.setBackgroundResource(R.drawable.back_circle_blank);
                holder.progress1.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
            }
        });

        final int secPos=firstPos+1;
        if(secPos<links.size())
        {
            Picasso.with(mContext).load(links.get(secPos)).fit().into(holder.img2, new Callback() {
                @Override
                public void onSuccess() {
                    holder.img1.setBackgroundResource(R.drawable.back_circle_blank);
                    holder.progress2.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                }
            });
        }
        else holder.frame2.setVisibility(View.INVISIBLE);

        int thirdPos=secPos+1;
        if(thirdPos<links.size())
        {
            Picasso.with(mContext).load(links.get(thirdPos)).fit().into(holder.img3, new Callback() {
                @Override
                public void onSuccess() {
                    holder.img1.setBackgroundResource(R.drawable.back_circle_blank);
                    holder.progress3.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                }
            });
        }
        else holder.frame3.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return numRow;
    }

    public class SymbolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView img1,img2,img3;
        public ProgressBar progress1,progress2,progress3;
        public FrameLayout frame1,frame2,frame3,tempView;
        private ToolTipRelativeLayout toolTipLayout;
        private ToolTipView toolTipView;

        public SymbolViewHolder(View view)
        {
            super(view);
            img1=(ImageView)view.findViewById(R.id.img1);
            img2=(ImageView)view.findViewById(R.id.img2);
            img3=(ImageView)view.findViewById(R.id.img3);
            progress1=(ProgressBar)view.findViewById(R.id.progress1);
            progress2=(ProgressBar)view.findViewById(R.id.progress2);
            progress3=(ProgressBar)view.findViewById(R.id.progress3);
            frame1=(FrameLayout)view.findViewById(R.id.frame1);
            frame2=(FrameLayout)view.findViewById(R.id.frame2);
            frame3=(FrameLayout)view.findViewById(R.id.frame3);
            toolTipLayout=(ToolTipRelativeLayout)view.findViewById(R.id.tooltiplayout);
            frame1.setOnClickListener(this);
            frame2.setOnClickListener(this);
            frame3.setOnClickListener(this);
        }

        public void onClick(View view)
        {
            int pos=getAdapterPosition();
            int tempPos=-1;
            if(view.getId()==R.id.frame1)
            {
                tempPos=pos*3;
                tempView=frame1;
            }
            else if(view.getId()==R.id.frame2)
            {
                tempPos=pos*3+1;
                tempView=frame2;
            }
            else if(view.getId()==R.id.frame3)
            {
                tempPos=pos*3+2;
                tempView=frame3;
            }
            if(tempPos!=-1)
            {
                if(prevPos!=-1)
                {
                    prevView.remove();
                }
                if(prevPos!=tempPos)
                {
                    ToolTip toolTip = new ToolTip()
                            .withText(details.get(tempPos))
                            .withColor(mContext.getResources().getColor(R.color.primary_color_dark))
                            .withTextColor(Color.parseColor("#ffffff"))
                            .withAnimationType(ToolTip.AnimationType.FROM_TOP);
                    toolTipView = toolTipLayout.showToolTipForView(toolTip, tempView);
                    prevPos=tempPos;
                    prevView=toolTipView;
                }
                else
                {
                    prevPos=-1;
                }
            }
        }
    }






}
