package com.example.dodger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class LevelSelect extends Activity implements View.OnClickListener {
    public static Level level;
    final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    static ArrayList<Level> levelArray = new ArrayList<>();
    boolean createLevels = true;
    MediaPlayer sfxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_select);

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

        if(createLevels) {
            levelArray.add(new Level(1, "Tutorial", "Welcome to Dodger! Move the player around by dragging your finger and try not to get hit by projectiles to achieve a high level grade. Also projectiles will only damage you if the small green circle in the middle of the player.", 1500));
            levelArray.add(new Level(2, "Switch Tutorial", "Your player has an ability - switching color states. To switch your color, tap anywhere on the screen while holding the player. You cannot get hit by projectiles which are the same color as the player.", 2800));
            levelArray.add(new Level(3, "Level 3", "Try to remember the movement direction of projectiles and keep track of your current color state.", 2800));
            levelArray.add(new Level(4, "Level 4", "Lots of projectiles here, you will have to do small and precise movements.", 2500));
            levelArray.add(new Level(5, "Final Level", "Good luck.", 3000));
            createLevels = false;
        }

        Button level1 = (Button) findViewById(R.id.level1);
        level1.setOnClickListener(this);

        Button level2 = (Button) findViewById(R.id.level2);
        level2.setOnClickListener(this);

        Button level3 = (Button) findViewById(R.id.level3);
        level3.setOnClickListener(this);

        Button level4 = (Button) findViewById(R.id.level4);
        level4.setOnClickListener(this);

        Button level5 = (Button) findViewById(R.id.level5);
        level5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //selects level
        switch (view.getId()) {
            case R.id.level1:
                level = levelArray.get(0);
                break;
            case R.id.level2:
                level = levelArray.get(1);
                break;
            case R.id.level3:
                level = levelArray.get(2);
                break;
            case R.id.level4:
                level = levelArray.get(3);
                break;
            case R.id.level5:
                level = levelArray.get(4);
                break;
        }
        sfxButton.start();
        level.setDamageTaken(0);

        //check if the play button is pressed
        if (view.getId() == R.id.buttonPlay) {
            startActivity(new Intent(LevelSelect.this, PlayArea.class));
        }
        else {
            //creates popup
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View viewPopupWindow = layoutInflater.inflate(R.layout.popup_window, null);
            PopupWindow popupWindow = new PopupWindow(viewPopupWindow, screenWidth - screenWidth/4, screenHeight - screenHeight/4, true);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            //sets popup window text
            TextView levelName = viewPopupWindow.findViewById(R.id.levelName);
            levelName.setText(level.getLevelName());

            TextView levelDescription = viewPopupWindow.findViewById(R.id.levelDescription);
            levelDescription.setText(level.getDescription());

            TextView levelGrade = viewPopupWindow.findViewById(R.id.levelGrade);
            levelGrade.setText("Best Grade: " + level.getLevelGrade());

            //defines popup button
            Button play = (Button) viewPopupWindow.findViewById(R.id.buttonPlay);
            play.setOnClickListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        //disabled back button
    }
}
