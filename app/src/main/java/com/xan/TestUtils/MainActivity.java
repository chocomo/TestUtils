package com.xan.TestUtils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    protected static final String MAIN_TAG = "HANK-";
    private static final String TAG = MainActivity.MAIN_TAG + MainActivity.class.getSimpleName();

    private ArrayList<HashMap<String, Item>> mListItems = new ArrayList<HashMap<String, Item>>();

    private final String[] mItemNames = new String[] { "WakeLock", "NFC", "Other" };
    private final Class<?>[] mItemActivity = new Class[] { WakeLockActivity.class, NfcActivity.class, WakeLockActivity.class };
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViews();
        putDataIntoArray();
        setListViewControls();
        setListeners();
    }

    private void findViews() {
        mListView = (ListView) findViewById(R.id.main_list_view);
    }

    private void putDataIntoArray() {
        for (int i = 0; i < mItemNames.length; i++) {
            Item item = new Item();
            item.name = mItemNames[i];
            item.activity = mItemActivity[i];

            HashMap<String, Item> itemMap = new HashMap<String, Item>();
            itemMap.put("Item", item);
            mListItems.add(itemMap);
        }
    }

    private void setListViewControls() {
        // Enable hot key
        mListView.setTextFilterEnabled(true);
        mListView.setEmptyView(findViewById(R.id.empty));

        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, mItemNames);
        mListView.setAdapter(adapter);
    }

    private void setListeners() {
        mListView.setOnItemClickListener(new ListClickListener());
    }

    private class ListClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemClick");

            ListView listView = (ListView) parent;
            /*
            HashMap<String, Item> map = (HashMap<String, Item>) listView.getItemAtPosition(position);
            Item item = map.get("Item");
             */

            Intent mIntent = new Intent(MainActivity.this, mItemActivity[position]);
            MainActivity.this.startActivity(mIntent);

            Toast.makeText(MainActivity.this, "ID:" + id + ", text:" + listView.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private class Item {
        private String name;
        private Class activity;
    }
}
