package chat.rocket.app.ui.widgets;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import chat.rocket.app.R;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

/**
 * Created by julio on 28/11/15.
 */

//TODO: save state
public class FabMenuLayout extends RevealFrameLayout {
    private FloatingActionButton mFab;
    private View mMenu;
    private SupportAnimator mMenuAnimator;
    private ViewPropertyAnimator mFabAnimation;
    private View mTopView;
    private View mContentView;
    private int mMenuWdidth;

    public interface MenuClickListener {
        void onMenuItemClick(int id);
    }

    private MenuClickListener mCallback;

    public void setMenuClickListener(MenuClickListener listener) {
        mCallback = listener;
    }

    public void setTopView(View topView) {
        mTopView = topView;
    }

    public void setContentView(View contentView) {
        mContentView = contentView;
    }

    public FabMenuLayout(Context context) {
        super(context);
        init();
    }

    public FabMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FabMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.fab_menu_internal_layout, this, true);
        mMenu = findViewById(R.id.fabtoolbar);
        mMenuWdidth = (int) getResources().getDimension(R.dimen.fab_menu_width);
        mFab = (FloatingActionButton) findViewById(R.id.fab);


        mFab.setOnClickListener(view -> {

            if (mMenu.getVisibility() == View.VISIBLE) {
                onBackPressed();
            } else {
                setContentMargin();
            }
        });

        OnClickListener listener = v -> {
            if (mCallback != null) {
                mCallback.onMenuItemClick(v.getId());
            }
        };

        findViewById(R.id.SettingsButton).setOnClickListener(listener);

        findViewById(R.id.SearchButton).setOnClickListener(listener);

        findViewById(R.id.MembersButton).setOnClickListener(listener);

        findViewById(R.id.FilesButton).setOnClickListener(listener);

        findViewById(R.id.StaredButton).setOnClickListener(listener);

        findViewById(R.id.PinnedButton).setOnClickListener(listener);

        findViewById(R.id.MicButton).setOnClickListener(listener);
    }

    private void rotateFabToLess90() {
        float initialRadius = 0;
        float finalRadius = getResources().getDisplayMetrics().heightPixels;
        ViewGroup.MarginLayoutParams param = ((ViewGroup.MarginLayoutParams) mFab.getLayoutParams());
        mFabAnimation = this.mFab.animate();
        float scale = getResources().getDimension(R.dimen.fab_menu_width) / (mMenuWdidth);

        mFabAnimation.rotation(-90)
                .scaleY(scale).scaleX(scale)
                .translationXBy(param.rightMargin + param.rightMargin * (1 - scale))
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        sendFabToTop();

                        int cx = (int) (mFab.getX());
                        int cy = (int) (mFab.getY());
                        createReveal(cx, cy, initialRadius, finalRadius);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                }).start();

    }

    private void sendFabToTop() {
        mFabAnimation.setListener(null);
        float y = 0;
        if (mTopView != null) {
            y = mTopView.getY() + mFab.getPaddingTop() - mFab.getY();
        }

        mFab.animate().translationY(y).setDuration(500).start();
    }

    private void createReveal(int cx, int cy, float initialRadius, float finalRadius) {
        mMenuAnimator = ViewAnimationUtils.createCircularReveal(mMenu, cx, cy, initialRadius, finalRadius);
        mMenuAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mMenuAnimator.setDuration(500);
        mMenuAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                mMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        mMenuAnimator.start();
    }


    public boolean onBackPressed() {
        if (mMenuAnimator != null) {
            if (!mMenuAnimator.isRunning()) {
                rotateFabTo90();
                ((ViewGroup) findViewById(R.id.MenuContentLayout)).removeAllViews();
                return true;
            } else {
                mMenuAnimator.cancel();
                mMenuAnimator = null;
                return true;
            }
        }
        return false;
    }

    private void rotateFabTo90() {
        mFabAnimation.setListener(null);
        mFab.animate().rotation(90).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFabAnimation.setListener(null);
                translateMenuToBottom();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void translateMenuToBottom() {
        SupportAnimator reverseMenuAnimator = mMenuAnimator.reverse();
        mMenuAnimator = null;
        reverseMenuAnimator.setDuration(700);
        reverseMenuAnimator.addListener(new SupportAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart() {
                translateFabToBottom();
            }

            @Override
            public void onAnimationEnd() {
                mMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        reverseMenuAnimator.start();
    }

    private void translateFabToBottom() {
        mFab.animate().translationX(0).translationY(0).setDuration(500).setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rotateFabToZero();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .start();
    }

    private void rotateFabToZero() {
        mFabAnimation.setListener(null);
        mFab.animate().rotation(0).scaleX(1).scaleY(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                restoreContentMargin();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void setContentMargin() {
        if (mContentView != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mContentView.getLayoutParams();
            mContentView.setTag(params.rightMargin);
            ValueAnimator anim = ValueAnimator.ofInt(params.rightMargin, mMenuWdidth);
            anim.addUpdateListener(animation -> {
                int rightMargin = (int) animation.getAnimatedValue();
                params.setMargins(params.leftMargin, params.topMargin, rightMargin, params.bottomMargin);
                mContentView.setLayoutParams(params);
            });
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    rotateFabToLess90();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        }
    }

    private void restoreContentMargin() {
        if (mContentView != null) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mContentView.getLayoutParams();
            ValueAnimator anim = ValueAnimator.ofInt(params.rightMargin, (int) mContentView.getTag());
            anim.addUpdateListener(animation -> {
                int rightMargin = (int) animation.getAnimatedValue();
                params.setMargins(params.leftMargin, params.topMargin, rightMargin, params.bottomMargin);
                mContentView.setLayoutParams(params);
            });
            anim.start();
        }
    }
}
