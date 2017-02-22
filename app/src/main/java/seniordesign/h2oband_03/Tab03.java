package seniordesign.h2oband_03;

/**
 * Created by Owner on 1/25/2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class Tab03 extends Fragment{

    String [] SPINNERLIST= {"Metiric", "American"};


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page03, container, false);

        /*
        Spinner mySpinner = (Spinner)getView(). findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myAdapter.setAdapter(myAdapter);
        */

        /*
        ArrayAdapter<String> arrayAdapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,SPINNERLIST);
        MaterialBetterSpinner betterSpinner= (MaterialBetterSpinner) findViewById(R.id.andori_material_design_spinner);
        betterSpinner.setAdapter(arrayAdapter);
        */
        return rootView;
    }


}

