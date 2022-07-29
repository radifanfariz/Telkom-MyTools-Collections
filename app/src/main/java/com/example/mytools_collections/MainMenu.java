package com.example.mytools_collections;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    TextView menu1;
    TextView menu2;
    TextView menu3;
    TextView menu4;
    TextView menu5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        menu1 = findViewById(R.id.menu1);
        menu2 = findViewById(R.id.menu2);
        menu3 = findViewById(R.id.menu3);
        menu4 = findViewById(R.id.menu4);
        menu5 = findViewById(R.id.menu5);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        menu5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == menu1){
            startActivity(new Intent(getApplicationContext(),ListCollections.class));
        }
        if (v == menu2){
            startActivity(new Intent(getApplicationContext(),DataCollections.class));
        }
        if (v == menu3){
            startActivity(new Intent(getApplicationContext(),LogInfo.class));
        }
        if (v == menu4){
            SharedPreferences preferences = this.getSharedPreferences("MyPref",0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            finish();
            Intent intent = new Intent(MainMenu.this,Login2.class);
            startActivity(intent);
        }
        if (v == menu5){
            startActivity(new Intent(getApplicationContext(),ChangePassword.class));
        }
    }
}
