package com.keven.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.keven.R;
import com.keven.utils.DisplayUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class SlidingDrawer extends FrameLayout {

    private LinearLayout drawer;
    private View content;
    DrawerState state;

    int containerWidth;
    int containerHeight;

    int MAX_MOVE_VALUE; //启动推拉动画的阀值
    int DRAG_BAR_VALUE; //拖动条高度
    int MAX_ANIMATION_DURATION = 600;
    int FAST_ANIMATION_DURATION = 100;

    int ANIMATION_BOUND_VALUE; //动画弹跳效果距离
    boolean isAniating; //是否正在运行动画


    public SlidingDrawer(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.sliding_drawer, this, true);
    }

    public SlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.sliding_drawer, this, true);
    }

    public SlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.sliding_drawer, this, true);
    }

    public void setInitState(DrawerState state) {
        this.state = state;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        drawer = (LinearLayout) findViewById(R.id.drawer_layout);
        content = findViewById(R.id.drawer_content);
        MAX_MOVE_VALUE = DisplayUtils.dip2px(20);
        DRAG_BAR_VALUE = DisplayUtils.dip2px(50);
        ANIMATION_BOUND_VALUE = DisplayUtils.dip2px(15);
        setInitState(DrawerState.Center);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        drawer.setOnTouchListener(new LayoutClickListener(w, h));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("SlidingDrawer", "--------changed=" + changed + ", left=" + left + ", right=" + right + ", top=" + top + ", bottom=" + bottom);
        int t = drawer.getTop();
        int b = drawer.getBottom();
        int l = drawer.getLeft();
        int r = drawer.getRight();
        if (changed) {// content 在 drawer 内部
            int dragLength = containerHeight - DRAG_BAR_VALUE;
            if (state == DrawerState.Top) {
                // no change
            } else if (state == DrawerState.Center) {
                t += dragLength / 2;
                b += dragLength / 2;
            } else if (state == DrawerState.Bottom) {
                t += dragLength;
                b += dragLength;
            }
            drawer.layout(l, t, r, b);
            content.layout(l, DRAG_BAR_VALUE, r, b - t);
        }
    }

    class LayoutClickListener implements OnTouchListener, AnimatorListener {

        int lastX, lastY;
        int orgY;// ACTION_DOWN时控件的Y值，用于计算用户拖拉过程中移动的距离，超过临界点时开启动画

        boolean isPressing;//防止误MotionEvent.ACTION_MOVE
        boolean isMoved;//主要用来判断tab事件的，如果移动过，将不产生tab事件。
        long pressTimeMillis;   //点击时间在200ms内，认为是tap

        public LayoutClickListener(int screenW, int screenH) {
            containerWidth = screenW;
            containerHeight = screenH;
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (isAniating) {
                return true;
            }
            int ea = event.getAction();
            switch (ea) {
                case MotionEvent.ACTION_DOWN: // 按下
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    orgY = lastY;
                    isPressing = true;
                    isMoved = false;
                    pressTimeMillis = System.currentTimeMillis();
                    return true;

                case MotionEvent.ACTION_MOVE: // 移动
                    if (!isPressing) {
                        return false;
                    }
                    int dy = (int) event.getRawY() - lastY;
                    if (dy > 10) {
                        isMoved = true;
                    }
                    int top = v.getTop() + dy;
                    int bottom = containerHeight;
                    if (top < 0) {
                        top = 0;
                    }
                    if (top > bottom - DRAG_BAR_VALUE) {
                        top = bottom - DRAG_BAR_VALUE;
                    }
                    v.layout(0, top, v.getWidth(), top + v.getHeight());
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    return true;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    if (System.currentTimeMillis() - pressTimeMillis < 200 && !isMoved) {
                        tapAnimation();
                        return true;
                    }
                    int curY = (int) event.getRawY();
                    doAnimateMove(orgY, curY);
                    isPressing = false;
                    break;
            }
            return false;
        }

        /**
         * when user tap the drag bar, change the state.
         */
        private void tapAnimation() {
            if (state == DrawerState.Top) { // top->bottom
                move2Bottom(MAX_ANIMATION_DURATION);
            } else if (state == DrawerState.Center) {   //center->top
                move2Top(MAX_ANIMATION_DURATION, true);
            } else {    //bottom->center
                move2Center(MAX_ANIMATION_DURATION, true);
            }
        }

        /**
         * do the animation by the down and up Y position
         *
         * @param orgY, the down Y value, this is absolute value in screen
         * @param curY, the up Y value, this is absolute value in screen
         */
        private void doAnimateMove(int orgY, int curY) {
            float offset = curY - orgY;
            float maxDuration = MAX_ANIMATION_DURATION;
            float contHeight = (containerHeight - DRAG_BAR_VALUE) >> 1; //右移表示除2

            switch (state) {
                case Top: {// 当前拖动条在最上面
                    if (offset > contHeight) { //过半滑动到底
                        float duration = maxDuration * (containerHeight - offset) / (float) (containerHeight >> 1);
                        move2Bottom((int) duration);
                    } else if (offset > MAX_MOVE_VALUE) {// move to center
                        float duration = maxDuration * ((float) (containerHeight >> 1) - offset) / (float) (containerHeight >> 1);
                        move2Center((int) duration, false);
                    } else {// back to top
                        move2Top(FAST_ANIMATION_DURATION, true);
                    }
                }
                break;
                case Center: {// 当前拖动条在中间
                    if (offset > 0) {
                        if (offset > MAX_MOVE_VALUE) {
                            float duration = maxDuration * ((float) (containerHeight >> 1) - offset) / (float) (containerHeight >> 1);
                            move2Bottom((int) duration);
                        } else {
                            move2Center(FAST_ANIMATION_DURATION, true);
                        }
                    } else {
                        if (offset < -MAX_MOVE_VALUE) {
                            float temp = -offset;
                            float duration = maxDuration * ((float) (containerHeight >> 1) - temp) / (float) (containerHeight >> 1);
                            move2Top((int) duration, true);
                        } else {
                            move2Center(FAST_ANIMATION_DURATION, false);
                        }
                    }
                }
                break;
                case Bottom: {// 当前拖动条在下面
                    if (offset < -contHeight) {
                        float temp = -offset;
                        float duration = maxDuration * (containerHeight - temp) / (float) (containerHeight >> 1);
                        move2Top((int) duration, true);
                    } else if (offset < -MAX_MOVE_VALUE) {
                        float temp = -offset;
                        float duration = maxDuration * ((float) (containerHeight >> 1) - temp) / (float) (containerHeight >> 1);
                        move2Center((int) duration, true);
                    } else {
                        move2Bottom(FAST_ANIMATION_DURATION);
                    }
                }
                break;
            }
        }

        private void move2Top(int duration, boolean isUp) {
            if (isUp) {
                drawer.layout(0, drawer.getTop(), drawer.getWidth(), drawer.getTop() + drawer.getHeight());
            }
            if (duration < 100) {
                duration = 100;
                Log.e("", "move2Top(int duration, boolean isUp) pass the wrong duration");
            }
            state = DrawerState.Top;
            ObjectAnimator ani;
            if (duration == FAST_ANIMATION_DURATION || duration < 200) {
                ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, -drawer.getTop()).setDuration(duration);
            } else {
                ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, -drawer.getTop(), -drawer.getTop() + ANIMATION_BOUND_VALUE, -drawer.getTop()).setDuration(duration);
            }
            ani.addListener(this);
            ani.start();
        }

        private void move2Center(int duration, boolean isUp) {
            if (isUp) {
                drawer.layout(0, drawer.getTop(), drawer.getWidth(), drawer.getTop() + drawer.getHeight());
            }
            if (duration < 100) {
                duration = 100;
                Log.e("", "move2Top(int duration, boolean isUp) pass the wrong duration");
            }
            state = DrawerState.Center;
            ObjectAnimator ani;
            int offset = (containerHeight - DRAG_BAR_VALUE) / 2 - drawer.getTop();
            if (duration == FAST_ANIMATION_DURATION || duration < 200) {
                ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, offset).setDuration(duration);
            } else {
                if (offset > 0) {
                    ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, offset + ANIMATION_BOUND_VALUE, offset - ANIMATION_BOUND_VALUE, offset).setDuration(duration);
                } else {
                    ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, offset - ANIMATION_BOUND_VALUE, offset + ANIMATION_BOUND_VALUE, offset).setDuration(duration);
                }
            }
            ani.addListener(this);
            ani.start();
        }

        private void move2Bottom(int duration) {
            if (duration < 100) {
                duration = 100;
                Log.e("", "move2Top(int duration, boolean isUp) pass the wrong duration");
            }
            state = DrawerState.Bottom;
            ObjectAnimator ani;
            int offTemp = containerHeight - DRAG_BAR_VALUE - drawer.getTop();
            if (duration == FAST_ANIMATION_DURATION || duration < 200) {
                ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, offTemp).setDuration(duration);
            } else {
                ani = ObjectAnimator.ofFloat(drawer, "translationY", 0, offTemp, offTemp - ANIMATION_BOUND_VALUE, offTemp).setDuration(duration);
            }
            ani.addListener(this);
            ani.start();
        }

        @Override
        public void onAnimationCancel(Animator arg0) {
            isAniating = false;
        }

        @Override
        public void onAnimationEnd(Animator arg0) {
            isAniating = false;
            int bottom = containerHeight - DRAG_BAR_VALUE;
            switch (state) {
                case Top:
                    drawer.layout(0, 0, drawer.getWidth(), drawer.getHeight());
                    ViewHelper.setTranslationY(drawer, 0);
                    break;
                case Center:
                    drawer.layout(0, bottom / 2, drawer.getWidth(), bottom / 2 + drawer.getHeight());
                    ViewHelper.setTranslationY(drawer, 0);
                    break;
                case Bottom:
                    drawer.layout(0, bottom, drawer.getWidth(), bottom + drawer.getHeight());
                    ViewHelper.setTranslationY(drawer, 0);
                    break;
            }
        }

        @Override
        public void onAnimationRepeat(Animator arg0) {
        }

        @Override
        public void onAnimationStart(Animator arg0) {
            isAniating = true;
        }
    }

    /**
     * SlidingDrawer widget have several state, and
     * support switch among these states.
     */
    enum DrawerState {
        Top,
        Center,
        Bottom
    }
}