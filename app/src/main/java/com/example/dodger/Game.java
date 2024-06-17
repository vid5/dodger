package com.example.dodger;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;

// Game manages all objects in the game and is responsible for rendering all objects on the screen

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private Player player;
    final private ArrayList<Bullet> bullets = new ArrayList<>();
    final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean invincible = false;
    private int numberOfUpdates = 0;
    private SurfaceHolder surfaceHolder;
    private Level level;
    private Intent intent = new Intent(getContext(), LevelSelect.class);
    private SpriteSheet spriteSheet;
    private boolean endLevel = false;
    private Grade oldGrade;
    private MediaPlayer sfxHit;
    private MediaPlayer sfxSwitch;
    private MediaPlayer sfxEnd;
    private MediaPlayer sfxButton;
    private MediaPlayer sfxButllet;

    public Game(Context context) {
        super(context);

        // Get surface holder and add callback
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        spriteSheet = new SpriteSheet(context);
        player = new Player(getContext(), screenWidth/2, screenHeight - screenHeight/6, 150, spriteSheet.getPlayerSpriteArray(), this);

        this.level = LevelSelect.level;

        //saves your previous best grade
        oldGrade = level.getLevelGrade();

        sfxHit = MediaPlayer.create(context, R.raw.hit);
        sfxSwitch = MediaPlayer.create(context, R.raw.switchstate);
        sfxEnd = MediaPlayer.create(context, R.raw.levelend);
        sfxButton = MediaPlayer.create(context, R.raw.button);
        sfxButllet = MediaPlayer.create(context, R.raw.bullet);

        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            surfaceHolder = getHolder();
            surfaceHolder.addCallback(this);
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    public boolean getInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public int getNumberOfUpdates() {
        return numberOfUpdates;
    }

    public void pause() {
        gameLoop.stopLoop();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for(Bullet i : bullets)
            i.draw(canvas);
        player.draw(canvas);

        drawUI(canvas);
        /*drawUPS(canvas);
        drawFPS(canvas);*/
        drawExitButton(canvas);
        drawTime(canvas);
        drawDamage(canvas);
        if(endLevel == true)
            drawLevelEnd(canvas);
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void drawTime(Canvas canvas) {
        String time = Double.toString(Math.round(((level.getLevelTime()-numberOfUpdates)/60.0)*100)/100.0);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Time: " + time, screenWidth - 350, screenHeight, paint);
    }

    public void drawDamage(Canvas canvas) {
        String damage = Integer.toString(level.getDamageTaken());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Damage: " + damage, 100, screenHeight, paint);
    }

    public void drawExitButton(Canvas canvas) {
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.lightGray);
        paint.setColor(color);

        //draws a triangle
        Path path = new Path();
        path.moveTo(screenWidth - 150, 100);
        path.lineTo(screenWidth - 50, 150);
        path.lineTo(screenWidth - 50, 50);
        path.lineTo(screenWidth - 150, 100);
        path.close();

        canvas.drawPath(path, paint);
    }

    public void drawLevelEnd(Canvas canvas) {
        String grade = level.getLevelGrade().toString();
        Paint paint = new Paint();

        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("LEVEL COMPLETE", screenWidth/2, screenHeight/2, paint);

        paint.setTextSize(50);
        canvas.drawText("Grade: " + grade, screenWidth/2, screenHeight/2+100, paint);
    }

    public void drawUI(Canvas canvas) {
        Paint paint = new Paint();

        int color = ContextCompat.getColor(getContext(), R.color.purple);
        paint.setColor(color);
        canvas.drawRect(0, screenHeight - 100, screenWidth, screenHeight + 500, paint);

        color = ContextCompat.getColor(getContext(), R.color.purple_200);
        paint.setColor(color);
        canvas.drawRect(0, screenHeight - 100, screenWidth, screenHeight - 75, paint);
    }

    public void update() {
        // Update game state
        player.update();

        switch(level.getLevelNumber()) {
            case 1:
                level1();
                break;
            case 2:
                level2();
                break;
            case 3:
                level3();
                break;
            case 4:
                level4();
                break;
            case 5:
                level5();
                break;
        }

        for(Bullet i : bullets)
            i.update();
        if (invincible == false) {
            Iterator<Bullet> iteratorBullets = bullets.iterator();
            while (iteratorBullets.hasNext()) {
                Bullet bullet = iteratorBullets.next();
                if (isColliding((CircleBullet) bullet, player) && player.getState() != bullet.getState()) {
                    invincible = true;
                    sfxHit.start();
                    level.increaseDamageTaken();
                    iteratorBullets.remove();
                    break;
                }
            }
        }
        numberOfUpdates++;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        //exits to level select
        if (touchX > screenWidth-150 && touchX < screenWidth-50 && touchY > 50 && touchY < 150 && event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("Game.java", "Exit");
            sfxButton.start();
            exitLevel();
        }

        int playerX = player.getX();
        int playerY = player.getY();
        int playerRadius = player.getRadius();

        int leniency = 50;

        if(touchX <= playerRadius/4) // Checks if the player hitbox is outside of screen edges
            touchX = playerRadius/4;
        else if(touchX >= screenWidth - playerRadius/4)
            touchX = screenWidth - playerRadius/4;
        if(touchY <= playerRadius/4)
            touchY = playerRadius/4;
        else if(touchY >= screenHeight)
            touchY = screenWidth - playerRadius/4;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (touchX >= (playerX - playerRadius) - leniency && // Checks if the player is being moved from their previous position
                        touchX <= (playerX + playerRadius) + leniency &&
                        touchY >= (playerY - playerRadius) - leniency &&
                        touchY <= (playerY + playerRadius) + leniency
                ) {
                    player.setX(touchX);
                    player.setY(touchY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                sfxSwitch.start();
                player.switchState(getContext());
                return true;
        }

        return super.onTouchEvent(event);
    }

    public static double getDistance(double bulletX, double bulletY, Player player) {
        double distanceX = bulletX - player.getX();
        double distanceY = bulletY - player.getY();

        return Math.sqrt((distanceX*distanceX) + (distanceY*distanceY));
    }

    public static boolean isColliding(CircleBullet bullet, Player player) {
        double distance = getDistance(bullet.getX(), bullet.getY(), player);
        double radiusDistance = bullet.getRadius()/2 + player.getRadius()/5;
        double distanceToCollision = Math.sqrt(radiusDistance * radiusDistance);
        if(distance < distanceToCollision)
            return true;
        else
            return false;
    }

    public static double getPlayerDirection(double bulletX, double bulletY, Player player) {
        double playerX = player.getX();
        double playerY = player.getY();

        double distanceX = bulletX - playerX;
        double distanceY = bulletY - playerY;

        return Math.toDegrees(Math.atan(distanceX/distanceY));
    }

    public void levelEnd() {
        sfxEnd.start();
        level.calculateLevelGrade();
        endLevel = true;
    }

    public void exitLevel() {
        //checks if the new grade is better than the last
        if (level.getLevelGrade().ordinal() < oldGrade.ordinal()) {
            level.setLevelGrade(oldGrade);
        }
        getContext().startActivity(intent);
    }

    /*
    ----------------
    BULLET FUNCTIONS
    ----------------
     */

    public void bulletSound() {
        if (sfxButllet.isPlaying())
            sfxButllet.seekTo(0);
        sfxButllet.start();
    }

    public void bulletCircle(double x, double y, double speed, int radius, double offset, int amount) {
        bulletSound();
        double degrees;
        for(int i = 0; i < amount; i++) {
            degrees = (360/(double)amount)*i + offset;
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray()));
        }
    }

    public void bulletCircle(double x, double y, double speed, int radius, double offset, int amount, Color state) {
        bulletSound();
        double degrees;
        for(int i = 0; i < amount; i++) {
            degrees = (360/(double)amount)*i + offset;
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray(), state));
        }
    }

    public void aimedBulletCircle(double x, double y, double speed, int radius, double offset, int amount) {
        bulletSound();
        double degrees;
        double aim = getPlayerDirection(x, y, player);
        for(int i = 0; i < amount; i++) {
            degrees = (360/(double)amount)*i + offset + aim;
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray()));
        }
    }

    public void aimedBulletCircle(double x, double y, double speed, int radius, double offset, int amount, Color state) {
        bulletSound();
        double degrees;
        double aim = getPlayerDirection(x, y, player);
        for(int i = 0; i < amount; i++) {
            degrees = (360/(double)amount)*i + offset + aim;
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray(), state));
        }
    }

    public void aimedBullet(double x, double y, double speed, int radius, double offset) {
        bulletSound();
        double aim = getPlayerDirection(x, y, player);
        double degrees = offset + aim;
        bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray()));
    }

    public void aimedBullet(double x, double y, double speed, int radius, double offset, Color state) {
        bulletSound();
        double aim = getPlayerDirection(x, y, player);
        double degrees = offset + aim;
        bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray(), state));
    }

    public void bullet(double x, double y, double speed, int radius, double degrees) {
        bulletSound();
        bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray()));
    }

    public void bullet(double x, double y, double speed, int radius, double degrees, Color state) {
        bulletSound();
        bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray(), state));
    }

    public void bulletLine(double startX, double endX, double startY, double endY, double speed, int radius, double degrees, int amount) {
        bulletSound();

        double x = startX;
        double y = startY;

        double differenceX = endX - startX;
        double differenceY = endY - startY;

        for (int i = 0; i < amount; i++) {
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray()));
            x += differenceX/(amount-1);
            y += differenceY/(amount-1);
        }
    }

    public void bulletLine(double startX, double endX, double startY, double endY, double speed, int radius, double degrees, int amount, Color state) {
        bulletSound();

        double x = startX;
        double y = startY;

        double differenceX = endX - startX;
        double differenceY = endY - startY;

        for (int i = 0; i < amount; i++) {
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray(), state));
            x += differenceX/(amount-1);
            y += differenceY/(amount-1);
        }
    }

    public void aimedBulletLine(double startX, double endX, double startY, double endY, double speed, int radius, double offset, int amount) {
        bulletSound();

        double x = startX;
        double y = startY;

        double differenceX = endX - startX;
        double differenceY = endY - startY;

        double aim = getPlayerDirection(x, y, player);
        double degrees = offset + aim;

        for (int i = 0; i < amount; i++) {
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray()));
            x += differenceX/(amount-1);
            y += differenceY/(amount-1);

            aim = getPlayerDirection(x, y, player);
            degrees = offset + aim;
        }
    }

    public void aimedBulletLine(double startX, double endX, double startY, double endY, double speed, int radius, double offset, int amount, Color state) {
        bulletSound();

        double x = startX;
        double y = startY;

        double differenceX = endX - startX;
        double differenceY = endY - startY;

        double aim = getPlayerDirection(x, y, player);
        double degrees = offset + aim;

        for (int i = 0; i < amount; i++) {
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray(), state));
            x += differenceX/(amount-1);
            y += differenceY/(amount-1);

            aim = getPlayerDirection(x, y, player);
            degrees = offset + aim;
        }
    }

    public void randomBulletLine(double startX, double endX, double y, double speed, int radius, double degrees, int amount) {
        bulletSound();

        double x;

        for (int i = 0; i < amount; i++) {
            x = Math.random()*(endX-startX)+startX;
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray()));
        }
    }

    public void randomBulletLine(double startX, double endX, double y, double speed, int radius, double degrees, int amount, Color state) {
        bulletSound();

        double x;

        for (int i = 0; i < amount; i++) {
            x = Math.random()*(endX-startX)+startX;
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius, spriteSheet.getBulletSpriteArray(), state));
        }
    }

    /*
    ------
    LEVELS
    ------
     */

    public void level1() {
        // 60 updates = 1 second
        switch(numberOfUpdates) {
            case 0:
            case 40:
            case 80:
            case 120:
            case 160:
            case 200:
            case 240:
            case 280:
            case 320:
            case 360:
            case 400:
                aimedBulletCircle(screenWidth/2, screenHeight/6, 5, 50, 20, 12);
                aimedBullet(screenWidth/2, screenHeight/6, 6,  60, 0);
                break;
            case 500:
            case 600:
            case 700:
                bullet(0, 0, 8,  120, 0);
                bullet(screenWidth/4, 0, 8, 120, 0);
                bullet(screenWidth - screenWidth/4, 0,  8, 120, 0);
                bullet(screenWidth, 0, 8,  120, 0);
                break;
            case 550:
                bullet(0, 0, 8,  120, 0);
                bullet(screenWidth/4, 0, 8,  120, 0);
                bullet(screenWidth/2, 0, 8,  120, 0);
                bullet(screenWidth, 0, 8,  120, 0);
                break;
            case 650:
                bullet(0, 0, 8,  120, 0);
                bullet(screenWidth/2, 0, 8,  120, 0);
                bullet(screenWidth - screenWidth/4, 0, 8,  120, 0);
                bullet(screenWidth, 0, 8, 120, 0);
                break;
            case 401:
            case 451:
            case 501:
            case 551:
            case 601:
            case 651:
            case 701:
                aimedBulletCircle(screenWidth/2, screenHeight/6, 5,  15, 30, 16);
                break;
            case 800:
                bulletCircle(screenWidth/2, screenHeight/6, 5,  100, 0, 32);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 6,  50, 30, 24);
                break;
            case 1200:
                levelEnd();
                break;
            case 1500:
                exitLevel();
                break;
        }
    }

    public void level2() {
        // 60 updates = 1 second
        switch(numberOfUpdates) {
            case 0:
            case 150:
            case 300:
            case 450:
            case 600:
            case 750:
                bulletLine(0, screenWidth, 0, 0, 8, 80, 0, 10, Color.BLUE);
                aimedBulletCircle(screenWidth/4, screenHeight/6, 10, 40, 0, 24, Color.BLUE);
                break;
            case 75:
            case 225:
            case 375:
            case 525:
            case 675:
            case 825:
                bulletLine(0, screenWidth, 0, 0, 8, 80, 0, 10, Color.RED);
                aimedBulletCircle(screenWidth - screenWidth/4, screenHeight/6, 10, 40, 0, 24, Color.RED);
                break;
            case 1000:
            case 1200:
            case 1400:
            case 1600:
            case 1800:
            case 2000:
                bulletLine(0, 0, 0, screenHeight, 5, 80, 90, 24, Color.RED);
                aimedBulletLine(0, screenWidth, 0, 0, 8, 60, 0, 8);
                break;
            case 1100:
            case 1300:
            case 1500:
            case 1700:
            case 1900:
            case 2100:
                bulletLine(screenWidth, screenWidth, 0, screenHeight, 5, 80, 270, 24, Color.BLUE);
                aimedBulletLine(0, screenWidth, 0, 0, 8, 60, 0, 8);
                break;
            case 2500:
                levelEnd();
                break;
            case 2800:
                exitLevel();
                break;
        }
    }

    public void level3() {
        switch(numberOfUpdates) {
            case 0:
            case 200:
            case 400:
            case 600:
            case 800:
                bulletLine(screenWidth, screenWidth, 0, screenHeight, 5, 100, 270, 8, Color.BLUE);
                bulletLine(screenWidth, screenWidth, 150, screenHeight+150, 5, 100, 270, 8);
                bulletCircle(screenWidth/4, screenHeight/6, 7, 50, 0, 16);
                aimedBulletCircle(screenWidth/4, screenHeight/6, 6, 50, 0, 12, Color.BLUE);
                break;
            case 100:
            case 300:
            case 500:
            case 700:
            case 900:
                bulletLine(screenWidth, screenWidth, 0, screenHeight, 5, 100, 270, 8);
                bulletLine(screenWidth, screenWidth, 150, screenHeight+150, 5, 100, 270, 8, Color.RED);
                bulletCircle(screenWidth - screenWidth/4, screenHeight/6, 7, 50, 0, 16);
                aimedBulletCircle(screenWidth - screenWidth/4, screenHeight/6, 6, 50, 0, 12, Color.RED);
                break;
            case 50:
            case 150:
            case 250:
            case 350:
            case 450:
            case 550:
            case 650:
            case 750:
            case 850:
            case 950:
                bulletCircle(screenWidth/2, screenHeight/6, 7, 50, 0, 8);
                break;
            case 1000:
            case 1100:
            case 1200:
            case 1300:
            case 1400:
            case 1500:
            case 1600:
            case 1700:
            case 1800:
            case 1900:
                aimedBulletLine(100, screenWidth-100, 0, 0, 5, 100, 0, 16, Color.BLUE);
                aimedBulletLine(100, screenWidth-100, 0, 0, 8, 100, 0, 16, Color.RED);
                aimedBulletCircle(screenWidth - screenWidth/4, screenHeight/6, 7, 70, 0, 16);
                aimedBulletCircle(screenWidth/4, screenHeight/6, 7, 70, 0, 16);
                break;
            case 1050:
            case 1150:
            case 1250:
            case 1350:
            case 1450:
            case 1550:
            case 1650:
            case 1750:
            case 1850:
            case 1950:
                bulletCircle(screenWidth/2, screenHeight/6, 7, 70, 0, 16);
                break;
            case 2100:
                bulletCircle(screenWidth/2, screenHeight/6, 5, 150, 0, 48, Color.RED);
                bulletCircle(screenWidth/2, screenHeight/6, 6, 150, 0, 32, Color.BLUE);
                break;
            case 2500:
                levelEnd();
                break;
            case 2800:
                exitLevel();
                break;
        }
    }

    public void level4() {
        switch(numberOfUpdates) {
            case 0:
            case 50:
            case 100:
            case 150:
            case 200:
            case 250:
            case 300:
            case 350:
            case 400:
            case 450:
            case 500:
            case 550:
            case 600:
            case 650:
            case 700:
            case 750:
            case 800:
            case 850:
            case 900:
            case 950:
            case 1000:
                aimedBulletCircle(screenWidth/4, screenHeight/6, 4,  30, 0, 32);
                aimedBulletCircle(screenWidth - screenWidth/4, screenHeight/6, 4,  30, 0, 32);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 5,  30, 0, 32);
                bulletLine(0, screenWidth, 0, 100, 5, 50, -10, 5, Color.BLUE);
                bulletLine(0, screenWidth, 100, 0, 5, 50, 10, 5, Color.RED);
                break;
            case 1200:
            case 1300:
            case 1400:
            case 1500:
            case 1600:
            case 1700:
            case 1800:
                aimedBulletCircle(screenWidth/4, screenHeight/6, 7, 50, 0, 32);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 8, 50, 0, 32);
                aimedBulletCircle(screenWidth/3, screenHeight/6, 8, 50, 0, 48, Color.RED);
                aimedBulletCircle(screenWidth/3, screenHeight/6, 6, 45, 0, 64, Color.RED);
                break;
            case 1250:
            case 1350:
            case 1450:
            case 1550:
            case 1650:
            case 1750:
            case 1850:
                aimedBulletCircle(screenWidth - screenWidth/4, screenHeight/6, 7, 50, 0, 32);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 8, 50, 0, 32);
                aimedBulletCircle(screenWidth - screenWidth/3, screenHeight/6, 8, 50, 0, 48, Color.BLUE);
                aimedBulletCircle(screenWidth - screenWidth/3, screenHeight/6, 6, 45, 0, 64, Color.BLUE);
                break;
            case 2200:
                levelEnd();
                break;
            case 2500:
                exitLevel();
                break;
        }
    }

    public void level5() {
        switch(numberOfUpdates) {
            case 0:
            case 100:
            case 200:
            case 300:
            case 400:
            case 500:
            case 600:
                bulletCircle(screenWidth/4, screenHeight/6, 7, 200, 0, 32, Color.RED);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 6, 50, 0, 32);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 7, 50, 0, 24);
                break;
            case 50:
            case 150:
            case 250:
            case 350:
            case 450:
            case 550:
            case 650:
                bulletCircle(screenWidth - screenWidth/4, screenHeight/6, 7, 200, 0, 32, Color.BLUE);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 6, 50, 0, 32);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 7, 50, 0, 24);
                break;
            case 700:
            case 800:
            case 900:
            case 1000:
            case 1100:
            case 1200:
                aimedBulletCircle(screenWidth/4, screenHeight/6, 4,  30, 0, 32, Color.BLUE);
                aimedBulletCircle(screenWidth - screenWidth/4, screenHeight/6, 4,  30, 0, 32, Color.BLUE);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 5,  30, 0, 32, Color.BLUE);
                bulletLine(0, screenWidth - screenWidth/4, 0, 0, 6, 100, 0, 20);
                break;
            case 750:
            case 850:
            case 950:
            case 1050:
            case 1150:
            case 1250:
                aimedBulletCircle(screenWidth/4, screenHeight/6, 4,  30, 0, 32, Color.RED);
                aimedBulletCircle(screenWidth - screenWidth/4, screenHeight/6, 4,  30, 0, 32, Color.RED);
                aimedBulletCircle(screenWidth/2, screenHeight/6, 5,  30, 0, 32, Color.RED);
                bulletLine(screenWidth/4, screenWidth, 0, 0, 6, 100, 0, 20);
                break;
            case 1500:
            case 1600:
            case 1700:
            case 1800:
            case 1900:
            case 2000:
            case 2100:
            case 2200:
                randomBulletLine(0, screenWidth, 0, 8, 50, 0, 12);
                randomBulletLine(0, screenWidth, 0, 9, 30, 0, 12);
                bulletLine(-1000, 500, 200, -1200, 7, 50, 20, 32, Color.BLUE);
                break;
            case 1550:
            case 1650:
            case 1750:
            case 1850:
            case 1950:
            case 2050:
            case 2150:
            case 2250:
                randomBulletLine(0, screenWidth, 0, 8, 50, 0, 12);
                randomBulletLine(0, screenWidth, 0, 9, 30, 0, 12);
                bulletLine(screenWidth+1000, screenWidth-500, 200, -1200, 7, 50, -20, 32, Color.RED);
                break;
            case 2700:
                levelEnd();
                break;
            case 3000:
                exitLevel();
                break;
        }
    }
}
