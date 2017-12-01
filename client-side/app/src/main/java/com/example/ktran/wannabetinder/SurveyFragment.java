package com.example.ktran.wannabetinder;


import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyFragment extends Fragment  {

    final String TAG = "SurveyFragment: ";
    final int numOfQuestions = 5;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    Spinner spinner5;
    String q1;
    String q2;
    String q3;
    String q4;
    String q5;
    String[] arrayOfAnswers = new String[numOfQuestions];
    Button button;
    SharedPreferences mSharedPreferences;

    public SurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_survey, container, false);

        //Initialize the Spinners
        initViews(view);

        //Initialize the sharedPreferences object
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Initialize the "done" button and add a click listener
        button = (Button) view.findViewById(R.id.doneButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneClicked();

                //Return to login
                goToLogin();
            }
        });

        return view;
    }

    private void doneClicked() {
        //Get the item selected for each spinner
        q1 = spinner1.getSelectedItem().toString();
        q2 = spinner2.getSelectedItem().toString();
        q3 = spinner3.getSelectedItem().toString();
        q4 = spinner4.getSelectedItem().toString();
        q5 = spinner5.getSelectedItem().toString();

        //TEST
        Snackbar.make(getView(), "Answers:" + q1+q2+q3+q4+q5, Snackbar.LENGTH_LONG).show();

        //Insert the items into the array of user responses
        updateArray();

        //Save the answers in sharedPreferences
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("q1", q1);
        editor.putString("q2", q2);
        editor.putString("q3", q3);
        editor.putString("q4", q4);
        editor.putString("q5", q5);
        editor.apply();
    }

    private void updateArray() {
        arrayOfAnswers[0] = q1;
        arrayOfAnswers[1] = q2;
        arrayOfAnswers[2] = q3;
        arrayOfAnswers[3] = q4;
        arrayOfAnswers[4] = q5;
    }

    private void goToLogin(){
        Fragment login = new Login();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }

    private void initViews(View view) {
        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        spinner3 = (Spinner) view.findViewById(R.id.spinner3);
        spinner4 = (Spinner) view.findViewById(R.id.spinner4);
        spinner5 = (Spinner) view.findViewById(R.id.spinner5);
    }
}
