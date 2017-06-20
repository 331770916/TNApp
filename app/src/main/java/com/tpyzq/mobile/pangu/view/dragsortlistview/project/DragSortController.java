package com.tpyzq.mobile.pangu.view.dragsortlistview.project;

import android.graphics.Point;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;

import com.tpyzq.mobile.pangu.view.dragsortlistview.DragSortListView;


/**
 * //TODO DragSortController这个助手类，提供了所有常用的 开始/停止/删除 拖拽操作功能。
 * 类,启动和停止项拖累{ @link DragSortListView }
 * 基于触控手势。这个类也继承自
 * { @link SimpleFloatViewManager },它提供了基本的浮动的观点
 * 创建。
 * <p/>
 * 这个类的一个实例是传递给方法
 * {@link DragSortListView#setTouchListener()} and
 * {@link DragSortListView#setFloatViewManager()} of your
 * {@link DragSortListView} instance.
 */
public class DragSortController extends SimpleFloatViewManager implements View.OnTouchListener, GestureDetector.OnGestureListener {

    /**
     * 联系的方式enum init。
     */
    public static final int ON_DOWN = 0;
    public static final int ON_DRAG = 1;
    public static final int ON_LONG_PRESS = 2;

    private int mDragInitMode = ON_DOWN;

    private boolean mSortEnabled = true;

    /**
     * 删除模式枚举。
     */
    public static final int CLICK_REMOVE = 0;
    public static final int FLING_REMOVE = 1;

    /**
     * 当前删除模式。
     */
    private int mRemoveMode;

    private boolean mRemoveEnabled = false;
    private boolean mIsRemoving = false;

    private GestureDetector mDetector;

    private GestureDetector mFlingRemoveDetector;

    private int mTouchSlop;

    public static final int MISS = -1;

    private int mHitPos = MISS;
    private int mFlingHitPos = MISS;

    private int mClickRemoveHitPos = MISS;

    private int[] mTempLoc = new int[2];

    private int mItemX;
    private int mItemY;

    private int mCurrX;
    private int mCurrY;

    private boolean mDragging = false;

    private float mFlingSpeed = 500f;

    private int mDragHandleId;

    private int mClickRemoveId;

    private int mFlingHandleId;
    private boolean mCanDrag;

    private DragSortListView mDslv;
    private int mPositionX;

    /**
     * 调用{ @link # DragSortController(DragSortListView,int)}了
     * 0拖动处理id,FLING_RIGHT_REMOVE删除模式,
     * 和ON_DOWN拖init。默认情况下,启用排序,
     * 删除是禁用的。
     *
     * @param dslv dslv实例
     */
    public DragSortController(DragSortListView dslv) {
        this(dslv, 0, ON_DOWN, FLING_REMOVE);
    }

