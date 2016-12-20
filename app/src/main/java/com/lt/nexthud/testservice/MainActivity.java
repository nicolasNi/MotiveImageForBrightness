package com.lt.nexthud.testservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private Button btn_start_service;
    private Button btn_stop_service;
    private Button btn_add;
    private Button btn_minus;
    private Button btn_unbundle;
    private int changeLight = 0;
    private FxService msgService;
    private boolean IS_START_SERVICE=false;
    private boolean IS_START_BINDER=false;
    private int defaultLightNumber=3;

    /* 通过Binder，实现Activity与Service通信 */
    private FxService.ServiceBinder mBinderService;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            msgService = ((FxService.ServiceBinder)service).getService();

            mBinderService = (FxService.ServiceBinder) service;
            try {
                mBinderService.startDownload(changeLight);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start_service=(Button)findViewById(R.id.button);
        btn_stop_service=(Button)findViewById(R.id.button2);
        btn_add=(Button)findViewById(R.id.button3);
        btn_unbundle=(Button)findViewById(R.id.button4);
        btn_minus=(Button)findViewById(R.id.button5);


        btn_start_service.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, FxService.class);
                startService(intent);
                IS_START_SERVICE=true;
            }
        });

        btn_stop_service.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                intent = new Intent(MainActivity.this, FxService.class);
                if(IS_START_SERVICE){
                    stopService(intent);
                }
                IS_START_SERVICE=false;
            }
        });

        btn_add.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                if(IS_START_BINDER)
                {
                    unbindService(connection);
                }
                if(defaultLightNumber + changeLight <6) {
                    changeLight++;
                }

                intent = new Intent(MainActivity.this, FxService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);

                IS_START_SERVICE=true;
                IS_START_BINDER=true;
                DelyUnbindService();
            }
        });

        btn_minus.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                if(IS_START_BINDER)
                {
                    unbindService(connection);
                }
                if(defaultLightNumber + changeLight >0) {
                    changeLight--;
                }

                intent = new Intent(MainActivity.this, FxService.class);
                bindService(intent, connection, BIND_AUTO_CREATE);

                IS_START_SERVICE=true;
                IS_START_BINDER=true;
                DelyUnbindService();
            }
        });


        btn_unbundle.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(View v) {
                if(IS_START_BINDER){
                    unbindService(connection);
                    Toast.makeText( MainActivity.this, "Binder unbind", Toast.LENGTH_SHORT).show();
                }
                IS_START_BINDER=false;
                IS_START_SERVICE=false;
            }
        });
    }

    private void DelyUnbindService()
    {
        new Thread() {
            @Override
            public void run() {
                if (IS_START_BINDER) {
                    try {
                        sleep(3500);
                        if(IS_START_BINDER)
                        {
                            unbindService(connection);
                            IS_START_BINDER=false;
                            IS_START_SERVICE=false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }


}
