package com.example.dodger;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.zip.Adler32;

public class GameLoop extends Thread {
    private static final double MAX_UPS = 60.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;
    private boolean isRunning = false;
    private SurfaceHolder surfaceHolder;
    private Game game;
    private double averageUPS;
    private double averageFPS;
    private long time;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.game = game;
    }

    public double getAverageUPS(){
        return averageUPS;
    }

    public double getAverageFPS(){
        return averageFPS;
    }

    public long getTime() {
        return time;
    }



    public void startLoop(){
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        long startTime2 = System.currentTimeMillis();
        long startTime3 = System.currentTimeMillis();

        //Game loop
        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while(isRunning){

            // Try to update objects from the Game class
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.update();
                    game.draw(canvas);
                    updateCount++;
                }
            }
            catch(IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Pause the game loop to not exceed the target UPS
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long)(updateCount*UPS_PERIOD - elapsedTime);
            if(sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Skip frames to keep up with the target UPS
            while(sleepTime < 0 && updateCount < MAX_UPS-1) {
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long)(updateCount*UPS_PERIOD - elapsedTime);
            }

            // Calculate the average UPS and FPS
            elapsedTime = System.currentTimeMillis() - startTime;
            if(elapsedTime >= 1000) {
                averageUPS = updateCount / (1E-3 * elapsedTime);
                averageFPS = frameCount / (1E-3 * elapsedTime);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }

            time = System.currentTimeMillis() - startTime2;

            if (game.getInvincible()) {
                if (System.currentTimeMillis() - startTime3 >= 500) {
                    game.setInvincible(false);
                }
            }
            else {
                startTime3 = System.currentTimeMillis();
            }
        }
    }

    public void stopLoop() {
        isRunning = false;
        // Wait for thread to join
        try {
            join();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
