package com.bscheme.linkkin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.MainActivity;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.RegActivity;
import com.bscheme.linkkin.adapter.LeftMenuAdapter;
import com.bscheme.linkkin.left_menu_fragment.KindividualFragment;
import com.bscheme.linkkin.models.KindividualInfo;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.utils.Font;
import com.bscheme.linkkin.helper.MyMenuClass;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;
import com.bscheme.linkkin.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LeftSideFragment extends Fragment {

  //  ListView lvLeftMenu;
    RecyclerView recyclerLeft;

    TextView tvFullName,logout;
    ImageView ivProfileImg,imgArrow;

    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;


    public LeftSideFragment newInstance(KindividualInfo info) {
        LeftSideFragment f = new LeftSideFragment();
        Bundle args = new Bundle();
        args.putParcelable("info",info);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
       // new getKindividualInfos().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_left_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFullName = (TextView) view.findViewById(R.id.tv_user_full_name);
        logout = (TextView) view.findViewById(R.id.tv_logout);
        //lvLeftMenu = (ListView) view.findViewById(R.id.lv_left_menu);
        ivProfileImg = (ImageView) view.findViewById(R.id.iv_profile_img);
        recyclerLeft=(RecyclerView)view.findViewById(R.id.recyclerLeft);
        imgArrow=(ImageView)view.findViewById(R.id.imgArrow);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerLeft.setLayoutManager(layoutManager);
      //  DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST);
      //  recyclerLeft.addItemDecoration(divider);

        ArrayList<String>menuTitles=new ArrayList<>();
        ArrayList<Integer>menuIcons=new ArrayList<>();
        menuTitles.add("Kindom");
        menuTitles.add("Kindividual");
        menuTitles.add("Kincoming");
        menuTitles.add("Kinteract");
        menuTitles.add("Kinformation");
        menuTitles.add("Kintertainment");
        menuTitles.add("Kinvironment");
        menuTitles.add("Setting");
        menuIcons.add(R.drawable.ic_kindom);
        menuIcons.add(R.drawable.ic_kindividual);
        menuIcons.add(R.drawable.ic_kincoming);
        menuIcons.add(R.drawable.ic_kinteract);
        menuIcons.add(R.drawable.ic_kinformation);
        menuIcons.add(R.drawable.ic_kintertainment);
        menuIcons.add(R.drawable.ic_kinvironment);
        menuIcons.add(R.drawable.ic_setting);

        rotateImageview(0, 90);

        LeftMenuAdapter.selectedPos=1;
        final LeftMenuAdapter leftAdapter=new LeftMenuAdapter(getActivity(),menuTitles,menuIcons);
        recyclerLeft.setAdapter(leftAdapter);

        Font.LATO_Bold.apply(getActivity(), logout);
        Font.LATO_Bold.apply(getActivity(), tvFullName);

        MyMenuClass tempClass=new MyMenuClass(getActivity());
        tempClass.setEmployee();
        tvFullName.setText(MyMenuClass.employeeName);

        SampleAdapter adapter = new SampleAdapter(getActivity());
        adapter.add(new SampleItem("Kindom", R.drawable.ic_kindom));
        adapter.add(new SampleItem("Kindividual", R.drawable.ic_kindividual));
        adapter.add(new SampleItem("Kincoming", R.drawable.ic_kincoming));
        adapter.add(new SampleItem("Kinteract", R.drawable.ic_kinteract));
        adapter.add(new SampleItem("Kinformation", R.drawable.ic_kinformation));
        adapter.add(new SampleItem("Kintertainment", R.drawable.ic_kintertainment));
        adapter.add(new SampleItem("Kinvironment", R.drawable.ic_kinvironment));
        adapter.add(new SampleItem("Setting", R.drawable.ic_setting));

        int firstVisibleItem = ((LinearLayoutManager)recyclerLeft.getLayoutManager()).findFirstVisibleItemPosition();
        int lastVisibleItem = ((LinearLayoutManager)recyclerLeft.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//        Log.e("visible items",""+firstVisibleItem+"/"+lastVisibleItem);

        /*if (){
            imgArrow.setVisibility(View.GONE);
        }else {
            imgArrow.setVisibility(View.VISIBLE);
        }*/

        // lvLeftMenu.setAdapter(adapter);
        ivProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMenuClass.menuType = "";
                LeftMenuAdapter.selectedPos=1;
                leftAdapter.notifyDataSetChanged();
                switchFragment(new KindividualFragment(), "Kindividual");
            }
        });
        tvFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMenuClass.menuType="";
                LeftMenuAdapter.selectedPos=1;
                leftAdapter.notifyDataSetChanged();
                switchFragment(new KindividualFragment(), "Kindividual");
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_MODIFIED_TYPE);
                dialog.setTitleText("Are you sure?");
                dialog.setConfirmText("Sign out");
                dialog.setCancelText("Cancel");
                dialog.setCancelable(false);
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        SharedDataSaveLoad.remove(getActivity(), getResources().getString(R.string.shared_pref_user_id));
                        startActivity(new Intent(getActivity(), RegActivity.class));
                        getActivity().finish();
                        sweetAlertDialog.dismiss();
                    }
                });
                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });

                dialog.show();
            }
        });



        recyclerLeft.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
