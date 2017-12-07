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

import com.example.ktran.wannabetinder.models.Constants;
import com.example.ktran.wannabetinder.models.RetroInterfaces;
import com.example.ktran.wannabetinder.models.ServerResponse;
import com.example.ktran.wannabetinder.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyFragment extends android.app.Fragment  {

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
    int[] array_answer_values = new int[numOfQuestions];
    User user;

    protected View mView;


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

        // Just need ints
        array_answer_values[0] = spinner1.getSelectedItemPosition();
        array_answer_values[1] = spinner2.getSelectedItemPosition();
        array_answer_values[2] = spinner3.getSelectedItemPosition();
        array_answer_values[3] = spinner4.getSelectedItemPosition();
        array_answer_values[4] = spinner5.getSelectedItemPosition();

        //Insert the items into the array of user responses
        updateArray();

        String sobj = mSharedPreferences.getString("reg_user", "");
        if(sobj.equals(""))
            Log.d("Error", "error");
        else
             user = new Gson().fromJson(sobj, User.class);

        user.setSurvey_results(array_answer_values);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroInterfaces requestInterface = retrofit.create(RetroInterfaces.class);

        Call<ServerResponse> response = requestInterface.registerUser(user);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                //progress.setVisibility(View.INVISIBLE);
                if(resp.getSuccess()){
                    Snackbar.make(getView(), "Regististratoin is successful!", Snackbar.LENGTH_LONG).show();

                    Log.d("SF", "Good");

                }else{
                    Log.d("SF", "Bad");
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                //progress.setVisibility(View.INVISIBLE);
                Log.d("SF", "Worse");
            }
        });
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
