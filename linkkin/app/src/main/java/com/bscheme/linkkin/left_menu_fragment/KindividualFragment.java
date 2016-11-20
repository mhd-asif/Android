package com.bscheme.linkkin.left_menu_fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.KindividualFullProfileActivity;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.utils.DividerItemDecoration;
import com.bscheme.linkkin.utils.Font;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;
import com.bscheme.linkkin.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class KindividualFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters



    TextView fullName, designation,employee_id, phone,present_address,company,more;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    SweetAlertDialog mDialog;
   // KindividualInfo info = null;


    String id,name,firstName,lastName,email,designationName,mobile="",present,permanent,father,mother,marital,religion,gender,birthday,blood,passport,bank,nationalId,companyName="SQ Group";
    ArrayList<String>titles=new ArrayList<>();
    ArrayList<String>values=new ArrayList<>();


    public KindividualFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kindividual, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullName = (TextView) view.findViewById(R.id.tv_user_full_name);
        designation = (TextView) view.findViewById(R.id.tv_designation);
        employee_id = (TextView) view.findViewById(R.id.tv_employee_id);
        phone = (TextView) view.findViewById(R.id.tv_phone);
        present_address = (TextView) view.findViewById(R.id.tv_address);
        company = (TextView) view.findViewById(R.id.tv_company_name);
        more = (TextView) view.findViewById(R.id.tv_more_info);


        Font.LATO_Bold.apply(getActivity(),fullName);
        Font.LATO_Bold.apply(getActivity(),designation);
        Font.LATO_Regular.apply(getActivity(),employee_id);
        Font.LATO_Regular.apply(getActivity(), phone);
        Font.LATO_Regular.apply(getActivity(),present_address);
        Font.LATO_Bold.apply(getActivity(),company);
        Font.LATO_Bold.apply(getActivity(),more);

        Font.LATO_Bold.apply(getActivity(),(TextView)view.findViewById(R.id.txtEmp1));
        Font.LATO_Bold.apply(getActivity(),(TextView)view.findViewById(R.id.txtPhone1));
        Font.LATO_Bold.apply(getActivity(),(TextView)view.findViewById(R.id.txtAddr1));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_kindividual);

        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        /// dividers between each item within the list
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);

        CheckConnectivity connectivity=new CheckConnectivity(getActivity());
        if(connectivity.isConnected()) new getKindividualInfos().execute();
        else
        {
            try {
                getOfflineRes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name != null) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        /*    Intent full = new Intent(getActivity(), KindividualFullProfileActivity.class);
                            full.putExtra("kindividual", info);
                            startActivity(full);   */

                            Intent full = new Intent(getActivity(), KindividualFullProfileActivity.class);
                            full.putExtra("name", name);
                            full.putExtra("email", email);
                            full.putExtra("designation", designationName);
                            full.putExtra("mobile", mobile);
                            full.putExtra("present", present);
                            full.putExtra("permanent", permanent);
                            full.putExtra("father", father);
                            full.putExtra("mother", mother);
                            full.putExtra("marital", marital);
                            full.putExtra("religion", religion);
                            full.putExtra("gender", gender);
                            full.putExtra("birthday", birthday);
                            full.putExtra("blood", blood);
                            full.putExtra("passport", passport);
                            full.putExtra("bank", bank);
                            full.putExtra("national_id", nationalId);
                            full.putExtra("company", companyName);
                            full.putExtra("titles",titles);
                            full.putExtra("values",values);
                            startActivity(full);
                        }
                    }, 100);
                }
               // else Toast.makeText(getActivity(),"Info not found",Toast.LENGTH_SHORT).show();
            }

        });


    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }



    private void getOfflineRes() throws IOException {
        String path_kindividual=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kindividual);
        // Log.e("catch path: ",path_kindom);
        File file = new File(path_kindividual);
        if(file.exists())
        {
            int length = (int) file.length();
            byte[] bytes = new byte[length];
            FileInputStream in = new FileInputStream(file);
            try {
                in.read(bytes);
                String contents = new String(bytes);
                showAllData(contents);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                in.close();
            }
        }
    }



    private void showAllData(String s)
    {
        try {
            JSONObject jObj1 = new JSONObject(s);
            JSONObject jObj = jObj1.getJSONObject("common_info");

            id = jObj.getString("ID");
            firstName = jObj.getString("first_name");
            lastName = jObj.getString("last_name");
            email = jObj.getString("email");
            present = jObj.getString("present_address");
            permanent = jObj.getString("permanent_address");
            father = jObj.getString("father_name");
            mother = jObj.getString("mother_name");
            marital = jObj.getString("marital_status");
            religion = jObj.getString("religion");
            gender = jObj.getString("gender");
            birthday = jObj.getString("date_of_birth");
            blood = jObj.getString("blood_group");
            passport = jObj.getString("passport_no");
            bank = jObj.getString("bank_account");
            nationalId = jObj.getString("national_id");

            JSONObject jCompany=jObj1.getJSONObject("conpany_info");
            designationName = jCompany.getString("designation");
            companyName = jCompany.getString("company_name");

            JSONArray jArray=jObj1.getJSONArray("induatrial");
            for(int i=0;i<jArray.length();i++)
            {
                JSONObject jInfo=jArray.getJSONObject(i);
                titles.add(jInfo.getString("title"));
                values.add(jInfo.getString("content"));
            }


            name=firstName+" "+lastName;
            fullName.setText(name);
            designation.setText(designationName);
            employee_id.setText(SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id)));
            phone.setText(mobile);
            present_address.setText(present);
            company.setText(companyName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private class getKindividualInfos extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
            mDialog.getProgressHelper().setBarColor(getActivity().getResources().getColor(R.color.primary_color_dark));
            mDialog.setTitleText("");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(Urls.URL_COMMON, "POST");
            helper.addData(new Uri.Builder().appendQueryParameter("action", "get_user_info")
                    .appendQueryParameter("user_id", SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id)))
                    .appendQueryParameter("blog_id", SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_web_id))));
            return helper.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mDialog.dismiss();
            if (!TextUtils.isEmpty(s)) // check response is null or not
            try {
                JSONObject jObj1 = new JSONObject(s);
                JSONObject jObj = jObj1.getJSONObject("common_info");

                id = jObj.getString("ID");
                firstName = jObj.getString("first_name");
                lastName = jObj.getString("last_name");
                email = jObj.getString("email");
                present = jObj.getString("present_address");
                permanent = jObj.getString("permanent_address");
                father = jObj.getString("father_name");
                mother = jObj.getString("mother_name");
                marital = jObj.getString("marital_status");
                religion = jObj.getString("religion");
                gender = jObj.getString("gender");
                birthday = jObj.getString("date_of_birth");
                blood = jObj.getString("blood_group");
                passport = jObj.getString("passport_no");
                bank = jObj.getString("bank_account");
                nationalId = jObj.getString("national_id");

                JSONObject jCompany=jObj1.getJSONObject("conpany_info");
                designationName = jCompany.getString("designation");
                companyName = jCompany.getString("company_name");

                JSONArray jArray=jObj1.getJSONArray("induatrial");
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject jInfo=jArray.getJSONObject(i);
                    titles.add(jInfo.getString("title"));
                    values.add(jInfo.getString("content"));
                }

                name=firstName+" "+lastName;
                fullName.setText(name);
                designation.setText(designationName);
                employee_id.setText(SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id)));
                phone.setText(mobile);
                present_address.setText(present);
                company.setText(companyName);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
