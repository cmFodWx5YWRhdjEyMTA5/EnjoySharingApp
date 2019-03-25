package enjoysharing.enjoysharing.AdapterObject;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import enjoysharing.enjoysharing.Activity.BaseActivity;
import enjoysharing.enjoysharing.R;

public class CardSwipeDetector implements View.OnTouchListener {

    private BaseActivity activity;
    private ImageView image;
    private TableRow row;
    private boolean swipeLeft, rightSwipeDone = false, leftSwipeDone = false, declineFinish = false, acceptFinish = false;
    static final int MIN_DISTANCE = 5, MAX_SWIPE_LEFT = 260, MAX_SWIPE_RIGHT = 300;
    private float downX, downY, upX, upY;
    protected boolean canManageList;
    protected int UserId;

    public void setUserId(int userId) {
        UserId = userId;
    }

    public void CanManageList(boolean canManageList) {
        this.canManageList = canManageList;
    }

    public void SetAccepted()
    {
        image.setForeground(ContextCompat.getDrawable(activity.getBaseContext(), R.drawable.image_with_border_green));
        SetBackground();
        leftSwipeDone = true;
        rightSwipeDone = false;
    }

    public void SetDeclined()
    {
        image.setForeground(ContextCompat.getDrawable(activity.getBaseContext(), R.drawable.image_with_border_red));
        SetBackground();
        rightSwipeDone = true;
        leftSwipeDone = false;
    }

    public CardSwipeDetector(BaseActivity activity, ImageView image, TableRow row){
        this.activity = activity;
        this.image = image;
        this.row = row;
    }

    public void onRightSwipe(float deltaX, View v){
        deltaX = -deltaX;
        SetBackground();
        if(deltaX > MAX_SWIPE_RIGHT) {
            deltaX = MAX_SWIPE_RIGHT;
            declineFinish = true;
        }
        else
            declineFinish = false;
        GradientDrawable drawable = (GradientDrawable) row.getBackground();
        drawable.setGradientCenter(deltaX/row.getWidth(),0.5f);
        row.setBackground(drawable);
        row.invalidate();
    }

    public void onLeftSwipe(float deltaX, View v){
        SetBackground();
        if(deltaX > MAX_SWIPE_LEFT) {
            deltaX = MAX_SWIPE_LEFT;
            acceptFinish = true;
        }
        else
            acceptFinish = false;
        GradientDrawable drawable = (GradientDrawable) row.getBackground();
        drawable.setGradientCenter(1-(deltaX/row.getWidth()),0.5f);
        row.setBackground(drawable);
        row.invalidate();
    }

    public void onDownSwipe(){ }

    public void onUpSwipe(){ }

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
                        if(deltaX < 0 && !rightSwipeDone) { swipeLeft = false; this.onRightSwipe(deltaX,v); return true; }
                        if(deltaX > 0 && activity.BeforeSwipe() && !leftSwipeDone) { swipeLeft = true; this.onLeftSwipe(deltaX,v); return true; }
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
                CheckForAction(v);
                SetBackground();
                return true;
            }
        }
    }

    protected void CheckForAction(View v)
    {
        if(declineFinish) {
            if (!rightSwipeDone)
                activity.onRightSwipe(v, leftSwipeDone,UserId);
            //SetDeclined();
        }
        if(acceptFinish) {
            if (!leftSwipeDone)
                activity.onLeftSwipe(v,UserId);
            //SetAccepted();
        }
        acceptFinish = false;
        declineFinish = false;
    }

    protected void SetBackground()
    {
        if(swipeLeft)
            row.setBackgroundResource(R.drawable.card_swipe_accept);
        else
            row.setBackgroundResource(R.drawable.card_swipe_refuse);
        GradientDrawable drawable = (GradientDrawable) row.getBackground();
        drawable.setGradientCenter(-1,0.5f);
        row.setBackground(drawable);
        row.invalidate();
    }

}
