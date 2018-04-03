package kualian.dc.deal.application.widget.codeView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.util.LogUtils;

/**
 *
 */
public class CodeView extends View {

    public final static int SHOW_TYPE_WORD = 1;
    public final static int SHOW_TYPE_PASSWORD = 2;

    //密码长度，默认6位
    private int length;
    //描边颜色，默认#E1E1E1
    private int borderColor;
    //描边宽度，默认1px
    private float borderWidth;
    //分割线颜色，默认#E1E1E1
    private int dividerColor;
    //分割线宽度，默认1px
    private float dividerWidth;
    //默认文本，在XML设置后可预览效果
    private String code;
    //密码点颜色，默认#000000
    private int codeColor;
    //密码点半径，默认8dp
    private float pointRadius;
    //显示明文时的文字大小，默认unitWidth/2
    private float textSize;
    //显示类型，支持密码、明文，默认明文
    private int showType;

    private float unitWidth;
    private Paint paint;
    private Listener listener;
    private boolean isDrawBorder;
    public CodeView(Context context) {
        super(context);
        init(null);
    }

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //根据宽度来计算单元格大小（高度）
        float width = getMeasuredWidth();
        //宽度-左右边宽-中间分割线宽度
        unitWidth = (width - (2 * borderWidth) - ((length - 1) * dividerWidth)) / (length+2);
        if (textSize == 0) {
            textSize = unitWidth / 2;
        }
        setMeasuredDimension((int) width, (int) (unitWidth + (2 * borderWidth)));
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        if (attrs == null) {
            length = 6;
            borderColor = Color.parseColor("#E1E1E1");
            borderWidth = 1;
            dividerColor = Color.parseColor("#E1E1E1");
            dividerWidth = 1;
            code = "";
            codeColor = Color.parseColor("#000000");
            pointRadius = Util.dpToPx(getContext(), 8);
            showType = SHOW_TYPE_WORD;
            textSize = 0;
        } else {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.CodeView);
            length = typedArray.getInt(R.styleable.CodeView_length, 6);
            borderColor = typedArray.getColor(R.styleable.CodeView_borderColor, Color.parseColor("#E1E1E1"));
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.CodeView_borderWidth, 1);
            isDrawBorder = typedArray.getBoolean(R.styleable.CodeView_isDrawBorder, false);
            dividerColor = typedArray.getColor(R.styleable.CodeView_dividerColor, Color.parseColor("#E1E1E1"));
            dividerWidth = typedArray.getDimensionPixelSize(R.styleable.CodeView_dividerWidth, 1);
            code = typedArray.getString(R.styleable.CodeView_code);
            if (code == null) {
                code = "";
            }
            codeColor = typedArray.getColor(R.styleable.CodeView_codeColor, Color.parseColor("#000000"));
            pointRadius = typedArray.getDimensionPixelSize(R.styleable.CodeView_pointRadius, Util.dpToPx(getContext(), 8));
            showType = typedArray.getInt(R.styleable.CodeView_showType, SHOW_TYPE_WORD);
            textSize = typedArray.getDimensionPixelSize(R.styleable.CodeView_textSize, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDivider(canvas);
        if (isDrawBorder){
            drawBorder(canvas);
        }
        switch (showType) {
            case SHOW_TYPE_PASSWORD:
                drawPoint(canvas);
                break;
            default:
                drawValue(canvas);
                break;
        }
    }

    /**
     * 描边
     */
    private void drawBorder(Canvas canvas) {
        if (borderWidth > 0) {
            paint.setColor(borderColor);
            /*canvas.drawRect(0, 0, getWidth(), borderWidth, paint);
            canvas.drawRect(0, getHeight() - borderWidth, getWidth(), getHeight(), paint);
            canvas.drawRect(0, 0, borderWidth, getHeight(), paint);
            canvas.drawRect(getWidth() - borderWidth, 0, getWidth(), getHeight(), paint);*/
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRoundRect(new RectF(unitWidth/2,0,getWidth()-unitWidth/2,getHeight()),unitWidth/2,unitWidth/2,paint);
            paint.setStyle(Paint.Style.FILL);

        }
    }

    /**
     * 画分割线
     */
    private void drawDivider(Canvas canvas) {
        if (dividerWidth > 0) {
            paint.setColor(dividerColor);
            for (int i = 0; i < length+1 ; i++) {
                final float left = unitWidth * (i + 1) + dividerWidth * i + borderWidth;
                canvas.drawRect(left, 5, left + dividerWidth, getHeight()-5, paint);
            }
        }
    }

    /**
     * 画输入文字
     */
    private void drawValue(Canvas canvas) {
        if (pointRadius > 0) {
            paint.setColor(codeColor);
            paint.setTextSize(textSize);
            for (int i = 0; i < code.length(); i++) {
                final float left = unitWidth * i + dividerWidth * i + borderWidth;
                canvas.drawText(code.charAt(i) + "",
                        left + unitWidth / 2,
                        Util.getTextBaseLine(0, getHeight(), paint),
                        paint);
            }
        }
    }

    /**
     * 画密码点
     */
    private void drawPoint(Canvas canvas) {
        if (pointRadius > 0) {
            paint.setColor(codeColor);
            for (int i = 0; i < code.length(); i++) {
                final float left = unitWidth * i + dividerWidth * i + borderWidth+3*unitWidth/2;
                canvas.drawCircle(left , getHeight() / 2, pointRadius, paint);
            }
        }
    }

    public void input(String number) {
        if (code.length() < length) {
            code += number;
            if (listener != null) {
                listener.onValueChanged(code);
                if (code.length() == length) {
                    listener.onComplete(code);
                }
            }
            invalidate();
        }
    }

    public void delete() {
        if (code.length() > 0) {
            code = code.substring(0, code.length() - 1);
            if (listener != null) {
                listener.onValueChanged(code);
            }
            invalidate();
        }
    }
    public void clear(){
        code = "";
        invalidate();
    }
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public float getDividerWidth() {
        return dividerWidth;
    }

    public void setDividerWidth(float dividerWidth) {
        this.dividerWidth = dividerWidth;
        invalidate();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        invalidate();
    }

    public int getCodeColor() {
        return codeColor;
    }

    public void setCodeColor(int codeColor) {
        this.codeColor = codeColor;
        invalidate();
    }

    public float getPointRadius() {
        return pointRadius;
    }

    public void setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
        invalidate();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {

        public void onValueChanged(String value);

        public void onComplete(String value);

    }

}
