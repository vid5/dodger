package com.example.dodger;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
    SpriteSheet spriteSheet;
    Rect rect;

    public Sprite(SpriteSheet spriteSheet, Rect rect) {
        this.spriteSheet = spriteSheet;
        this.rect = rect;
    }

    public void draw(Canvas canvas, int x, int y, int radius) {
        canvas.drawBitmap(spriteSheet.getBitmap(), rect, new Rect(x, y, x+radius*2, y+radius*2), null);
    }
}
