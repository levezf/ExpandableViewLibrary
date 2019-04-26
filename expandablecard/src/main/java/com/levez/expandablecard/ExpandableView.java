package com.levez.expandablecard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import static com.levez.expandablecard.AnimationUtils.collapse;
import static com.levez.expandablecard.AnimationUtils.expand;

/**
 * ExpandableView - This class is used as a custom view for displaying expansive cards.
 * @author  Felipe Levez
 * @version 0.1.1
 * @see FrameLayout
 * @see android.view.ViewGroup
 */

public class ExpandableView extends FrameLayout implements View.OnClickListener {

    /**
     * OnChangeStateListener - This interface is used as the callback of the card's state change.
     * @author  Felipe Levez
     * @version 0.1.0
     */
    public interface OnChangeStateListener {

        /**
         * Performed when a status change is initiated.
         */
        void changeState();

        /**
         * Performed when the change to extended card is completed.
         */
        void expanded();

        /**
         * Performed when the change to the collapsed card is completed.
         */
        void collapsed();
    }

    @DrawableRes
    private static final int DRAWABLE_INDICATOR_EXPANDED    =  R.drawable.ic_keyboard_arrow_up_black_24dp;

    @DrawableRes
    private static final int DRAWABLE_INDICATOR_COLLAPSED   =  R.drawable.ic_keyboard_arrow_down_black_24dp;

    private boolean mIsExpand = false;
    private AppCompatImageView mButtonExpand;
    private AppCompatImageView mImageViewIcon;
    private AppCompatTextView mTextViewTitle;
    private String mTitle;
    private CardView mHeader;
    private FrameLayout mContainerChildView;
    private OnChangeStateListener mOnChangeStateListener;
    private boolean mIsAnimation;
    private boolean mStartExpanded;
    private boolean mHeaderClickable;
    private float mHeaderElevation;

    @ColorRes
    private int mTitleHeaderColor;

    @ColorRes
    private int mIconColor;

    @ColorRes
    private int mDrawableIndicatorColor;

    @ColorRes
    private int mHeaderColor;

    @DrawableRes
    private int mDrawableIndicator;

    @DrawableRes
    private int mIcon;

    @LayoutRes
    private int mLayoutChild;


    public ExpandableView(@NonNull Context context) {
        super(context);
    }

