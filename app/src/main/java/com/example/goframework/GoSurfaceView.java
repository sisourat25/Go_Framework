package com.example.goframework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

import com.example.GameFramework.utilities.FlashSurfaceView;
import com.example.goframework.GoGameState;

public class GoSurfaceView extends FlashSurfaceView {

    protected GoGameState state;
    private int EMPTY = -1;
    private int WHITE = -2;
    private int BLACK = -3;
    private int WHITE_IN_PERIL = -4;
    private int BLACK_IN_PERIL = -5;
    public float pixelDelta;
    public GoSurfaceView(Context context) {
        super(context);
        init();
    }
    public GoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void setState(GoGameState state) {
        this.state = state;
    }

    public void onDraw(Canvas g) {
        init();
        pixelDelta = pixelRatio(g);
        drawGrid(g,pixelDelta);
        if (state == null) {
            return;
        }
        drawStones(g,pixelDelta);
    }

    public void init() {
        setBackgroundColor(Color.parseColor("#E6D2B4"));
        invalidate();
    }
    Point translateToIndex(Point pos) {
        Log.d("tag","pixelDelta:"+pixelDelta);
        Log.d("tag","x:"+pos.x);
        Log.d("tag","y:"+pos.y);
        int x = -1;
        int y = -1;
        for (float i = 0; i < 9; i++) {
            if (pos.x >= ((pixelDelta*(i+1)-pixelDelta/2F))-pixelDelta/2F && pos.x <= ((pixelDelta*(i+1))-pixelDelta/2F) +pixelDelta/2F) {
                y = (int)i;
            }
        }
        for (float j = 0; j < 9; j++) {
            if (pos.y >= ((pixelDelta*(j+1)-pixelDelta/2F))-pixelDelta/2F && pos.y <= ((pixelDelta*(j+1))-pixelDelta/2F)+pixelDelta/2F) {
                x = (int)j;
            }
        }
        if (x == -1 || y == -1) {
            return null;
        }
        else {
            state.setX(x);
            state.setY(y);
            return new Point(x, y);
        }
    }
    public void drawGrid(Canvas g, float pixelDelta) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10f); // Set the stroke width to 10 pixels
        float boardLength = 9;

        // Load the bitmap from the resources
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.wooden_board_background);

        // Draw the bitmap as the background
        g.drawBitmap(background, null, new RectF(0, 0, getWidth(), getHeight()), null);

        for (float i = 0; i < 9; i++) {
            g.drawLine(pixelDelta-pixelDelta/2, (pixelDelta * (i + 1))-pixelDelta/2,
                    (pixelDelta * boardLength)-pixelDelta/2, (pixelDelta * (i + 1))-pixelDelta/2,paint);
        }
        paint.setColor(Color.BLACK);
        for (float j = 0; j < 9; j++) {

            g.drawLine((pixelDelta * (j + 1))-pixelDelta/2, pixelDelta-pixelDelta/2,
                    (pixelDelta * (j + 1))-pixelDelta/2, (pixelDelta * (boardLength))-pixelDelta/2,paint);
        }
    }
    public void drawStones(Canvas g, float pixelDelta) {
        Paint paint = new Paint();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(state.getGameBoard(i, j) == WHITE) {
                    paint.setColor(Color.WHITE);
                    g.drawArc(pixelDelta / 2 + ((pixelDelta * j)-pixelDelta/2),
                            pixelDelta / 2 + ((pixelDelta * i)-pixelDelta/2),
                            3 * pixelDelta / 2 + ((pixelDelta * j)-pixelDelta/2),
                            3 * pixelDelta / 2 + ((pixelDelta * i)-pixelDelta/2),
                            0F,
                            360F,
                            false,
                            paint);
                }
                else if(state.getGameBoard(i, j) == BLACK) {
                    paint.setColor(Color.BLACK);
                    g.drawArc(pixelDelta / 2 + ((pixelDelta * j)-pixelDelta/2),
                            pixelDelta / 2 + ((pixelDelta * i)-pixelDelta/2),
                            3 * pixelDelta / 2 + ((pixelDelta * j)-pixelDelta/2),
                            3 * pixelDelta / 2 + ((pixelDelta * i)-pixelDelta/2),
                            0F,
                            360F,
                            false,
                            paint);
                }
            }
        }
    }
    public float pixelRatio(Canvas canvas) {
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        int xNeed = 9;
        int yNeed = 9;
        return Math.min(w / xNeed, h / yNeed);
    }


}
