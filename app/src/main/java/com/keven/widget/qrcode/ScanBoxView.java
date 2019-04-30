package com.keven.widget.qrcode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.core.R.mipmap;
import cn.bingoogolapple.qrcode.core.R.styleable;

/**
 * 扫描框控件
 */
public class ScanBoxView extends View {
    private int mMoveStepDistance;
    private int mAnimDelayTime;
    private Rect mFramingRect;
    private float mScanLineTop;
    private float mScanLineLeft;
    private Paint mPaint = new Paint();
    private TextPaint mTipPaint;
    private int mMaskColor;
    private int mCornerColor;
    private int mCornerLength;
    private int mCornerSize;
    private int mRectWidth;
    private int mRectHeight;
    private int mBarcodeRectHeight;
    private int mTopOffset;
    private int mScanLineSize;
    private int mScanLineColor;
    private int mScanLineMargin;
    private boolean mIsShowDefaultScanLineDrawable;
    private Drawable mCustomScanLineDrawable;
    private Bitmap mScanLineBitmap;
    private int mBorderSize;
    private int mBorderColor;
    private int mAnimTime;
    private boolean mIsCenterVertical;
    private int mToolbarHeight;
    private boolean mIsBarcode;
    private String mQRCodeTipText;
    private String mBarCodeTipText;
    private String mTipText;
    private int mTipTextSize;
    private int mTipTextColor;
    private boolean mIsTipTextBelowRect;
    private int mTipTextMargin;
    private boolean mIsShowTipTextAsSingleLine;
    private int mTipBackgroundColor;
    private boolean mIsShowTipBackground;
    private boolean mIsScanLineReverse;
    private boolean mIsShowDefaultGridScanLineDrawable;
    private Drawable mCustomGridScanLineDrawable;
    private Bitmap mGridScanLineBitmap;
    private float mGridScanLineBottom;
    private float mGridScanLineRight;
    private Bitmap mOriginQRCodeScanLineBitmap;
    private Bitmap mOriginBarCodeScanLineBitmap;
    private Bitmap mOriginQRCodeGridScanLineBitmap;
    private Bitmap mOriginBarCodeGridScanLineBitmap;
    private float mHalfCornerSize;
    private StaticLayout mTipTextSl;
    private int mTipBackgroundRadius;
    private boolean mIsOnlyDecodeScanBoxArea;

    public ScanBoxView(Context context) {
        super(context);
        this.mPaint.setAntiAlias(true);
        this.mMaskColor = Color.parseColor("#33FFFFFF");
        this.mCornerColor = -1;
        this.mCornerLength = BGAQRCodeUtil.dp2px(context, 20.0F);
        this.mCornerSize = BGAQRCodeUtil.dp2px(context, 3.0F);
        this.mScanLineSize = BGAQRCodeUtil.dp2px(context, 1.0F);
        this.mScanLineColor = -1;
        this.mTopOffset = BGAQRCodeUtil.dp2px(context, 90.0F);
        this.mRectWidth = BGAQRCodeUtil.dp2px(context, 200.0F);
        this.mBarcodeRectHeight = BGAQRCodeUtil.dp2px(context, 140.0F);
        this.mScanLineMargin = 0;
        this.mIsShowDefaultScanLineDrawable = false;
        this.mCustomScanLineDrawable = null;
        this.mScanLineBitmap = null;
        this.mBorderSize = BGAQRCodeUtil.dp2px(context, 1.0F);
        this.mBorderColor = -1;
        this.mAnimTime = 1000;
        this.mIsCenterVertical = false;
        this.mToolbarHeight = 0;
        this.mIsBarcode = false;
        this.mMoveStepDistance = BGAQRCodeUtil.dp2px(context, 2.0F);
        this.mTipText = null;
        this.mTipTextSize = BGAQRCodeUtil.sp2px(context, 14.0F);
        this.mTipTextColor = -1;
        this.mIsTipTextBelowRect = false;
        this.mTipTextMargin = BGAQRCodeUtil.dp2px(context, 20.0F);
        this.mIsShowTipTextAsSingleLine = false;
        this.mTipBackgroundColor = Color.parseColor("#22000000");
        this.mIsShowTipBackground = false;
        this.mIsScanLineReverse = false;
        this.mIsShowDefaultGridScanLineDrawable = false;
        this.mTipPaint = new TextPaint();
        this.mTipPaint.setAntiAlias(true);
        this.mTipBackgroundRadius = BGAQRCodeUtil.dp2px(context, 4.0F);
        this.mIsOnlyDecodeScanBoxArea = false;
    }

