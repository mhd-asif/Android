package com.example.saddam.petsofasiav2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saddam.petsofasiav2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    TextView textView;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page,container,false);
        textView = (TextView)view.findViewById(R.id.textView);
        Bundle bundle = getArguments();
        String message = Integer.toString(bundle.getInt("count"));

        if(message.equals("1")){
            textView.setText("Cum sociis natoque penatibus et" +
                    "\nmagnis dis parturient montes," +
                    "\nnascetur ridiculus mus. Sed" +
                    "\nposuere consectetur est at lobortis.");
        }
        else if (message.equals("2")){
            textView.setText("This space contains message for page TWO.");
        }
        else if (message.equals("3")) {
            textView.setText("This space contains message for page THREE.");
        }

        return view;
    }


}
