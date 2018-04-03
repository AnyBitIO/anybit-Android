package kualian.dc.deal.application.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import kualian.dc.deal.application.R;


public class PswText extends View {
    private InputMethodManager input;//输入法管理
    private ArrayList<Integer> result;//保存当前输入的密码
    private int saveResult;//保存按下返回键时输入的密码总数
    private int pswLength;//密码长度
    private int borderColor;//密码框颜色
    private int borderShadowColor;//密码框阴影颜色
    private int pswColor;//明文密码颜色
    private int pswTextSize;//明文密码字体大小
    private int inputBorderColor;//输入时密码边框颜色

    private boolean isShowBorderShadow;//是否绘制在输入时，密码框的阴影颜色

    private Paint pswDotPaint;//密码圆点画笔
    private Paint pswTextPaint;//明文密码画笔
    private Paint borderPaint;//边框画笔
    private Paint inputBorderPaint;//输入时边框画笔
    private RectF borderRectF;//边框圆角矩形
    private int borderWidth;//边框宽度
    private int spacingWidth;//边框之间的间距宽度
    private InputCallBack inputCallBack;//输入完成时监听
    private int height;//整个view的高度
    private int mDistance;

    public interface InputCallBack {
        void onInputFinish(String password);
    }


    public PswText(Context context) {
        super(context);
    }

    public PswText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public PswText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    //初始化
    private void initView(Context context, AttributeSet attrs) {
        this.setOnKeyListener(new NumKeyListener());
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        input = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        result = new ArrayList<>();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PswText);
        if (array != null) {
            pswLength = array.getInt(R.styleable.PswText_pswLength, 6);
            pswColor = array.getColor(R.styleable.PswText_pswColor, Color.parseColor("#3779e3"));
            inputBorderColor = array.getColor(R.styleable.PswText_inputBorder_color, Color.parseColor("#3779e3"));
            borderShadowColor = array.getColor(R.styleable.PswText_borderShadow_color, Color.parseColor("#3577e2"));
            isShowBorderShadow = array.getBoolean(R.styleable.PswText_isShowBorderShadow, false);

            pswTextSize = (int) array.getDimension(R.styleable.PswText_psw_textSize,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics()));
            pswColor = Color.parseColor("#3779e3");
            borderColor = Color.parseColor("#E0E0E0");
            inputBorderColor = Color.parseColor("#3779e3");
            borderShadowColor = Color.parseColor("#3577e2");

