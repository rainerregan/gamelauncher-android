package com.rainerregan.gamelauncher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rainerregan.gamelauncher.flappybird.AndroidLauncher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchFlappyBird(View v){
        Intent flappyBirdIntent = new Intent(MainActivity.this, AndroidLauncher.class);
        startActivity(flappyBirdIntent);
    }
}