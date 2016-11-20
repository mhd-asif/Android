package com.bscheme.linkkin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bscheme.linkkin.R;

import java.util.ArrayList;

public class SpinAdapter
  extends ArrayAdapter<String>
{
  Context context;
  ArrayList<String> dists = new ArrayList();
  LayoutInflater inflater;
  
  public SpinAdapter(Context context, ArrayList<String> dists)
  {
    super(context, R.layout.spinner_item, dists);
    this.context = context;
    this.dists = dists;
    this.inflater = LayoutInflater.from(context);
  }
  
  public View getDropDownView(int position, View view, ViewGroup parent)
  {
    if (position == 0)
    {
      view = this.inflater.inflate(R.layout.spinner_item_special, null);
    }
    else
    {
      view = this.inflater.inflate(R.layout.spinner_item, null);
    }
    ((TextView)view.findViewById(R.id.txtItem)).setText((CharSequence)this.dists.get(position));
    return view;
  }
  
  public View getView(int position, View view, ViewGroup parent)
  {
    view = this.inflater.inflate(R.layout.drop_down_item, parent, false);
    ((TextView)view.findViewById(R.id.txtItem)).setText(dists.get(position));
    return view;
  }

}

