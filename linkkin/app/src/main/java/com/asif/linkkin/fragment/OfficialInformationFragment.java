package com.asif.linkkin.fragment;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asif.linkkin.helper.ConnectionHelper;
import com.asif.linkkin.R;
import com.asif.linkkin.adapter.OfficialInfoAdapter;
import com.asif.linkkin.utils.SharedDataSaveLoad;
import com.asif.linkkin.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OfficialInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfficialInformationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   // TextView company,id,department,unit,joining;
    RecyclerView recycler;
    ArrayList<String> titles=new ArrayList<>();
    ArrayList<String>values=new ArrayList<>();

   // private KindividualInfo info;

    // TODO: Rename and change types and number of parameters
    public static OfficialInformationFragment newInstance(Bundle bundle) {
        OfficialInformationFragment fragment = new OfficialInformationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public OfficialInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          //  info = getArguments().getParcelable("kindividual_info");
            Bundle bundle=getArguments();
            titles=(ArrayList<String>)bundle.getSerializable("titles");
            values=(ArrayList<String>)bundle.getSerializable("values");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_official_information, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler=(RecyclerView)view.findViewById(R.id.recycler);
    /*    company = (TextView) view.findViewById(R.id.tv_company_name);
        id = (TextView) view.findViewById(R.id.tv_employee_id);
        department = (TextView) view.findViewById(R.id.tv_department);
        unit= (TextView) view.findViewById(R.id.tv_unit);
       // line = (TextView) view.findViewById(R.id.tv_shift);
        joining = (TextView) view.findViewById(R.id.tv_joining);

        Font.ProximaNova_Regular.apply(getActivity(), company);
        Font.ProximaNova_Regular.apply(getActivity(), id);
        Font.ProximaNova_Regular.apply(getActivity(), department);
        Font.ProximaNova_Regular.apply(getActivity(), unit);
       // Font.ProximaNova_Regular.apply(getActivity(), line);
        Font.ProximaNova_Regular.apply(getActivity(), joining);   */

        //Toast.makeText(getActivity(),"It was called",Toast.LENGTH_SHORT).show();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);

        setAdapter();

       // new getKindividualInfos().execute();
    }


    private void setAdapter()
    {
     //   Toast.makeText(getActivity(),"size "+titles.size(),Toast.LENGTH_SHORT).show();
        OfficialInfoAdapter adapter=new OfficialInfoAdapter(getActivity(),titles,values);
        recycler.setAdapter(adapter);
    }




    private class getKindividualInfos extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
           // ConnectionHelper ch = new ConnectionHelper();
          //  return ch.getDataFromUrlByGET(Urls.KINDIVIDUAL_INFO + "/" +
                  //  SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_employee_id)));

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

           // Log.i("response kindividual", s);
            try {

                JSONObject jObj1 = new JSONObject(s);

                JSONArray jArray=jObj1.getJSONArray("induatrial");
                for(int i=0;i<jArray.length();i++)
                {
                    JSONObject jInfo=jArray.getJSONObject(i);
                    titles.add(jInfo.getString("title"));
                    values.add(jInfo.getString("content"));
                }

                setAdapter();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
