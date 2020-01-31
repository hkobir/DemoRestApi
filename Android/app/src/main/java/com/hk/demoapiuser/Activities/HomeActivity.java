package com.hk.demoapiuser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hk.demoapiuser.ModelClass.User;
import com.hk.demoapiuser.R;
import com.hk.demoapiuser.Storage.SharedPrefferenceManager;

public class HomeActivity extends AppCompatActivity {
    private TextView id, name, email, school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        id = findViewById(R.id.idTV);
        name = findViewById(R.id.nameTV);
        email = findViewById(R.id.emailTV);
        school = findViewById(R.id.schoolTV);

        User user = SharedPrefferenceManager.getInstance(HomeActivity.this).getuser();

        id.setText("ID: " + user.getId());
        name.setText("Name: " + user.getName());
        email.setText("Email: " + user.getEmail());
        school.setText("School: " + user.getSchool());

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPrefferenceManager.getInstance(HomeActivity.this).isLogIn()) {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
