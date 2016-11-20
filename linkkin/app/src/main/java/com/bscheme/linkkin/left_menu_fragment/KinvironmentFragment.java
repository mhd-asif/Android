package com.bscheme.linkkin.left_menu_fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bscheme.linkkin.helper.ConnectionHelper;
import com.bscheme.linkkin.R;
import com.bscheme.linkkin.helper.DirectionsJSONParser;
import com.bscheme.linkkin.helper.MyMenuClass;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class KinvironmentFragment extends Fragment implements OnClickListener
{

  String API_KEY = "AIzaSyCRhvDKMe-2B10DJ097gTo69rDJen4eoRw";

  Activity activity;
  String alertTitle;
//  Bitmap bitBarber;
//  Bitmap bitGrocery;
//  Bitmap bitHospital;
//  Bitmap bitLaundry;
//  Bitmap bitPolice;
//  Bitmap bitRestaurant;
//  Bitmap bitSchool;
  Bitmap bitUniversity;
  Context context;
  ArrayList<Boolean> draw;
  private String employeeName;
  FloatingActionButton fab;
  boolean isLocFound;
  boolean isMapLoaded;
  boolean isMapZoomed;
  ArrayList<String> latitudes;
  ArrayList<LatLng> locs;
  ArrayList<String> longitudes;
  GoogleMap map;
  AlertDialog mapDialog;
  double definedLat,definedLon;
  LatLng definedLoc;
  ArrayList<String> names;
  String option;
  SweetAlertDialog sweetDialog;
  ArrayList<String> types;
  

  
  public static KinvironmentFragment newInstance(String paramString1, String paramString2)
  {
    KinvironmentFragment localKinvironmentFragment = new KinvironmentFragment();
    Bundle localBundle = new Bundle();
    localBundle.putString("param1", paramString1);
    localBundle.putString("param2", paramString2);
    localKinvironmentFragment.setArguments(localBundle);
    return localKinvironmentFragment;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getArguments() != null)
    {
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View view = paramLayoutInflater.inflate(R.layout.fragment_kinvironment, paramViewGroup, false);
    this.employeeName = MyMenuClass.employeeName;
    if (this.employeeName == null) {
      this.employeeName = "You are here";
    }
    this.isMapLoaded = false;
    this.isMapZoomed = false;
    this.isLocFound = false;
    this.activity = getActivity();
    this.context = paramLayoutInflater.getContext();
    this.fab = ((FloatingActionButton)view.findViewById(R.id.fab));

    definedLat=24.2851247;
    definedLon=90.3920371;

    Toast.makeText(this.context, "InsideOnCreateView()", Toast.LENGTH_SHORT).show();

    createBitmap();
    try
    {
      this.map = ((SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map)).getMap();
      if (this.map != null)
      {
        this.map.setMyLocationEnabled(true);
        this.sweetDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
        this.sweetDialog.getProgressHelper().setBarColor(getActivity().getResources().getColor(R.color.primary_color_dark));
        this.sweetDialog.setTitleText("");
        this.alertTitle = "places";
        this.sweetDialog.setContentText("Searching nearby " + this.alertTitle + "...");
        this.sweetDialog.setCancelable(false);
        this.sweetDialog.show();

        definedLoc = new LatLng(definedLat, definedLon);
        findNearbyPlaces("places");


        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
          @Override
          public void onMapLoaded() {
            KinvironmentFragment.this.isMapLoaded = true;
            if ((KinvironmentFragment.this.isLocFound) && (!KinvironmentFragment.this.isMapZoomed)) {
              KinvironmentFragment.this.zoomMap();
            }
          }
        });


        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
          @Override
          public void onInfoWindowClick(Marker marker) {
            String marketTitle = marker.getTitle();
            for (int i = 0; i < names.size(); i++) {
              if (names.get(i).equals(marketTitle))
              {
                drawPath(i);
              }
            }
          }
        });


      }


      this.fab.setOnClickListener(new OnClickListener()
      {
        public void onClick(View v)
        {
          View vMap = LayoutInflater.from(getActivity()).inflate(R.layout.map_dialog, null);
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setView(vMap);
          builder.setCancelable(false);
//          LinearLayout linearHospital = (LinearLayout)vMap.findViewById(R.id.linearHospital);
//          LinearLayout linearRestaurant = (LinearLayout)vMap.findViewById(R.id.linearRestaurant);
//          LinearLayout linearPolice = (LinearLayout)vMap.findViewById(R.id.linearPolice);
//          LinearLayout linearGrocery = (LinearLayout)vMap.findViewById(R.id.linearGrocery);
//          LinearLayout linearPrimary = (LinearLayout)vMap.findViewById(R.id.linearPrimary);
          LinearLayout linearUniversity = (LinearLayout)vMap.findViewById(R.id.linearUniversity);
//          LinearLayout linearLanundry = (LinearLayout)vMap.findViewById(R.id.linearLanundry);
//          LinearLayout linearBarber = (LinearLayout)vMap.findViewById(R.id.linearBarber);
          LinearLayout linearDone = (LinearLayout)vMap.findViewById(R.id.linearDone);
//          linearHospital.setOnClickListener(KinvironmentFragment.this);
//          linearRestaurant.setOnClickListener(KinvironmentFragment.this);
//          linearPolice.setOnClickListener(KinvironmentFragment.this);
//          linearGrocery.setOnClickListener(KinvironmentFragment.this);
//          linearPrimary.setOnClickListener(KinvironmentFragment.this);
          linearUniversity.setOnClickListener(KinvironmentFragment.this);
//          linearLanundry.setOnClickListener(KinvironmentFragment.this);
//          linearBarber.setOnClickListener(KinvironmentFragment.this);
          linearDone.setOnClickListener(KinvironmentFragment.this);
          mapDialog = builder.create();
          mapDialog.show();
          linearDone.setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymous2View) {
              mapDialog.dismiss();
            }
          });
        }
      });

    }
    catch (Exception e)
    {
      if(sweetDialog!=null&&sweetDialog.isShowing()) sweetDialog.dismiss();
      Toast.makeText(this.context, "Map can't be displayed", Toast.LENGTH_SHORT).show();
    }
    return view;
  }
  
  public void addMarkers()
  {
    try
    {
      if (this.map != null) {
        this.map.clear();
      }
      this.locs = new ArrayList();
      this.draw = new ArrayList();
      for(int i=0;i<latitudes.size();i++)
      {
        String str = (String)this.types.get(i);
        LatLng loc = new LatLng(Double.valueOf((String) this.latitudes.get(i)), Double.valueOf((String) this.longitudes.get(i)));
        this.locs.add(loc);
        this.draw.add(false);

        Marker marker = this.map.addMarker(new MarkerOptions().position(loc).title((String)this.names.get(i)).snippet("Click to see direction"));
//        if (str.equals("hospital")) {
//          marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.bitHospital));
//        }
//        else if (str.equals("restaurant")) {
//          marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.bitRestaurant));
//        }
//        else if (str.equals("police")) {
//          marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.bitPolice));
//        }
//        else if (str.equals("grocery_or_supermarket")) {
//          marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.bitGrocery));
//        }
//        else if (str.equals("school")) {
//          marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.bitSchool));
//        }
        if (str.equals("university")) {
          marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.bitUniversity));
        }
//        else if (str.equals("laundry")) {
//          marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.bitLaundry));
//        }
//        else if (str.equals("bar")) {
//          marker.setIcon(BitmapDescriptorFactory.fromBitmap(this.bitBarber));
//        }

      }

      //this.map.addMarker(new MarkerOptions().position(this.myLoc).title(this.employeeName)).setIcon(BitmapDescriptorFactory.defaultMarker(getColorHue("#0B8081")));
      this.map.addMarker(new MarkerOptions().position(definedLoc).title("Sq celsius 2, Dhaka")).setIcon(BitmapDescriptorFactory.defaultMarker(getColorHue("#0B8081")));

      this.isLocFound = true;
      if ((this.isMapLoaded) && (!this.isMapZoomed)) {
        zoomMap();
      }
    }
    catch (Exception e)
    {
      if(sweetDialog!=null&&sweetDialog.isShowing()) sweetDialog.dismiss();
      Toast.makeText(getActivity(),"Problem occured while adding markers",Toast.LENGTH_SHORT).show();
    }
  }
  
  public void createBitmap()
  {
    int i = getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);

    Canvas localCanvas;
    Drawable localDrawable;

