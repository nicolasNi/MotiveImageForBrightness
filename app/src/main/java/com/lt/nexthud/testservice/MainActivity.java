package com.lt.nexthud.testservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private Button btn_add;
    private Button btn_minus;
    private int changeLight = 0;
    private boolean IS_START_BINDER = false;
    private int defaultLightNumber = 3;
    private DelayDisappearWindowThread delayDisappearWindowThread = new DelayDisappearWindowThread();

    /* 通过Binder，实现Activity与Service通信 */
    private FxService.ServiceBinder mBinderService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderService = (FxService.ServiceBinder) service;
            mBinderService.changeLight(changeLight);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_add = (Button) findViewById(R.id.button3);
        btn_minus = (Button) findViewById(R.id.button5);

        btn_add.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                if (defaultLightNumber + changeLight < 6) {
                    changeLight++;
                }
                handleLight();
            }
        });

        btn_minus.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                if (defaultLightNumber + changeLight > 0) {
                    changeLight--;
                    handleLight();
                }
            }
        });
    }

    private void handleLight() {
        if (IS_START_BINDER) {
            mBinderService.changeLight(changeLight);
        } else {
            intent = new Intent(MainActivity.this, FxService.class);
            bindService(intent, connection, BIND_AUTO_CREATE);
        }
        IS_START_BINDER = true;
        handleDelayDisppearWindowThread();
    }

    private void handleDelayDisppearWindowThread() {
        if (delayDisappearWindowThread.isAlive()) {
            try {
                delayDisappearWindowThread.interrupt();
                delayDisappearWindowThread.join();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        delayDisappearWindowThread = new DelayDisappearWindowThread();
        delayDisappearWindowThread.start();
    }

    public class DelayDisappearWindowThread extends Thread {
        @Override
        public void run() {
            if (IS_START_BINDER) {
                while (!isInterrupted()) {
                    try {
                        sleep(4000);
                        if (IS_START_BINDER) {
                            unbindService(connection);
                            IS_START_BINDER = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }

            }
        }
    }
}
