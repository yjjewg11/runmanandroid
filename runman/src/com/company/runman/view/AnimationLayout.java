package com.company.runman.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import com.company.runman.R;


public class AnimationLayout extends FrameLayout implements GestureDetector.OnGestureListener {
    private static final float MAX_V = 1000.0f;
    private static final float MAX_A = 4000.0f;
    private static final int MSG_AN = 1000;
    private GestureDetector g;
    private View left, right;
    int x;
    boolean scroll = false;
    private Rect r = new Rect();
    private int d;
    private boolean u = false;

    private float ma;
    private float mvs;

    private float a;
    private float vs;
    private float dis;
    private long l;
    private long n;
    AnHandler h = new AnHandler();
    boolean isAt = false;
    private float b;
    private float c;
    private boolean in = false;
    private boolean m = false;
    private InputMethodManager imm;

    public AnimationLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AnimationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimationLayout(Context context) {
        super(context);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        left = this.findViewById(R.id.left);
        right = this.findViewById(R.id.right);
        
        left.setPressed(false);
        //right.setDrawingCacheEnabled(true);
        
        View headerMenu = right.findViewById(R.id.headerMenu);
        OnClickListener l = new OnClickListener() {

            @Override
            public void onClick(View view) {
            	imm.hideSoftInputFromWindow(view.getWindowToken(), 0); 
                ant();
            }
        };
        headerMenu.setOnClickListener(l);
    }

