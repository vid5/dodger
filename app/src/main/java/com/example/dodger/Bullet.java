package com.example.dodger;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

public abstract class Bullet {
    protected ImageView image;
    protected double x;
    protected double y;
    protected double degrees;
    protected double speed;
    protected Paint paint;

    public Bullet(ImageView image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;

        this.image.setX((int)x);
        this.image.setY((int)y);
    }

    public Bullet(double x, double y, double degrees, double speed) {
        this.x = x;
        this.y = y;
        this.degrees = degrees;
        this.speed = speed;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        //this.image.setX((int)x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        //this.image.setY((int)y);
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
