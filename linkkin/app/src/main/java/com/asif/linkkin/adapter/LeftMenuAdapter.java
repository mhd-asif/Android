package com.asif.linkkin.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asif.linkkin.MainActivity;
import com.asif.linkkin.R;
import com.asif.linkkin.left_menu_fragment.ContainerFragment;
import com.asif.linkkin.left_menu_fragment.KincomingFragment;
import com.asif.linkkin.left_menu_fragment.KindividualFragment;
import com.asif.linkkin.left_menu_fragment.KindomFragment;
import com.asif.linkkin.left_menu_fragment.KinformationFragment;
import com.asif.linkkin.left_menu_fragment.KintertainmentFragment;
import com.asif.linkkin.left_menu_fragment.KinvironmentFragment;
import com.asif.linkkin.left_menu_fragment.SettingFragment;
import com.asif.linkkin.utils.CheckConnectivity;
import com.asif.linkkin.utils.Font;
import com.asif.linkkin.helper.MyMenuClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ASUS on 10/18/2015.
 */
public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.LeftMenuViewHolder>{


    public static Activity mContext;
    ArrayList<String>titles=new ArrayList<>();
    ArrayList<Integer>iconResources=new ArrayList<>();

    public static int selectedPos;
    int prevPos;

    String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };


    public LeftMenuAdapter(Activity mContext,ArrayList<String>titles,ArrayList<Integer>iconResources)
    {
        this.mContext = mContext;
        this.titles=titles;
        this.iconResources=iconResources;
        selectedPos=1;
        prevPos=1;
    }



    @Override
    public LeftMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        return new LeftMenuViewHolder(mInflater.inflate(R.layout.row_menu_list, parent, false));
    }

    @Override
    public void onBindViewHolder(LeftMenuViewHolder holder, int pos) {
        holder.imgIcon.setImageResource(iconResources.get(pos));
        Font.LATO_Bold.apply(mContext, holder.txtTitle);
        holder.txtTitle.setText(titles.get(pos));
        if(pos==selectedPos) holder.root.setBackgroundColor(Color.parseColor("#009999"));
        else holder.root.setBackgroundColor(mContext.getResources().getColor(R.color.bg_left_menu_dark));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class LeftMenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView imgIcon;
        public TextView txtTitle;
        RelativeLayout root;

        public LeftMenuViewHolder(View view)
        {
            super(view);
            imgIcon=(ImageView)view.findViewById(R.id.row_icon);
            txtTitle=(TextView)view.findViewById(R.id.row_title);
            root=(RelativeLayout)view.findViewById(R.id.root);
            view.setOnClickListener(this);
        }

        public void onClick(View view)
        {
            int pos=getAdapterPosition();
            prevPos=selectedPos;
            selectedPos=pos;
            switch (pos) {
                case 0:
                    switchFragment(new KindomFragment(), titles.get(pos));
                    break;
                case 1:
                    switchFragment(new KindividualFragment(),titles.get(pos));
                    break;
                case 2:
                    switchFragment(new KincomingFragment(),titles.get(pos));
                    break;
                case 3:
                    switchFragment(new ContainerFragment(),titles.get(pos));
                    break;
                case 4:
                    switchFragment(new KinformationFragment(),titles.get(pos));
                    break;
                case 5:
                    switchFragment(new KintertainmentFragment(),titles.get(pos));
                    break;
                case 6:
                    switchFragment(new KinvironmentFragment(),titles.get(pos));
                    break;
                case 7:
                    switchFragment(new SettingFragment(),titles.get(pos));
                    break;

                default:
                    switchFragment(new ContainerFragment(),titles.get(pos));
            }
        }
    }