//    this.bitHospital = Bitmap.createBitmap(i, i, Config.ARGB_8888);
//    localCanvas = new Canvas(this.bitHospital);
//    localDrawable = getResources().getDrawable(R.drawable.img_hospital);
//    localDrawable.setBounds(0, 0, this.bitHospital.getWidth(), this.bitHospital.getHeight());
//    localDrawable.draw(localCanvas);

//    this.bitRestaurant = Bitmap.createBitmap(i, i, Config.ARGB_8888);
//    localCanvas = new Canvas(this.bitRestaurant);
//    localDrawable = getResources().getDrawable(R.drawable.img_restaurant);
//    localDrawable.setBounds(0, 0, this.bitRestaurant.getWidth(), this.bitRestaurant.getHeight());
//    localDrawable.draw(localCanvas);

//    this.bitPolice = Bitmap.createBitmap(i, i, Config.ARGB_8888);
//    localCanvas = new Canvas(this.bitPolice);
//    localDrawable = getResources().getDrawable(R.drawable.img_police);
//    localDrawable.setBounds(0, 0, this.bitPolice.getWidth(), this.bitPolice.getHeight());
//    localDrawable.draw(localCanvas);
//
//    this.bitGrocery = Bitmap.createBitmap(i, i, Config.ARGB_8888);
//    localCanvas = new Canvas(this.bitGrocery);
//    localDrawable = getResources().getDrawable(R.drawable.img_grocery);
//    localDrawable.setBounds(0, 0, this.bitGrocery.getWidth(), this.bitGrocery.getHeight());
//    localDrawable.draw(localCanvas);

