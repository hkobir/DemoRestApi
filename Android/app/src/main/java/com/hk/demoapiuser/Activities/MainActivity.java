package com.hk.demoapiuser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.hk.demoapiuser.API.RetrofitClient;
import com.hk.demoapiuser.ModelClass.DefaultResponse;
import com.hk.demoapiuser.R;
import com.hk.demoapiuser.Storage.SharedPrefferenceManager;
import com.hk.demoapiuser.databinding.ActivityMainBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Please wait...");

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();

            }
        });
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }

    private void userSignUp() {

        if (binding.emailEt.getText().toString().equals("")) {
            binding.emailEt.setError("Required email");
            binding.emailEt.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEt.getText().toString()).matches()) {
            binding.emailEt.setError("invalid email");
            binding.emailEt.requestFocus();
            return;
        }
        if (binding.passwordEt.getText().toString().equals("")) {
            binding.passwordEt.setError("Required password");
            binding.passwordEt.requestFocus();
            return;
        }

        if (binding.passwordEt.getText().toString().length() < 6) {
            binding.passwordEt.setError("password must be 6 character long");
            binding.passwordEt.requestFocus();
            return;
        }

        if (binding.nameEt.getText().toString().equals("")) {
            binding.nameEt.setError("Required Name");
            binding.nameEt.requestFocus();
            return;
        }
        if (binding.schoolEt.getText().toString().equals("")) {
            binding.schoolEt.setError("Required Name");
            binding.schoolEt.requestFocus();
            return;
        }

        //when has no error

        dialog.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().createUser(
                binding.emailEt.getText().toString(),
                binding.passwordEt.getText().toString(),
                binding.nameEt.getText().toString(),
                binding.schoolEt.getText().toString()
        );

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if(response.code()==201){
                    DefaultResponse defaultResponse =response.body();
                    Toast.makeText(MainActivity.this, ""+defaultResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
                else if(response.code()==422){
                    Toast.makeText(MainActivity.this, "user already exist!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        if(SharedPrefferenceManager.getInstance(MainActivity.this).isLogIn()){
            Intent intent =new Intent(MainActivity.this,HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
