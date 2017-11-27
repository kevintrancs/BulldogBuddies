package com.example.ktran.wannabetinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ktran.wannabetinder.models.Constants;
import com.example.ktran.wannabetinder.models.RetroInterfaces;
import com.example.ktran.wannabetinder.models.ServerResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity{
    private SharedPreferences mSharedPreferences;
    private String mToken;
    private String mName;
    private TextView tv_name;
    private TextView tv_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initValues();
        initSharedPreferences();
        loadProfile();

        Button btn = (Button) findViewById(R.id.log_out);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("myName", "");
                editor.putString("myToken", "");
                editor.apply();
                finish();
            }
        });
    }


    private void initSharedPreferences() {

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mName = mSharedPreferences.getString("myName","");
        mToken = mSharedPreferences.getString("myToken","");

    }

    private void initValues() {
        tv_name = (TextView) findViewById(R.id.et_email);
        tv_token = (TextView) findViewById(R.id.token_id);
    }

    private void loadProfile(){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroInterfaces profileInterface = retrofit.create(RetroInterfaces.class);

        Call<ServerResponse> response = profileInterface.getProfile(mToken, mName);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Snackbar.make(findViewById(R.id.ap), resp.getMessage() + ", Sucessful:" + resp.getSuccess(), Snackbar.LENGTH_LONG).show();
                tv_name.setText(resp.getMessage());
                tv_token.setText(mToken);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Snackbar.make(findViewById(R.id.ap), t.getCause() + " shits busted", Snackbar.LENGTH_LONG).show();

            }
        });
    }


}
