package com.example.dodger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

public abstract class Bullet {
    protected double x;
    protected double y;
    protected double degrees;
    protected double speed;
    protected Paint paint;
    protected int color;
    protected Color state;
    protected Sprite sprite;

    public Bullet(Context context, double x, double y, double degrees, double speed, Sprite[] spriteArray) {
        this.x = x;
        this.y = y;
        this.degrees = degrees;
        this.speed = speed;
        this.state = Color.PURPLE;
        this.color = ContextCompat.getColor(context, R.color.purple_200);
        this.sprite = spriteArray[2];
    }

    public Bullet(Context context, double x, double y, double degrees, double speed, Sprite[] spriteArray, Color state) {
        this.x = x;
        this.y = y;
        this.degrees = degrees;
        this.speed = speed;
        this.state = state;

        if (state == Color.RED) {
            this.color = ContextCompat.getColor(context, R.color.red);
            this.sprite = spriteArray[0];
        }
        else {
            this.color = ContextCompat.getColor(context, R.color.blue);
            this.sprite = spriteArray[1];
        }
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getState() {
        return this.state;
    }

    abstract public void draw(Canvas canvas);

    public void update() {
        move();
    }

    public void move(){
        double radians = Math.toRadians(degrees);
        double directionX = Math.sin(radians);
        double directionY = Math.cos(radians);

        x += directionX * speed;
        y += directionY * speed;
    }
}