    public ExpandableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public ExpandableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ExpandableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Initializes all settings and takes the values set in the layout.
     * @param context The application context
     * @param attrs The layout attributes
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableView);

        mDrawableIndicator       = typedArray.getResourceId ( R.styleable.ExpandableView_drawable_indicator       ,  View.NO_ID  );
        mLayoutChild             = typedArray.getResourceId ( R.styleable.ExpandableView_child_layout             ,  View.NO_ID  );
        mIcon                    = typedArray.getResourceId ( R.styleable.ExpandableView_icon                     ,  View.NO_ID  );
        mTitleHeaderColor        = typedArray.getResourceId ( R.styleable.ExpandableView_title_header_color       ,  View.NO_ID  );
        mIconColor               = typedArray.getResourceId ( R.styleable.ExpandableView_icon_color               ,  View.NO_ID  );
        mDrawableIndicatorColor  = typedArray.getResourceId ( R.styleable.ExpandableView_drawable_indicator_color ,  View.NO_ID  );
        mHeaderColor             = typedArray.getResourceId ( R.styleable.ExpandableView_header_color             ,  View.NO_ID  );
        mIsAnimation             = typedArray.getBoolean    ( R.styleable.ExpandableView_animation                ,true  );
        mStartExpanded           = typedArray.getBoolean    ( R.styleable.ExpandableView_start_expanded           ,false );
        mHeaderClickable         = typedArray.getBoolean    ( R.styleable.ExpandableView_header_clickable         ,false );
        mTitle                   = typedArray.getString     ( R.styleable.ExpandableView_title_header                            );
        mHeaderElevation         = typedArray.getDimension  ( R.styleable.ExpandableView_header_elevation         ,1     );


        typedArray.recycle();

        verifyExceptions();

        initView(context);

        inflate(context, mLayoutChild, mContainerChildView);

        initActions();

        setLayoutValues();

    }

    /**
     * Sets initial layout values.
     */
    private void setLayoutValues() {

        setHeaderElevation(mHeaderElevation);

        mTextViewTitle.setText(mTitle);

        if(mHeaderColor!= NO_ID){
            setHeaderColor(mHeaderColor);
        }

        if(mDrawableIndicator != NO_ID) {
            setDrawableIndicator(mDrawableIndicator);

        }else{
            setDrawableIndicator(DRAWABLE_INDICATOR_COLLAPSED);
        }

        if(mTitleHeaderColor!= NO_ID){
            setTitleHeaderColor(mTitleHeaderColor);
        }


        if(mDrawableIndicatorColor != View.NO_ID){
            setDrawableIndicatorColor(mDrawableIndicatorColor);
        }

        if(mIcon!= View.NO_ID){
            mImageViewIcon.setVisibility(View.VISIBLE);
            setIcon(mIcon);
            if(mIconColor != View.NO_ID){
                setIconColor(mIconColor);
            }

        }else{
            mImageViewIcon.setVisibility(GONE);
        }

        if(mStartExpanded){
            mIsExpand=true;
            mContainerChildView.setVisibility(VISIBLE);
            if(mIsAnimation) {
                animExpand();
            }else{
                setDrawableIndicator(DRAWABLE_INDICATOR_EXPANDED);

            }
        }
    }

    /**
     * Checks if the required fields have been set correctly,
     * if they do not have, exceptions will be thrown.
     * @see ExpandableViewException
     * @see ExpandableViewException.LayoutChildNotFound
     * @see ExpandableViewException.TitleNotFound
     */
    private void verifyExceptions() {
        if(mLayoutChild == View.NO_ID ){
            throw new ExpandableViewException.LayoutChildNotFound();
        }

        if(mTitle == null){
            throw new ExpandableViewException.TitleNotFound();
        }
    }

    /**
     * Starts layout / views settings.
     * @param context The application context
     */
    private void initView(Context context) {

        View v = LayoutInflater.from(context).inflate(R.layout.component_card_expand, this, true);

        mButtonExpand       = v.findViewById ( R.id.btn_expand  );
        mTextViewTitle      = v.findViewById ( R.id.tv_title    );
        mHeader             = v.findViewById ( R.id.cv_header   );
        mContainerChildView = v.findViewById ( R.id.child_view  );
        mImageViewIcon      = v.findViewById ( R.id.iv_icon     );
    }

    /**
     * Start Listener
     */
    private void initActions() {

        mButtonExpand.setOnClickListener(this);

        if(mHeaderClickable){
            setHeaderClickable(true);
        }
    }

    @Override
    public void onClick(View v) {
        changeState();

    }

    /**
     * Perform state change.
     */
    private void changeState(){

        if(mOnChangeStateListener!=null)
            mOnChangeStateListener.changeState();

        if(mIsExpand){
            mIsExpand = false;

            if(mOnChangeStateListener !=null)
                mOnChangeStateListener.collapsed();
            if(mIsAnimation){
                animCollapse();
            }else{
                mContainerChildView.setVisibility(GONE);
                mButtonExpand.setImageDrawable(getResources().getDrawable(DRAWABLE_INDICATOR_COLLAPSED));
                setDrawableIndicatorColor(mDrawableIndicatorColor);
            }
        }else{
            mIsExpand = true;
            mContainerChildView.setVisibility(VISIBLE);

            if(mOnChangeStateListener !=null)
                mOnChangeStateListener.expanded();
            if(mIsAnimation){
                animExpand();
            }else{
                mButtonExpand.setImageDrawable(getResources().getDrawable(DRAWABLE_INDICATOR_EXPANDED));
                setDrawableIndicatorColor(mDrawableIndicatorColor);
            }
        }
    }

    /**
     * Perform expand animation
     */
    private void animExpand(){
        mButtonExpand.animate().rotation(180F).setInterpolator(new AccelerateDecelerateInterpolator());
        expand(mHeader,mContainerChildView);
    }

    /**
     * Perform collapse animation
     */
    private void animCollapse(){
        mButtonExpand.animate().rotation(0).setInterpolator(new AccelerateDecelerateInterpolator());
        collapse(mHeader,mContainerChildView);
    }

    /**
     * Returns the child view.
     * @return Child view.
     */
    public View getChildView(){
        return mContainerChildView;
    }

    /**
     * Returns the header title
     * @return Header title
     */
    public String getTitle(){
        return mTitle;
    }

    /**
     * Returns the header icon
     * @return Header icon
     */
    @DrawableRes
    public int getIcon(){
        return mIcon;
    }

    /**
     * Returns the animated icon or not indicating
     * whether the card is expanded or collapsed.
     * @return Icon indicator
     */
    @DrawableRes
    public int getDrawableIndicator() {
        return mDrawableIndicator;
    }

    /**
     * Returns the title color of the header.
     * @return Title color
     */
    @ColorRes
    public int getTitleHeaderColor() {
        return mTitleHeaderColor;
    }

    /**
     * Returns the icon indicator color.
     * @return  Icon indicator color
     */
    @ColorRes
    public int getDrawableIndicatorColor() {
        return mDrawableIndicatorColor;
    }

    /**
     * Returns the status change listener.
     * @return OnChangeStateListener
     */
    public OnChangeStateListener getOnChangeStateListener() {
        return mOnChangeStateListener;
    }

    /**
     * Returns whether the item is expanded or not.
     * @return A boolean
     */
    public boolean isExpand() {
        return mIsExpand;
    }

    /**
     * Returns the card header.
     * @return A CardView
     */
    public CardView getHeader() {
        return mHeader;
    }

    /**
     * Returns whether the item has animation or not.
     * @return A boolean
     */
    public boolean isAnimation() {
        return mIsAnimation;
    }

    /**
     * Returns whether the item starts expanded or not.
     * @return A boolean
     */
    public boolean isStartExpanded() {
        return mStartExpanded;
    }

    /**
     * Returns whether the header is clickable or not.
     * @return A boolean
     */
    public boolean isHeaderClickable() {
        return mHeaderClickable;
    }

    /**
     * Returns the header color.
     * @return Header color
     */
    @ColorRes
    public int getHeaderColor() {
        return mHeaderColor;
    }

    /**
     * Returns the header elevation.
     * @return Header elevation
     */
    public float getHeaderElevation() {
        return mHeaderElevation;
    }

    /**
     * Change the status of the item (expanded / collapsed).
     * @param isExpand A boolean (true -> expanded / false -> collapsed)
     */
    public void setIsExpand(boolean isExpand) {
        this.mIsExpand = isExpand;
        changeState();
    }

    /**
     * Change header title.
     * @param title An object of the type String
     */
    public void setTitle(String title) {
        this.mTitle = title;
        this.mTextViewTitle.setText(mTitle);
    }

    /**
     * Change header title.
     * @param title An object of the type int (StringRes)
     */
    public void setTitle(@StringRes int title) {
        this.mTitle = getResources().getString(title);
        this.mTextViewTitle.setText(title);
    }

    /**
     * Changes if the action of expanding and collapsing should be done with animation.
     * @param isAnimation An object of the type boolean
     */
    public void setIsAnimation(boolean isAnimation) {
        this.mIsAnimation = isAnimation;
    }

    /**
     * Changes if item should start expanded in layout construction.
     * @param startExpanded An object of the type boolean
     */
    public void setStartExpanded(boolean startExpanded) {
        this.mStartExpanded = startExpanded;
    }

    /**
     * Change if header is clickable
     * @param headerClickable An object of the type boolean
     */
    public void setHeaderClickable(boolean headerClickable) {
        this.mHeaderClickable = headerClickable;
        mHeader.setFocusable(mHeaderClickable);
        mHeader.setClickable(mHeaderClickable);
        mHeader.setOnClickListener(this);
    }

    /**
     * Change title header color.
     * @param titleHeaderColor An object of the type int (ColorRes)
     */
    public void setTitleHeaderColor(@ColorRes int titleHeaderColor) {
        this.mTitleHeaderColor = titleHeaderColor;
        mTextViewTitle.setTextColor(getResources().getColor(mTitleHeaderColor));
    }

    /**
     * Change the color of the header icon.
     * @param iconColor An object of the type int (ColorRes)
     */
    public void setIconColor(@ColorRes int iconColor) {
        this.mIconColor = iconColor;
        mImageViewIcon.getDrawable().setColorFilter(getResources().getColor(mIconColor), PorterDuff.Mode.SRC_IN);
    }

    /**
     * Change the header indicator color.
     * @param drawableIndicatorColor An object of the type int (ColorRes)
     */
    public void setDrawableIndicatorColor(@ColorRes int drawableIndicatorColor) {
        this.mDrawableIndicatorColor = drawableIndicatorColor;
        mButtonExpand.getDrawable().setColorFilter(getResources().getColor(mDrawableIndicatorColor), PorterDuff.Mode.SRC_IN);
    }

    /**
     * Changes the drawable indicator.
     * When using this feature, consider turning off the animation.
     * @param drawableIndicator An object of the type int (DrawableRes)
     */
    public void setDrawableIndicator(@DrawableRes int drawableIndicator) {
        this.mDrawableIndicator = drawableIndicator;
        mButtonExpand.setImageDrawable(getResources().getDrawable(mDrawableIndicator));
    }

    /**
     * Changes the header icon.
     * @param icon An object of the type int (DrawableRes)
     */
    public void setIcon(@DrawableRes int icon) {
        this.mIcon = icon;
        mImageViewIcon.setImageDrawable(getResources().getDrawable(mIcon));
    }

    /**
     * Change the status change listener.
     * @param onChangeStateListener An object of the type OnChangeStateListener
     */
    public void setOnChangeStateListener(OnChangeStateListener onChangeStateListener) {
        this.mOnChangeStateListener = onChangeStateListener;
    }

    /**
     * Change the header color
     * @param headerColor An object of the type int (ColorRes)
     */
    public void setHeaderColor(@ColorRes int headerColor) {
        this.mHeaderColor = headerColor;
        mHeader.setBackgroundColor(getResources().getColor(mHeaderColor));
        findViewById(R.id.card_image).getBackground().setColorFilter(getResources().getColor(mHeaderColor), PorterDuff.Mode.SRC_IN);

    }

    /**
     * Change the header elevation
     * @param headerElevation An object of the type float - value in dp
     */
    private void setHeaderElevation(float headerElevation) {
        mHeaderElevation = headerElevation;
        mHeader.setCardElevation(getPixelsFromDPs(mHeaderElevation));
    }

    /**
     * This function performs the conversion of dp to pixels.
     * @param dp Value in dp
     * @return The value, in pixels, of the value passed by parameter in dp
     */
    private int getPixelsFromDPs(float dp){
        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics()));
    }
}
