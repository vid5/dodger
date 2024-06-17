package com.example.dodger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

public class CircleBullet extends Bullet {
    private int radius;

    public CircleBullet(Context context, double x, double y, double degrees, double speed, int radius, Sprite[] spriteArray) {
        super(context, x, y, degrees, speed, spriteArray);
        this.radius = radius;

        paint = new Paint();
        paint.setColor(color);
    }

    public CircleBullet(Context context, double x, double y, double degrees, double speed, int radius, Sprite[] spriteArray, Color state) {
        super(context, x, y, degrees, speed, spriteArray, state);
        this.radius = radius;

        paint = new Paint();
        paint.setColor(color);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawCircle((float)x, (float)y, radius, paint);
        sprite.draw(canvas, (int)x - radius, (int)y - radius, radius);
    }
}
