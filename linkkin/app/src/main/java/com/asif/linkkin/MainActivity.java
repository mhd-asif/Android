package com.asif.linkkin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.asif.linkkin.fragment.LeftSideFragment;
import com.asif.linkkin.fragment.RightSideFragment;
import com.asif.linkkin.left_menu_fragment.KindividualFragment;
import com.asif.linkkin.utils.CheckConnectivity;
import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.utils.Font;
import com.asif.linkkin.helper.ManageNotification;
import com.asif.linkkin.helper.MyMenuClass;
import com.asif.linkkin.helper.NotificationCounter;
import com.asif.linkkin.utils.SharedDataSaveLoad;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    TextView toolBarTitle,txtCountNoti;
    Fragment mContent;
    String titleText = null ;
    String type,category;
    int count=0;
    NotificationCounter counter;

    SlidingMenu sm;


    public MainActivity(){
//        super(R.string.app_name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);


        setContentView(R.layout.content_frame);

       // type=getIntent().getExtras().getString("type");
       // category=getIntent().getExtras().getString("category");

        DisconnectClass disconnectClass=new DisconnectClass(this);


        final Toolbar customToolbar = (Toolbar) this.findViewById(R.id.custom_toolbar);
        customToolbar.setTitle("");
        MainActivity.this.setSupportActionBar(customToolbar);
        ImageView navHome = (ImageView) customToolbar.findViewById(R.id.iv_nav_home);
        ImageView imgMenuRight = (ImageView) customToolbar.findViewById(R.id.imgRightMenu);
        toolBarTitle =(TextView) customToolbar.findViewById(R.id.tool_bar_title);
        txtCountNoti=(TextView)customToolbar.findViewById(R.id.txtCountNoti);

//        customToolbar.setLogo(R.drawable.close);
        customToolbar.setNavigationIcon(R.drawable.ic_action_manu);

        Font.LATO_Bold.apply(MainActivity.this, toolBarTitle);
        Font.LATO_Regular.apply(MainActivity.this, txtCountNoti);

        // set the Behind View
        setBehindContentView(R.layout.left_menu_frame);

        getSlidingMenu().setSecondaryMenu(R.layout.right_menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            t.replace(R.id.menu_frame_left, new LeftSideFragment());
            t.commit();
        }
        else {
            getSupportFragmentManager().findFragmentById(R.id.menu_frame_left);
        }


        // customize the SlidingMenu
        sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setFadeEnabled(true);
        //sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        setSlidingActionBarEnabled(true);



        counter=new NotificationCounter(this);
        counter.setTextView(txtCountNoti);

        // set the Above View Fragment for home content
        if (savedInstanceState != null)
        {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            MyMenuClass.menuType="";
        }
        if (mContent == null)
        {
            titleText = "Kindividual";
            MyMenuClass.menuType="";
            mContent = new KindividualFragment();
            setHomeView();
            setNotificationText();
            CheckConnectivity connectivity=new CheckConnectivity(MainActivity.this);
            if(connectivity.isConnected())
            {
                Handler hh=new Handler();
                hh.post(new Runnable() {
                    @Override
                    public void run() {
                        new NotifyAsyncTask().execute();
                    }
                });
            }
        }



        sm.setSecondaryOnOpenListner(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                FragmentTransaction tr = MainActivity.this.getSupportFragmentManager().beginTransaction();
                tr.replace(R.id.menu_frame_right, new RightSideFragment());
                tr.commit();
            }
        });


        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler hh=new Handler();
                hh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggle();
                    }
                },100);
            }
        });


        imgMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSlidingMenu().showSecondaryMenu();
            }
        });


    }


/***************************************************************************************************************/
    private void handleRestTasks()
    {
        count=counter.getNotificationCount();
        if(count>=1)
        {
            final Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setNotificationText();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sm.showSecondaryMenu();
                        }
                    },50);
                }
            },1150);
         //   setNotificationText();
         //   sm.showSecondaryMenu();
        }
    }



    private void setHomeView()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mContent).commit();
        toolBarTitle.setText(titleText);
    }


    private void setNotificationText()
    {
        if (count < 1) counter.setNotificationText("");
        else counter.setNotificationText("" + count);
    }


    public void setToolBarTitle(String title) {
        this.titleText = title;
        if (toolBarTitle != null)    toolBarTitle.setText(titleText);
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }


    @Override
    public void onBackPressed() {
        System.exit(1);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        DisconnectClass.resetDisconnectTimer();
        pinActivation();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        DisconnectClass.resetDisconnectTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      //  DisconnectClass.stopDisconnectTimer();
        DisconnectClass.removeActivity(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*check pin added or not */
        /*if(!TextUtils.isEmpty(SharedDataSaveLoad.load(MainActivity.this, getResources().getString(R.string.shared_pref_lock_pin)))){
            Intent changePassword = new Intent(MainActivity.this, LockActivity.class);
            startActivity(changePassword);
        }else {
//            Intent changePassword = new Intent(MainActivity.this, LockActivity.class);
//            startActivity(changePassword);
        }*/
    }

    public void switchContainerFragment(Fragment fragment){
        mContent = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 100);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Handler hh=new Handler();
                hh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggle();
                    }
                },100);
                break;
        }
        return true;
    }

    private class NotifyAsyncTask extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            ManageNotification manage=new ManageNotification(MainActivity.this);
            manage.manageAllNotifications();
            return null;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String s) {
            handleRestTasks();
        }
    }

    //////////

    private void pinActivation(){
        /*check pin added or not */
        if(TextUtils.isEmpty(SharedDataSaveLoad.load(MainActivity.this, getResources().getString(R.string.shared_pref_lock_pin)))){
            Intent changePassword = new Intent(MainActivity.this, LockConfirmActivity.class);
            startActivity(changePassword);
        }else {
//            Intent changePassword = new Intent(MainActivity.this, LockActivity.class);
//            startActivity(changePassword);
        }
    }
    ////////////
}
