package lx.changeabletextview;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.text.Spanned.SPAN_INCLUSIVE_EXCLUSIVE;

/**
 * NightRaid1 on 2017/2/20 17:16
 */

/**
 * 1,先设置文字，再去配置颜色以及各种属性。
 * 2,所有操作只对上一次设置的文本片段生效。
 * 3,一段文本之后可以设置多个属性。
 **/


public class ChangeableTextView extends TextView {
    private static final String TAG = "ChangeableTextView";
    //设置动画要作用的最低行数 默认三行
    private int lines = 3;
    private int animBackgroundColor = 0x80FF4081;
    private int mLineHeight;
    private int mLineSpacingExtra;
    //默认动画间距 不作用于文本
    private static final int DEFAULT_LINE_SPACINGEXTRA = 5;
    //默认文本颜色
    public static final int DEFAULT_COLOR = Color.BLACK;
    //存储文本
    private StringBuilder mStringBuilder;
    private int mOldLength;
    private SpannableStringBuilder mSpannableStringBuilder;
    //绘制矩形
    private Path mPath;
    //宽度
    private int mWidth;
    private Paint mPaint;
    //是否打开动画
    private boolean isOpenAnim;
    private int values;
    //duration
    private int mDuration = 1300;
    private String[] mMaskword;
    private String mReplacetext;
    //是否开启了正则屏蔽 或者 高亮
    private boolean isShield;
    public ChangeableTextView(Context context) {
        super(context);
        init(context);
    }

    public ChangeableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.ChangeableTextView);
        isOpenAnim = typedArray.getBoolean(R.styleable.ChangeableTextView_openAnim,false);
        lines = typedArray.getInt(R.styleable.ChangeableTextView_minLines,3);
        animBackgroundColor = typedArray.getColor(R.styleable.ChangeableTextView_animBackgroundColor,animBackgroundColor);
        mDuration = typedArray.getInt(R.styleable.ChangeableTextView_duration,mDuration);
        mMaskword = typedArray.getString(R.styleable.ChangeableTextView_maskword).split(",");
        mReplacetext = typedArray.getString(R.styleable.ChangeableTextView_replacetext);
        typedArray.recycle();
        if(mMaskword!=null&&mMaskword.length>0&&mReplacetext!=null&&mReplacetext.length()>0){
            isShield = true;


        }

        init(context);
    }


    //设置文字
    public ChangeableTextView addText(String str) {
        if(isShield){
           for(int i=0;i<mMaskword.length;i++){
               str = str.replaceAll(mMaskword[i],mReplacetext);
           }
        }
        mOldLength = mStringBuilder.length();
        mStringBuilder.append(str);
        setText(mStringBuilder.toString());
        mSpannableStringBuilder.append(str);
        return this;
    }

    public void setAnimLineHeight(int lineDpHeight){
        mLineHeight = (int) (getResources().getDisplayMetrics().density*lineDpHeight+0.5f);
    }
    private void setAnimLineSpacingExtra(int lineSpacing){
        mLineSpacingExtra =  (int) (getResources().getDisplayMetrics().density*lineSpacing+0.5f);
    }

    public ChangeableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setAnimLineSpacingExtra(DEFAULT_LINE_SPACINGEXTRA);
        mSpannableStringBuilder = new SpannableStringBuilder();
        mStringBuilder = new StringBuilder();
        setHighlightColor(Color.TRANSPARENT);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(animBackgroundColor);
        mLineHeight = getLineHeight()+getPaddingBottom()+getPaddingBottom();
        setMinHeight(mLineHeight*lines+mLineSpacingExtra*(lines-1));
    }
    private void startAnim(){
        if(isOpenAnim){
            final ValueAnimator valueAnimator = ValueAnimator.ofInt(0,mWidth/2,0);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    values = (int) animation.getAnimatedValue();
                    if(getText().length()>0){
                        isOpenAnim = false;
                        valueAnimator.cancel();
                    }
                    invalidate();
                }
            });
            valueAnimator.setDuration(mDuration);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.start();

        }
    }



    public ChangeableTextView setColor(int color) {
        setSpan(new ForegroundColorSpan(color));
        return this;
    }

    private void setSpan(Object o) {
        mSpannableStringBuilder.setSpan(o, mOldLength, mStringBuilder.length(), SPAN_INCLUSIVE_EXCLUSIVE);
        setText(mSpannableStringBuilder);
    }

    //设置颜色
    public ChangeableTextView setColor(String str) {
        if (str == null || str.length() == 0 || '#' != str.charAt(0) || (str.length() != 7 && str.length() != 9)) {
            Log.e(TAG, "Set Color Exception");
            setColor(DEFAULT_COLOR);
            return this;
        }
        setColor(Color.parseColor(str));
        return this;
    }

    //设置文字大小
    public ChangeableTextView setTextSize(int size) {
        setSpan(new AbsoluteSizeSpan(size, true));
        return this;
    }

    //设置粗体
    public ChangeableTextView setBold() {
        setSpan(new StyleSpan(Typeface.BOLD));
        return this;
    }

    //设置粗斜体
    public ChangeableTextView setBoldItalic() {
        setSpan(new StyleSpan(Typeface.BOLD_ITALIC));
        return this;
    }

    //设置斜体
    public ChangeableTextView setItalic() {
        setSpan(new StyleSpan(Typeface.ITALIC));
        return this;
    }

    //设置删除线
    public ChangeableTextView setDeleteLine() {
        setSpan(new StrikethroughSpan());
        return this;
    }

    //设置下划线
    public ChangeableTextView setUnderline() {
        setSpan(new UnderlineSpan());
        return this;
    }

    //设置超链接
    public ChangeableTextView setHyperLinks(String url, final OnTextClickListener o) {
        setMovementMethod(LinkMovementMethod.getInstance());
        setSpan(new URLSpan(url) {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                if (o != null) {
                    ds.setColor(o.getColor());
                }

            }
        });

        return this;
    }


    //设置点击事件
    public ChangeableTextView setOnTextClickListener(final OnTextClickListener o) {
        if (o == null) {
            return this;
        }
        setMovementMethod(LinkMovementMethod.getInstance());
        setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                o.onClick(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(o.getColor());

            }
        });
        return this;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        if(isOpenAnim){
            for (int i = 0; i < lines; i++) {
                int lineWidth = (i==lines-1?mWidth/2:mWidth);
                int lastValue = (i==lines-1?values/2:values);
                int updateValue = i%2==0?lineWidth/2+lastValue:lineWidth-lastValue;
                mPath.addRect(0,mLineHeight*i+mLineSpacingExtra*i,updateValue,mLineHeight*(i+1)+mLineSpacingExtra*i, Path.Direction.CW);
            }
            canvas.drawPath(mPath,mPaint);
        }else{

        }


    }
    private boolean isSizeChanged;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        values = mWidth/2;
        if(!isSizeChanged){
            isSizeChanged = true;
            startAnim();
        }

    }
}
