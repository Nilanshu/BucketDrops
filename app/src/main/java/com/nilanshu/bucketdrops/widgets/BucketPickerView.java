package com.nilanshu.bucketdrops.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nilanshu.bucketdrops.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Nilanshu on 13-Dec-16.
 */
public class BucketPickerView extends LinearLayout implements View.OnTouchListener {

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    public static final int DELAY = 250;
    private Calendar mCalendar;
    private TextView mTextDate;
    private TextView mTextMonth;
    private TextView mTextYear;
    private SimpleDateFormat mFormatter;
    private boolean mIncreament;
    private boolean mDecreament;
    private int MESSAGE_WHAT = 123;
    private int mActiveId;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mIncreament) {
                increment(mActiveId);
            }
            if (mDecreament) {
                decrement(mActiveId);
            }
            if (mIncreament || mDecreament) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
            }
            return true;
        }
    });

    //Used when BucketPickerView initialized from code
    public BucketPickerView(Context context) {
        super(context);
        init(context);
    }

    //Used when BucketPickerView initialized from xml
    public BucketPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //Used when BucketPickerView initialized from xml
    public BucketPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.bucket_picker_view, this);
        mCalendar = Calendar.getInstance();
        mFormatter = new SimpleDateFormat("MMM");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTextDate = (TextView) this.findViewById(R.id.tv_date); //we are using 'this' because this class extends LinearLayout
        mTextMonth = (TextView) this.findViewById(R.id.tv_month);
        mTextYear = (TextView) this.findViewById(R.id.tv_year);
        mTextDate.setOnTouchListener(this);
        mTextMonth.setOnTouchListener(this);
        mTextYear.setOnTouchListener(this);
        int date = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);
        update(date, month, year, 0, 0, 0);
    }

    private void update(int date, int month, int year, int hour, int minute, int second) {
        mCalendar.set(Calendar.DATE, date);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);
        mTextDate.setText(date + "");
        mTextYear.setText(year + "");
        mTextMonth.setText(mFormatter.format(mCalendar.getTime()));
    }

    public long getTime() {
        return mCalendar.getTimeInMillis();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.tv_month:
                processEventsFor(mTextMonth, event);
                break;
            case R.id.tv_date:
                processEventsFor(mTextDate, event);
                break;
            case R.id.tv_year:
                processEventsFor(mTextYear, event);
                break;
        }
        return true;
    }

    private void processEventsFor(TextView textView, MotionEvent event) {
        Drawable[] drawables = textView.getCompoundDrawables();//Returns drawables for the left, top, right, and bottom borders.
        if (hasDrawableTop(drawables) && hasDrawableBottom(drawables)) {
            Rect topBounds = drawables[TOP].getBounds();
            Rect bottomBounds = drawables[BOTTOM].getBounds();
            float x = event.getX();
            float y = event.getY();
            mActiveId = textView.getId();
            if (topDrawableHit(textView, topBounds.height(), x, y)) {
                if (isActionDown(event)) {
                    mIncreament = true;
                    increment(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                }
                if (isActionUpOrCancel(event)) {
                    mIncreament = false;
                }
            } else if (bottomDrawableHit(textView, bottomBounds.height(), x, y)) {
                if (isActionDown(event)) {
                    mDecreament = true;
                    decrement(textView.getId());
                    mHandler.removeMessages(MESSAGE_WHAT);
                    mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, DELAY);
                }
                if (isActionUpOrCancel(event)) {
                    mDecreament = false;
                }
            } else {
                mIncreament = false;
                mDecreament = false;
            }
        }
    }

    private void increment(int id) {
        switch (id) {
            case R.id.tv_date:
                mCalendar.add(Calendar.DATE, 1);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, 1);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, 1);
                break;
        }
        set(mCalendar);
    }

    private void decrement(int id) {
        switch (id) {
            case R.id.tv_date:
                mCalendar.add(Calendar.DATE, -1);
                break;
            case R.id.tv_month:
                mCalendar.add(Calendar.MONTH, -1);
                break;
            case R.id.tv_year:
                mCalendar.add(Calendar.YEAR, -1);
                break;
        }
        set(mCalendar);
    }

    private void set(Calendar calendar) {
        int date = calendar.get(Calendar.DATE);
        int year = calendar.get(Calendar.YEAR);
        mTextDate.setText(date + "");
        mTextYear.setText(year + "");
        mTextMonth.setText(mFormatter.format(calendar.getTime()));
    }

    private boolean isActionDown(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_DOWN;
    }

    private boolean isActionUpOrCancel(MotionEvent event) {
        return event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL;
    }

    private boolean topDrawableHit(TextView textView, int drawableHeight, float x, float y) {
        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymin = textView.getPaddingTop();
        int ymax = textView.getPaddingTop() + drawableHeight;
        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean bottomDrawableHit(TextView textView, int drawableHeight, float x, float y) {
        int xmin = textView.getPaddingLeft();
        int xmax = textView.getWidth() - textView.getPaddingRight();
        int ymax = textView.getHeight() - textView.getPaddingBottom();
        int ymin = ymax - drawableHeight;
        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    private boolean hasDrawableTop(Drawable[] drawables) {
        return drawables[TOP] != null;
    }

    private boolean hasDrawableBottom(Drawable[] drawables) {
        return drawables[BOTTOM] != null;
    }
}
