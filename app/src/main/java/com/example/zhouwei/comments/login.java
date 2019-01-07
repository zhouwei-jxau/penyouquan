package com.example.zhouwei.comments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends AppCompatActivity {
    final private String loginSuccess="login success";

    private String uid=null;
    private String password=null;

    Activity this_activity=this;

    private Thread thread=new Thread()
    {
        private String login(String uidType,String uid,String password)
        {
            String spec="http://119.29.60.170/index.aspx?type=login&"+uidType+"="+uid+"&password="+password;

            try {
                URL url=new URL(spec);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestProperty("Charaset","utf-8");
                conn.connect();
                if(conn.getResponseCode()==200)
                {
                    InputStream inputStream=conn.getInputStream();
                    InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                    BufferedReader bufferedReader=new BufferedReader(inputStreamReader);

                    StringBuffer stringBuffer=new StringBuffer();
                    String t;

                    while((t=bufferedReader.readLine())!=null) {
                        stringBuffer.append(t);
                    }

                    return stringBuffer.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private String loginByUserName(String username,String password)
        {
            return login("username",username,password);
        }

        private String loginByEmail(String email,String password)
        {
            return login("email",email,password);
        }

        private String loginByPhone(String phone,String password)
        {
            return  login("phone",phone,password);
        }

        public void run()
        {
            boolean flag=false;

            if(loginByUserName(uid,password).equals(loginSuccess))
            {
                flag=true;
            }
            else if(loginByEmail(uid,password).equals(loginSuccess))
            {
                flag=true;
            }
            else if(loginByPhone(uid,password).equals(loginSuccess))
            {
                flag=true;
            }

            if(flag)
            {
                Looper.prepare();
                Toast.makeText(this_activity, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.setClass(this_activity,comment.class);
                Bundle bundle=new Bundle();
                bundle.putString("uid",uid);
                intent.putExtras(bundle);

                startActivity(intent);
                Looper.loop();
            }
            else
            {
                Looper.prepare();
                Toast.makeText(this_activity, "登录失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    };

    private Thread newThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_button=(Button)findViewById(R.id.button_login);
        EditText username_editText=(EditText) findViewById(R.id.editText_uid);
        EditText password_editText=(EditText)findViewById(R.id.editText_password);
        Button to_regist_buton=(Button)findViewById(R.id.loginToRegist);
        to_regist_buton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        login_button.setOnClickListener((View v) ->{
            uid=username_editText.getText().toString();
            password=password_editText.getText().toString();
            if(Verification.isEmpty(this,uid,"请输入账号"))
                return;
            if(Verification.isEmpty(this,password,"请输入密码"))
                return;
            newThread=new Thread(thread);
            newThread.start();
        });

        to_regist_buton.setOnClickListener(v->{

            Intent intent=new Intent();
            intent.setClass(this,regist.class);
            startActivity(intent);
        });
    }
}