    public DragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode, int removeMode) {
        this(dslv, dragHandleId, dragInitMode, removeMode, 0);
    }

    public DragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode, int removeMode, int clickRemoveId) {
        this(dslv, dragHandleId, dragInitMode, removeMode, clickRemoveId, 0);
    }

    /**
     * 默认情况下,启用排序,和删除是禁用的。
     *
     * @param dslv         dslv实例
     * @param dragHandleId 资源视图的id表示
     *                     拖动处理列表项。
     */
    public DragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode,
                              int removeMode, int clickRemoveId, int flingHandleId) {
        super(dslv);
        mDslv = dslv;
        mDetector = new GestureDetector(dslv.getContext(), this);
        mFlingRemoveDetector = new GestureDetector(dslv.getContext(), mFlingRemoveListener);
        mFlingRemoveDetector.setIsLongpressEnabled(false);
        mTouchSlop = ViewConfiguration.get(dslv.getContext()).getScaledTouchSlop();
        mDragHandleId = dragHandleId;
        mClickRemoveId = clickRemoveId;
        mFlingHandleId = flingHandleId;
        setRemoveMode(removeMode);
        setDragInitMode(dragInitMode);
    }


    public int getDragInitMode() {
        return mDragInitMode;
    }

    /**
     * 设置拖启动方式。需要之一
     * { @link ON_DOWN },{ @link ON_DRAG },或{ @link ON_LONG_PRESS }。
     *
     * @param模式拖动初始化模式。
     */
    public void setDragInitMode(int mode) {
        mDragInitMode = mode;
    }

    /**
     * 启用/禁用列表项排序。如果只有项禁用是有用的
     * 需要移除。防止拖在垂直方向。
     *
     * @param enabled Set <code>true</code> to enable list
     *                项目排序.
     */
    public void setSortEnabled(boolean enabled) {
        mSortEnabled = enabled;
    }

    public boolean isSortEnabled() {
        return mSortEnabled;
    }

    /**
     * One of {@link CLICK_REMOVE}, {@link FLING_RIGHT_REMOVE},
     * {@link FLING_LEFT_REMOVE},
     * {@link SLIDE_RIGHT_REMOVE}, or {@link SLIDE_LEFT_REMOVE}.
     */
    public void setRemoveMode(int mode) {
        mRemoveMode = mode;
    }

    public int getRemoveMode() {
        return mRemoveMode;
    }

    /**
     * 启用/禁用项删除不影响删除模式。
     */
    public void setRemoveEnabled(boolean enabled) {
        mRemoveEnabled = enabled;
    }

    public boolean isRemoveEnabled() {
        return mRemoveEnabled;
    }

    /**
     * 设置的资源id的观点代表了阻力
     * 处理列表项。
     *
     * @param id An android resource id.
     */
    public void setDragHandleId(int id) {
        mDragHandleId = id;
    }

    /**
     * 设置的资源id的观点代表了舞
     * 处理列表项。
     *
     * @param id An android resource id.
     */
    public void setFlingHandleId(int id) {
        mFlingHandleId = id;
    }

    /**
     * 设置的资源id表示单击视图
     * 删除按钮。
     *
     * @param id An android resource id.
     */
    public void setClickRemoveId(int id) {
        mClickRemoveId = id;
    }

    /**
     * 集旗帜限制浮动的某些运动的观点
     * 基于DragSortController设置(例如删除模式)。
     * 开始DragSortListView的拖累。
     *
     * @param position The list item position (includes headers).
     * @param deltaX   Touch x-coord minus left edge of floating View.
     * @param deltaY   Touch y-coord minus top edge of floating View.
     * @return True if drag started, false otherwise.
     */
    public boolean startDrag(int position, int deltaX, int deltaY) {

        int dragFlags = 0;
        if (mSortEnabled && !mIsRemoving) {
            dragFlags |= DragSortListView.DRAG_POS_Y | DragSortListView.DRAG_NEG_Y;
        }
        if (mRemoveEnabled && mIsRemoving) {
            dragFlags |= DragSortListView.DRAG_POS_X;
            dragFlags |= DragSortListView.DRAG_NEG_X;
        }

        mDragging = mDslv.startDrag(position - mDslv.getHeaderViewsCount(), dragFlags, deltaX,
                deltaY);
        return mDragging;
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (!mDslv.isDragEnabled() || mDslv.listViewIntercepted()) {
            return false;
        }

        mDetector.onTouchEvent(ev);
        if (mRemoveEnabled && mDragging && mRemoveMode == FLING_REMOVE) {
            mFlingRemoveDetector.onTouchEvent(ev);
        }

        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mCurrX = (int) ev.getX();
                mCurrY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (mRemoveEnabled && mIsRemoving) {
                    int x = mPositionX >= 0 ? mPositionX : -mPositionX;
                    int removePoint = mDslv.getWidth() / 2;
                    if (x > removePoint) {
                        mDslv.stopDragWithVelocity(true, 0);
                    }
                }
            case MotionEvent.ACTION_CANCEL:
                mIsRemoving = false;
                mDragging = false;
                break;
        }

        return false;
    }

    /**
     * 覆盖提供启用滑动删除时消退。
     */
    @Override
    public void onDragFloatView(View floatView, Point position, Point touch) {

        if (mRemoveEnabled && mIsRemoving) {
            mPositionX = position.x;
        }
    }

    /**
     * 得到这个职位开始基于ACTION_DOWN拖
     * MotionEvent。这个函数只调用
     * { @link # dragHandleHitPosition(MotionEvent)}。覆盖
     *改变拖动处理行为;
     *当一个ACTION_DOWN内部调用这个函数
     *事件检测。
     *
     * @param ev ACTION_DOWN MotionEvent。
     * @return拖如果drag-init姿态位置列表
     * detected; MISS if unsuccessful.
     */
    public int startDragPosition(MotionEvent ev) {
        return dragHandleHitPosition(ev);
    }

    public int startFlingPosition(MotionEvent ev) {
        return mRemoveMode == FLING_REMOVE ? flingHandleHitPosition(ev) : MISS;
    }

    /**
     *检查触摸一个条目的拖动处理(指定的
     * { @link # setDragHandleId(int)}),并返回项的位置
     *如果一个拖动处理触摸检测。
     *
     *@param ev的ACTION_DOWN MotionEvent。
     * @return拖动处理的列表项的位置
     * touched; MISS if unsuccessful.
     */
    public int dragHandleHitPosition(MotionEvent ev) {
        return viewIdHitPosition(ev, mDragHandleId);
    }

    public int flingHandleHitPosition(MotionEvent ev) {
        return viewIdHitPosition(ev, mFlingHandleId);
    }

    public int viewIdHitPosition(MotionEvent ev, int id) {
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();

        int touchPos = mDslv.pointToPosition(x, y); // includes headers/footers

        final int numHeaders = mDslv.getHeaderViewsCount();
        final int numFooters = mDslv.getFooterViewsCount();
        final int count = mDslv.getCount();

        // Log.d("mobeta", "touch down on position " + itemnum);
        // We're only interested if the touch was on an
        // item that's not a header or footer.
        if (touchPos != AdapterView.INVALID_POSITION && touchPos >= numHeaders
                && touchPos < (count - numFooters)) {
            final View item = mDslv.getChildAt(touchPos - mDslv.getFirstVisiblePosition());
            final int rawX = (int) ev.getRawX();
            final int rawY = (int) ev.getRawY();

            View dragBox = id == 0 ? item : item.findViewById(id);
            if (dragBox != null) {
                dragBox.getLocationOnScreen(mTempLoc);

                if (rawX > mTempLoc[0] && rawY > mTempLoc[1] &&
                        rawX < mTempLoc[0] + dragBox.getWidth() &&
                        rawY < mTempLoc[1] + dragBox.getHeight()) {

                    mItemX = item.getLeft();
                    mItemY = item.getTop();

                    return touchPos;
                }
            }
        }

        return MISS;
    }

    @Override
    public boolean onDown(MotionEvent ev) {
        if (mRemoveEnabled && mRemoveMode == CLICK_REMOVE) {
            mClickRemoveHitPos = viewIdHitPosition(ev, mClickRemoveId);
        }

        mHitPos = startDragPosition(ev);
        if (mHitPos != MISS && mDragInitMode == ON_DOWN) {
            startDrag(mHitPos, (int) ev.getX() - mItemX, (int) ev.getY() - mItemY);
        }

        mIsRemoving = false;
        mCanDrag = true;
        mPositionX = 0;
        mFlingHitPos = startFlingPosition(ev);

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        final int x1 = (int) e1.getX();
        final int y1 = (int) e1.getY();
        final int x2 = (int) e2.getX();
        final int y2 = (int) e2.getY();
        final int deltaX = x2 - mItemX;
        final int deltaY = y2 - mItemY;

        if (mCanDrag && !mDragging && (mHitPos != MISS || mFlingHitPos != MISS)) {
            if (mHitPos != MISS) {
                if (mDragInitMode == ON_DRAG && Math.abs(y2 - y1) > mTouchSlop && mSortEnabled) {
                    startDrag(mHitPos, deltaX, deltaY);
                } else if (mDragInitMode != ON_DOWN && Math.abs(x2 - x1) > mTouchSlop && mRemoveEnabled) {
                    mIsRemoving = true;
                    startDrag(mFlingHitPos, deltaX, deltaY);
                }
            } else if (mFlingHitPos != MISS) {
                if (Math.abs(x2 - x1) > mTouchSlop && mRemoveEnabled) {
                    mIsRemoving = true;
                    startDrag(mFlingHitPos, deltaX, deltaY);
                } else if (Math.abs(y2 - y1) > mTouchSlop) {
                    mCanDrag = false; // if started to scroll the list then
                    // don't allow sorting nor fling-removing
                }
            }
        }
        // return whatever
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // Log.d("mobeta", "lift listener long pressed");
        if (mHitPos != MISS && mDragInitMode == ON_LONG_PRESS) {
            mDslv.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            startDrag(mHitPos, mCurrX - mItemX, mCurrY - mItemY);
        }
    }

    //完成OnGestureListener接口
    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    // 完成OnGestureListener接口
    @Override
    public boolean onSingleTapUp(MotionEvent ev) {
        if (mRemoveEnabled && mRemoveMode == CLICK_REMOVE) {
            if (mClickRemoveHitPos != MISS) {
                mDslv.removeItem(mClickRemoveHitPos - mDslv.getHeaderViewsCount());
            }
        }
        return true;
    }

    //完成OnGestureListener接口
    @Override
    public void onShowPress(MotionEvent ev) {
        // do nothing
    }

    private GestureDetector.OnGestureListener mFlingRemoveListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                             float velocityY) {
                    // Log.d("mobeta", "on fling remove called");
                    if (mRemoveEnabled && mIsRemoving) {
                        int w = mDslv.getWidth();
                        int minPos = w / 5;
                        if (velocityX > mFlingSpeed) {
                            if (mPositionX > -minPos) {
                                mDslv.stopDragWithVelocity(true, velocityX);
                            }
                        } else if (velocityX < -mFlingSpeed) {
                            if (mPositionX < minPos) {
                                mDslv.stopDragWithVelocity(true, velocityX);
                            }
                        }
                        mIsRemoving = false;
                    }
                    return false;
                }
            };

}
