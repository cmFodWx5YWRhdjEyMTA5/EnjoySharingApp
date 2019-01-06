package enjoysharing.enjoysharing.AdapterObject;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TableRow;
import enjoysharing.enjoysharing.Activity.BaseActivity;

public class ActivitySwipeDetector implements View.OnTouchListener {

    private BaseActivity activity;
    private int originalWidth, originalMarginLeft, originalMarginRight;
    private boolean rightOk = false, leftOk = false;  // True if left/right swipe finish (move 100/200)
    static final int MIN_DISTANCE = 50, MAX_MARING_LEFT = 60, MAX_MARGIN_RIGHT = 100;
    private float downX, downY, upX, upY;
    private float deltaXRightSum, deltaXLeftSum;
    protected boolean canManageList;

    public void SetOriginals(View v, boolean canManageList) {
        TableRow.LayoutParams originalMargins = (TableRow.LayoutParams)v.getLayoutParams();
        originalWidth = v.getWidth();  // 0
        originalMarginLeft = originalMargins.leftMargin;  // 0
        originalMarginRight = originalMargins.rightMargin;  // 0
        this.canManageList = canManageList;
    }

    public void SetAccepted(View v)
    {
        leftOk = true;
        rightOk = false;
        if(originalWidth == 0)
            originalWidth = v.getWidth();
        ResetWidth(v);
    }

    public void SetDecined(View v)
    {
        leftOk = false;
        rightOk = true;
        if(originalWidth == 0)
            originalWidth = v.getWidth();
        ResetWidth(v);
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
        int limit = leftOk ? MAX_MARING_LEFT+100 : MAX_MARING_LEFT;
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
                leftMargin = (int)(params.leftMargin+(deltaXRightSum-(limit-100)));
            }
            int w = (int) (v.getWidth()-deltaXRightSum);
            if(leftOk)
            {
                if(deltaXRightSum < MAX_MARING_LEFT)
                    w = (int) (v.getWidth()+deltaXRightSum);
                else
                    w = (int) (v.getWidth()-(deltaXRightSum-MAX_MARING_LEFT));
            }
            if(deltaXRightSum >= limit) {
                activity.onRightSwipe(v,leftOk);
                rightOk = true;
                leftOk = false;
                leftMargin = MAX_MARING_LEFT;
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
        int limit = rightOk ? MAX_MARGIN_RIGHT+100 : MAX_MARGIN_RIGHT;
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
                rightMargin = (int)(params.rightMargin+(deltaXLeftSum-(limit-MAX_MARGIN_RIGHT)));
            }
            int w = (int) (v.getWidth()-deltaXLeftSum);
            if(rightOk)
            {
                if(deltaXLeftSum < MAX_MARGIN_RIGHT)
                    w = (int) (v.getWidth()+deltaXLeftSum);
                else
                    w = (int) (v.getWidth()-(deltaXLeftSum-MAX_MARGIN_RIGHT));
            }
            if(deltaXLeftSum >= limit) {
                activity.onLeftSwipe(v,rightOk);
                leftOk = true;
                rightOk = false;
                rightMargin = MAX_MARGIN_RIGHT;
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
        int marginL = rightOk ? originalMarginLeft + MAX_MARING_LEFT : originalMarginLeft;
        int marginR = leftOk ? originalMarginRight + MAX_MARGIN_RIGHT : originalMarginRight;
        int w = rightOk || leftOk ? originalWidth - 100 : originalWidth;
        params.setMargins(marginL, params.topMargin, marginR, params.bottomMargin);
        params.width = w;
        v.setLayoutParams(params);
    }

    public boolean onTouch(View v, MotionEvent event) {
        if(!canManageList) return false;
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
                        if(deltaX > 0 && activity.BeforeSwipe()) { this.onLeftSwipe(deltaX,v); return true; }
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