//                    onHide();
                    controlsVisible = false;
                    scrolledDistance = 0;
                    rotateImageview(-90,90);
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
//                    onShow();
                    controlsVisible = true;
                    scrolledDistance = 0;
                    rotateImageview(90,-90);
                }

                if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                    scrolledDistance += dy;
                }
            }
        });



    }

    private void rotateImageview(float toDegree,float fromDegree) {
        final RotateAnimation rotateAnim = new RotateAnimation(toDegree, fromDegree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(500);
        rotateAnim.setFillAfter(true);
        imgArrow.startAnimation(rotateAnim);
    }

    private void onHide()
    {
        imgArrow.setVisibility(View.INVISIBLE);
    }


    private void onShow()
    {
        imgArrow.setVisibility(View.VISIBLE);
    }


    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

     /*   lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final SampleItem item = (SampleItem) parent.getAdapter().getItem(position);
                switch (position) {
                    case 0:
                        switchFragment(new KindomFragment(), item.tag);
                        break;
                    case 1:
                        switchFragment(new KindividualFragment(),item.tag);
                        break;
                    case 2:
                        switchFragment(new KincomingFragment(),item.tag);
                        break;
                    case 3:
                        switchFragment(new ContainerFragment(),item.tag);
                        break;
                    case 4:
                        switchFragment(new KinformationFragment(),item.tag);
                        break;
                    case 5:
                        switchFragment(new KintertainmentFragment(),item.tag);
                        break;
                    case 6:
                        switchFragment(new KinvironmentFragment(),item.tag);
                        break;
                    case 7:
                        switchFragment(new SettingFragment(),item.tag);
                        break;

                    default:
                        switchFragment(new ContainerFragment(),item.tag);
                }

            }
        });   */
    }

    //switch fragment on left menu list clicked
    public void switchFragment(Fragment fragment,String title) {
        if (getActivity() == null) {
            return;
        }
        if (getActivity() instanceof MainActivity) {
            MyMenuClass.menuType="";
            CheckConnectivity connectivity=new CheckConnectivity(getActivity());
            if(connectivity.isConnected())
            {
                MainActivity home = (MainActivity) getActivity();
                home.switchContainerFragment(fragment);
                home.setToolBarTitle(title);
            }
            else
            {
                if(title.equals("Setting"))
                {
                    MainActivity home = (MainActivity) getActivity();
                    home.switchContainerFragment(fragment);
                    home.setToolBarTitle(title);
                }
                else if(title.equals("Kindom"))
                {
                    SharedPreferences pref=getActivity().getSharedPreferences("database_linkkin", 0);
                    String kindom=pref.getString("kindom","no");
                    if(kindom.equals("no")) Toast.makeText(getActivity(),"Please turn your internet connection ON",Toast.LENGTH_SHORT).show();
                    else
                    {
                        MainActivity home = (MainActivity) getActivity();
                        home.switchContainerFragment(fragment);
                        home.setToolBarTitle(title);
                    }
                }
                else if(title.equals("Kindividual"))
                {
                    // SharedPreferences pref=mContext.getSharedPreferences("database_linkkin", 0);
                    //  String kindom=pref.getString("kindom","no");
                    String path_kindividual=getActivity().getCacheDir().getAbsolutePath()+"/"+getActivity().getString(R.string.file_kindividual);
                    // Log.e("catc path: ",path_kindom);
                    File file = new File(path_kindividual);
                    if(!file.exists())
                    {
                        Toast.makeText(getActivity(), "Please turn your internet connection ON", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        MainActivity home = (MainActivity) getActivity();
                        home.switchContainerFragment(fragment);
                        home.setToolBarTitle(title);
                    }
                }
                else Toast.makeText(getActivity(),"Please turn your internet connection ON",Toast.LENGTH_SHORT).show();
            }
        }
    }




    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }




    private class SampleItem {
        public String tag;
        public int iconRes;

        public SampleItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }

    public class SampleAdapter extends ArrayAdapter<SampleItem> {

        public SampleAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_menu_list, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
            icon.setImageResource(getItem(position).iconRes);
            TextView title = (TextView) convertView.findViewById(R.id.row_title);

            Font.LATO_Bold.apply(getActivity(),title);

            title.setText(getItem(position).tag);

            return convertView;
        }

    }
    private class getKindividualInfos extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            ConnectionHelper ch = new ConnectionHelper();
       /*     return ch.getDataFromUrlByGET(Urls.KINDIVIDUAL_INFO + "/" +
                    SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id)));   */
            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(Urls.URL_COMMON, "POST");
            helper.addData(new Uri.Builder().appendQueryParameter("action", "get_user_info")
                    .appendQueryParameter("user_id", SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id))));
            return helper.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

         //   Log.i("response kindividual", s);
            if (!TextUtils.isEmpty(s)) // check response is null or not

                try {
                JSONObject jObj = new JSONObject(s);

                KindividualInfo info = new KindividualInfo();

                info.id = jObj.getString("ID");
                //  info.name = jObj.getString("employee_name");
                info.first_name = jObj.getString("first_name");
                info.last_name = jObj.getString("last_name");
                info.email = jObj.getString("email");
                info.unit = jObj.getString("unit");
                info.designation = jObj.getString("designation");
                // info.mobile = jObj.getString("mobile_number");
                info.present_address = jObj.getString("present_address");
                info.permanet_address = jObj.getString("permanent_address");
                info.father_name = jObj.getString("father_name");
                info.mother_name = jObj.getString("mother_name");
                info.marital_status = jObj.getString("marital_status");
                info.religion = jObj.getString("religion");
                info.gender = jObj.getString("gender");
                info.department = jObj.getString("department");
                //  info.section = jObj.getString("section");
                //  info.shift = jObj.getString("shift");
                info.joining = jObj.getString("date_of_joining");
                info.birthday = jObj.getString("date_of_birth");
                //  info.company_name = "SQ Group";
                info.blood_group = jObj.getString("blood_group");
                info.passport_no = jObj.getString("passport_no");
                info.bank_account = jObj.getString("bank_account");
                info.national_id = jObj.getString("national_id");

                if (info != null){
                    tvFullName.setText(info.first_name+" "+info.last_name);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
