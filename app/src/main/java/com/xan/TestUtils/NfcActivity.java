package com.xan.TestUtils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NfcActivity extends Activity {
    private static final String TAG = MainActivity.MAIN_TAG + NfcActivity.class.getSimpleName();

    private NfcControl mNfcControl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNfcControl = new NfcControl(this);

        createUI();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();

        // update all text label to unlocked
        setText(R.id.nfc_status, getString(R.string.unlock), 0xFF00FF00);
    }

    private void createUI() {
        Log.d(TAG, "createUI");

        createButtonListener(R.id.nfc_on, R.id.nfc_off);
        createButtonListener(R.id.nfc_lock, R.id.nfc_unlock);
        createButtonListener(R.id.nfc_test1, R.id.nfc_test2);
    }

    private void createButtonListener(int lock_res, int unlock_res) {
        Button lock = (Button) findViewById(lock_res);
        Button unlock = (Button) findViewById(unlock_res);
        
        LockListener lockListener = new LockListener();

        lock.setOnClickListener(lockListener);
        unlock.setOnClickListener(lockListener);
    }

    private void setText(int id, String text, int color) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(text);
        tv.setTextColor(color);
    }

    private class LockListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.nfc_on:
                    mNfcControl.turnOnNfc(true);
                    break;
                case R.id.nfc_off:
                    mNfcControl.turnOnNfc(false);
                    break;
                case R.id.nfc_lock:
                    mNfcControl.lockNfc(true);
                    setText(R.id.nfc_status, getString(R.string.lock), 0xFFFF0000);
                    break;
                case R.id.nfc_unlock:
                    mNfcControl.lockNfc(false);
                    setText(R.id.nfc_status, getString(R.string.unlock), 0xFF00FF00);
                    break;
                case R.id.nfc_test1:
                    mNfcControl.getDefaultAdapter();
                    break;
                case R.id.nfc_test2:
                    // Do nothing.
                    break;
            }
        }
    };
}
