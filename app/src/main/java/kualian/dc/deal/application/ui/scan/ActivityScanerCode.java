package kualian.dc.deal.application.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.zxing.Result;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import kualian.dc.deal.application.R;
import kualian.dc.deal.application.ui.scan.scaner.CameraManager;
import kualian.dc.deal.application.ui.scan.scaner.CaptureActivityHandler;
import kualian.dc.deal.application.ui.scan.scaner.OnRxScanerListener;
import kualian.dc.deal.application.ui.scan.scaner.decoding.InactivityTimer;
import kualian.dc.deal.application.util.RxBarTool;
import kualian.dc.deal.application.util.RxToast;
import kualian.dc.deal.application.util.WalletTool;
import kualian.dc.deal.application.util.callback.CallbackManager;
import kualian.dc.deal.application.util.callback.CallbackType;
import kualian.dc.deal.application.util.callback.IGlobalCallback;

public class ActivityScanerCode extends AppCompatActivity {

    private static OnRxScanerListener mScanerListener;//扫描结果监听
    private InactivityTimer inactivityTimer;
    private CaptureActivityHandler handler;//扫描处理
    private RelativeLayout mContainer = null;//整体根布局
    private RelativeLayout mCropLayout = null;//扫描框根布局
    private int mCropWidth = 0;//扫描边界的宽度
    private int mCropHeight = 0;//扫描边界的高度
    private boolean hasSurface;//是否有预览
    private boolean vibrate = true;//扫描成功后是否震动
    private boolean mFlashing = true;//闪光灯开启状态
    private LinearLayout mLlScanHelp;//生成二维码 & 条形码 布局

   /* public static void setScanerListener(OnRxScanerListener scanerListener) {
        mScanerListener = scanerListener;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.setNoTitle(this);
        setContentView(R.layout.activity_scaner_code);
        RxBarTool.setTransparentStatusBar(this);
        initView();//界面控件初始化
        initScanerAnimation();//扫描动画初始化
        CameraManager.init(this);//初始化 CameraManager
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);//Camera初始化
        } else {
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (!hasSurface) {
                        hasSurface = true;
                        initCamera(holder);
                    }
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    hasSurface = false;

                }
            });
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        mScanerListener = null;
        super.onDestroy();
    }

    private void initView() {
        mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
        mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);
        mLlScanHelp = (LinearLayout) findViewById(R.id.ll_scan_help);
        //请求Camera权限 与 文件读写 权限


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.camera_tips)
                    .setMessage(R.string.camera_sure)
                    .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(ActivityScanerCode.this, new String[]{Manifest.permission.CAMERA}, 1);
                        }
                    });
            builder.show();
        }
    }

    private void initScanerAnimation() {
        ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
        ScaleUpDowm(mQrLineView);
    }

    public void ScaleUpDowm(View view) {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        view.startAnimation(animation);
    }

    public int getCropWidth() {
        return mCropWidth;
    }

    public void setCropWidth(int cropWidth) {
        mCropWidth = cropWidth;
        CameraManager.FRAME_WIDTH = mCropWidth;

    }

    public int getCropHeight() {
        return mCropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.mCropHeight = cropHeight;
        CameraManager.FRAME_HEIGHT = mCropHeight;
    }

    public void btn(View view) {
        int viewId = view.getId();
        if (viewId == R.id.top_mask) {
            light();
        } else if (viewId == R.id.top_back) {
            finish();
        }
    }

    private void light() {
        if (mFlashing) {
            mFlashing = false;
            // 开闪光灯
            CameraManager.get().openLight();
        } else {
            mFlashing = true;
            // 关闪光灯
            CameraManager.get().offLight();
        }

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            Point point = CameraManager.get().getCameraResolution();
            AtomicInteger width = new AtomicInteger(point.y);
            AtomicInteger height = new AtomicInteger(point.x);
            int cropWidth = mCropLayout.getWidth() * width.get() / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * height.get() / mContainer.getHeight();
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
        } catch (IOException | RuntimeException ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(ActivityScanerCode.this);
        }
    }
    //========================================打开本地图片识别二维码 end=================================

    //--------------------------------------打开本地图片识别二维码 start---------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                // 开始对图像资源解码
                Result rawResult = RxQrBarTool.decodeFromPhoto(photo);
                if (rawResult != null) {
                    if (mScanerListener == null) {
                        //initDialogResult(rawResult);
                    } else {
                        mScanerListener.onSuccess("From to Picture", rawResult);
                    }
                } else {
                    if (mScanerListener == null) {
                        RxToast.error("图片识别失败.");
                    } else {
                        mScanerListener.onFail("From to Picture", "图片识别失败");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //==============================================================================================解析结果 及 后续处理 end

    /*private void initDialogResult(Result result) {
        BarcodeFormat type = result.getBarcodeFormat();
        String realContent = result.getText();

        if (rxDialogSure == null) {
            rxDialogSure = new RxDialogSure(mContext);//提示弹窗
        }

        if (BarcodeFormat.QR_CODE.equals(type)) {
            rxDialogSure.setTitle("二维码扫描结果");
        } else if (BarcodeFormat.EAN_13.equals(type)) {
            rxDialogSure.setTitle("条形码扫描结果");
        } else {
            rxDialogSure.setTitle("扫描结果");
        }

        rxDialogSure.setContent(realContent);
        rxDialogSure.setSureListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rxDialogSure.cancel();
            }
        });
        rxDialogSure.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (handler != null) {
                    // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
                    handler.sendEmptyMessage(R.id.restart_preview);
                }
            }
        });

        if (!rxDialogSure.isShowing()) {
            rxDialogSure.show();
        }

        RxSPTool.putContent(mContext, RxConstants.SP_SCAN_CODE, RxDataTool.stringToInt(RxSPTool.getContent(mContext, RxConstants.SP_SCAN_CODE)) + 1 + "");
    }*/

    public void handleDecode(Result result) {
        inactivityTimer.onActivity();
        WalletTool.playBeep(this, vibrate);//扫描成功之后的振动与声音提示

        String result1 = result.getText();

        @SuppressWarnings("unchecked") final IGlobalCallback<String> callback = CallbackManager
                .getInstance()
                .getCallback(CallbackType.ON_SCAN);
        if (callback != null) {
            callback.executeCallback(result1);
        }
        finish();
      /*  if (mScanerListener == null) {
            RxToast.success(result1);
            //initDialogResult(result);
        } else {
            mScanerListener.onSuccess("From to Camera", result);
        }*/
    }

    public Handler getHandler() {
        return handler;
    }
}