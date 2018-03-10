package com.asif.linkkin;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.asif.linkkin.adapter.ProfileAdapter;
import com.asif.linkkin.helper.DisconnectClass;
import com.asif.linkkin.utils.Font;

import java.util.ArrayList;

public class KindividualFullProfileActivity extends AppCompatActivity {

    TextView fullName, designation;
    ImageView profileImg,iv_back;

    PagerSlidingTabStrip tabs;
    ViewPager pager;

    private LinearLayout mTabsLinearLayout;


    //KindividualInfo profileInfo;

    String name,email,designationName,mobile="",present,permanent,father,mother,marital,religion,gender,birthday,blood,passport,bank,nationalId,companyName="SQ Group";
    ArrayList<String> titles=new ArrayList<>();
    ArrayList<String>values=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kindividual_full_profile);
        DisconnectClass disconnectClass=new DisconnectClass(this);

        fullName = (TextView) findViewById(R.id.tv_user_full_name);
        designation = (TextView) findViewById(R.id.tv_designation);
        profileImg = (ImageView) findViewById(R.id.iv_profile_img);
        iv_back = (ImageView) findViewById(R.id.iv_back);

        tabs=(PagerSlidingTabStrip)findViewById(R.id.tabs);
        pager=(ViewPager)findViewById(R.id.pager);
        tabs.setTypeface(Font.LATO_Bold.setFont(this), 0);



        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler hh=new Handler();
                hh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },100);
            }
        });


        Font.LATO_Bold.apply(KindividualFullProfileActivity.this,fullName);
        Font.LATO_Regular.apply(KindividualFullProfileActivity.this, designation);

       // profileInfo = getIntent().getParcelableExtra("kindividual");

        /*
        fullName.setText(profileInfo.first_name+" "+profileInfo.last_name);
        designation.setText(profileInfo.designation);   */

        titles=(ArrayList<String>)getIntent().getSerializableExtra("titles");
        values=(ArrayList<String>)getIntent().getSerializableExtra("values");
        name=getIntent().getExtras().getString("name");
        email=getIntent().getExtras().getString("email");
        designationName=getIntent().getExtras().getString("designation");
        mobile=getIntent().getExtras().getString("mobile");
        present=getIntent().getExtras().getString("present");
        permanent=getIntent().getExtras().getString("permanent");
        father=getIntent().getExtras().getString("father");
        mother=getIntent().getExtras().getString("mother");
        marital=getIntent().getExtras().getString("marital");
        religion=getIntent().getExtras().getString("religion");
        gender=getIntent().getExtras().getString("gender");
        birthday=getIntent().getExtras().getString("birthday");
        blood=getIntent().getExtras().getString("blood");
        passport=getIntent().getExtras().getString("passport");
        bank=getIntent().getExtras().getString("bank");
        nationalId=getIntent().getExtras().getString("national_id");
        companyName=getIntent().getExtras().getString("company");

        fullName.setText(name);
        designation.setText(designationName);

     //   ViewGroup tab = (ViewGroup) findViewById(R.id.tab);
     //   tab.addView(LayoutInflater.from(this).inflate(R.layout.demo_always_in_center, tab, false));

     //   ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
      //  SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

     //   FragmentPagerItems pages = new FragmentPagerItems(this);

       // Bundle args = new Bundle();
       // args.putParcelable("kindividual_info",profileInfo);

        Bundle full=new Bundle();
        full.putString("email", email);
        full.putString("mobile", mobile);
        full.putString("present", present);
        full.putString("permanent", permanent);
        full.putString("father", father);
        full.putString("mother", mother);
        full.putString("marital", marital);
        full.putString("religion", religion);
        full.putString("gender", gender);
        full.putString("birthday", birthday);
        full.putString("blood", blood);
        full.putString("passport", passport);
        full.putString("bank", bank);
        full.putString("national_id", nationalId);
        full.putString("company", companyName);

        Bundle office=new Bundle();
        office.putSerializable("titles",titles);
        office.putSerializable("values",values);

        ProfileAdapter adapter=new ProfileAdapter(getSupportFragmentManager(),full,office);
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        setUpTabStrip(0);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setUpTabStrip(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

     /*   pages.add(FragmentPagerItem.of("Personal", PersonalInformationFragment.class, full));
        pages.add(FragmentPagerItem.of("Work", OfficialInformationFragment.class));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages,false);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);  */

    }


    @Override
    protected void onResume() {
        super.onResume();
        DisconnectClass.resetDisconnectTimer();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        DisconnectClass.resetDisconnectTimer();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // DisconnectClass.stopDisconnectTimer();
        DisconnectClass.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void setUpTabStrip(int paramInt)
    {
        this.mTabsLinearLayout = ((LinearLayout)this.tabs.getChildAt(0));
        for(int i=0;i<mTabsLinearLayout.getChildCount();i++)
        {
            TextView localTextView = (TextView)this.mTabsLinearLayout.getChildAt(i);
            if (i == paramInt) {
                localTextView.setTextColor(Color.parseColor("#FFFFFF"));
            }
            else
            {
                localTextView.setTextColor(Color.parseColor("#007F80"));
            }
        }
    }


}
