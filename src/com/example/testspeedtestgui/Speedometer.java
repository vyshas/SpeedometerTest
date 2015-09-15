package com.example.testspeedtestgui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;



public final class Speedometer extends View{
    private final float SMOOTHING_VALUE = 10.0f;
    private Paint outerLogoPaint;
    private Paint centerPaint,centerOuterPaint;
    private Path outerLogo;
    private Path center;
    private Path outerLogoEdge;


    private Paint handPaint;
    private Path handPath;

    private float mSpeed;
    private float mNeedleAngle;
    RectF rectOuter;

    int appBG;

    public Speedometer(Context context) {
        super(context);
        init(context);
    }

    public Speedometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Speedometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        if(Build.VERSION.SDK_INT >= 11){
            this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //appBG = context.getResources().getColor(R.color.appBackground);
        // appBG = ContextCompat.getColor(context,R.color.appBackground);
        initDrawingTools();
        mSpeed = 0f;
        mNeedleAngle = 0f;
    }

    private void initDrawingTools() {
        ///Outer Logo init
        /* int leftHandSide = getResources().getColor(R.color.Speedometer_LeftHandSide);
        int rightHandSide = getResources().getColor(R.color.Speedometer_RightHandSide);*/
        int leftHandSide = ContextCompat.getColor(getContext(),R.color.Speedometer_LeftHandSide);
        int rightHandSide = ContextCompat.getColor(getContext(),R.color.Speedometer_RightHandSide);

        /**
         * get inner and outer stroke width from resources
         */
        TypedValue typedValue = new TypedValue();
        getResources().getValue(R.dimen.speedometer_innerBorderStrokeWidth, typedValue, true);
        float innerStrokeWidth = typedValue.getFloat();
        getResources().getValue(R.dimen.speedometer_outerBorderStrokeWidth, typedValue, true);
        float outerStrokeWidth = typedValue.getFloat();

        int[] colors = {leftHandSide, rightHandSide};
        float[] positions = {0.65f,1.0f};
        Shader gradient = new SweepGradient (0.5f,0.7f, colors, positions);
        outerLogoPaint = new Paint();
        outerLogoPaint.setAntiAlias(true);
        outerLogoPaint.setStrokeWidth(innerStrokeWidth);
        outerLogoPaint.setShader(gradient);
        outerLogoPaint.setStrokeCap(Paint.Cap.ROUND);
        outerLogoPaint.setStyle(Paint.Style.STROKE);
        RectF rect = new RectF();

        float angle = getSemicircle(0.115f,0.495f,0.885f,0.495f,rect);
        outerLogo = new Path();
        outerLogo.addArc(rect, angle, 180);

        //Edge surrounding the upper part of the outer semi circle(Logo edge Init)
        centerOuterPaint = new Paint();
        centerOuterPaint.setAntiAlias(true);
        centerOuterPaint.setColor(Color.BLUE);
        centerOuterPaint.setStrokeCap(Paint.Cap.ROUND);
        centerOuterPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        centerOuterPaint.setStrokeWidth(outerStrokeWidth);


        angle = getSemicircle(0.025f,0.5f,0.975f,0.5f,rect);
        outerLogoEdge = new Path();
        outerLogoEdge.addArc(rect, angle, 180);

        /* outerLogoEdge.moveTo(0.025f, 0.495f);
        outerLogoEdge.arcTo(rect, angle, 180);
        outerLogoEdge.moveTo(0.025f, 0.495f);
        outerLogoEdge.lineTo(0.2f, 0.495f);*/

        //Edge/border covering the lower part of outer semi circle
        /*   angle = getSemicircle(0.20f,0.5f,0.80f,0.5f,rect);
        Log.d("Speedometer","Angle:"+angle );
        outerLogoEdge.arcTo(rect, angle, 180);
        outerLogoEdge.moveTo(0.975f, 0.495f);
        outerLogoEdge.lineTo(0.8f, 0.495f);*/

        ///Center init
        centerPaint = new Paint();
        centerPaint.setAntiAlias(true);
        centerPaint.setColor(ContextCompat.getColor(getContext(),R.color.Speedometer_CenterPaintColor));
        centerPaint.setStyle(Paint.Style.STROKE);
        centerPaint.setStrokeWidth(outerStrokeWidth);
        angle = getSemicircle(0.40f,0.5f,0.60f,0.5f,rect);
        center = new Path();
        center.addArc(rect, angle, 180);

        ///Hand Init
        handPaint = new Paint();
        handPaint.setAntiAlias(true);
        handPaint.setColor(ContextCompat.getColor(getContext(),R.color.Speedometer_handPaintColor));
        handPaint.setStyle(Paint.Style.FILL);

        handPath = new Path();
        //draw main hand
        handPath.moveTo(0.48f, 0.48f);
        handPath.lineTo(0.5f, 0.1f);
        handPath.lineTo(0.52f, 0.48f);
        //draw hand end
        handPath.lineTo(0.5f, 0.5f);
        handPath.lineTo(0.48f, 0.48f);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize);
        int chosenHeight = chooseDimension(heightMode, heightSize);

