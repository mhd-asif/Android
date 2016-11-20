package com.bscheme.linkkin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bscheme.linkkin.R;
import com.bscheme.linkkin.utils.Font;


/**
 * create an instance of this fragment.
 */
public class PersonalInformationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
   // private KindividualInfo info;

    TextView phone,father_name,mother_name,maritual_status,gender,present_address,
            permanent_address,religion, email, dob, blood_group, passport, bank_account, national_id;

    String name,emailValue,designationName,mobile="",present,permanent,father,mother,marital,religionValue,genderValue,birthday,blood,passportNo,bank,nationalId,companyName="SQ Group";


    // TODO: Rename and change types and number of parameters
    public static PersonalInformationFragment newInstance(Bundle bundle) {
        PersonalInformationFragment fragment = new PersonalInformationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public PersonalInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // info = getArguments().getParcelable("kindividual_info");
           Bundle bundle=getArguments();
            name=bundle.getString("name");
            emailValue=bundle.getString("email");
            designationName=bundle.getString("designation");
            mobile=bundle.getString("mobile");
            present=bundle.getString("present");
            permanent=bundle.getString("permanent");
            father=bundle.getString("father");
            mother=bundle.getString("mother");
            marital=bundle.getString("marital");
            religionValue=bundle.getString("religion");
            genderValue=bundle.getString("gender");
            birthday=bundle.getString("birthday");
            blood=bundle.getString("blood");
            passportNo=bundle.getString("passport");
            bank=bundle.getString("bank");
            nationalId=bundle.getString("national_id");
            companyName=bundle.getString("company");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_information, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phone = (TextView) view.findViewById(R.id.tv_phone);
        father_name = (TextView) view.findViewById(R.id.tv_father_name);
        mother_name = (TextView) view.findViewById(R.id.tv_mother_name);
        maritual_status = (TextView) view.findViewById(R.id.tv_marital_status);
        gender = (TextView) view.findViewById(R.id.tv_gender);
        present_address = (TextView) view.findViewById(R.id.tv_address);
        permanent_address = (TextView) view.findViewById(R.id.tv_permanent_address);
        religion = (TextView) view.findViewById(R.id.tv_religion);
        email = (TextView) view.findViewById(R.id.tv_gender);
        dob = (TextView) view.findViewById(R.id.tv_gender);
        blood_group = (TextView) view.findViewById(R.id.tv_gender);
        passport = (TextView) view.findViewById(R.id.tv_gender);
        bank_account = (TextView) view.findViewById(R.id.tv_gender);
        national_id = (TextView) view.findViewById(R.id.tv_gender);

        Font.LATO_Regular.apply(getActivity(), phone);
        Font.LATO_Regular.apply(getActivity(), father_name);
        Font.LATO_Regular.apply(getActivity(), mother_name);
        Font.LATO_Regular.apply(getActivity(), maritual_status);
        Font.LATO_Regular.apply(getActivity(), gender);
        Font.LATO_Regular.apply(getActivity(), present_address);
        Font.LATO_Regular.apply(getActivity(), permanent_address);
        Font.LATO_Regular.apply(getActivity(), religion);
        Font.LATO_Regular.apply(getActivity(), email);
        Font.LATO_Regular.apply(getActivity(), dob);
        Font.LATO_Regular.apply(getActivity(), blood_group);
        Font.LATO_Regular.apply(getActivity(), passport);
        Font.LATO_Regular.apply(getActivity(), bank_account);
        Font.LATO_Regular.apply(getActivity(), national_id);

        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtPhone1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtEmail1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtAddr1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtFather1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtMother1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtPermanent1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtMarital1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtGender1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtReligion1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtBirth1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtBlood1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtPassport1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtBank1));
        Font.LATO_Regular.apply(getActivity(), (TextView)view.findViewById(R.id.txtNational1));

        phone.setText(mobile);
        father_name.setText(father);
        mother_name.setText(mother);
        maritual_status.setText(marital);
        gender.setText(genderValue);
        present_address.setText(present);
        permanent_address.setText(permanent);
        religion.setText(religionValue);
        email.setText(emailValue);
        dob.setText(birthday);
        blood_group.setText(blood);
        passport.setText(passportNo);
        bank_account.setText(bank);
        national_id.setText(nationalId);

      //  new getKindividualInfos().execute();
    }


    /*
    private class getKindividualInfos extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            ConnectionHelper ch = new ConnectionHelper();
           // return ch.getDataFromUrlByGET(Urls.KINDIVIDUAL_INFO + "/" +SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id)));
            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(Urls.URL_COMMON, "POST");
            helper.addData(new Uri.Builder().appendQueryParameter("action", "get_user_info")
                    .appendQueryParameter("user_id", SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id))));
            return helper.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("response kindividual", s);
            try {
                JSONObject jObj = new JSONObject(s).getJSONObject("common_info");

                info = new KindividualInfo();

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

                phone.setText(info.mobile);
                father_name.setText(info.father_name);
                mother_name.setText(info.mother_name);
                maritual_status.setText(info.marital_status);
                gender.setText(info.gender);
                present_address.setText(info.present_address);
                permanent_address.setText(info.permanet_address);
                religion.setText(info.religion);
                email.setText(info.email);
                dob.setText(info.birthday);
                blood_group.setText(info.blood_group);
                passport.setText(info.passport_no);
                bank_account.setText(info.bank_account);
                national_id.setText(info.national_id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }   */
}