    public void init() {
        imm = (InputMethodManager) this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        g = new GestureDetector(this.getContext(), this);
        d = ViewConfiguration.get(getContext()).getScaledTouchSlop(); //threshhold
        
        float density = getResources().getDisplayMetrics().density;
        mvs = (int) (MAX_V * density + 0.5f);
        ma = (int) (MAX_A * density + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        right.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(this.right.getMeasuredWidth(), this.right.getMeasuredHeight());
        if (left != null) {
            int w = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.UNSPECIFIED);
            int h = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
            left.measure(w, h);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(isAt){
            return;
        }
        int h = getMeasuredHeight();
        if (left != null) {
            int i11 = this.left.getMeasuredWidth();
            int v1h = this.left.getMeasuredHeight();
            int i13 = (h - v1h) / 2;
            this.left.layout(0, i13, i11 + 0, v1h + i13);
        }
        int i3 = this.right.getMeasuredWidth();
        int i4 = this.right.getMeasuredHeight();
        int i5 = 0;
        if (u) {
            i5 = this.left.getMeasuredWidth();
        }
        int i6 = (h - i4) / 2;
        this.right.layout(i5, i6, i3 + i5, i4 + i6);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean bl = false;
        if (u) {
            right.getHitRect(r);
            if (r.contains((int) ev.getX(), (int) ev.getY())) {
                bl = true;
            }
        }
        int ac = ev.getAction();
        switch (ac) {
        case MotionEvent.ACTION_DOWN:
            b = ev.getX();
            c = ev.getY();
            if(!isAt){
                r(); 
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (!u && in && !m) {
                boolean re = cs(ev, ev.getX() - b, ev.getY() - this.c, false);
                if (re) {
                    isAt = true;
                    h.removeMessages(MSG_AN); // add 
                    m = true;
                    int de = (int) b - (int) ev.getX();
                    b(de);
                    b = ev.getX();
                    c = ev.getY();
                    bl = true;
                }
            }
            break;
        case MotionEvent.ACTION_UP:
            m = false;
            break;
        default:
            break;
        }
        return bl || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean bool = g.onTouchEvent(ev);
        int a = ev.getAction();
        switch (a) {
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            if (!bool && isAt) {
                int l = right.getLeft();//(int) ev.getX() - this.s;
                pf(l, 0.0f, false);
            }
            //isAt = false;
            break;
        default:
            break;
        }
        return bool || super.onTouchEvent(ev);

    }

    @Override
    protected void dispatchDraw(Canvas c) {
    	try{
	        long l1 = getDrawingTime();
	        c.save();
	        int i1 = getScrollX() + getPaddingLeft();
	        int i2 = getScrollY() + getPaddingTop();
	        c.clipRect(i1, i2, i1 + getWidth() - getPaddingRight(), i2 + getHeight() - getPaddingBottom());
            if(isAt || u) {
                drawChild(c, left, l1);
            }
	        drawChild(c, right, l1);
	        c.restore();
    	} catch (Exception e){
    		//do nothing
    	}
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {

        if (isAt) {
            pf(right.getLeft(), vX, false); //
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float arg3) {
        if (!isAt && u) {
            int ii = ((int) b - (int) e2.getX());
            if (ii <= 0 || ((ii > 0) && (Math.abs(ii) < d))) {
                b = e2.getX();
                return true;
            }
            isAt = true;
            h.removeMessages(MSG_AN); // add;
        }
        if (isAt) {
            b((int) b - (int) e2.getX());
            b = e2.getX();
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    private void b(int dx) {
        final int l = right.getLeft();
        final int r = left.getRight();
        if (dx > 0) {
            if (l - dx < 0) {
                dx = l;
            }
            right.offsetLeftAndRight(-dx);
        } else {
            if (l - dx > r) {
                dx = l - r;  
            }
            right.offsetLeftAndRight(-dx);
        }
        invalidate();
    }

    public void ai(boolean b) {
        in = b;
    }

    private boolean cs(MotionEvent ev, float dx, float dy, boolean bp) {
        //Log.i("HalfView33", "cs");
        float f1 = Math.abs(dx);
        float f2 = Math.abs(dy);
        if (!bp) {
            if (f2 > f1) {
                return false;
            }
            if (dx > 0 && f1 > d) {
                right.setPressed(false);
                return true;
            }
            if (dx < 0) {
                return false;
            }
        } else {
            if (f2 > f1) {
                return false;
            }
            if (dx < 0 && f1 > d) {
                right.setPressed(false);
                return true;
            }
            if (dx > 0) {
                return false;
            }
        }
        return false;
    }

    private void pf(int p, float vx, boolean isAm) {
        int w = left.getWidth();
        this.dis = p;
        this.vs = vx;
        if(left.getVisibility() != View.VISIBLE){
            left.setVisibility(View.VISIBLE);
        }
        if (u) { // u = true  a = -ma vx > 0
            if (isAm || (vx > mvs) || ((p > w / 2) && vx > -mvs)) {
                a = ma;
                if (vx > 0) {
                    vs = 0;
                }
            } else {
                a = -ma;
                if (vx < 0) {
                    vs = 0;
                }
            }
        } else {
            if (!isAm && (vx > mvs) || (p > w / 2 && vx > -mvs)) {
                a = ma;
                if (vx > 0) {
                    vs = 0;
                }
            } else {
                a = -ma;
                if (vx < 0) {
                    vs = 0;
                }
            }
        }

        long t = SystemClock.uptimeMillis();
        l = t;
        n = t + 16L;
        //mAnimating = true;
        h.removeMessages(MSG_AN);
        h.sendMessageAtTime(h.obtainMessage(MSG_AN), n);
    }

    private void a(int x) {
        //Log.i("HalfView33", " a(int x) ");
        int l = right.getLeft();
        int delta = x - l;
        right.offsetLeftAndRight(delta);
        invalidate();
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.right.getHitRect(r);
        if (r.contains((int) e.getX(), (int) e.getY())) {
            if (u) {
                pf(right.getLeft(), -ma, true);
            }
        }
        return true;
    }

    private class AnHandler extends Handler {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
            case MSG_AN:
                an();
                break;
            default:
                break;
            }
        }
    }

    private void an() {
        ac();
        final int r = left.getRight();
        if (dis > r) {
            u = true;
            dis = r;
            m = false;
            isAt= false;
            //h.removeMessages(MSG_AN);
            notifyAnListener(true);
            
            requestLayout();
            invalidate();
            return;
        }
        if (dis < 0) {
            u = false;
            dis = 0;
            m = false;
            isAt= false;
            left.setVisibility(View.GONE);
            //h.removeMessages(MSG_AN);
            notifyAnListener(false);
            
            requestLayout();
            invalidate();
            return;
        }
        a((int) dis);
        long t = SystemClock.uptimeMillis();
        l = t;
        n = t + 16L;
        //h.removeMessages(MSG_AN);
        h.sendMessageAtTime(h.obtainMessage(MSG_AN), n);
    }

    private void ac() {
        long now = SystemClock.uptimeMillis();
        float t = (now - l) / 1000.0f; // ms -> s
        final float position = dis;
        final float v = vs; // px/s
        final float aa = a; // px/s/s
        dis = position + (v * t) + (0.5f * aa * t * t); // px
        vs = v + (aa * t); // px/s
        l = now; // ms

    }

    private void r() {
        a = ma;
        vs = mvs;
        if (u) {
            dis = left.getRight();
        } else {
            dis = 0;
        }
    }

    public void ant() {
        isAt = true;
        int l = right.getLeft();
        if (u) {
            pf(l, -ma, true);
        } else {
            pf(l, ma, true);
        }
    }

    public void notifyAnListener(boolean bOpen) {
    	if (bOpen) {
    		left.setClickable(true);
    	} else {
    		left.setClickable(false);
    	}
    }

};