        int chosenDimension = Math.min(chosenWidth, chosenHeight);

        setMeasuredDimension(chosenDimension,(int)( chosenDimension/2.0f));
    }

    private int chooseDimension(int mode, int size) {
        return size;
    }

    private void drawLogo(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);

        //outer semi circle
        canvas.drawPath(outerLogo, outerLogoPaint);
        //centerPaint.setStyle(Paint.Style.STROKE);

        //inner semi circle
        canvas.drawPath(center, centerPaint);
        centerPaint.setStyle(Paint.Style.FILL);


        //edge surrounding the outer semi circle
        canvas.drawPath(outerLogoEdge, centerOuterPaint);
        centerOuterPaint.setStyle(Paint.Style.STROKE);


        //canvas.drawArc(rectOuter, 180,180, false, centerPaint);
        canvas.restore();
    }

    private void drawHand(Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.rotate(-90.0f + mNeedleAngle, 0.5f, 0.48f);
        canvas.drawPath(handPath, handPaint);
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        float scale = getWidth();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(scale, scale);

        drawLogo(canvas);
        drawHand(canvas);

        canvas.restore();
        moveHand();

    }


    private boolean handNeedsToMove(float angle) {
        return  (Math.abs(mNeedleAngle - angle) > 0.1f);
    }


    private void moveHand() {
        // if we are near the current hand angle do nothing

        if (mSpeed == 0) mSpeed = 0.01f;
        float angle = (float)(60.7f * (Math.log10(mSpeed) + 1.55f));
        if(angle > 179f) angle = 179.0f;
        if(angle < 1f) angle = 1.0f;

        if(Math.abs(mNeedleAngle - angle) > 0.7f) {
            mNeedleAngle = (((SMOOTHING_VALUE - 1f) / SMOOTHING_VALUE) * mNeedleAngle) +
                    ((1f / SMOOTHING_VALUE) * angle);
        } else {
            mNeedleAngle = angle;
        }
        // if we dont need to move dont invalidate the screen
        if (! handNeedsToMove(angle)) {
            return;
        }

        invalidate();

    }

    // this is called periodically form the tests at a high interval
    // target is a percentage
    public void setHandTarget(float Target) {
        mSpeed = Target;
        moveHand();
    }

    public float getSemicircle(float xStart, float yStart, float xEnd,
            float yEnd, RectF ovalRectOUT) {

        float centerX = xStart + ((xEnd - xStart) / 2);
        float centerY = yStart + ((yEnd - yStart) / 2);

        double xLen = (xEnd - xStart);
        double yLen = (yEnd - yStart);
        float radius = (float) (Math.sqrt(xLen * xLen + yLen * yLen) / 2);

        RectF oval = new RectF(centerX - radius,centerY - radius, centerX + radius,centerY + radius);

        ovalRectOUT.set(oval);

        double radStartAngle = 0;
        radStartAngle = Math.atan2(yStart - centerY, xStart - centerX);
        float startAngle = (float) Math.toDegrees(radStartAngle);

        return startAngle;

    }
}
