package com.hk.demoapiuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.hk.demoapiuser.API.RetrofitClient;
import com.hk.demoapiuser.API.RetrofitInterface;
import com.hk.demoapiuser.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
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
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().createUser(
                binding.emailEt.getText().toString(),
                binding.passwordEt.getText().toString(),
                binding.nameEt.getText().toString(),
                binding.schoolEt.getText().toString()
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(MainActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                String s = "";
                try {
                    s = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });


    }
}
