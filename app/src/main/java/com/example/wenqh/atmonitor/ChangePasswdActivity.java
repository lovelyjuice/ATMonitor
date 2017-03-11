package com.example.wenqh.atmonitor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswdActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText old;
    EditText new_;
    EditText confirm;
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
        String currentUser = MainActivity.currentUser;
        Button button = (Button) findViewById(R.id.change_passwd_button);
        button.setOnClickListener(this);
        old = (EditText) findViewById(R.id.old_passwd);
        new_ = (EditText) findViewById(R.id.new_passwd);
        confirm = (EditText) findViewById(R.id.confirm_passwd);

    }

    @Override
    public void onClick(View v)
    {
        String realPasswd = "123456";     //123456替换为数据库中获取的密码
        switch (v.getId())
        {
            case R.id.change_passwd_button:
                if (!new_.getText().toString().equals(confirm.getText().toString()))
                {
                    Toast.makeText(ChangePasswdActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
                else if (!old.getText().toString().equals(realPasswd))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ChangePasswdActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("原密码错误！！！\n请重新输入原密码");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    dialog.show();
                }
                else
                {
                    /*
                    更改密码
                     */
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
