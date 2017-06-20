package com.tpyzq.mobile.pangu.interfac;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by zhangwenbo on 2016/8/13.
 */
public class CalendarDecorator implements DayViewDecorator {
    private int color;
    private int radius;
    private HashSet<CalendarDay> dates;

    public CalendarDecorator(int color, int radius, Collection<CalendarDay> dates) {
        this.color = color;
        this.radius = radius;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(radius, color));
    }


    private class DotSpan implements LineBackgroundSpan {
        /**
         * Default radius used
         */
        public static final float DEFAULT_RADIUS = 3;

        private final float radius;
        private final int color;

        /**
         * Create a span to draw a dot using default radius and color
         *
         * @see #DEFAULT_RADIUS
         */
        public DotSpan() {
            this.radius = DEFAULT_RADIUS;
            this.color = 0;
        }


        /**
         * Create a span to draw a dot using a specified color
         *
         * @param color color of the dot
         * @see #DotSpan(float, int)
         * @see #DEFAULT_RADIUS
         */
        public DotSpan(int color) {
            this.radius = DEFAULT_RADIUS;
            this.color = color;
        }

        /**
         * Create a span to draw a dot using a specified radius
         *
         * @param radius radius for the dot
         * @see #DotSpan(float, int)
         */
        public DotSpan(float radius) {
            this.radius = radius;
            this.color = 0;
        }

        /**
         * Create a span to draw a dot using a specified radius and color
         *
         * @param radius radius for the dot
         * @param color  color of the dot
         */
        public DotSpan(float radius, int color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void drawBackground(
                Canvas canvas, Paint paint,
                int left, int right, int top, int baseline, int bottom,
                CharSequence charSequence,
                int start, int end, int lineNum
        ) {
            int oldColor = paint.getColor();
            if (color != 0) {
                paint.setColor(color);
            }

            paint.setStyle(Paint.Style.STROKE);//填充样式改为描边
            paint.setStrokeWidth(3);

            canvas.drawCircle((left + right) / 2, (bottom + top)/2 , radius, paint);
            paint.setColor(oldColor);
        }

    }
}
