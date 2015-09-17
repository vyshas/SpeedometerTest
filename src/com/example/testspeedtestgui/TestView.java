package com.example.testspeedtestgui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class TestView extends View {

    private Paint outerLogoPaint;
    private Paint centerOuterPaint;
    private Path outerLogoEdge;


    public TestView(Context context) {
        super(context);
        init(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    private void init(Context context) {
        if(Build.VERSION.SDK_INT >= 11){
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        initDrawingTools();

    }

    private void initDrawingTools() {

        float strokeWidth=0.0f;
        centerOuterPaint=new Paint();
        centerOuterPaint.setAntiAlias(true);
        centerOuterPaint.setColor(Color.BLUE);
        centerOuterPaint.setStrokeWidth(strokeWidth);
        centerOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        centerOuterPaint.setStyle(Paint.Style.STROKE);

        RectF rect = new RectF();
        float angle = getSemicircle(0.025f,0.5f,0.975f,0.5f,rect);
        outerLogoEdge = new Path();
        outerLogoEdge.moveTo(0.025f, 0.495f);
        outerLogoEdge.arcTo(rect, angle, 180);
        outerLogoEdge.moveTo(0.025f, 0.495f);
        outerLogoEdge.lineTo(0.2f, 0.495f);
        //Edge surrounding the lower part of outer semi circle(Logo edge Init) Logo edge Init
        angle = getSemicircle(0.20f,0.5f,0.80f,0.5f,rect);
        outerLogoEdge.arcTo(rect, angle, 180);
        outerLogoEdge.moveTo(0.975f, 0.495f);
        outerLogoEdge.lineTo(0.8f, 0.495f);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        float scale = getWidth();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale, scale);
        drawLogo(canvas);
        canvas.restore();

    }

    private void drawLogo(Canvas canvas) {

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.drawPath(outerLogoEdge, centerOuterPaint);
        canvas.restore();
    }


    public float getSemicircle(float xStart, float yStart, float xEnd,
            float yEnd, RectF ovalRectOUT) {

        float centerX = xStart + ((xEnd - xStart) / 2);
        float centerY = yStart + ((yEnd - yStart) / 2);

        double xLen = (xEnd - xStart);
        double yLen = (yEnd - yStart);
        float radius = (float) (Math.sqrt(xLen * xLen + yLen * yLen) / 2);

        RectF oval = new RectF(centerX - radius,
                centerY - radius, centerX + radius,
                centerY + radius);

        ovalRectOUT.set(oval);

        double radStartAngle = 0;
        radStartAngle = Math.atan2(yStart - centerY, xStart - centerX);
        float startAngle = (float) Math.toDegrees(radStartAngle);

        return startAngle;

    }


}
