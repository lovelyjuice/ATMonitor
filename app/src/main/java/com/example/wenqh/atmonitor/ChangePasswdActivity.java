package com.example.wenqh.atmonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.wenqh.atmonitor.MainActivity.currentUser;

public class ChangePasswdActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText old;
    EditText new_;
    EditText confirm;
    static int times = 0;                  //记录输错密码的次数，超过3次强制下线

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivitySet.removeActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwd);
        ActivitySet.addActivity(this);
        Button button = (Button) findViewById(R.id.change_passwd_button);
        button.setOnClickListener(this);
        old = (EditText) findViewById(R.id.old_passwd);
        new_ = (EditText) findViewById(R.id.new_passwd);
        confirm = (EditText) findViewById(R.id.confirm_passwd);

    }

    @Override
    public void onClick(View v)
    {
        String oldPasswd = old.getText().toString();
        String newPasswd = new_.getText().toString();
        String confirmPasswd = confirm.getText().toString();
        if (oldPasswd.equals("") || newPasswd.equals("") || confirmPasswd.equals("")) {
            Toast.makeText(this, "请确认是否有未输入的数据！", Toast.LENGTH_SHORT).show();
        }
        else {
            String realPasswd;
            Cursor cursor = LoginActivity.db.rawQuery("select passwd from UserInfo where name=?", new String[]{currentUser});
            cursor.moveToFirst();
            realPasswd = cursor.getString(cursor.getColumnIndex("passwd"));
            switch (v.getId()) {
                case R.id.change_passwd_button:
                    if (!newPasswd.equals(confirmPasswd)) {
                        Toast.makeText(ChangePasswdActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                    else if (!oldPasswd.equals(realPasswd)) {
                        times++;
                        if (times >= 3) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePasswdActivity.this);
                            dialog.setTitle("⚠️️警告");
                            dialog.setMessage("连续3次密码错误，即将退出登录！");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("退出", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent("com.example.wenqh.atmonitor.FORCE_OFFLINE");
                                    sendBroadcast(intent);
                                    finish();
                                }
                            });
                            dialog.show();
                        }
                        else {
                            Log.d("times", String.valueOf(times));
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePasswdActivity.this);
                            dialog.setTitle("提示");
                            dialog.setMessage("原密码错误！！！\n请重新输入原密码\n" + (3 - times) + "次错误后将退出登录");
                            dialog.setCancelable(true);
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });
                            dialog.show();
                        }
                    }
                    else {
                    /*
                    更改密码
                     */
                        LoginActivity.db.execSQL("update UserInfo set passwd=? where name=?", new String[]{newPasswd, currentUser});
                        times = 0;
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePasswdActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("密码更改成功，请牢记新密码！");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        dialog.show();
                    }
            }
        }
    }
}
