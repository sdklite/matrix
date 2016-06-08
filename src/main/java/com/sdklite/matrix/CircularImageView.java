package com.sdklite.matrix;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An ImageView class with a circle mask so that all images are drawn in a
 * circle instead of a square.
 *
 * @author johnsonlee
 */
public class CircularImageView extends ImageView {

    /**
     * Convert drawable to bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof DrawableContainer) {
            drawable = drawable.getCurrent();
        }

        final int intrinsicWidth = drawable.getIntrinsicWidth();
        final int intrinsicHeight = drawable.getIntrinsicHeight();

        if (!(intrinsicWidth > 0 && intrinsicHeight > 0))
            return null;

        try {
            final Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (final OutOfMemoryError e) {
            return null;
        }
    }

    private final Matrix matrix;

    private final RectF source;

    private final RectF destination;

    private final Paint bitmapPaint;

    private final Paint borderPaint;

    private int borderColor;

    private int borderWidth;

    public CircularImageView(final Context context) {
        this(context, null, 0);
    }

    public CircularImageView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        final int dftStrokeSize = (int) (context.getResources().getDisplayMetrics().density);
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircularImageView);
        this.borderColor = ta.getColor(R.styleable.CircularImageView_circularImageViewBorderColor, Color.WHITE);
        this.borderWidth = ta.getDimensionPixelOffset(R.styleable.CircularImageView_circularImageViewBorderWidth, dftStrokeSize);
        ta.recycle();

        this.matrix = new Matrix();
        this.source = new RectF();
        this.destination = new RectF();
        this.bitmapPaint = new Paint();
        this.bitmapPaint.setAntiAlias(true);
        this.bitmapPaint.setFilterBitmap(true);
        this.bitmapPaint.setDither(true);
        this.borderPaint = new Paint();
        this.borderPaint.setColor(this.borderColor);
        this.borderPaint.setStyle(Paint.Style.STROKE);
        this.borderPaint.setStrokeWidth(this.borderWidth);
        this.borderPaint.setAntiAlias(true);
    }

    public int getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(final int borderColor) {
        this.borderColor = borderColor;
        this.borderPaint.setColor(this.borderColor);
        invalidate();
    }

    public int getBorderWidth() {
        return this.borderWidth;
    }

    public void setBorderWidth(final int borderWidth) {
        this.borderWidth = borderWidth;
        this.borderPaint.setStrokeWidth(this.borderWidth);
        requestLayout();
        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        final Bitmap bitmap = drawableToBitmap(getDrawable());
        if (null == bitmap) {
            return;
        }

        this.source.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        this.destination.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        this.drawBitmapWithCircleOnCanvas(bitmap, canvas, this.source, this.destination);
    }

    /**
     * Given the source bitmap and a canvas, draws the bitmap through a circular
     * mask. Only draws a circle with diameter equal to the destination width.
     *
     * @param bitmap The source bitmap to draw.
     * @param canvas The canvas to draw it on.
     * @param source The source bound of the bitmap.
     * @param dest   The destination bound on the canvas.
     */
    protected void drawBitmapWithCircleOnCanvas(final Bitmap bitmap, final Canvas canvas, final RectF source, final RectF dest) {
        this.matrix.reset();
        this.matrix.setRectToRect(source, dest, Matrix.ScaleToFit.FILL);
        final BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader.setLocalMatrix(this.matrix);
        this.bitmapPaint.setShader(shader);
        canvas.drawCircle(dest.centerX(), dest.centerY(), dest.width() / 2f, this.bitmapPaint);

        if (this.borderWidth > 0) {
            canvas.drawCircle(dest.centerX(), dest.centerY(), (dest.width() - this.borderWidth) / 2f, this.borderPaint);
        }
    }
}
