package com.example.dodger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

public class Player{
    private Paint paint;
    private Paint hitboxPaint;
    private int color;
    private int hitboxColor;
    private int x;
    private int y;
    private int radius;
    private Color state;
    private Sprite[] spriteArray;
    private Game game;

    public Player(Context context, int x, int y, int radius, Sprite[] spriteArray, Game game) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.spriteArray = spriteArray;
        this.game = game;

        paint = new Paint();
        hitboxPaint = new Paint();
        color = ContextCompat.getColor(context, R.color.white);
        hitboxColor = ContextCompat.getColor(context, R.color.green);
        paint.setColor(color);
        hitboxPaint.setColor(hitboxColor);

        state = Color.RED;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        //this.image.setX(this.x);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        //this.image.setY(this.y);
    }

        public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        /*this.image.getLayoutParams().height = this.radius*2;
        this.image.getLayoutParams().width = this.radius*2;*/
    }

    public void draw(Canvas canvas) {
        paint.setColor(color);
        int offset = 0;
        if (state == Color.BLUE)
            offset = 4;
        else
            offset = 0;
        if(game.getInvincible() == false) {
            spriteArray[game.getNumberOfUpdates() / 3 % 4 + offset].draw(canvas, (int) x - radius, (int) y - radius, radius);
            canvas.drawCircle((float) x, (float) y, radius / 5, hitboxPaint);
        }
        else if(game.getNumberOfUpdates()%8 >= 4) {
            spriteArray[game.getNumberOfUpdates() / 3 % 4 + offset].draw(canvas, (int) x - radius, (int) y - radius, radius);
            canvas.drawCircle((float) x, (float) y, radius / 5, hitboxPaint);
        }
    }

    public void update() {

    }

    public void switchState(Context context) {
        if (state == Color.RED) {
            state = Color.BLUE;
            color = ContextCompat.getColor(context, R.color.blue);
        }
        else {
            state = Color.RED;
            color = ContextCompat.getColor(context, R.color.red);
        }
    }

    public Color getState() {
        return this.state;
    }
}
