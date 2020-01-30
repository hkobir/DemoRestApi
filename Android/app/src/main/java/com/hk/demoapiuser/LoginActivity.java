package com.hk.demoapiuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.hk.demoapiuser.API.RetrofitClient;
import com.hk.demoapiuser.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Please wait...");

        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    private void userlogin() {
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


        //when has no error

        dialog.show();
        Call<LoginResponse> call = RetrofitClient.getInstance().getApi().userLogin(
                binding.emailEt.getText().toString(),
                binding.passwordEt.getText().toString()
        );

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (!loginResponse.getErr()) {
                    //save user and go profile

                    Toast.makeText(LoginActivity.this, "" + loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } else {
                    Toast.makeText(LoginActivity.this, "" + loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }
}
