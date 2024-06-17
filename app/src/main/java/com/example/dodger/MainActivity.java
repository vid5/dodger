package com.example.dodger;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
    MediaPlayer sfxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set to fullscreen
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        sfxButton = MediaPlayer.create(this, R.raw.button);

        configureButton();
    }

    private void configureButton() {
        Button button = (Button) findViewById(R.id.switchActivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                sfxButton.start();
                startActivity(new Intent(MainActivity.this, LevelSelect.class));
            }
        });
    }
}