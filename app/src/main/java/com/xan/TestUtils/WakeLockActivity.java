package com.xan.TestUtils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class WakeLockActivity extends Activity {
    private static final String TAG = MainActivity.MAIN_TAG + WakeLockActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wakelock);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        createUI();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();

        // update all text label to unlocked
        setText(R.id.cpu_freq_min_status, getString(R.string.unlock), 0xFF00FF00);
        setText(R.id.status2, getString(R.string.unlock), 0xFF00FF00);
        setText(R.id.status3, getString(R.string.unlock), 0xFF00FF00);
        setText(R.id.status4, getString(R.string.unlock), 0xFF00FF00);
        setText(R.id.status5, getString(R.string.unlock), 0xFF00FF00);
    }

    private void createUI() {
        Log.d(TAG, "createUI");

        ArrayAdapter<String> freq_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new String[] { "LOWEST", "LOW", "MEDIUM", "HIGH", "HIGHEST"});
        ArrayAdapter<String> num_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new String[] { "SINGLE", "DUAL", "TRIPLE", "QUAD"});

        createAdapter(freq_adapter, R.id.spinner1, R.id.spinner2);
        createAdapter(num_adapter, R.id.spinner3, R.id.spinner4);

        createButtonListener(R.id.button1, R.id.button2);
        createButtonListener(R.id.button3, R.id.button4);
        createButtonListener(R.id.button5, R.id.button6);
        createButtonListener(R.id.button7, R.id.button8);
        createButtonListener(R.id.button9, R.id.button10);
    }

    private void createAdapter(ArrayAdapter<String> adapter, int spinner_id_min, int spinner_id_max) {
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner min_spinner = (Spinner) findViewById(spinner_id_min);
        min_spinner.setAdapter(adapter);
        min_spinner.setOnItemSelectedListener(new SpinnerListener());

        Spinner max_spinner = (Spinner) findViewById(spinner_id_max);
        max_spinner.setAdapter(adapter);
        max_spinner.setOnItemSelectedListener(new SpinnerListener());
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
                case R.id.button1:
                    setText(R.id.cpu_freq_min_status, getString(R.string.lock), 0xFFFF0000);
                    break;
                case R.id.button2:
                    setText(R.id.cpu_freq_min_status, getString(R.string.unlock), 0xFF00FF00);
                    break;
                case R.id.button3:
                    setText(R.id.status2, getString(R.string.lock), 0xFFFF0000);
                    break;
                case R.id.button4:
                    setText(R.id.status2, getString(R.string.unlock), 0xFF00FF00);
                    break;
                case R.id.button5:
                    setText(R.id.status3, getString(R.string.lock), 0xFFFF0000);
                    break;
                case R.id.button6:
                    setText(R.id.status3, getString(R.string.unlock), 0xFF00FF00);
                    break;
                case R.id.button7:
                    setText(R.id.status4, getString(R.string.lock), 0xFFFF0000);
                    break;
                case R.id.button8:
                    setText(R.id.status4, getString(R.string.unlock), 0xFF00FF00);
                    break;
                case R.id.button9:
                    setText(R.id.status5, getString(R.string.lock), 0xFFFF0000);
                    break;
                case R.id.button10:
                    setText(R.id.status5, getString(R.string.unlock), 0xFF00FF00);
                    break;
            }
        }
    };

    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Log.d(TAG, "onItemSelected:"
                    + " adapterView.getId()=" + adapterView.getId()
                    + ", view.getId()=" + view.getId()
                    + ", position=" + position
                    + ", id=" + id);

            switch (adapterView.getId()) {
                case R.id.spinner1:

                    break;
                case R.id.spinner2:

                    break;
                case R.id.spinner3:

                    break;
                case R.id.spinner4:

                    break;
            }
        }
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}

