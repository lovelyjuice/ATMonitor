package com.example.wenqh.atmonitor;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CredentialActivity extends AppCompatActivity
{
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credential);
        textView = (TextView) findViewById(R.id.cretencial);
        Cursor cursor = LoginActivity.db.rawQuery("select * from OperationHistory where id in (select id from UserInfo where name=?) order by time desc limit 1", new String[]{String.valueOf(MainActivity.currentUser)});
        cursor.moveToFirst();
        double mount = cursor.getDouble(cursor.getColumnIndex("mount"));
        double balance = cursor.getDouble(cursor.getColumnIndex("balance"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        int op = cursor.getInt(cursor.getColumnIndex("operation"));
        String opstr;
        if (op == 1) opstr = "存款";
        else opstr = "取款";
        String temp=String.format("ID:%d\n用户名：%s\n操作：%s\n数量（元）：%.2f\n余额：%.2f\n时间：%s", id, MainActivity.currentUser, opstr, mount, balance, time);
        textView.setText(temp);
        cursor.close();
    }
}
