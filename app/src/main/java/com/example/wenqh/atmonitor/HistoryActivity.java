package com.example.wenqh.atmonitor;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Double mount = 0.0;
        Double balance = 0.0;
        String time = "";
        String opstr;
        int op;
        ArrayList<String> data = new ArrayList<>();
        data.add(String.format("%-15s %-8s %-10s %-6s", "操作", "数量", "余额", "时间"));
        data.add(String.format("%-10s %5d %5.2f", "arthinking", 2, 9.6512));
        data.add(String.format("%-10s %5d %5.2f", "Jason", 1, 9.87654321));
        Log.d("123", String.format("%-10s %5d %5.2f", "arthinking", 2, 9.6512));
        Log.d("123", String.format("%-10s %5d %5.2f", "Jason", 1, 9.87654321));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActivitySet.addActivity(this);
        Cursor cursor = LoginActivity.db.rawQuery("select * from OperationHistory where id in (select id from UserInfo where name=?) order by time desc", new String[]{String.valueOf(MainActivity.currentUser)});
        if (cursor.moveToFirst()) {
            do {
                mount = cursor.getDouble(cursor.getColumnIndex("mount"));
                balance = cursor.getDouble(cursor.getColumnIndex("balance"));
                time = cursor.getString(cursor.getColumnIndex("time"));
                op = cursor.getInt(cursor.getColumnIndex("operation"));
                if (op == 1) opstr = "存款";
                else opstr = "取款";
                data.add(String.format("%-4s%20.2f%20.2f", opstr, mount, balance));
            }
            while (cursor.moveToNext());
        }
        Log.d("haha", mount + " " + balance + " " + time);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(HistoryActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivitySet.removeActivity(this);
    }
}
