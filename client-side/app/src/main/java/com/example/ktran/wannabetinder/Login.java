package com.example.ktran.wannabetinder;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.ktran.wannabetinder.models.Constants;
import com.example.ktran.wannabetinder.models.RetroInterfaces;
import com.example.ktran.wannabetinder.models.ServerResponse;
import com.example.ktran.wannabetinder.models.User;
import com.google.firebase.messaging.FirebaseMessaging;
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
public class Login extends android.app.Fragment implements View.OnClickListener {

    private AppCompatButton btn_login;
    private EditText et_email,et_password;
    private TextView tv_register;
    private ProgressBar progress;
    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);
        initViews(view);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return view;
    }

    private void initViews(View view) {
        btn_login = (AppCompatButton)view.findViewById(R.id.btn_login);
        tv_register = (TextView)view.findViewById(R.id.tv_register);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                goToRegister();
                break;
            case R.id.btn_login:
                String username = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (!username.isEmpty() && !password.isEmpty()) {
                    progress.setVisibility(View.VISIBLE);
                    loginProcess(username, password);
                }
                else {

                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void loginProcess(String username, String password) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetroInterfaces requestInterface = retrofit.create(RetroInterfaces.class);
        final User user = new User(username, password, null, null, null );
        Call<ServerResponse> response = requestInterface.authUser(user);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Log.d("ERROR", resp.getMessage() + "   " + resp.getjsonToken());
                progress.setVisibility(View.INVISIBLE);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(resp.getuser());
                editor.putString("myUser", json);
                editor.putString("myName", resp.getMessage());
                editor.putString("myToken", resp.getjsonToken());
                editor.commit();

                if(!resp.getSuccess()){
                    Snackbar.make(getView(), "Something is wrong my friend.", Snackbar.LENGTH_LONG).show();
                    et_password.setText("");
                }
                else {
                    //TODO: sets the user to subscribe to topic
                    FirebaseMessaging.getInstance().subscribeToTopic("user_gina");
                    Intent intent = new Intent(getActivity(), Profile.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d("ERROR", t.getCause() +  "");
            }
        });
    }

    private void goToRegister(){
        Fragment register = new Register();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,register);
        ft.commit();
    }

}
