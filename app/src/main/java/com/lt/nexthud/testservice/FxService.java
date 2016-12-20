package com.lt.nexthud.testservice;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FxService extends Service {
    //定义浮动窗口布局
    LinearLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    WindowManager mWindowManager;

    private static final String TAG = "FxService";

    private boolean serviceRunning = false;

    public ServiceBinder mBinder = new ServiceBinder(); /* 数据通信的桥梁 */

    ImageView light1,light2,light3,light4,light5,light6,light7,light8,light9,light10,light11,light12,light13;

    private int defaultLightNumber=3;

    public FxService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // 创建Service时调用该方法，只调用一次
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("--onCreate()--");
        serviceRunning = true;
        createFloatView();

        new Thread() {
            @Override
            public void run() {
                while (serviceRunning) {
                    System.out.println("---Service运行中---");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }

    // 每次启动Servcie时都会调用该方法
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("--onStartCommand()--");
        return super.onStartCommand(intent, flags, startId);
    }

    // 解绑Servcie调用该方法
    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("--onUnbind()--");
        return super.onUnbind(intent);
    }

    // 退出或者销毁时调用该方法
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mFloatLayout != null)
        {
            mWindowManager.removeView(mFloatLayout);
        }
    }


    private void createFloatView()
    {
        wmParams = new WindowManager.LayoutParams();
        //获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmParams.type = android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.TRANSLUCENT;
        wmParams.alpha = 0.9f;
        wmParams.dimAmount = 0.9f;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =
                android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
        ;

        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.CENTER;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 0;
        wmParams.y = 0;

        // 设置悬浮窗口长宽数据
        wmParams.width = 300;
        wmParams.height =300;

        //设置悬浮窗口长宽数据
//        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);

        light1 = (ImageView)mFloatLayout.findViewById(R.id.light1);
        light2 = (ImageView)mFloatLayout.findViewById(R.id.light2);
        light3 = (ImageView)mFloatLayout.findViewById(R.id.light3);
        light4 = (ImageView)mFloatLayout.findViewById(R.id.light4);
        light5 = (ImageView)mFloatLayout.findViewById(R.id.light5);
        light6 = (ImageView)mFloatLayout.findViewById(R.id.light6);
        light7 = (ImageView)mFloatLayout.findViewById(R.id.light7);
        light8 = (ImageView)mFloatLayout.findViewById(R.id.light8);
        light9 = (ImageView)mFloatLayout.findViewById(R.id.light9);
        light10 = (ImageView)mFloatLayout.findViewById(R.id.light10);
        light11 = (ImageView)mFloatLayout.findViewById(R.id.light11);
        light12 = (ImageView)mFloatLayout.findViewById(R.id.light12);
        light13 = (ImageView)mFloatLayout.findViewById(R.id.light13);

        SetLight(defaultLightNumber);
        //添加mFloatLayout
        try
        {
            mWindowManager.addView(mFloatLayout, wmParams);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public class ServiceBinder extends Binder {

        /* 返回当前类的一个函数 */
        public FxService getService() {
            return FxService.this;
        }

        public void startDownload(int i) throws InterruptedException {
            SetLight(defaultLightNumber+i);
        }
    }

    private void SetLight(int lightNumber)
    {
        switch (lightNumber)
        {
            case 0:
                SwitchToDark(light1);SwitchToDark(light2);SwitchToDark(light3);SwitchToDark(light4);
                SwitchToDark(light5);SwitchToDark(light6);SwitchToDark(light7);SwitchToDark(light8);
                SwitchToDark(light9);SwitchToDark(light10);SwitchToDark(light11);SwitchToDark(light12);
                SwitchToDark(light13);break;
            case 1:
                SwitchToLight(light1);SwitchToLight(light2);SwitchToDark(light3);SwitchToDark(light4);
                SwitchToDark(light5);SwitchToDark(light6);SwitchToDark(light7);SwitchToDark(light8);
                SwitchToDark(light9);SwitchToDark(light10);SwitchToDark(light11);SwitchToDark(light12);
                SwitchToDark(light13);break;
            case 2:
                SwitchToLight(light1);SwitchToLight(light2);SwitchToLight(light3);SwitchToLight(light4);
                SwitchToDark(light5);SwitchToDark(light6);SwitchToDark(light7);SwitchToDark(light8);
                SwitchToDark(light9);SwitchToDark(light10);SwitchToDark(light11);SwitchToDark(light12);
                SwitchToDark(light13);break;
            case 3:
                SwitchToLight(light1);SwitchToLight(light2);SwitchToLight(light3);SwitchToLight(light4);
                SwitchToLight(light5);SwitchToLight(light6);SwitchToDark(light7);SwitchToDark(light8);
                SwitchToDark(light9);SwitchToDark(light10);SwitchToDark(light11);SwitchToDark(light12);
                SwitchToDark(light13);break;
            case 4:
                SwitchToLight(light1);SwitchToLight(light2);SwitchToLight(light3);SwitchToLight(light4);
                SwitchToLight(light5);SwitchToLight(light6);SwitchToLight(light7);SwitchToLight(light8);
                SwitchToLight(light9);SwitchToDark(light10);SwitchToDark(light11);SwitchToDark(light12);
                SwitchToDark(light13);break;
            case 5:
                SwitchToLight(light1);SwitchToLight(light2);SwitchToLight(light3);SwitchToLight(light4);
                SwitchToLight(light5);SwitchToLight(light6);SwitchToLight(light7);SwitchToLight(light8);
                SwitchToLight(light9);SwitchToLight(light10);SwitchToLight(light11);SwitchToDark(light12);
                SwitchToDark(light13);break;
            case 6:
                SwitchToLight(light1);SwitchToLight(light2);SwitchToLight(light3);SwitchToLight(light4);
                SwitchToLight(light5);SwitchToLight(light6);SwitchToLight(light7);SwitchToLight(light8);
                SwitchToLight(light9);SwitchToLight(light10);SwitchToLight(light11);SwitchToLight(light12);
                SwitchToLight(light13);break;
        }
    }

    private void SwitchToDark(ImageView light)
    {
        light.setImageResource(R.mipmap.darker);
    }

    private void SwitchToLight(ImageView light)
    {
        light.setImageResource(R.mipmap.lighter);
    }
}
