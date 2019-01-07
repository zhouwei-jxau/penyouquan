package com.example.zhouwei.comments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.network.zhouwei.http_network.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class regist extends AppCompatActivity {
    final private int maxSendSize=4096;
    final private String romoteHost="119.29.60.170";
    private String username=null;
    private String password=null;
    private String email=null;
    private String phone=null;

    Activity this_activity=this;

    private ImageView selectHeadpartrait;

    private Thread registThread=new Thread()
    {

        public void run()
        {
            String spec="http://"+romoteHost+"/index.aspx?type=regist&username="+
                    username+"&password="+
                    password+"&email="+
                    email+"&phone="+
                    phone;

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

                    if(stringBuffer.toString().equals("regist success"))
                    {
                        Server uploadPartraitToServer=new Server();
                        uploadPartraitToServer.setServer("119.29.60.170");
                        uploadPartraitToServer.imageFileUpload(selectHeadpartrait,username);

                        Looper.prepare();
                        Toast.makeText(this_activity, "注册成功", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent();
                        intent.setClass(this_activity,login.class);
                        startActivity(intent);

                        Looper.loop();
                    }

                    else
                    {
                        Looper.prepare();
                        Toast.makeText(this_activity, "注册失败:"+stringBuffer.toString(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Thread newThread=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        Button regist_button=(Button)findViewById(R.id.button_regist);
        EditText username_editText=(EditText) findViewById(R.id.editText_username);
        EditText password_editText=(EditText)findViewById(R.id.editText_password);
        EditText email_editText=(EditText)findViewById(R.id.editText_email);
        EditText phone_editText=(EditText)findViewById(R.id.editText_phone);

        regist_button.setOnClickListener((View v) ->{
            username=username_editText.getText().toString();
            password=password_editText.getText().toString();
            email=email_editText.getText().toString();
            phone=phone_editText.getText().toString();

            if(Verification.isEmpty(this,username,"请输入用户名"))
                return;
            if(Verification.isMinLength(this,username,"用户名长度不能低于2位",2))
                return;
            if(Verification.isEmpty(this,password,"请输入密码"))
                return;
            if(Verification.isMinLength(this,password,"密码长度不能小于6位",6))
                return;
            if(Verification.isEmpty(this,email,"请输入邮箱"))
                return;
            if(Verification.isEmpty(this,phone,"请输入手机号码"))
                return;;
            if(Verification.isMinLength(this,phone,"请输入正确的手机号码",11))
                return;
            if(Verification.isMaxLength(this,phone,"请输入正确的手机号码",12))
                return;

            newThread=new Thread(registThread);
            newThread.start();
        });

        selectHeadpartrait=(ImageView)findViewById(R.id.selectHeadPartrait);

        selectHeadpartrait.setOnClickListener(v-> {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent,1);
        });

    }


    protected  void onActivityResult(int requestCode,int resultCode,Intent data) {
        if(resultCode==Activity.RESULT_OK)
        {
            if(requestCode==1)
            {
                Uri uri=data.getData();
                selectHeadpartrait.setImageURI(uri);
            }
        }
    }
}
