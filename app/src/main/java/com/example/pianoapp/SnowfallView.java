package com.example.pianoapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SnowfallView extends View {

    private final List<Snowflake> snowflakes = new ArrayList<>();
    private final Random random = new Random();
    private final Paint paint = new Paint();
    private static final int MAX_SNOWFLAKES = 20; // Maximum number of snowflakes

    public SnowfallView(Context context) {
        super(context);
        init();
    }

    public SnowfallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Snowflake snowflake : snowflakes) {
            drawSnowflake(canvas, snowflake.getX(), snowflake.getY(), snowflake.getSize());
        }
    }

    private void drawSnowflake(Canvas canvas, int x, int y, int size) {
        int branches = 6; // Number of branches in the snowflake
        float branchLength = size * 1.5f;

        for (int i = 0; i < branches; i++) {
            float angle = (float) (i * 2 * Math.PI / branches);
            float x1 = x + size * (float) Math.cos(angle);
            float y1 = y + size * (float) Math.sin(angle);

            float x2 = x + branchLength * (float) Math.cos(angle);
            float y2 = y + branchLength * (float) Math.sin(angle);

            canvas.drawLine(x, y, x2, y2, paint);
            canvas.drawLine(x2, y2, x1, y1, paint);
        }
    }


    public void addSnowflake() {
        if (snowflakes.size() < MAX_SNOWFLAKES) {
            int size = random.nextInt(10) + 6; // Random size between 6 and 15
            int x = random.nextInt(getWidth());
            int y = -size; // Start above the view

            snowflakes.add(new Snowflake(x, y, size));
        }
    }

    public void moveSnowflakes() {
        Iterator<Snowflake> iterator = snowflakes.iterator();
        while (iterator.hasNext()) {
            Snowflake snowflake = iterator.next();
            snowflake.setY(snowflake.getY() + snowflake.getSpeed());

            // If snowflake is out of bounds, remove it
            if (snowflake.getY() > getHeight()) {
                iterator.remove();
            }
        }

        addSnowflake(); // Add a new snowflake if there's room
        invalidate();   // Redraw the view
    }

    private static class Snowflake {
        private int x;
        private int y;
        private int size;
        private int speed;

        public Snowflake(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = size / 4; // Adjust speed based on size
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSize() {
            return size;
        }

        public int getSpeed() {
            return speed;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
