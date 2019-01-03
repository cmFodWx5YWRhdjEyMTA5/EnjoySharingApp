package enjoysharing.enjoysharing.AdapterObject;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TableRow;

import enjoysharing.enjoysharing.Activity.BaseActivity;

public class ActivitySwipeDetector implements View.OnTouchListener {

    private BaseActivity activity;
    private int originalWidth, originalMarginLeft, originalMarginRight;
    private boolean rightOk = false, leftOk = false;  // True if left/right swipe finish (move 200/400)
    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private float deltaXRightSum, deltaXLeftSum;

    public void SetOriginals(View v) {
        TableRow.LayoutParams originalMargins = (TableRow.LayoutParams)v.getLayoutParams();
        originalWidth = v.getWidth();  // 0
        originalMarginLeft = originalMargins.leftMargin;  // 0
        originalMarginRight = originalMargins.rightMargin;  // 0
    }

    public ActivitySwipeDetector(BaseActivity activity){
        this.activity = activity;
        deltaXRightSum = 0;
    }

    public void onRightSwipe(float deltaX, View v){
        if(originalWidth == 0)
            originalWidth = v.getWidth();
        deltaX = -deltaX;
        TableRow.LayoutParams params = (TableRow.LayoutParams)v.getLayoutParams();
        int limit = leftOk ? 400 : 200;
        if(!rightOk)
        {
            deltaXRightSum += deltaX;
            int rightMargin = params.rightMargin;
            int leftMargin = params.leftMargin;
            if(leftOk && rightMargin != 0)
            {
                rightMargin -= deltaXRightSum;
                if(rightMargin <= 0) rightMargin = 0;
            }
            if(rightMargin == 0){
                leftMargin = (int)(params.leftMargin+(deltaXRightSum-(limit-200)));
            }
            int w = (int) (v.getWidth()-deltaXRightSum);
            if(leftOk)
            {
                if(deltaXRightSum < 200)
                    w = (int) (v.getWidth()+deltaXRightSum);
                else
                    w = (int) (v.getWidth()-(deltaXRightSum-200));
            }
            if(deltaXRightSum >= limit) {
                activity.onRightSwipe(v);
                rightOk = true;
                leftOk = false;
                leftMargin = 200;
            }
            params.setMargins(leftMargin, params.topMargin, rightMargin, params.bottomMargin);
            params.width = w;
            v.setLayoutParams(params);
        }
    }

    public void onLeftSwipe(float deltaX, View v){
        if(originalWidth == 0)
            originalWidth = v.getWidth();
        TableRow.LayoutParams params = (TableRow.LayoutParams)v.getLayoutParams();
        int limit = rightOk ? 400 : 200;
        if(!leftOk)
        {
            deltaXLeftSum += deltaX;
            int rightMargin = params.rightMargin;
            int leftMargin = params.leftMargin;
            if(rightOk && leftMargin != 0)
            {
                leftMargin -= deltaXLeftSum;
                if(leftMargin <= 0){
                    leftMargin = 0;
                }
            }
            if(leftMargin == 0){
                rightMargin = (int)(params.rightMargin+(deltaXLeftSum-(limit-200)));
            }
            int w = (int) (v.getWidth()-deltaXLeftSum);
            if(rightOk)
            {
                if(deltaXLeftSum < 200)
                    w = (int) (v.getWidth()+deltaXLeftSum);
                else
                    w = (int) (v.getWidth()-(deltaXLeftSum-200));
            }
            if(deltaXLeftSum >= limit) {
                activity.onLeftSwipe(v);
                leftOk = true;
                rightOk = false;
                rightMargin = 200;
            }
            params.setMargins(leftMargin, params.topMargin, rightMargin, params.bottomMargin);
            params.width = w;
            v.setLayoutParams(params);
        }
    }

    public void onDownSwipe(){ }

    public void onUpSwipe(){ }

    protected void ResetWidth(View v) {
        TableRow.LayoutParams params = (TableRow.LayoutParams)v.getLayoutParams();
        int marginL = rightOk ? originalMarginLeft + 200 : originalMarginLeft;
        int marginR = leftOk ? originalMarginRight + 200 : originalMarginRight;
        int w = rightOk || leftOk ? originalWidth - 200 : originalWidth;
        params.setMargins(marginL, params.topMargin, marginR, params.bottomMargin);
        params.width = w;
        v.setLayoutParams(params);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if(Math.abs(deltaX) > Math.abs(deltaY))
                {
                    if(Math.abs(deltaX) > MIN_DISTANCE){
                        // left or right
                        if(deltaX < 0) { this.onRightSwipe(deltaX,v); return true; }
                        if(deltaX > 0) { this.onLeftSwipe(deltaX,v); return true; }
                    }
                    else {
                        return false; // We don't consume the event
                    }
                }
                // swipe vertical?
                else
                {
                    if(Math.abs(deltaY) > MIN_DISTANCE){
                        // top or down
                        if(deltaY < 0) { this.onDownSwipe(); return true; }
                        if(deltaY > 0) { this.onUpSwipe(); return true; }
                    }
                    else {
                        return false; // We don't consume the event
                    }
                }

                return true;
            }
            default: {
                if(!rightOk)
                {
                    ResetWidth(v);
                    deltaXRightSum = 0;
                }
                else if(!leftOk)
                {
                    ResetWidth(v);
                    deltaXLeftSum = 0;
                }
                return true;
            }
        }
    }

}