            isShowBorderShadow = false;
            //明文密码字体大小，初始化18sp
            pswTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
            //边框圆角程度初始化8dp
        }
        //边框宽度初始化40dp
        borderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        //边框之间的间距初始化10dp
        spacingWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        borderRectF = new RectF();
        initPaint();
    }

    //初始化画笔
    private void initPaint() {
        //密码圆点画笔初始化
        pswDotPaint = new Paint();
        pswDotPaint.setAntiAlias(true);
        pswDotPaint.setStrokeWidth(3);
        pswDotPaint.setStyle(Paint.Style.FILL);
        pswDotPaint.setColor(pswColor);

        //明文密码画笔初始化
        pswTextPaint = new Paint();
        pswTextPaint.setAntiAlias(true);
        pswTextPaint.setFakeBoldText(true);
        pswTextPaint.setColor(pswColor);

        //边框画笔初始化
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(3);

        //输入时边框画笔初始化
        inputBorderPaint = new Paint();
        inputBorderPaint.setAntiAlias(true);
        inputBorderPaint.setColor(inputBorderColor);
        inputBorderPaint.setStyle(Paint.Style.STROKE);
        inputBorderPaint.setStrokeWidth(3);
        //是否绘制边框阴影
        if (isShowBorderShadow) {
            inputBorderPaint.setShadowLayer(6, 0, 0, borderShadowColor);
            setLayerType(LAYER_TYPE_SOFTWARE, inputBorderPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpec == MeasureSpec.AT_MOST) {
            if (heightSpec != MeasureSpec.AT_MOST) {//高度已知但宽度未知时
                spacingWidth = heightSize / 4;
                widthSize = (heightSize * pswLength) + (spacingWidth * pswLength);
                borderWidth = heightSize;
            } else {//宽高都未知时
                widthSize = (borderWidth * pswLength) + (spacingWidth * pswLength);
                heightSize = (int) (borderWidth + ((borderPaint.getStrokeWidth()) * 2));
            }
        } else {
            //宽度已知但高度未知时
            if (heightSpec == MeasureSpec.AT_MOST) {
                borderWidth = (widthSize * 4) / (5 * pswLength);
                spacingWidth = borderWidth / 4;
                heightSize = (int) (borderWidth + ((borderPaint.getStrokeWidth()) * 2));
            }
        }
        height = heightSize;
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDistance = getMeasuredWidth() / 8;
        dotRadius = borderWidth / 6;//密码圆点为边框宽度的六分之一

    }

    int dotRadius;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

		/*
        * 如果明文密码字体大小为默认大小，则取边框宽度的八分之一，否则用自定义大小
		* */
        if (pswTextSize == (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics())) {
            pswTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, borderWidth / 8, getResources().getDisplayMetrics());
        }
        pswTextPaint.setTextSize(pswTextSize);
        //绘制密码边框
        drawBorder(canvas, height);
        for (int i = 0; i < result.size(); i++) {
            //明文密码
            String num = result.get(i) + "";
            drawText(canvas, num, i);
        }


    }

    //绘制明文密码
    private void drawText(Canvas canvas, String num, int i) {

        float circleX = 3 * mDistance / 2 + i * mDistance;
        float circleY = height / 2;
        if (saveResult != 0 || saveResult < result.size()) {
            canvas.drawCircle(circleX, circleY, dotRadius,pswDotPaint);
        }
    }



    //绘制初始密码框时判断是否用图片绘制密码框
    private void drawBorder(Canvas canvas, int height) {
        borderRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        //canvas.drawRoundRect(borderRectF, getMeasuredWidth() / 8, getMeasuredWidth() / 8, borderPaint);
        for (int i = 1; i < pswLength + 2; i++) {
            int left = (int) ((i * getMeasuredWidth() / 8));
            canvas.drawLine(left, 10, left, height - 10, borderPaint);
        }

    }



    //清除密码
    public void clearPsw() {
        result.clear();
        invalidate();
    }

    //获取密码
    public String getPsw() {
        StringBuffer sb = new StringBuffer();
        for (int i : result) {
            sb.append(i);
        }
        return sb.toString();
    }

    //隐藏键盘
    public void hideKeyBord() {
        input.hideSoftInputFromWindow(this.getWindowToken(), 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//点击弹出键盘
            requestFocus();
            input.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            input.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;//只允许输入数字
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new NumInputConnection(this, false);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    class NumInputConnection extends BaseInputConnection {

        public NumInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            //这里是接收文本的输入法，我们只允许输入数字，则不做任何处理
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            //屏蔽返回键，发送自己的删除事件
            if (beforeLength == 1 && afterLength == 0) {
                return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    /**
     * 输入监听
     */
    class NumKeyListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.isShiftPressed()) {//处理*#等键
                    return false;
                }
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {//只处理数字
                    if (result.size() < pswLength) {
                        result.add(keyCode - 7);
                        invalidate();
                        FinishInput();
                    }
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (!result.isEmpty()) {//不为空时，删除最后一个数字
                        saveResult = result.size();
                        result.remove(result.size() - 1);
                        invalidate();
                    }
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    FinishInput();
                    return true;
                }
            }
            return false;
        }

        /**
         * 输入完成后调用的方法
         */
        void FinishInput() {
            if (result.size() == pswLength && inputCallBack != null) {//输入已完成
                StringBuffer sb = new StringBuffer();
                for (int i : result) {
                    sb.append(i);
                }
                inputCallBack.onInputFinish(sb.toString());
                InputMethodManager imm = (InputMethodManager) PswText.this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(PswText.this.getWindowToken(), 0); //输入完成后隐藏键盘
            }
        }
    }
}