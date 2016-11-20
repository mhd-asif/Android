package com.bscheme.linkkin.left_menu_fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.utils.CheckConnectivity;
import com.bscheme.linkkin.utils.Font;
import com.bscheme.linkkin.utils.SharedDataSaveLoad;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import us.feras.mdv.MarkdownView;

//import com.expandable.view.ExpandableView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KindomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KindomFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SweetAlertDialog mDialog;


    TextView title;
    RelativeLayout relativeDesc,relativeBanner,relativeLogo;
    LinearLayout linearOptions;
    ImageView imgBanner;
    ImageView imgLogo;
    ScrollView scrollRoot;
    ProgressBar progressBanner,progressLogo;


    String name,description,logo,banner;
    ArrayList<String>optionTitles=new ArrayList<>();
    ArrayList<String>optionDetails=new ArrayList<>();



    public static KindomFragment newInstance(String param1, String param2) {
        KindomFragment fragment = new KindomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public KindomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kindom1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = (TextView) view.findViewById(R.id.tv_title);
      //  short_desc = (TextView) view.findViewById(R.id.tv_short_desc);
        imgBanner=(ImageView)view.findViewById(R.id.imgBanner);
        imgLogo=(ImageView)view.findViewById(R.id.iv_kindom_logo);
        linearOptions=(LinearLayout)view.findViewById(R.id.linearOptions);
        relativeBanner=(RelativeLayout)view.findViewById(R.id.relativeBanner);
        relativeLogo=(RelativeLayout)view.findViewById(R.id.relativeLogo);
        relativeDesc=(RelativeLayout)view.findViewById(R.id.relativeDesc);
        scrollRoot=(ScrollView)view.findViewById(R.id.scrollRoot);
        progressBanner=(ProgressBar)view.findViewById(R.id.progressBanner);
        progressLogo=(ProgressBar)view.findViewById(R.id.progressLogo);

        progressBanner.setIndeterminate(true);
        progressBanner.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary_color_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
        progressLogo.setIndeterminate(true);
        progressLogo.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary_color_dark), android.graphics.PorterDuff.Mode.MULTIPLY);

        Font.LATO_Bold.apply(getActivity(), title);


        CheckConnectivity connectivity=new CheckConnectivity(getActivity());
        if(connectivity.isConnected()) new getRes().execute();
        else
        {
            try {
                getOfflineRes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



    private void setKindomDescription(String desc)
    {
        MarkdownView markdownView = new MarkdownView(getActivity());
        markdownView.setBackgroundColor(Color.parseColor("#00000000"));
        markdownView.loadMarkdown(desc, "file:///android_asset/css/new.css");
        relativeDesc.addView(markdownView);
    }

    public static void slide_down(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
    public static void slide_up(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }



    private void setAllOptions()
    {
        int size=optionTitles.size();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View[] views=new View[size];
        TextView[] txtTitles=new TextView[size];
        TextView[] txtSeparatores=new TextView[size];
        RelativeLayout[] relativeContainers=new RelativeLayout[size];
        linearOptions.removeAllViewsInLayout();
        for(int i=0;i<size;i++)
        {
            views[i]=inflater.inflate(R.layout.kindom_option_layout,null);
            txtTitles[i]=(TextView)views[i].findViewById(R.id.txtTitle);
            txtSeparatores[i]=(TextView)views[i].findViewById(R.id.txtSeparator);
            relativeContainers[i]=(RelativeLayout)views[i].findViewById(R.id.relativeContainer);
            if(i==0) txtSeparatores[i].setVisibility(View.GONE);
            txtTitles[i].setText(optionTitles.get(i));
            relativeContainers[i].setVisibility(View.GONE);
            txtTitles[i].setOnClickListener(handleOnClick(i, relativeContainers[i],views[i]));
            linearOptions.addView(views[i]);
        }
    }




    View.OnClickListener handleOnClick(final int pos, final RelativeLayout container,final View view)
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(container.isShown())
                {
                    slide_up(getActivity(), container);
                    view.clearFocus();
                    view.setFocusableInTouchMode(false);
                    container.setVisibility(View.GONE);
                }
                else
                {
                    container.setVisibility(View.VISIBLE);
                    view.setFocusableInTouchMode(true);
                    slide_down(getActivity(), container);

                    MarkdownView markdownView = new MarkdownView(getActivity());
                    markdownView.setBackgroundColor(Color.parseColor("#00000000"));
                    markdownView.loadMarkdown(optionDetails.get(pos), "file:///android_asset/css/new.css");
                    container.addView(markdownView);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          //  scrollRoot.fullScroll(View.FOCUS_DOWN);
                            view.requestFocus();
                        }
                    }, 500);
                }
            }
        };
    }



    private void getOfflineRes() throws IOException {
        String path_kindom=getActivity().getCacheDir().getAbsolutePath()+"/"+getResources().getString(R.string.file_kindom);
       // Log.e("catch path: ",path_kindom);
        File file = new File(path_kindom);
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
        Log.e("kindom_response", s);
        try {
            JSONObject jObj = new JSONObject(s);

            name = jObj.getString("name");
            description = jObj.getString("description");
            logo = jObj.getString("logo");
            banner = jObj.getString("banner");

            JSONArray jOptions=jObj.getJSONArray("option");
            for(int i=0;i<jOptions.length();i++)
            {
                JSONObject jOption=jOptions.getJSONObject(i);
                optionTitles.add(jOption.getString("title"));
                optionDetails.add(jOption.getString("content"));
            }

            title.setText(name);
            setKindomDescription(description);
            setAllOptions();

            Picasso.with(getActivity()).load(logo).transform(new CircleTransform()).fit().into(imgLogo, new Callback() {
                @Override
                public void onSuccess() {
                    //  imgLogo.setBackgroundResource(R.drawable.back_circle_blank);
                    relativeLogo.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {
                }
            });

            Picasso.with(getActivity()).load(banner).fit().into(imgBanner, new Callback() {
                @Override
                public void onSuccess() {
                    relativeBanner.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError() {

                }
            });


        } catch (JSONException e) {
            //  e.printStackTrace();
            Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
        }
    }




    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }





    private class getRes extends AsyncTask<String,Void,String> {
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
        /*    ConnectionHelper ch = new ConnectionHelper();
            return ch.getDataFromUrlByGET(Urls.KINDOM_LIST);  */
            ConnectionHelper helper = new ConnectionHelper();
            helper.createConnection(SharedDataSaveLoad.load(getActivity(),getActivity().getResources().getString(R.string.shared_pref_api_url)), "POST");
            helper.addData(new Uri.Builder().appendQueryParameter("action", "get_kindominfo")
            .appendQueryParameter("blog_id", SharedDataSaveLoad.load(getActivity(), getResources().getString(R.string.shared_pref_web_id))));
            return helper.getResponse();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mDialog.dismiss();
            if (!TextUtils.isEmpty(s)) // check response is null or not

                try {
                JSONObject jObj = new JSONObject(s);

                name = jObj.getString("name");
                description = jObj.getString("description");
                logo = jObj.getString("logo");
                banner = jObj.getString("banner");

                JSONArray jOptions=jObj.getJSONArray("option");
                for(int i=0;i<jOptions.length();i++)
                {
                    JSONObject jOption=jOptions.getJSONObject(i);
                    optionTitles.add(jOption.getString("title"));
                    optionDetails.add(jOption.getString("content"));
                }

                title.setText(name);
                setKindomDescription(description);
                setAllOptions();

                Picasso.with(getActivity()).load(logo).transform(new CircleTransform()).fit().into(imgLogo, new Callback() {
                    @Override
                    public void onSuccess() {
                      //  imgLogo.setBackgroundResource(R.drawable.back_circle_blank);
                        relativeLogo.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                    }
                });

                Picasso.with(getActivity()).load(banner).fit().into(imgBanner, new Callback() {
                    @Override
                    public void onSuccess() {
                        relativeBanner.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });


            } catch (JSONException e) {
              //  e.printStackTrace();
                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
            }
        }

    }




    public class CircleTransform implements Transformation {
        private final int BORDER_COLOR = Color.parseColor("#CCCCCC");
        private final int BORDER_RADIUS = 4;

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;

            // Prepare the background
            Paint paintBg = new Paint();
            paintBg.setColor(BORDER_COLOR);
            paintBg.setAntiAlias(true);

            // Draw the background circle
            canvas.drawCircle(r, r, r, paintBg);

            // Draw the image smaller than the background so a little border will be seen
            canvas.drawCircle(r, r, r - BORDER_RADIUS, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }





}