//    this.bitSchool = Bitmap.createBitmap(i, i, Config.ARGB_8888);
//    localCanvas = new Canvas(this.bitSchool);
//    localDrawable = getResources().getDrawable(R.drawable.img_primary);
//    localDrawable.setBounds(0, 0, this.bitSchool.getWidth(), this.bitSchool.getHeight());
//    localDrawable.draw(localCanvas);

    this.bitUniversity = Bitmap.createBitmap(i, i, Config.ARGB_8888);
    localCanvas = new Canvas(this.bitUniversity);
    localDrawable = getResources().getDrawable(R.drawable.img_university);
    localDrawable.setBounds(0, 0, this.bitUniversity.getWidth(), this.bitUniversity.getHeight());
    localDrawable.draw(localCanvas);

//    this.bitLaundry = Bitmap.createBitmap(i, i, Config.ARGB_8888);
//    localCanvas = new Canvas(this.bitLaundry);
//    localDrawable = getResources().getDrawable(R.drawable.img_laundry);
//    localDrawable.setBounds(0, 0, this.bitLaundry.getWidth(), this.bitLaundry.getHeight());
//    localDrawable.draw(localCanvas);
//
//    this.bitBarber = Bitmap.createBitmap(i, i, Config.ARGB_8888);
//    localCanvas = new Canvas(this.bitBarber);
//    localDrawable = getResources().getDrawable(R.drawable.img_barbar);
//    localDrawable.setBounds(0, 0, this.bitBarber.getWidth(), this.bitBarber.getHeight());
//    localDrawable.draw(localCanvas);
  }




  public void drawPath(int mPos)
  {
    if(draw.get(mPos)==false)
    {
      try
      {
        draw.set(mPos,true);
        Toast.makeText(getActivity(),"Please wait to see the path",Toast.LENGTH_LONG).show();
              try{
                    final LatLngBounds bounds = new LatLngBounds.Builder().include(definedLoc).include(locs.get(mPos)).build();
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 65));
                }
                catch(Exception e)
                {
                    draw.set(mPos,false);
                Toast.makeText(getActivity(),"Path can't be shown, please try again",Toast.LENGTH_LONG).show();
                }
        String str_origin = "origin="+definedLat+","+definedLon;
        // Destination of route
        String str_dest = "destination="+latitudes.get(mPos)+","+longitudes.get(mPos);
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
      }
      catch(Exception e)
      {
        draw.set(mPos,false);
        Toast.makeText(getActivity(),"Path can't be shown, please try again",Toast.LENGTH_LONG).show();
      }
    }
    else Toast.makeText(getActivity(),"Please wait to see the path",Toast.LENGTH_LONG).show();
  }


  
  public void findNearbyPlaces(String tempOption)
  {
    String str = tempOption;
    try
    {
      if(tempOption.equals("places"))
      {
        str = URLEncoder.encode("hospital|restaurant|police|grocery_or_supermarket|school|university|laundry|bar", "UTF-8");
      }

      String placeUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + definedLat + "," + definedLon + "&radius=3000&types=" + str + "&sensor=false&key=" + this.API_KEY;
      new PlaceFinder().execute(placeUrl);
    }
    catch (Exception e)
    {
      if(sweetDialog!=null&&sweetDialog.isShowing()) sweetDialog.dismiss();
      Toast.makeText(getActivity(),"Problem occured while searching nearby places",Toast.LENGTH_SHORT).show();
    }
  }
  
  public float getColorHue(String color)
  {
    float[] aa = new float[3];
    Color.colorToHSV(Color.parseColor(color), aa);
    return aa[0];
  }




  public void onClick(View paramView)
  {
    this.option = "";
    if (paramView.getId() == R.id.linearHospital)
    {
      this.option = "hospital";
      this.alertTitle = "hospitals";
    }

     else if (paramView.getId() == R.id.linearRestaurant)
      {
        this.option = "restaurant";
        this.alertTitle = "restaurants";
      }
      else if (paramView.getId() == R.id.linearPolice)
      {
        this.option = "police";
        this.alertTitle = "police stations";
      }
      else if (paramView.getId() == R.id.linearGrocery)
      {
        this.option = "grocery_or_supermarket";
        this.alertTitle = "grocery shops";
      }
      else if (paramView.getId() == R.id.linearPrimary)
      {
        this.option = "school";
        this.alertTitle = "schools";
      }
      else if (paramView.getId() == R.id.linearUniversity)
      {
        this.option = "university";
        this.alertTitle = "universities";
      }
      else if (paramView.getId() == R.id.linearLanundry)
      {
        this.option = "laundry";
        this.alertTitle = "laundries";
      }
      else if (paramView.getId() == R.id.linearBarber)
      {
        this.option = "bar";
        this.alertTitle = "barber shops";
      }

    if (!this.option.equals(""))
    {
      if ((this.mapDialog != null) && (this.mapDialog.isShowing())) {
        this.mapDialog.dismiss();
      }

      this.isMapZoomed = false;
      this.isLocFound = false;
      this.sweetDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_WITHOUT_TEXT_TYPE);
      this.sweetDialog.getProgressHelper().setBarColor(getActivity().getResources().getColor(R.color.primary_color_dark));
      this.sweetDialog.setTitleText("");
      this.sweetDialog.setContentText("Searching nearby " + this.alertTitle + "...");
      this.sweetDialog.setCancelable(false);
      this.sweetDialog.show();
      findNearbyPlaces(this.option);
    }
  }

  
  public void zoomMap()
  {
    try
    {
      CameraPosition localCameraPosition = new CameraPosition.Builder().target(definedLoc).zoom(13.0F).build();
      this.map.animateCamera(CameraUpdateFactory.newCameraPosition(localCameraPosition));
      if ((this.sweetDialog != null) && (this.sweetDialog.isShowing())) {
        this.sweetDialog.dismiss();
      }
      return;
    }
    catch (Exception localException) {
      if(sweetDialog!=null&&sweetDialog.isShowing()) sweetDialog.dismiss();
    }
  }
  

  
  class PlaceFinder extends AsyncTask<String, Void, String>
  {
    protected String doInBackground(String... paramVarArgs)
    {
      return new ConnectionHelper().getDataFromUrlByGET(paramVarArgs[0]);
    }
    
    protected void onPostExecute(String response)
    {
      if (!TextUtils.isEmpty(response)) // check response is null or not

        try
        {
          JSONArray jArray = new JSONArray(new JSONObject(response).getString("results"));
          for(int i=0;i<jArray.length();i++)
          {
            JSONObject placeItem = jArray.getJSONObject(i);
            Double latTemp = Double.parseDouble(placeItem.getJSONObject("geometry").getJSONObject("location").getString("lat"));
            Double lonTemp = Double.parseDouble(placeItem.getJSONObject("geometry").getJSONObject("location").getString("lng"));
            String nameTemp = placeItem.getString("name");

            if(alertTitle.equals("places"))
            {
              JSONArray jTypes=placeItem.getJSONArray("types");
              for(int j=0;j<jTypes.length();j++)
              {
                String str2=jTypes.getString(j);
                if ((str2.equals("hospital")) || (str2.equals("restaurant")) || (str2.equals("police")) || (str2.equals("grocery_or_supermarket")) || (str2.equals("school")) || (str2.equals("university")) || (str2.equals("laundry")) || (str2.equals("bar"))) {
                  types.add(str2);
                }
              }
            }
            else
            {
              types.add(KinvironmentFragment.this.option);
            }

            names.add(nameTemp);
            latitudes.add(String.valueOf(Double.valueOf(latTemp)));
            longitudes.add(String.valueOf(Double.valueOf(lonTemp)));

          }
          //Toast.makeText(KinvironmentFragment.this.context, ""+names.size(), Toast.LENGTH_SHORT).show();
          addMarkers();

        }
        catch (JSONException paramString)
        {
          paramString.printStackTrace();
          if(sweetDialog!=null&&sweetDialog.isShowing()) sweetDialog.dismiss();
          Toast.makeText(KinvironmentFragment.this.context, "Can't find Places", Toast.LENGTH_SHORT).show();
        }
    }
    
    protected void onPreExecute()
    {
      KinvironmentFragment.this.latitudes = new ArrayList();
      KinvironmentFragment.this.longitudes = new ArrayList();
      KinvironmentFragment.this.names = new ArrayList();
      KinvironmentFragment.this.types = new ArrayList();
    }
  }



  /** A method to download json data from url */
  private String downloadUrl(String strUrl) throws IOException {
    String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    try{
      URL url = new URL(strUrl);
      // Creating an http connection to communicate with url
      urlConnection = (HttpURLConnection) url.openConnection();
      // Connecting to url
      urlConnection.connect();
      // Reading data from url
      iStream = urlConnection.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
      StringBuffer sb = new StringBuffer();
      String line = "";
      while( ( line = br.readLine()) != null){
        sb.append(line);
      }
      data = sb.toString();
      br.close();

    }catch(Exception e){
    }finally{
      iStream.close();
      urlConnection.disconnect();
    }
    return data;
  }




  // Fetches data from url passed
  private class DownloadTask extends AsyncTask<String, Void, String> {

    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) {

      // For storing data from web service
      String data = "";

      try{
        // Fetching the data from web service
        data = downloadUrl(url[0]);
      }catch(Exception e){
        //Log.d("Background Task",e.toString());
      }
      return data;
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);

      ParserTask parserTask = new ParserTask();

      // Invokes the thread for parsing the JSON data
      parserTask.execute(result);
    }
  }




  /** A class to parse the Google Places in JSON format */
  private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

      JSONObject jObject;
      List<List<HashMap<String, String>>> routes = null;

      try{
        jObject = new JSONObject(jsonData[0]);
        DirectionsJSONParser parser = new DirectionsJSONParser();

        // Starts parsing data
        routes = parser.parse(jObject);
      }catch(Exception e){
        e.printStackTrace();
      }
      return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
      ArrayList<LatLng> points = null;
      PolylineOptions lineOptions = null;
      MarkerOptions markerOptions = new MarkerOptions();

      // Traversing through all the routes
      for(int i=0;i<result.size();i++){
        points = new ArrayList<LatLng>();
        lineOptions = new PolylineOptions();

        // Fetching i-th route
        List<HashMap<String, String>> path = result.get(i);

        // Fetching all the points in i-th route
        for(int j=0;j<path.size();j++){
          HashMap<String,String> point = path.get(j);

          double lat = Double.parseDouble(point.get("lat"));
          double lng = Double.parseDouble(point.get("lng"));
          LatLng position = new LatLng(lat, lng);

          points.add(position);
        }

        // Adding all the points in the route to LineOptions
        lineOptions.addAll(points);
        lineOptions.width(8);
        lineOptions.color(Color.RED);
      }

      // Drawing polyline in the Google Map for the i-th route
      map.addPolyline(lineOptions);
    }

  }

}

