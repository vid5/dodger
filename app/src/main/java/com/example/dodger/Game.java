package com.example.dodger;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
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
    final private GameLoop gameLoop;
    //final ConstraintLayout myLayout = (ConstraintLayout) findViewById(R.id.playArea);
    final private Player player;
    final private ArrayList<Bullet> bullets = new ArrayList<>();
    final private ArrayList<Level> levels = new ArrayList<>();
    final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean invincible = false;
    private int numberOfUpdates = 0;

    public Game(Context context) {
        super(context);

        // Get surface holder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        /*ImageView playerImage = new ImageView(getContext());
        playerImage.setImageResource(R.drawable.redcircle);

        myLayout.addView(playerImage)*/

        //player = new Player(playerImage, screenWidth/2-100, screenHeight/2-100, 100);
        player = new Player(getContext(), screenWidth/2, screenHeight - screenHeight/6, 100);

        levels.add(new Level(1, 30));


        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
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

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);
        drawTime(canvas);
        drawDamage(canvas);

        player.draw(canvas);
        for(Bullet i : bullets)
            i.draw(canvas);
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
        String time = Double.toString((levels.get(0).getLevelTime() - gameLoop.getTime())/1000.0);
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Time: " + time, 100, screenHeight-100, paint);
    }

    public void drawDamage(Canvas canvas) {
        String damage = Integer.toString(levels.get(0).getDamageTaken());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("Damage: " + damage, 100, screenHeight-200, paint);
    }

    public void update() {
        // Update game state
        player.update();
        level1(numberOfUpdates);
        for(Bullet i : bullets)
            i.update();
        player.invincibility(getContext(), invincible);
        if (invincible == false) {
            Iterator<Bullet> iteratorBullets = bullets.iterator();
            while (iteratorBullets.hasNext()) {
                if (isColliding((CircleBullet) iteratorBullets.next(), player)) {
                    invincible = true;
                    levels.get(0).increaseDamageTaken();
                    iteratorBullets.remove();
                    break;
                }
            }
        }
        numberOfUpdates++;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /*int x = (int) event.getX();
        int y = (int) event.getY();
        int leniency = 50;

        if ((x >= player.getX() - leniency && x <= (player.getX()+player.getRadius()*2) + leniency) && (y >= player.getY() - leniency && y <= (player.getY()+player.getRadius()*2) + leniency)) {
            if (x >= player.getRadius() * 2 && x <= screenWidth - player.getRadius() * 2) {
                player.setX(x - player.getRadius());
            }
            if (y >= player.getRadius() * 2 && y <= screenHeight - player.getRadius() * 2) {
                player.setY(y - player.getRadius());
            }
        }*/

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        int playerX = player.getX();
        int playerY = player.getY();
        int playerRadius = player.getRadius();

        int leniency = 50;

        if(touchX <= playerRadius) // Checks if the player is outside of screen edges
            touchX = playerRadius;
        else if(touchX >= screenWidth - playerRadius)
            touchX = screenWidth - playerRadius;
        if(touchY <= playerRadius)
            touchY = playerRadius;
        else if(touchY >= screenHeight - playerRadius)
            touchY = screenWidth - playerRadius;

        if (touchX >= (playerX - playerRadius) - leniency && // Checks if the player is being moved from their previous position
                touchX <= (playerX + playerRadius) + leniency &&
                touchY >= (playerY - playerRadius) - leniency &&
                touchY <= (playerY + playerRadius) + leniency
        ) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    player.setX(touchX);
                    player.setY(touchY);
                    return true;
            }
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
        double radiusDistance = bullet.getRadius()/2 + player.getRadius()/2.5;
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

    //Bullet functions
    public void bulletCircle(double x, double y, double speed, int radius, int amount, double offset) {
        double degrees;
        for(int i = 0; i < amount; i++) {
            degrees = (360/(double)amount)*i + offset;
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius));
        }
    }

    public void aimedBulletCircle(double x, double y, double speed, int radius, double offset, int amount) {
        double degrees;
        double aim = getPlayerDirection(x, y, player);
        for(int i = 0; i < amount; i++) {
            degrees = (360/(double)amount)*i + offset + aim;
            bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius));
        }
    }

    public void aimedBullet(double x, double y, double speed, int radius, double offset) {
        double aim = getPlayerDirection(x, y, player);
        double degrees = offset + aim;
        bullets.add(new CircleBullet(getContext(), x, y, degrees, speed, radius));
    }

    //Levels
    public void level1(int update) {
        // 60 updates = 1 second
        switch(update) {
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
                aimedBullet(screenWidth/2, screenHeight/6, 6, 60, 0);
                break;
        }
    }
}
