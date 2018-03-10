package com.asif.linkkin.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.asif.linkkin.PolicyDetailsActivity;
import com.asif.linkkin.R;
import com.asif.linkkin.models.KinformationDetailsInfo;
import com.asif.linkkin.utils.Font;
import com.asif.linkkin.widgets.ExpandableTextView;

import java.util.ArrayList;

/**
 * Created by ASUS on 10/18/2015.
 */
public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyViewHolder>{


    public static Context mContext;

    String tempPollId;

    ArrayList<String> ids = new ArrayList();
    ArrayList<String> titles = new ArrayList();
    ArrayList<String> shortDescs = new ArrayList();
    ArrayList<String> descs = new ArrayList();


    public PolicyAdapter(Context paramContext, ArrayList<String> ids, ArrayList<String> titles, ArrayList<String> shortDescs, ArrayList<String> descs)
    {
        this.mContext = paramContext;
        this.ids=ids;
        this.titles=titles;
        this.shortDescs=shortDescs;
        this.descs=descs;
    }



    @Override
    public PolicyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        return new PolicyViewHolder(mInflater.inflate(R.layout.row_item_policy, parent, false));
    }

    @Override
    public void onBindViewHolder(PolicyViewHolder holder, int paramInt) {
        Font.LATO_Regular.apply(mContext, holder.tvDate);
        Font.LATO_Regular.apply(mContext, holder.tvPolicyTitle);
        holder.tvDate.setText(titles.get(paramInt));
        holder.tvPolicyTitle.setText(shortDescs.get(paramInt));

    }

    @Override
    public int getItemCount() {
        return this.titles.size();
    }

    public class PolicyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView imgPoll,imgLike;
        public RippleView mRippleView;
        public TextView tvDate,txtLikeCount;
        public ExpandableTextView tvPolicyTitle;
        FrameLayout framePoll;
        LinearLayout linearLike;

        public PolicyViewHolder(View paramView)
        {
            super(paramView);
            this.tvPolicyTitle = ((ExpandableTextView)paramView.findViewById(R.id.tv_policy_title));
            this.tvDate = ((TextView)paramView.findViewById(R.id.tv_policy_date));
            imgLike=(ImageView)paramView.findViewById(R.id.imgLike);
            txtLikeCount=(TextView)paramView.findViewById(R.id.txtLikeCount);
            this.mRippleView = ((RippleView)paramView.findViewById(R.id.mRippleView));
            imgPoll=(ImageView)paramView.findViewById(R.id.imgPoll);
            framePoll=(FrameLayout)paramView.findViewById(R.id.framePoll);
            linearLike=(LinearLayout)paramView.findViewById(R.id.linearLike);
            this.mRippleView.setOnClickListener(this);
        }

        public void onClick(View paramView)
        {
            Handler hh=new Handler();
            hh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int pos=getAdapterPosition();
                    KinformationDetailsInfo info=new KinformationDetailsInfo();
                    info.title=titles.get(pos);
                    info.desc=descs.get(pos);
                    Intent in = new Intent(mContext, PolicyDetailsActivity.class);
                    in.putExtra("info",info);
                    mContext.startActivity(in);
                }
            }, 100);
        }
    }



}
