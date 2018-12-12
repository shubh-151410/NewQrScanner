package com.shubhanshu.newqrscanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.Result;

import de.hdodenhof.circleimageview.CircleImageView;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String FLASH_STATE = "FLASH_STATE";
    private ZXingScannerView mScannerView;
    Toolbar toolbar;
    CircleImageView flashoff;
    private boolean mFlash;
    int left = 100;
    int right = 100;
    int top = 100;
    int bottom = 100;
    Paint paint;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(R.layout.activity_main);
        flashoff = (CircleImageView)findViewById(R.id.btnSwitch);
        flashoff.setBackgroundDrawable(null);

        if (state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);

        } else {
            mFlash = false;

        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        mTitle.setText(toolbar.getTitle());
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(getDrawable(R.mipmap.noun_back_1446447));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);


        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        contentFrame.addView(mScannerView);


        flashoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableFlash();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        //MainActivity.result_text.setText(rawResult.getText());
        onBackPressed();
    }


    private boolean EnableFlash() {
        mFlash = !mFlash;
        if (mFlash) {
            flashoff.setImageResource(R.mipmap.noun_flash);
        }
        mScannerView.setFlash(mFlash);
        return true;
    }

    public void onCameraSelected(int cameraId) {

        mScannerView.setFlash(mFlash);

    }
    protected IViewFinder createViewFinderView(Context context) {
        return new CustomViewFinderView(context);
    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "ZXing";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas ) {
            Rect rect = getDrawingRect( Rect rect);
            super.onDraw(canvas);
            drawTradeMark(canvas);
            getDrawingRect();

        }
          private void getDrawingRect(Rect outRect)
          {

          }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            int top,left,right,Bottom;

            float tradeMarkTop;
            float tradeMarkLeft;
            float tradeMarkRight;
            float tradeMarkBottom;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkBottom = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkRight = framingRect.right;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkBottom = 10;
                tradeMarkRight = canvas.getHeight()-PAINT.getTextSize()-10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop,PAINT);
        }
    }
}
