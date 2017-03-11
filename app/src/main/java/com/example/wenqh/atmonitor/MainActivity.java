package com.example.wenqh.atmonitor;

import android.content.DialogInterface;
import android.content.Intent;
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
    private static final double LIMIT=-2000;
    public static String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivitySet.addActivity(this);
        Button store_button=(Button) findViewById(R.id.store_button);
        Button fetch_button=(Button) findViewById(R.id.fetch_button);
        store_button.setOnClickListener(this);
        fetch_button.setOnClickListener(this);
        Intent parentIntent=getIntent();
        currentUser= parentIntent.getStringExtra("username");
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
        EditText editText=(EditText) findViewById(R.id.piece);
        TextView textView=(TextView)findViewById(R.id.balance_text);
        int moneyPiece=Integer.parseInt(editText.getText().toString());
        double balance=Double.parseDouble(textView.getText().toString());
        switch(v.getId())
        {
            case R.id.store_button:
                /*
                存款
                 */
                textView.setText((balance+moneyPiece*100)+"");
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("已存入"+moneyPiece*100+"元！");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                dialog.show();
                break;
            case R.id.fetch_button:
                if(moneyPiece>50)
                {
                    Toast.makeText(this,"单次取出金额不得超过5000元",Toast.LENGTH_SHORT).show();
                }
                else if((balance-moneyPiece*100)<LIMIT)
                {
                    Toast.makeText(this,"超过透支额度",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    /*
                    将操作同步至数据库
                     */
                    textView.setText((balance-moneyPiece*100)+"");
                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(MainActivity.this);
                    dialog1.setTitle("提示");
                    dialog1.setMessage("已取出"+moneyPiece*100+"元！");
                    dialog1.setCancelable(true);
                    dialog1.setPositiveButton("确定", new DialogInterface.OnClickListener()
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
