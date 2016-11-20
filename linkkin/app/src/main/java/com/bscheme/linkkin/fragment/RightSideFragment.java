package com.bscheme.linkkin.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bscheme.linkkin.MainActivity;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.db.Constants;
import com.bscheme.linkkin.db.DbHelper;
import com.bscheme.linkkin.left_menu_fragment.ContainerFragment;
import com.bscheme.linkkin.left_menu_fragment.KincomingFragment;
import com.bscheme.linkkin.left_menu_fragment.KinformationFragment;
import com.bscheme.linkkin.left_menu_fragment.KintertainmentFragment;
import com.bscheme.linkkin.models.KindividualInfo;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.helper.MyMenuClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RightSideFragment extends Fragment {

    LinearLayout linearKincoming,linearKinteract,linearKinformation,linearKintertainment;
    CardView cardKincoming,cardKinformation,cardKinteract,cardKintertainment;
    RelativeLayout updateNotifyMsg;

    String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    private int trimLength=60;

    ArrayList<String>eventDetails=new ArrayList<String>();
    ArrayList<String>eventDates=new ArrayList<String>();
    ArrayList<String>eventIds=new ArrayList<String>();

    ArrayList<String>infoDetails=new ArrayList<String>();
    ArrayList<String>infoDates=new ArrayList<String>();
    ArrayList<String>infoIds=new ArrayList<String>();
    ArrayList<String>infoTypes=new ArrayList<String>();
    ArrayList<String>infoImgLinks=new ArrayList<String>();

    ArrayList<String>enterIds=new ArrayList<String>();
    ArrayList<String>enterTitles=new ArrayList<String>();
    ArrayList<String>enterDetails=new ArrayList<String>();
    ArrayList<String>enterTypes=new ArrayList<String>();
    ArrayList<String>enterImgLinks=new ArrayList<String>();
    ArrayList<String>enterTimes=new ArrayList<String>();

    ArrayList<String>interactIds=new ArrayList<String>();
    ArrayList<String>interactPostIds=new ArrayList<String>();
    ArrayList<String>interactPostTitles=new ArrayList<String>();
    ArrayList<String>interactEmpIds=new ArrayList<String>();
    ArrayList<String>interactCommentors=new ArrayList<String>();
    ArrayList<String>interactDates=new ArrayList<String>();
    ArrayList<String>interactDescriptions=new ArrayList<String>();

    DbHelper helper;


    public RightSideFragment newInstance(KindividualInfo info) {
        RightSideFragment f = new RightSideFragment();
     /*   Bundle args = new Bundle();
        args.putParcelable("info",info);
        f.setArguments(args);    */
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
       // new getKindividualInfos().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_right_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearKincoming=(LinearLayout)view.findViewById(R.id.linearKincoming);
        linearKinteract=(LinearLayout)view.findViewById(R.id.linearKinteract);
        linearKinformation=(LinearLayout)view.findViewById(R.id.linearKinformation);
        linearKintertainment=(LinearLayout)view.findViewById(R.id.linearKintertainment);
        cardKincoming=(CardView)view.findViewById(R.id.cardKincoming);
        cardKinformation=(CardView)view.findViewById(R.id.cardKinformation);
        cardKinteract=(CardView)view.findViewById(R.id.cardKinteract);
        cardKintertainment=(CardView)view.findViewById(R.id.cardKintertainment);
        updateNotifyMsg =(RelativeLayout)view.findViewById(R.id.notify_update_msg);

        retreiveUpdatedData();


    }





    public void retreiveUpdatedData()
    {
        eventDetails=new ArrayList<String>();
        eventDates=new ArrayList<String>();
        eventIds=new ArrayList<String>();

        infoDetails=new ArrayList<String>();
        infoDates=new ArrayList<String>();
        infoIds=new ArrayList<String>();
        infoTypes=new ArrayList<String>();
        infoImgLinks=new ArrayList<String>();

        enterIds=new ArrayList<String>();
        enterTitles=new ArrayList<String>();
        enterDetails=new ArrayList<String>();
        enterTypes=new ArrayList<String>();
        enterImgLinks=new ArrayList<String>();
        enterTimes=new ArrayList<String>();

        interactIds=new ArrayList<String>();
        interactPostIds=new ArrayList<String>();
        interactPostTitles=new ArrayList<String>();
        interactEmpIds=new ArrayList<String>();
        interactCommentors=new ArrayList<String>();
        interactDates=new ArrayList<String>();
        interactDescriptions=new ArrayList<String>();

        try {
            helper=new DbHelper(getActivity(), Constants.DATABASE_NAME,1);
        }
        catch (Exception e) {}

        try
        {
            HashMap<String,ArrayList<String>> data1=helper.getAllRow(Constants.TABLE_EVENT,Constants.EVENT_DATE);
            eventIds=data1.get(Constants.EVENT_ID);
            eventDates=data1.get(Constants.EVENT_DATE);
            eventDetails=data1.get(Constants.EVENT_DETAIL);

            HashMap<String,ArrayList<String>>data2=helper.getAllRow(Constants.TABLE_INFORMATION,Constants.INFORMATION_DATE);
            infoIds=data2.get(Constants.INFORMATION_ID);
            infoDates=data2.get(Constants.INFORMATION_DATE);
            infoDetails=data2.get(Constants.INFORMATION_DETAILS);
            infoTypes=data2.get(Constants.INFORMATION_TYPE);
            infoImgLinks=data2.get(Constants.INFORMATION_IMAGE_LINK);

            HashMap<String,ArrayList<String>>data3=helper.getAllRow(Constants.TABLE_ENTERTAINMENT,Constants.ENTERTAINMENT_ID);
            enterIds=data3.get(Constants.ENTERTAINMENT_ID);
            enterTitles=data3.get(Constants.ENTERTAINMENT_TITLE);
            enterDetails=data3.get(Constants.ENTERTAINMENT_DETAIL);
            enterTypes=data3.get(Constants.ENTERTAINMENT_TYPE);
            enterImgLinks=data3.get(Constants.ENTERTAINMENT_IMAGE_LINK);
            enterTimes=data3.get(Constants.ENTERTAINMENT_TIME);

            HashMap<String,ArrayList<String>>data4=helper.getAllRow(Constants.TABLE_INTERACT_COMMENT,Constants.INTERACT_COMMENT_DATE);
            interactIds=data4.get(Constants.INTERACT_COMMENT_ID);
            interactPostIds=data4.get(Constants.INTERACT_COMMENT_POST_ID);
            interactEmpIds=data4.get(Constants.INTERACT_COMMENT_EMPLOYEE_ID);
            interactPostTitles=data4.get(Constants.INTERACT_COMMENT_POST);
            interactCommentors=data4.get(Constants.INTERACT_COMMENT_COMMENTOR);
            interactDates=data4.get(Constants.INTERACT_COMMENT_DATE);
            interactDescriptions=data4.get(Constants.INTERACT_COMMENT_DESCRIPTION);

            addAllItems();
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),""+e,Toast.LENGTH_SHORT).show();
        }
    }





    private void addAllItems()
    {
        if(infoIds.size()==0&&eventIds.size()==0&&interactIds.size()==0&&enterIds.size()==0)
        {
            cardKinformation.setVisibility(View.GONE);
            cardKincoming.setVisibility(View.GONE);
            cardKinteract.setVisibility(View.GONE);
            cardKintertainment.setVisibility(View.GONE);
            updateNotifyMsg.setVisibility(View.VISIBLE);
           // Toast.makeText(getActivity(),"No update available",Toast.LENGTH_SHORT).show();
        }
        else
        {
            updateNotifyMsg.setVisibility(View.GONE);
            if(infoIds.size()>0) addInformationItems();
            else cardKinformation.setVisibility(View.GONE);
            if(eventIds.size()>0) addEventItems();
            else cardKincoming.setVisibility(View.GONE);
            if(interactIds.size()>0) addInteractItems();
            else cardKinteract.setVisibility(View.GONE);
            if(enterIds.size()>0) addEntertainmentItems();
            else cardKintertainment.setVisibility(View.GONE);
        }
    }



    private void addInformationItems()
    {
        linearKinformation.removeAllViewsInLayout();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View[] views=new View[infoDates.size()];
        TextView[] txtDates=new TextView[infoDates.size()];
        TextView[] txtDetails=new TextView[infoDates.size()];
        TextView[] txtTypes=new TextView[infoDates.size()];
        LinearLayout[] linearUpdates=new LinearLayout[infoDates.size()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<infoDates.size();i++)
        {
            views[i]=inflater.inflate(R.layout.row_update,null);
            txtDates[i]=(TextView)views[i].findViewById(R.id.txtDate);
            txtDetails[i]=(TextView)views[i].findViewById(R.id.txtEvent);
            txtTypes[i]=(TextView)views[i].findViewById(R.id.txtType);
            linearUpdates[i]=(LinearLayout)views[i].findViewById(R.id.linearUpdate);
            if(i==0)
            {
                ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
            }

            Date sDate = null;
            try {
                sDate = format.parse(infoDates.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal=Calendar.getInstance();
            cal.setTime(sDate);
            int yy = cal.get(Calendar.YEAR);
            int mm = cal.get(Calendar.MONTH);
            int dd = cal.get(Calendar.DAY_OF_MONTH);
            String sss = this.months[mm] + " " + dd + ", " + yy;

            txtDates[i].setText(sss);
            CharSequence ss=getTrimmedText(infoDetails.get(i));
            txtDetails[i].setText(ss);
            txtTypes[i].setText(infoTypes.get(i).toUpperCase());
            linearKinformation.addView(views[i]);
            linearUpdates[i].setOnClickListener(handleOnClick(linearUpdates[i], i,"information"));
        }
    }



    private void addEventItems()
    {
        linearKincoming.removeAllViewsInLayout();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View[] views=new View[eventDates.size()];
        TextView[] txtDates=new TextView[eventDates.size()];
        TextView[] txtDetails=new TextView[eventDates.size()];
        TextView[] txtTypes=new TextView[eventDates.size()];
        LinearLayout[] linearUpdates=new LinearLayout[eventDates.size()];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<eventDates.size();i++)
        {
            views[i]=inflater.inflate(R.layout.row_update,null);
            txtDates[i]=(TextView)views[i].findViewById(R.id.txtDate);
            txtDetails[i]=(TextView)views[i].findViewById(R.id.txtEvent);
            txtTypes[i]=(TextView)views[i].findViewById(R.id.txtType);
            linearUpdates[i]=(LinearLayout)views[i].findViewById(R.id.linearUpdate);
            if(i==0)
            {
                ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
            }

            Date sDate = null;
            try {
                sDate = format.parse(eventDates.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal=Calendar.getInstance();
            cal.setTime(sDate);
            int yy = cal.get(Calendar.YEAR);
            int mm = cal.get(Calendar.MONTH);
            int dd = cal.get(Calendar.DAY_OF_MONTH);
            String sss = this.months[mm] + " " + dd + ", " + yy;

            txtDates[i].setText(sss);
            CharSequence ss=getTrimmedText(eventDetails.get(i));
            txtDetails[i].setText(ss);
            txtTypes[i].setVisibility(View.GONE);
            // txtTypes[i].setText(infoTypes.get(i));
            linearKincoming.addView(views[i]);
            linearUpdates[i].setOnClickListener(handleOnClick(linearUpdates[i], i, "event"));
        }
    }


    private void addInteractItems()
    {
        linearKinteract.removeAllViewsInLayout();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View[] views=new View[interactIds.size()];
        TextView[] txtDates=new TextView[interactIds.size()];
        TextView[] txtDetails=new TextView[interactIds.size()];
        TextView[] txtTypes=new TextView[interactIds.size()];
        LinearLayout[] linearUpdates=new LinearLayout[interactIds.size()];
        for(int i=0;i<interactIds.size();i++)
        {
            views[i]=inflater.inflate(R.layout.row_update,null);
            txtDates[i]=(TextView)views[i].findViewById(R.id.txtDate);
            txtDetails[i]=(TextView)views[i].findViewById(R.id.txtEvent);
            txtTypes[i]=(TextView)views[i].findViewById(R.id.txtType);
            linearUpdates[i]=(LinearLayout)views[i].findViewById(R.id.linearUpdate);
            if(i==0)
            {
                ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
            }
            txtDates[i].setText(interactPostTitles.get(i));
            CharSequence ss=getTrimmedText(interactDescriptions.get(i));
            txtDetails[i].setText(ss);
            txtTypes[i].setVisibility(View.GONE);
            // txtTypes[i].setText(infoTypes.get(i));
            //txtDates[i].setTextColor(Color.parseColor("#282828"));
            linearKinteract.addView(views[i]);
            linearUpdates[i].setOnClickListener(handleOnClick(linearUpdates[i], i, "interact"));
        }
    }

    private void addEntertainmentItems()
    {
        linearKintertainment.removeAllViewsInLayout();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View[] views=new View[enterIds.size()];
        TextView[] txtDates=new TextView[enterIds.size()];
        TextView[] txtDetails=new TextView[enterIds.size()];
        TextView[] txtTypes=new TextView[enterIds.size()];
        LinearLayout[] linearUpdates=new LinearLayout[enterIds.size()];
        for(int i=0;i<enterIds.size();i++)
        {
            views[i]=inflater.inflate(R.layout.row_update,null);
            txtDates[i]=(TextView)views[i].findViewById(R.id.txtDate);
            txtDetails[i]=(TextView)views[i].findViewById(R.id.txtEvent);
            txtTypes[i]=(TextView)views[i].findViewById(R.id.txtType);
            linearUpdates[i]=(LinearLayout)views[i].findViewById(R.id.linearUpdate);
            if(i==0)
            {
                ((TextView)views[i].findViewById(R.id.txtSeparator)).setVisibility(View.INVISIBLE);
            }
            txtDates[i].setText(enterTitles.get(i));
            CharSequence ss=getTrimmedText(enterDetails.get(i));
            txtDetails[i].setText(ss);
            txtTypes[i].setText(enterTypes.get(i).toUpperCase());
            txtDates[i].setTextColor(Color.parseColor("#282828"));
            linearKintertainment.addView(views[i]);
            linearUpdates[i].setOnClickListener(handleOnClick(linearUpdates[i], i, "entertainment"));
        }
    }



    private CharSequence getTrimmedText(String originalText)
    {
        if(originalText.length()<=trimLength) return originalText;
        else return new SpannableStringBuilder(originalText, 0, trimLength + 1).append(".....");
    }





    View.OnClickListener handleOnClick(final LinearLayout linear, final int pos,final String tag) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(),"Clicked "+tag+": "+pos,Toast.LENGTH_SHORT).show();
                CheckConnectivity connectivity=new CheckConnectivity(getActivity());
                if(connectivity.isConnected())
                {
                    if(tag.equals("information"))
                    {
                        MyMenuClass.menuType=infoTypes.get(pos);
                        MainActivity home = (MainActivity) getActivity();
                        home.switchContainerFragment(new KinformationFragment());
                        home.setToolBarTitle("Kinformation");

                          /*      Intent in=new Intent(getActivity(),MainActivity.class);
                                in.putExtra("type","information");
                                in.putExtra("category",infoTypes.get(pos));
                                startActivity(in);
                                getActivity().finish();   */
                    }
                    else if(tag.equals("event"))
                    {
                        MyMenuClass.menuType=eventDates.get(pos);
                        MainActivity home = (MainActivity) getActivity();
                        home.switchContainerFragment(new KincomingFragment());
                        home.setToolBarTitle("Kincoming");

                           /*     Intent in=new Intent(getActivity(),MainActivity.class);
                                in.putExtra("type","event");
                                in.putExtra("category", eventDates.get(pos));
                                startActivity(in);
                                getActivity().finish();   */
                    }
                    else if(tag.equals("interact"))
                    {
                        MyMenuClass.menuType="";
                        MainActivity home = (MainActivity) getActivity();
                        home.switchContainerFragment(new ContainerFragment());
                        home.setToolBarTitle("Kinteract");

                             /*   Intent in=new Intent(getActivity(),MainActivity.class);
                                in.putExtra("type","interact");
                                in.putExtra("category", "");
                                startActivity(in);
                                getActivity().finish();  */
                    }
                    else if(tag.equals("entertainment"))
                    {
                        MyMenuClass.menuType=enterTypes.get(pos);
                        MainActivity home = (MainActivity) getActivity();
                        home.switchContainerFragment(new KintertainmentFragment());
                        home.setToolBarTitle("Kintertainment");

                          /*      Intent in=new Intent(getActivity(),MainActivity.class);
                                in.putExtra("type","entertainment");
                                in.putExtra("category", enterTypes.get(pos));
                                startActivity(in);
                                getActivity().finish();  */
                    }
                }
                else Toast.makeText(getActivity(),"Please turn on internet connection of your device",Toast.LENGTH_SHORT).show();
            }
        };
    }





}
