package com.example.dodger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

public class Player{
    private ImageView image;
    private Paint paint;
    private Paint hitboxPaint;
    private int color;
    private int hitboxColor;
    private int x;
    private int y;
    private int radius;

    public Player(ImageView image, int x, int y, int radius) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.radius = radius;

        this.image.setX(this.x);
        this.image.setY(this.y);
        this.image.getLayoutParams().height = this.radius*2;
        this.image.getLayoutParams().width = this.radius*2;
    }

    public Player(Context context, int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;

        paint = new Paint();
        hitboxPaint = new Paint();
        color = ContextCompat.getColor(context, R.color.white);
        hitboxColor = ContextCompat.getColor(context, R.color.green);
        paint.setColor(color);
        hitboxPaint.setColor(hitboxColor);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
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
        canvas.drawCircle((float)x, (float)y, radius, paint);
        canvas.drawCircle((float)x, (float)y, radius/3, hitboxPaint);
    }

    public void update() {

    }

    public void invincibility(Context context, boolean b) {
        if (b) {
            color = ContextCompat.getColor(context, R.color.blue);
        }
        else {
            color = ContextCompat.getColor(context, R.color.white);
        }
    }
}
