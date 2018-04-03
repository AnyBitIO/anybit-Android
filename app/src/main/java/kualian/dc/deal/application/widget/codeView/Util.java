package kualian.dc.deal.application.widget.codeView;

import android.content.Context;
import android.graphics.Paint;

/**
 *
 */
public class Util {

    /**
     * DP转PX
     */
    public static int dpToPx(Context context, float dpSize) {
        return (int) (context.getResources().getDisplayMetrics().density * dpSize);
    }

    /**
     * @param backgroundTop
     * @param backgroundBottom
     * @param paint
     * @return paint绘制居中文字时，获取文本底部坐标
     */
    public static float getTextBaseLine(float backgroundTop, float backgroundBottom, Paint paint) {
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (backgroundTop + backgroundBottom - fontMetrics.bottom - fontMetrics.top) / 2;
    }


}
