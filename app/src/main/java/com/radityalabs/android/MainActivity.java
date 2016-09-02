package com.radityalabs.android;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        View.OnTouchListener {

    private final Handler handler = new Handler();
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.btn_test)).setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d("TAG", "Down time : " + mLastClickTime);
            mLastClickTime = SystemClock.elapsedRealtime();
            handler.postDelayed(new CustomHandler(new Callback() {
                @Override
                public Boolean onListen(Object obj, Boolean flag) {
                    boolean time = SystemClock.elapsedRealtime() - mLastClickTime > 1000;
                    Log.d("TAG", "Result : " + (SystemClock.elapsedRealtime() - mLastClickTime));
                    Log.d("TAG", "" + time);
                    if (flag && time) {
                        handler.removeCallbacks((Runnable) obj);
                        Toast.makeText(MainActivity.this, "Hello World", Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        handler.removeCallbacks((Runnable) obj);
                        return true;
                    }
                }
            }), 1000);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d("TAG", "Up time : " + SystemClock.elapsedRealtime());
            mLastClickTime = SystemClock.elapsedRealtime();
        }
        return false;
    }

    public class CustomHandler implements Runnable {
        private Callback callback;

        public CustomHandler(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            callback.onListen(this, true);
        }
    }

    public interface Callback<T> {
        public Boolean onListen(T obj, Boolean flag);
    }
}
