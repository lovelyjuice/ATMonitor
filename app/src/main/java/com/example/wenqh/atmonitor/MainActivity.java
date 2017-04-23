package com.example.wenqh.atmonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final double LIMIT = -2000;
    static String currentUser;
    static double balance;
    static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivitySet.addActivity(this);
        Button store_button = (Button) findViewById(R.id.store_button);
        Button fetch_button = (Button) findViewById(R.id.fetch_button);
        store_button.setOnClickListener(this);
        fetch_button.setOnClickListener(this);
        Intent parentIntent = getIntent();
        currentUser = parentIntent.getStringExtra("username");
        TextView textView = (TextView) findViewById(R.id.username_text);
        textView.setText(currentUser);
        Cursor cursor = LoginActivity.db.rawQuery("select id,balance from UserInfo where name=?", new String[]{currentUser});
        cursor.moveToFirst();
        balance = cursor.getDouble(cursor.getColumnIndex("balance"));
        id = cursor.getInt(cursor.getColumnIndex("id"));
        TextView balanceTxt = (TextView) findViewById(R.id.balance_text);
        balanceTxt.setText(String.format("%.2f", balance));
        cursor.close();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivitySet.removeActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.mian_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.change_passwd:
                Intent intent1 = new Intent(MainActivity.this, ChangePasswdActivity.class);
                startActivity(intent1);
                break;
            case R.id.history:
                Intent intent2 = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent2);
                break;
            case R.id.quit:
                ActivitySet.destroyAll();
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        EditText editText = (EditText) findViewById(R.id.piece);
        TextView textView = (TextView) findViewById(R.id.balance_text);
        if (editText.getText().toString().equals(""))
        { Toast.makeText(this, "您还没有输入任何内容！", Toast.LENGTH_SHORT).show(); }
        else
        {
            int moneyPiece = Integer.parseInt(editText.getText().toString());
            switch (v.getId())
            {
                case R.id.store_button:
                /*
                存款
                 */
                    balance = balance + moneyPiece * 100;
                    LoginActivity.db.execSQL("update UserInfo set balance=? where name=?", new String[]{String.format("%.2f", balance), MainActivity.currentUser});
                    LoginActivity.db.execSQL("insert into OperationHistory(id,mount,balance,operation,time) values(?,?,?,?,datetime('now','localtime'))", new String[]{id + "", moneyPiece * 100 + "", String.format("%.2f", balance), "1"});
//                    Cursor ss=LoginActivity.db.rawQuery("select time from OperationHistory",null);
//                    ss.moveToFirst();
//                    Log.d("haha",ss.getString(ss.getColumnIndex("time")));
                    textView.setText(String.format("%.2f", balance));
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("已存入" + moneyPiece * 100 + "元！\n是否打印凭条？");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(MainActivity.this, CredentialActivity.class);
                            startActivity(intent);
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    dialog.show();
                    break;
                case R.id.fetch_button:
                    if (moneyPiece > 50)
                    {
                        Toast.makeText(this, "单次取出金额不得超过5000元", Toast.LENGTH_SHORT).show();
                    }
                    else if ((balance - moneyPiece * 100) < LIMIT)
                    {
                        Toast.makeText(this, "您的透支额度只有" + -LIMIT + "元", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                    /*
                    将操作同步至数据库
                     */
                        balance = balance - moneyPiece * 100;
                        LoginActivity.db.execSQL("update UserInfo set balance=? where name=?", new String[]{String.format("%.2f", balance), MainActivity.currentUser});
                        LoginActivity.db.execSQL("insert into OperationHistory(id,mount,balance,operation,time) values(?,?,?,?,datetime('now','localtime'))", new String[]{id + "", moneyPiece * 100 + "", String.format("%.2f", balance), "0"});
                        textView.setText(String.format("%.2f", balance));
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(MainActivity.this);
                        dialog1.setTitle("提示");
                        dialog1.setMessage("已取出" + moneyPiece * 100 + "元！\n是否打印凭条？");
                        dialog1.setCancelable(true);
                        dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(MainActivity.this, CredentialActivity.class);
                                startActivity(intent);
                            }
                        });
                        dialog1.setNegativeButton("取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        dialog1.show();
                    }
                    break;
            }
        }

    }
}