/*
    private void setSelectedBackground()
    {
        Handler hh=new Handler();
        hh.postDelayed(new Runnable() {
            @Override
            public void run() {
                tempLayout.setBackgroundColor(Color.parseColor("#ff00ff"));
            }
        },100);
    }   */



    public void notifyAdapter()
    {
        Handler hh=new Handler();
        hh.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        },120);
    }



    public void switchFragment(Fragment fragment,String title) {
        if (mContext == null) {
            return;
        }
        if (mContext instanceof MainActivity) {
            MyMenuClass.menuType="";
            CheckConnectivity connectivity=new CheckConnectivity(mContext);
            if(connectivity.isConnected())
            {
                MainActivity home = (MainActivity) mContext;
                home.switchContainerFragment(fragment);
                home.setToolBarTitle(title);
                notifyAdapter();
            }
            else
            {
                if(title.equals("Setting"))
                {
                    MainActivity home = (MainActivity) mContext;
                    home.switchContainerFragment(fragment);
                    home.setToolBarTitle(title);
                    notifyAdapter();
                }
                else if(title.equals("Kindom"))
                {
                // SharedPreferences pref=mContext.getSharedPreferences("database_linkkin", 0);
                //  String kindom=pref.getString("kindom","no");
                String path_kindom=mContext.getCacheDir().getAbsolutePath()+"/"+mContext.getString(R.string.file_kindom);
                // Log.e("catc path: ",path_kindom);
                File file = new File(path_kindom);
                if(!file.exists())
                {
                    Toast.makeText(mContext, "Please turn your internet connection ON", Toast.LENGTH_SHORT).show();
                    selectedPos=prevPos;
                }
                else
                {
                    MainActivity home = (MainActivity) mContext;
                    home.switchContainerFragment(fragment);
                    home.setToolBarTitle(title);
                    notifyAdapter();
                }
                }
                else if(title.equals("Kindividual"))
                {
                    // SharedPreferences pref=mContext.getSharedPreferences("database_linkkin", 0);
                    //  String kindom=pref.getString("kindom","no");
                    String path_kindividual=mContext.getCacheDir().getAbsolutePath()+"/"+mContext.getString(R.string.file_kindividual);
                    // Log.e("catc path: ",path_kindom);
                    File file = new File(path_kindividual);
                    if(!file.exists())
                    {
                        Toast.makeText(mContext, "Please turn your internet connection ON", Toast.LENGTH_SHORT).show();
                        selectedPos=prevPos;
                    }
                    else
                    {
                        MainActivity home = (MainActivity) mContext;
                        home.switchContainerFragment(fragment);
                        home.setToolBarTitle(title);
                        notifyAdapter();
                    }
                }
                else if(title.equals("Kinformation"))
                {
                    // SharedPreferences pref=mContext.getSharedPreferences("database_linkkin", 0);
                    //  String kindom=pref.getString("kindom","no");
                    String path_kinfo=mContext.getCacheDir().getAbsolutePath()+"/"+mContext.getString(R.string.file_kinformation_cat);
                    File file = new File(path_kinfo);
                    if(!file.exists())
                    {
                        Toast.makeText(mContext, "Please turn your internet connection ON", Toast.LENGTH_SHORT).show();
                        selectedPos=prevPos;
                    }
                    else
                    {
                        MainActivity home = (MainActivity) mContext;
                        home.switchContainerFragment(fragment);
                        home.setToolBarTitle(title);
                        notifyAdapter();
                    }
                }
                else if(title.equals("Kintertainment"))
                {
                    // SharedPreferences pref=mContext.getSharedPreferences("database_linkkin", 0);
                    //  String kindom=pref.getString("kindom","no");
                    String path_kintertainment=mContext.getCacheDir().getAbsolutePath()+"/"+mContext.getString(R.string.file_kintertainment_cat);
                    File file = new File(path_kintertainment);
                    if(!file.exists())
                    {
                        Toast.makeText(mContext, "Please turn your internet connection ON", Toast.LENGTH_SHORT).show();
                        selectedPos=prevPos;
                    }
                    else
                    {
                        MainActivity home = (MainActivity) mContext;
                        home.switchContainerFragment(fragment);
                        home.setToolBarTitle(title);
                        notifyAdapter();
                    }
                }
                else if(title.equals("Kincoming"))
                {
                    // SharedPreferences pref=mContext.getSharedPreferences("database_linkkin", 0);
                    //  String kindom=pref.getString("kindom","no");
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    int month = cal.get(Calendar.MONTH);
                    int year = cal.get(Calendar.YEAR);
                    String path_kincoming=mContext.getCacheDir().getAbsolutePath()+"/"+mContext.getString(R.string.file_kincoming)+"_"+months[month]+"_"+year;
                    File file = new File(path_kincoming);
                    if(!file.exists())
                    {
                        Toast.makeText(mContext, "Please turn your internet connection ON", Toast.LENGTH_SHORT).show();
                        selectedPos=prevPos;
                    }
                    else
                    {
                        MainActivity home = (MainActivity) mContext;
                        home.switchContainerFragment(fragment);
                        home.setToolBarTitle(title);
                        notifyAdapter();
                    }
                }
                else if(title.equals("Kinteract"))
                {
                    // SharedPreferences pref=mContext.getSharedPreferences("database_linkkin", 0);
                    //  String kindom=pref.getString("kindom","no");
                    String path_kinteract=mContext.getCacheDir().getAbsolutePath()+"/"+mContext.getString(R.string.file_kinteract);
                    File file = new File(path_kinteract);
                    if(!file.exists())
                    {
                        Toast.makeText(mContext, "Please turn your internet connection ON", Toast.LENGTH_SHORT).show();
                        selectedPos=prevPos;
                    }
                    else
                    {
                        MainActivity home = (MainActivity) mContext;
                        home.switchContainerFragment(fragment);
                        home.setToolBarTitle(title);
                        notifyAdapter();
                    }
                }
                else
                {
                    Toast.makeText(mContext,"Please turn your internet connection ON",Toast.LENGTH_SHORT).show();
                    selectedPos=prevPos;
                }
            }
        }
    }






}
