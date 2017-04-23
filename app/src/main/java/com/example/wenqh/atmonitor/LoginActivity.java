package com.example.wenqh.atmonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    EditText nameEdt;
    EditText passwdEdt;
    CheckBox nameChk;
    CheckBox passwdChk;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button loginButton;
    MyDatabaseHelper dbHelper;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivitySet.addActivity(this);

        nameEdt = (EditText) findViewById(R.id.login_user);
        passwdEdt = (EditText) findViewById(R.id.login_passwd);
        nameChk = (CheckBox) findViewById(R.id.remember_username);
        passwdChk = (CheckBox) findViewById(R.id.remember_passwd);
        preferences = getSharedPreferences("pre_data", MODE_PRIVATE);
        editor = getSharedPreferences("pre_data", MODE_PRIVATE).edit();
        loginButton = (Button) findViewById(R.id.login_button);

        loginButton.setOnClickListener(this);
        String preName = preferences.getString("name", "");
        String prePasswd = preferences.getString("passwd", "");
        nameEdt.setText(preName);
        passwdEdt.setText(prePasswd);
        nameChk.setChecked(preferences.getBoolean("remember_name",false));
        passwdChk.setChecked(preferences.getBoolean("remember_passwd",false));
        passwdChk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //记住密码就一定得记住用户名
                if (!nameChk.isChecked() && passwdChk.isChecked()) nameChk.setChecked(true);
            }
        });
        nameChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //忘记用户名就一定要忘记密码
                if (!nameChk.isChecked() && passwdChk.isChecked())passwdChk.setChecked(false);
            }
        });
        dbHelper=new MyDatabaseHelper(this,"user.db",null,1);
        db=dbHelper.getWritableDatabase();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ActivitySet.removeActivity(this);
        if(!nameChk.isChecked())
        {
            editor.putBoolean("remember_name",false);
            editor.putString("name","");
        }
        if(!passwdChk.isChecked())
        {
            editor.putBoolean("remember_passwd",false);
            editor.putString("passwd","");
        }
        editor.apply();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ChangePasswdActivity.times=0;               //静态变量的值在活动销毁后不会重置，需要手动置0
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.login_button):
                String username = nameEdt.getText().toString();
                String passwd = passwdEdt.getText().toString();
                if (username.equals("") || passwd.equals(""))
                { Toast.makeText(this, "你忘记输用户名或密码了吧(⊙﹏⊙)", Toast.LENGTH_SHORT).show(); }
                else
                {
                    boolean status=true;
                    Cursor cursor=db.rawQuery("select name from UserInfo where name=? and passwd=?",new String[]{username,passwd});
                    if(cursor.getCount()==0)status=false;
                    cursor.close();
                    /*
                    验证用户名和密码。。。
                     */
                    if (status)         //登录成功
                    {
                        if(nameChk.isChecked())
                        {
                            editor.putBoolean("remember_name",true);
                            editor.putString("name",username);
                        }
                        else
                        {
                            editor.putBoolean("remember_name",false);
                            editor.putString("name","");
                        }
                        if(passwdChk.isChecked())
                        {
                            editor.putBoolean("remember_passwd",true);
                            editor.putString("passwd",passwd);
                        }
                        else
                        {
                            editor.putBoolean("remember_passwd",false);
                            editor.putString("passwd","");
                        }
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                    else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                        dialog.setTitle("提示");
                        dialog.setMessage("抱歉，用户名或密码错误！");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        dialog.show();
                    }
                }
                break;
            default:
                break;
        }

    }
}
