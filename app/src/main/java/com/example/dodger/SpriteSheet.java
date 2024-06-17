package com.example.dodger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class SpriteSheet {
    private Bitmap bitmap;

    public SpriteSheet(Context context) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spirtesheet, bitmapOptions);
    }

    public Sprite[] getPlayerSpriteArray() {
        Sprite[] spriteArray = new Sprite[8];
        spriteArray[0] = new Sprite(this, new Rect(0*256, 0*256, 1*256, 1*256));
        spriteArray[1] = new Sprite(this, new Rect(1*256, 0*256, 2*256, 1*256));
        spriteArray[2] = new Sprite(this, new Rect(2*256, 0*256, 3*256, 1*256));
        spriteArray[3] = new Sprite(this, new Rect(3*256, 0*256, 4*256, 1*256));
        spriteArray[4] = new Sprite(this, new Rect(0*256, 1*256, 1*256, 2*256));
        spriteArray[5] = new Sprite(this, new Rect(1*256, 1*256, 2*256, 2*256));
        spriteArray[6] = new Sprite(this, new Rect(2*256, 1*256, 3*256, 2*256));
        spriteArray[7] = new Sprite(this, new Rect(3*256, 1*256, 4*256, 2*256));
        return spriteArray;
    }

    public Sprite[] getBulletSpriteArray() {
        Sprite[] spriteArray = new Sprite[3];
        spriteArray[0] = new Sprite(this, new Rect(0*256, 3*256, 1*256, 4*256));
        spriteArray[1] = new Sprite(this, new Rect(1*256, 3*256, 2*256, 4*256));
        spriteArray[2] = new Sprite(this, new Rect(2*256, 3*256, 3*256, 4*256));
        return spriteArray;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
