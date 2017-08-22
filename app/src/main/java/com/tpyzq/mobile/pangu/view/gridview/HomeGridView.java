package com.tpyzq.mobile.pangu.view.gridview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.tpyzq.mobile.pangu.R;

/**
 * Created by Admin on 2017/8/22.
 */

public class HomeGridView extends GridView {

    public HomeGridView(Context context) {
        super(context);
    }

    public HomeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        View localView1 = getChildAt(0);
        int column = getWidth() / localView1.getWidth();//列数
        int childCount = getChildCount();
        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setColor(getContext().getResources().getColor(R.color.lineColor));//这个就是设置分割线的颜色
        for (int i = 0; i < childCount; i++) {
            View cellView = getChildAt(i);

            if ((i + 1) < (childCount - (childCount % column))) {//最后一行的item
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            }


//            if ((i + 1) % column == 0) {//每一行最后一个
//
//                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//            } else if ((i + 1) > (childCount - (childCount % column))) {//最后一行的item
//                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
////                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
//            } else {
////                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
//                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
//            }
        }

    }
}