    public void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, styleable.QRCodeView);
        int count = typedArray.getIndexCount();

        for (int i = 0; i < count; ++i) {
            this.initCustomAttr(typedArray.getIndex(i), typedArray);
        }

        typedArray.recycle();
        this.afterInitCustomAttrs();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == styleable.QRCodeView_qrcv_topOffset) {
            this.mTopOffset = typedArray.getDimensionPixelSize(attr, this.mTopOffset);
        } else if (attr == styleable.QRCodeView_qrcv_cornerSize) {
            this.mCornerSize = typedArray.getDimensionPixelSize(attr, this.mCornerSize);
        } else if (attr == styleable.QRCodeView_qrcv_cornerLength) {
            this.mCornerLength = typedArray.getDimensionPixelSize(attr, this.mCornerLength);
        } else if (attr == styleable.QRCodeView_qrcv_scanLineSize) {
            this.mScanLineSize = typedArray.getDimensionPixelSize(attr, this.mScanLineSize);
        } else if (attr == styleable.QRCodeView_qrcv_rectWidth) {
            this.mRectWidth = typedArray.getDimensionPixelSize(attr, this.mRectWidth);
        } else if (attr == styleable.QRCodeView_qrcv_maskColor) {
            this.mMaskColor = typedArray.getColor(attr, this.mMaskColor);
        } else if (attr == styleable.QRCodeView_qrcv_cornerColor) {
            this.mCornerColor = typedArray.getColor(attr, this.mCornerColor);
        } else if (attr == styleable.QRCodeView_qrcv_scanLineColor) {
            this.mScanLineColor = typedArray.getColor(attr, this.mScanLineColor);
        } else if (attr == styleable.QRCodeView_qrcv_scanLineMargin) {
            this.mScanLineMargin = typedArray.getDimensionPixelSize(attr, this.mScanLineMargin);
        } else if (attr == styleable.QRCodeView_qrcv_isShowDefaultScanLineDrawable) {
            this.mIsShowDefaultScanLineDrawable = typedArray.getBoolean(attr, this.mIsShowDefaultScanLineDrawable);
        } else if (attr == styleable.QRCodeView_qrcv_customScanLineDrawable) {
            this.mCustomScanLineDrawable = typedArray.getDrawable(attr);
        } else if (attr == styleable.QRCodeView_qrcv_borderSize) {
            this.mBorderSize = typedArray.getDimensionPixelSize(attr, this.mBorderSize);
        } else if (attr == styleable.QRCodeView_qrcv_borderColor) {
            this.mBorderColor = typedArray.getColor(attr, this.mBorderColor);
        } else if (attr == styleable.QRCodeView_qrcv_animTime) {
            this.mAnimTime = typedArray.getInteger(attr, this.mAnimTime);
        } else if (attr == styleable.QRCodeView_qrcv_isCenterVertical) {
            this.mIsCenterVertical = typedArray.getBoolean(attr, this.mIsCenterVertical);
        } else if (attr == styleable.QRCodeView_qrcv_toolbarHeight) {
            this.mToolbarHeight = typedArray.getDimensionPixelSize(attr, this.mToolbarHeight);
        } else if (attr == styleable.QRCodeView_qrcv_barcodeRectHeight) {
            this.mBarcodeRectHeight = typedArray.getDimensionPixelSize(attr, this.mBarcodeRectHeight);
        } else if (attr == styleable.QRCodeView_qrcv_isBarcode) {
            this.mIsBarcode = typedArray.getBoolean(attr, this.mIsBarcode);
        } else if (attr == styleable.QRCodeView_qrcv_barCodeTipText) {
            this.mBarCodeTipText = typedArray.getString(attr);
        } else if (attr == styleable.QRCodeView_qrcv_qrCodeTipText) {
            this.mQRCodeTipText = typedArray.getString(attr);
        } else if (attr == styleable.QRCodeView_qrcv_tipTextSize) {
            this.mTipTextSize = typedArray.getDimensionPixelSize(attr, this.mTipTextSize);
        } else if (attr == styleable.QRCodeView_qrcv_tipTextColor) {
            this.mTipTextColor = typedArray.getColor(attr, this.mTipTextColor);
        } else if (attr == styleable.QRCodeView_qrcv_isTipTextBelowRect) {
            this.mIsTipTextBelowRect = typedArray.getBoolean(attr, this.mIsTipTextBelowRect);
        } else if (attr == styleable.QRCodeView_qrcv_tipTextMargin) {
            this.mTipTextMargin = typedArray.getDimensionPixelSize(attr, this.mTipTextMargin);
        } else if (attr == styleable.QRCodeView_qrcv_isShowTipTextAsSingleLine) {
            this.mIsShowTipTextAsSingleLine = typedArray.getBoolean(attr, this.mIsShowTipTextAsSingleLine);
        } else if (attr == styleable.QRCodeView_qrcv_isShowTipBackground) {
            this.mIsShowTipBackground = typedArray.getBoolean(attr, this.mIsShowTipBackground);
        } else if (attr == styleable.QRCodeView_qrcv_tipBackgroundColor) {
            this.mTipBackgroundColor = typedArray.getColor(attr, this.mTipBackgroundColor);
        } else if (attr == styleable.QRCodeView_qrcv_isScanLineReverse) {
            this.mIsScanLineReverse = typedArray.getBoolean(attr, this.mIsScanLineReverse);
        } else if (attr == styleable.QRCodeView_qrcv_isShowDefaultGridScanLineDrawable) {
            this.mIsShowDefaultGridScanLineDrawable = typedArray.getBoolean(attr, this.mIsShowDefaultGridScanLineDrawable);
        } else if (attr == styleable.QRCodeView_qrcv_customGridScanLineDrawable) {
            this.mCustomGridScanLineDrawable = typedArray.getDrawable(attr);
        } else if (attr == styleable.QRCodeView_qrcv_isOnlyDecodeScanBoxArea) {
            this.mIsOnlyDecodeScanBoxArea = typedArray.getBoolean(attr, this.mIsOnlyDecodeScanBoxArea);
        }
    }

    private void afterInitCustomAttrs() {
        if (this.mCustomGridScanLineDrawable != null) {
            this.mOriginQRCodeGridScanLineBitmap = ((BitmapDrawable) this.mCustomGridScanLineDrawable).getBitmap();
        }

        if (this.mOriginQRCodeGridScanLineBitmap == null) {
            this.mOriginQRCodeGridScanLineBitmap = BitmapFactory.decodeResource(this.getResources(), mipmap.qrcode_default_grid_scan_line);
            this.mOriginQRCodeGridScanLineBitmap = BGAQRCodeUtil.makeTintBitmap(this.mOriginQRCodeGridScanLineBitmap, this.mScanLineColor);
        }

        this.mOriginBarCodeGridScanLineBitmap = BGAQRCodeUtil.adjustPhotoRotation(this.mOriginQRCodeGridScanLineBitmap, 90);
        this.mOriginBarCodeGridScanLineBitmap = BGAQRCodeUtil.adjustPhotoRotation(this.mOriginBarCodeGridScanLineBitmap, 90);
        this.mOriginBarCodeGridScanLineBitmap = BGAQRCodeUtil.adjustPhotoRotation(this.mOriginBarCodeGridScanLineBitmap, 90);
        if (this.mCustomScanLineDrawable != null) {
            this.mOriginQRCodeScanLineBitmap = ((BitmapDrawable) this.mCustomScanLineDrawable).getBitmap();
        }

        if (this.mOriginQRCodeScanLineBitmap == null) {
            this.mOriginQRCodeScanLineBitmap = BitmapFactory.decodeResource(this.getResources(), mipmap.qrcode_default_scan_line);
            this.mOriginQRCodeScanLineBitmap = BGAQRCodeUtil.makeTintBitmap(this.mOriginQRCodeScanLineBitmap, this.mScanLineColor);
        }

        this.mOriginBarCodeScanLineBitmap = BGAQRCodeUtil.adjustPhotoRotation(this.mOriginQRCodeScanLineBitmap, 90);
        this.mTopOffset += this.mToolbarHeight;
        this.mHalfCornerSize = 1.0F * (float) this.mCornerSize / 2.0F;
        this.mTipPaint.setTextSize((float) this.mTipTextSize);
        this.mTipPaint.setColor(this.mTipTextColor);
        this.setIsBarcode(this.mIsBarcode);
    }

    public void onDraw(Canvas canvas) {
        if (this.mFramingRect != null) {
            this.drawMask(canvas);
            this.drawBorderLine(canvas);
            this.drawCornerLine(canvas);
            this.drawScanLine(canvas);
            this.drawTipText(canvas);
            this.moveScanLine();
        }
    }

    private void drawMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (this.mMaskColor != 0) {
            this.mPaint.setStyle(Style.FILL);
            this.mPaint.setColor(this.mMaskColor);
            canvas.drawRect(0.0F, 0.0F, (float) width, (float) this.mFramingRect.top, this.mPaint);
            canvas.drawRect(0.0F, (float) this.mFramingRect.top, (float) this.mFramingRect.left, (float) (this.mFramingRect.bottom + 1), this.mPaint);
            canvas.drawRect((float) (this.mFramingRect.right + 1), (float) this.mFramingRect.top, (float) width, (float) (this.mFramingRect.bottom + 1), this.mPaint);
            canvas.drawRect(0.0F, (float) (this.mFramingRect.bottom + 1), (float) width, (float) height, this.mPaint);
        }
    }

    private void drawBorderLine(Canvas canvas) {
        if (this.mBorderSize > 0) {
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setColor(this.mBorderColor);
            this.mPaint.setStrokeWidth((float) this.mBorderSize);
            canvas.drawRect(this.mFramingRect, this.mPaint);
        }
    }

    private void drawCornerLine(Canvas canvas) {
        if (this.mHalfCornerSize > 0.0F) {
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setColor(this.mCornerColor);
            this.mPaint.setStrokeWidth((float) this.mCornerSize);
            canvas.drawLine((float) this.mFramingRect.left - this.mHalfCornerSize, (float) this.mFramingRect.top, (float) this.mFramingRect.left - this.mHalfCornerSize + (float) this.mCornerLength, (float) this.mFramingRect.top, this.mPaint);
            canvas.drawLine((float) this.mFramingRect.left, (float) this.mFramingRect.top - this.mHalfCornerSize, (float) this.mFramingRect.left, (float) this.mFramingRect.top - this.mHalfCornerSize + (float) this.mCornerLength, this.mPaint);
            canvas.drawLine((float) this.mFramingRect.right + this.mHalfCornerSize, (float) this.mFramingRect.top, (float) this.mFramingRect.right + this.mHalfCornerSize - (float) this.mCornerLength, (float) this.mFramingRect.top, this.mPaint);
            canvas.drawLine((float) this.mFramingRect.right, (float) this.mFramingRect.top - this.mHalfCornerSize, (float) this.mFramingRect.right, (float) this.mFramingRect.top - this.mHalfCornerSize + (float) this.mCornerLength, this.mPaint);
            canvas.drawLine((float) this.mFramingRect.left - this.mHalfCornerSize, (float) this.mFramingRect.bottom, (float) this.mFramingRect.left - this.mHalfCornerSize + (float) this.mCornerLength, (float) this.mFramingRect.bottom, this.mPaint);
            canvas.drawLine((float) this.mFramingRect.left, (float) this.mFramingRect.bottom + this.mHalfCornerSize, (float) this.mFramingRect.left, (float) this.mFramingRect.bottom + this.mHalfCornerSize - (float) this.mCornerLength, this.mPaint);
            canvas.drawLine((float) this.mFramingRect.right + this.mHalfCornerSize, (float) this.mFramingRect.bottom, (float) this.mFramingRect.right + this.mHalfCornerSize - (float) this.mCornerLength, (float) this.mFramingRect.bottom, this.mPaint);
            canvas.drawLine((float) this.mFramingRect.right, (float) this.mFramingRect.bottom + this.mHalfCornerSize, (float) this.mFramingRect.right, (float) this.mFramingRect.bottom + this.mHalfCornerSize - (float) this.mCornerLength, this.mPaint);
        }
    }

    private void drawScanLine(Canvas canvas) {
        RectF lineRect;
        Rect srcRect;
        if (this.mIsBarcode) {
            if (this.mGridScanLineBitmap != null) {
                lineRect = new RectF((float) this.mFramingRect.left + this.mHalfCornerSize + 0.5F, (float) this.mFramingRect.top + this.mHalfCornerSize + (float) this.mScanLineMargin, this.mGridScanLineRight, (float) this.mFramingRect.bottom - this.mHalfCornerSize - (float) this.mScanLineMargin);
                srcRect = new Rect((int) ((float) this.mGridScanLineBitmap.getWidth() - lineRect.width()), 0, this.mGridScanLineBitmap.getWidth(), this.mGridScanLineBitmap.getHeight());
                if (srcRect.left < 0) {
                    srcRect.left = 0;
                    lineRect.left = lineRect.right - (float) srcRect.width();
                }
                canvas.drawBitmap(this.mGridScanLineBitmap, srcRect, lineRect, this.mPaint);
            } else if (this.mScanLineBitmap != null) {
                lineRect = new RectF(this.mScanLineLeft, (float) this.mFramingRect.top + this.mHalfCornerSize + (float) this.mScanLineMargin, this.mScanLineLeft + (float) this.mScanLineBitmap.getWidth(), (float) this.mFramingRect.bottom - this.mHalfCornerSize - (float) this.mScanLineMargin);
                canvas.drawBitmap(this.mScanLineBitmap, (Rect) null, lineRect, this.mPaint);
            } else {
                this.mPaint.setStyle(Style.FILL);
                this.mPaint.setColor(this.mScanLineColor);
                canvas.drawRect(this.mScanLineLeft, (float) this.mFramingRect.top + this.mHalfCornerSize + (float) this.mScanLineMargin, this.mScanLineLeft + (float) this.mScanLineSize, (float) this.mFramingRect.bottom - this.mHalfCornerSize - (float) this.mScanLineMargin, this.mPaint);
            }
        } else if (this.mGridScanLineBitmap != null) {
            lineRect = new RectF((float) this.mFramingRect.left + this.mHalfCornerSize + (float) this.mScanLineMargin, (float) this.mFramingRect.top + this.mHalfCornerSize + 0.5F, (float) this.mFramingRect.right - this.mHalfCornerSize - (float) this.mScanLineMargin, this.mGridScanLineBottom);
            srcRect = new Rect(0, (int) ((float) this.mGridScanLineBitmap.getHeight() - lineRect.height()), this.mGridScanLineBitmap.getWidth(), this.mGridScanLineBitmap.getHeight());
            if (srcRect.top < 0) {
                srcRect.top = 0;
                lineRect.top = lineRect.bottom - (float) srcRect.height();
            }
            canvas.drawBitmap(this.mGridScanLineBitmap, srcRect, lineRect, this.mPaint);
        } else if (this.mScanLineBitmap != null) {
            lineRect = new RectF((float) this.mFramingRect.left + this.mHalfCornerSize + (float) this.mScanLineMargin, this.mScanLineTop, (float) this.mFramingRect.right - this.mHalfCornerSize - (float) this.mScanLineMargin, this.mScanLineTop + (float) this.mScanLineBitmap.getHeight());
            canvas.drawBitmap(this.mScanLineBitmap, (Rect) null, lineRect, this.mPaint);
        } else {
            this.mPaint.setStyle(Style.FILL);
            this.mPaint.setColor(this.mScanLineColor);
            canvas.drawRect((float) this.mFramingRect.left + this.mHalfCornerSize + (float) this.mScanLineMargin, this.mScanLineTop, (float) this.mFramingRect.right - this.mHalfCornerSize - (float) this.mScanLineMargin, this.mScanLineTop + (float) this.mScanLineSize, this.mPaint);
        }
    }

    private void drawTipText(Canvas canvas) {
        if (!TextUtils.isEmpty(this.mTipText) && this.mTipTextSl != null) {
            Rect tipRect;
            float left;
            if (this.mIsTipTextBelowRect) {
                if (this.mIsShowTipBackground) {
                    this.mPaint.setColor(this.mTipBackgroundColor);
                    this.mPaint.setStyle(Style.FILL);
                    if (this.mIsShowTipTextAsSingleLine) {
                        tipRect = new Rect();
                        this.mTipPaint.getTextBounds(this.mTipText, 0, this.mTipText.length(), tipRect);
                        left = (float) ((canvas.getWidth() - tipRect.width()) / 2 - this.mTipBackgroundRadius);
                        canvas.drawRoundRect(new RectF(left, (float) (this.mFramingRect.bottom + this.mTipTextMargin - this.mTipBackgroundRadius), left + (float) tipRect.width() + (float) (2 * this.mTipBackgroundRadius), (float) (this.mFramingRect.bottom + this.mTipTextMargin + this.mTipTextSl.getHeight() + this.mTipBackgroundRadius)), (float) this.mTipBackgroundRadius, (float) this.mTipBackgroundRadius, this.mPaint);
                    } else {
                        canvas.drawRoundRect(new RectF((float) this.mFramingRect.left, (float) (this.mFramingRect.bottom + this.mTipTextMargin - this.mTipBackgroundRadius), (float) this.mFramingRect.right, (float) (this.mFramingRect.bottom + this.mTipTextMargin + this.mTipTextSl.getHeight() + this.mTipBackgroundRadius)), (float) this.mTipBackgroundRadius, (float) this.mTipBackgroundRadius, this.mPaint);
                    }
                }

                canvas.save();
                if (this.mIsShowTipTextAsSingleLine) {
                    canvas.translate(0.0F, (float) (this.mFramingRect.bottom + this.mTipTextMargin));
                } else {
                    canvas.translate((float) (this.mFramingRect.left + this.mTipBackgroundRadius), (float) (this.mFramingRect.bottom + this.mTipTextMargin));
                }

                this.mTipTextSl.draw(canvas);
                canvas.restore();
            } else {
                if (this.mIsShowTipBackground) {
                    this.mPaint.setColor(this.mTipBackgroundColor);
                    this.mPaint.setStyle(Style.FILL);
                    if (this.mIsShowTipTextAsSingleLine) {
                        tipRect = new Rect();
                        this.mTipPaint.getTextBounds(this.mTipText, 0, this.mTipText.length(), tipRect);
                        left = (float) ((canvas.getWidth() - tipRect.width()) / 2 - this.mTipBackgroundRadius);
                        canvas.drawRoundRect(new RectF(left, (float) (this.mFramingRect.top - this.mTipTextMargin - this.mTipTextSl.getHeight() - this.mTipBackgroundRadius), left + (float) tipRect.width() + (float) (2 * this.mTipBackgroundRadius), (float) (this.mFramingRect.top - this.mTipTextMargin + this.mTipBackgroundRadius)), (float) this.mTipBackgroundRadius, (float) this.mTipBackgroundRadius, this.mPaint);
                    } else {
                        canvas.drawRoundRect(new RectF((float) this.mFramingRect.left, (float) (this.mFramingRect.top - this.mTipTextMargin - this.mTipTextSl.getHeight() - this.mTipBackgroundRadius), (float) this.mFramingRect.right, (float) (this.mFramingRect.top - this.mTipTextMargin + this.mTipBackgroundRadius)), (float) this.mTipBackgroundRadius, (float) this.mTipBackgroundRadius, this.mPaint);
                    }
                }

                canvas.save();
                if (this.mIsShowTipTextAsSingleLine) {
                    canvas.translate(0.0F, (float) (this.mFramingRect.top - this.mTipTextMargin - this.mTipTextSl.getHeight()));
                } else {
                    canvas.translate((float) (this.mFramingRect.left + this.mTipBackgroundRadius), (float) (this.mFramingRect.top - this.mTipTextMargin - this.mTipTextSl.getHeight()));
                }

                this.mTipTextSl.draw(canvas);
                canvas.restore();
            }
        }
    }

    private void moveScanLine() {
        int scanLineSize;
        if (this.mIsBarcode) {
            if (this.mGridScanLineBitmap == null) {
                this.mScanLineLeft += (float) this.mMoveStepDistance;
                scanLineSize = this.mScanLineSize;
                if (this.mScanLineBitmap != null) {
                    scanLineSize = this.mScanLineBitmap.getWidth();
                }

                if (this.mIsScanLineReverse) {
                    if (this.mScanLineLeft + (float) scanLineSize > (float) this.mFramingRect.right - this.mHalfCornerSize || this.mScanLineLeft < (float) this.mFramingRect.left + this.mHalfCornerSize) {
                        this.mMoveStepDistance = -this.mMoveStepDistance;
                    }
                } else if (this.mScanLineLeft + (float) scanLineSize > (float) this.mFramingRect.right - this.mHalfCornerSize) {
                    this.mScanLineLeft = (float) this.mFramingRect.left + this.mHalfCornerSize + 0.5F;
                }
            } else {
                this.mGridScanLineRight += (float) this.mMoveStepDistance;
                if (this.mGridScanLineRight > (float) this.mFramingRect.right - this.mHalfCornerSize) {
                    this.mGridScanLineRight = (float) this.mFramingRect.left + this.mHalfCornerSize + 0.5F;
                }
            }
        } else if (this.mGridScanLineBitmap == null) {
            this.mScanLineTop += (float) this.mMoveStepDistance;
            scanLineSize = this.mScanLineSize;
            if (this.mScanLineBitmap != null) {
                scanLineSize = this.mScanLineBitmap.getHeight();
            }

            if (this.mIsScanLineReverse) {
                if (this.mScanLineTop + (float) scanLineSize > (float) this.mFramingRect.bottom - this.mHalfCornerSize || this.mScanLineTop < (float) this.mFramingRect.top + this.mHalfCornerSize) {
                    this.mMoveStepDistance = -this.mMoveStepDistance;
                }
            } else if (this.mScanLineTop + (float) scanLineSize > (float) this.mFramingRect.bottom - this.mHalfCornerSize) {
                this.mScanLineTop = (float) this.mFramingRect.top + this.mHalfCornerSize + 0.5F;
            }
        } else {
            this.mGridScanLineBottom += (float) this.mMoveStepDistance;
            if (this.mGridScanLineBottom > (float) this.mFramingRect.bottom - this.mHalfCornerSize) {
                this.mGridScanLineBottom = (float) this.mFramingRect.top + this.mHalfCornerSize + 0.5F;
            }
        }

        this.postInvalidateDelayed((long) this.mAnimDelayTime, this.mFramingRect.left, this.mFramingRect.top, this.mFramingRect.right, this.mFramingRect.bottom);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.calFramingRect();
    }

    private void calFramingRect() {
        Point screenResolution = BGAQRCodeUtil.getScreenResolution(this.getContext());
        int leftOffset = (screenResolution.x - this.mRectWidth) / 2;
        this.mFramingRect = new Rect(leftOffset, this.mTopOffset, leftOffset + this.mRectWidth, this.mTopOffset + this.mRectHeight);
        if (this.mIsBarcode) {
            this.mGridScanLineRight = this.mScanLineLeft = (float) this.mFramingRect.left + this.mHalfCornerSize + 0.5F;
        } else {
            this.mGridScanLineBottom = this.mScanLineTop = (float) this.mFramingRect.top + this.mHalfCornerSize + 0.5F;
        }

    }

    public Rect getScanBoxAreaRect(int previewHeight) {
        if (this.mIsOnlyDecodeScanBoxArea) {
            Rect rect = new Rect(this.mFramingRect);
            rect.top = (int) (1.0F * (float) rect.top * (float) previewHeight / (float) this.getMeasuredHeight());
            rect.bottom = (int) (1.0F * (float) rect.bottom * (float) previewHeight / (float) this.getMeasuredHeight());
            return rect;
        } else {
            return null;
        }
    }

    public boolean getIsBarcode() {
        return this.mIsBarcode;
    }

    public void setIsBarcode(boolean isBarcode) {
        this.mIsBarcode = isBarcode;
        if (this.mCustomGridScanLineDrawable == null && !this.mIsShowDefaultGridScanLineDrawable) {
            if (this.mCustomScanLineDrawable != null || this.mIsShowDefaultScanLineDrawable) {
                if (this.mIsBarcode) {
                    this.mScanLineBitmap = this.mOriginBarCodeScanLineBitmap;
                } else {
                    this.mScanLineBitmap = this.mOriginQRCodeScanLineBitmap;
                }
            }
        } else if (this.mIsBarcode) {
            this.mGridScanLineBitmap = this.mOriginBarCodeGridScanLineBitmap;
        } else {
            this.mGridScanLineBitmap = this.mOriginQRCodeGridScanLineBitmap;
        }

        if (this.mIsBarcode) {
            this.mTipText = this.mBarCodeTipText;
            this.mRectHeight = this.mBarcodeRectHeight;
            this.mAnimDelayTime = (int) (1.0F * (float) this.mAnimTime * (float) this.mMoveStepDistance / (float) this.mRectWidth);
        } else {
            this.mTipText = this.mQRCodeTipText;
            this.mRectHeight = this.mRectWidth;
            this.mAnimDelayTime = (int) (1.0F * (float) this.mAnimTime * (float) this.mMoveStepDistance / (float) this.mRectHeight);
        }

        if (!TextUtils.isEmpty(this.mTipText)) {
            if (this.mIsShowTipTextAsSingleLine) {
                this.mTipTextSl = new StaticLayout(this.mTipText, this.mTipPaint, BGAQRCodeUtil.getScreenResolution(this.getContext()).x, Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
            } else {
                this.mTipTextSl = new StaticLayout(this.mTipText, this.mTipPaint, this.mRectWidth - 2 * this.mTipBackgroundRadius, Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
            }
        }

        if (this.mIsCenterVertical) {
            int screenHeight = BGAQRCodeUtil.getScreenResolution(this.getContext()).y;
            if (this.mToolbarHeight == 0) {
                this.mTopOffset = (screenHeight - this.mRectHeight) / 2;
            } else {
                this.mTopOffset = (screenHeight - this.mRectHeight) / 2 + this.mToolbarHeight / 2;
            }
        }

        this.calFramingRect();
        this.postInvalidate();
    }
}