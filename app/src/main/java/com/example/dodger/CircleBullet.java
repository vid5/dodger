package com.example.dodger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

public class CircleBullet extends Bullet {
    private int radius;

    public CircleBullet(ImageView image, double x, double y, int radius) {
        super(image, x, y);
        this.radius = radius;

        super.getImage().getLayoutParams().height = this.radius*2;
        super.getImage().getLayoutParams().width = this.radius*2;
    }

    public CircleBullet(Context context, double x, double y, double degrees, double speed, int radius) {
        super(x, y, degrees, speed);
        this.radius = radius;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.red);
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
        canvas.drawCircle((float)x, (float)y, radius, paint);
    }
}
