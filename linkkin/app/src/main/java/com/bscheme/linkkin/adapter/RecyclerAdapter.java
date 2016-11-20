package com.bscheme.linkkin.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bscheme.linkkin.KintertainmentDetailsActivity;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.utils.Font;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>
{
    Activity context;
    ImageView[] imgViews = new ImageView[7];
    private LayoutInflater inflater;

    ArrayList<String> ids = new ArrayList();
    ArrayList<String> titles = new ArrayList();
    ArrayList<String> shortDescs = new ArrayList();
    ArrayList<String> descs = new ArrayList();
    ArrayList<String> imgLinks = new ArrayList();
    ArrayList<String> times = new ArrayList();
    ArrayList<String> youtubes = new ArrayList();
    ArrayList<String> tvs = new ArrayList();
    HashMap<Integer, ArrayList<String>> allImageLinks = new HashMap();


    public RecyclerAdapter(Activity activity, ArrayList<String> ids, ArrayList<String> titles, ArrayList<String> shortDescs, ArrayList<String> descs, ArrayList<String> imgLinks,ArrayList<String> times, ArrayList<String> youtubes, ArrayList<String> tvs, HashMap<Integer, ArrayList<String>> allImageLinks)
    {
        this.inflater = LayoutInflater.from(activity);
        this.context = activity;
        this.ids=ids;
        this.titles = titles;
        this.shortDescs = shortDescs;
        this.descs = descs;
        this.times = times;
        this.imgLinks = imgLinks;
        this.youtubes = youtubes;
        this.tvs = tvs;
        this.allImageLinks=allImageLinks;
        this.imgViews = new ImageView[titles.size()];
    }

    public int getItemCount()
    {
        return this.titles.size();
    }

    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Font.LATO_Regular.apply(context, holder.txtData);
        Font.LATO_Regular.apply(context, holder.txtHeader);
        Font.LATO_Regular.apply(context, holder.txtTime);
        holder.txtTime.setText(times.get(position));
        holder.txtHeader.setText((CharSequence)this.titles.get(position));
        holder.txtData.setText((CharSequence)this.shortDescs.get(position));
        Picasso.with(this.context).load((String)this.imgLinks.get(position)).fit().into(holder.imgMain);
        ArrayList<String> extraLinks = allImageLinks.get(position);
        for(int j=0;j<4;j++)
        {
            if (j < extraLinks.size()) {
                Picasso.with(this.context).load(extraLinks.get(j)).resizeDimen(R.dimen.list_small_image_size, R.dimen.list_small_image_size).into(holder.imgs[j]);
            }
            else holder.frames[j].setVisibility(View.GONE);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position)
    {
        View view = inflater.inflate(R.layout.custom_row,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements OnClickListener
    {
        int[] frameIds = { R.id.frame1, R.id.frame2, R.id.frame3, R.id.frame4 };
        FrameLayout[] frames = new FrameLayout[7];
        ImageView imgTv;
        int[] imgIds = { R.id.img1, R.id.img2, R.id.img3, R.id.img4 };
        ImageView imgMain;
        ImageView imgYoutube;
        ImageView[] imgs = new ImageView[7];
        int[] progressIds = { R.id.progress1, R.id.progress2, R.id.progress3, R.id.progress4 };
        ProgressBar progressMain;
        ProgressBar[] progresses = new ProgressBar[7];
        TextView txtData;
        TextView txtHeader;
        TextView txtTime;
        LinearLayout linearRoot;

        public MyViewHolder(View paramView)
        {
            super(paramView);
            this.txtHeader = ((TextView)paramView.findViewById(R.id.txtHeader));
            this.txtTime = ((TextView)paramView.findViewById(R.id.txtTime));
            this.txtData = ((TextView)paramView.findViewById(R.id.txtData));
            this.imgMain = ((ImageView)paramView.findViewById(R.id.imgMain));
            this.progressMain = ((ProgressBar)paramView.findViewById(R.id.progressMain));
            linearRoot=(LinearLayout)paramView.findViewById(R.id.linearRoot);
            progressMain.setIndeterminate(true);
            progressMain.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.primary_color_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
            int i = 0;
            while (i < this.imgIds.length)
            {
                this.imgs[i] = ((ImageView)paramView.findViewById(this.imgIds[i]));
                this.frames[i] = ((FrameLayout)paramView.findViewById(this.frameIds[i]));
                this.progresses[i] = ((ProgressBar)paramView.findViewById(this.progressIds[i]));
                progresses[i].setIndeterminate(true);
                progresses[i].getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.primary_color_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
                i += 1;
            }
            this.imgYoutube = ((ImageView)paramView.findViewById(R.id.imgYoutube));
            this.imgTv = ((ImageView)paramView.findViewById(R.id.imgTv));
            linearRoot.setOnClickListener(MyViewHolder.this);
            imgYoutube.setOnClickListener(MyViewHolder.this);
            imgTv.setOnClickListener(MyViewHolder.this);
        }

        public void onClick(View v)
        {
            int pos=getAdapterPosition();
            if(v.getId()==R.id.imgYoutube)
            {
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse(youtubes.get(pos)));
                context.startActivity(in);
            }
            else if(v.getId()==R.id.imgTv)
            {
                Intent in=new Intent(Intent.ACTION_VIEW, Uri.parse(tvs.get(pos)));
                context.startActivity(in);
            }
            else if(v.getId()==R.id.linearRoot)
            {
                Intent in=new Intent(context, KintertainmentDetailsActivity.class);
                in.putExtra("title",titles.get(pos));
                in.putExtra("desc",descs.get(pos));
                context.startActivity(in);
            }
        }

    }
}
