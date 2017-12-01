package com.example.ktran.wannabetinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.ktran.wannabetinder.models.Constants;
import com.example.ktran.wannabetinder.models.RetroInterfaces;
import com.example.ktran.wannabetinder.models.ServerResponse;
import com.example.ktran.wannabetinder.models.User;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ktran on 11/26/17.
 */

public class Register extends android.app.Fragment implements View.OnClickListener {

    private AppCompatButton btn_register;
    private EditText et_password,et_name, et_phone;
    private Spinner mySpinner;
    private TextView tv_login;
    private ProgressBar progress;
    SharedPreferences mSharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_fragment,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        btn_register = (AppCompatButton)view.findViewById(R.id.btn_register);
        tv_login = (TextView)view.findViewById(R.id.tv_login);
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_password = (EditText)view.findViewById(R.id.et_password);
        et_phone = (EditText) view.findViewById(R.id.et_phone);
        mySpinner = view.findViewById(R.id.spinner);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_login:
                goToLogin();
                break;

            case R.id.btn_register:

                String name = et_name.getText().toString();
                String password = et_password.getText().toString();
                String department = mySpinner.getSelectedItem().toString();
                String phone = et_phone.getText().toString();
                progress.setVisibility(View.VISIBLE);
                registerProcess(name, password, department, phone);
                break;
        }

    }

    private void registerProcess(String name, String password, String department, String phone){
        User user = new User(name, password, department, phone, null);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("reg_user",new Gson().toJson(user));
        editor.commit();

        goToSurvey();

    }

    private void goToSurvey() {
        Fragment survey = new SurveyFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, survey);
        ft.commit();

    }

    private void goToLogin(){
        Fragment login = new Login();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }
}
