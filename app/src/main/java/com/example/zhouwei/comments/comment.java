package com.example.zhouwei.comments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.network.zhouwei.http_network.Comment;
import com.network.zhouwei.http_network.Server;

import java.util.ArrayList;
import java.util.List;

public class comment extends AppCompatActivity {
    private final int RESULT_WRITED=2;
    private final int UPDATE_HEADPARTRAIT=3;
    ImageView display_headpartrait=null;
    private Server server=new Server();
    private static List<Comment> comments=null;
    private CommentAdapter commentAdapter=null;
    private CompatiableListView list=null;

    private Thread thread_downloadHeadpartrait=new Thread()
    {
        public void run()
        {
            //设置头像
            String username=CurrentUser.getUser().getUsername();
            Bitmap headpartrait=server.imageFileDownload(username);
            Message message=new Message();
            message.arg1=UPDATE_HEADPARTRAIT;
            message.obj=headpartrait;
            handler.sendMessage(message);
        }
    };

    private Handler handler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            display_headpartrait.setImageBitmap((Bitmap)msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        server.setServer("119.29.60.170");
        //返回
        ImageView button_back=(ImageView)findViewById(R.id.imageView_back_login);
        ImageView button_write=(ImageView)findViewById(R.id.imageView_write);

        button_back.setOnClickListener(v-> {
            Intent intent=new Intent();
            intent.setClass(this,login.class);
            startActivity(intent);
        });

        button_write.setOnClickListener(v->{
            Intent intent=new Intent();
            intent.setClass(this,write.class);
            startActivity(intent);
        });

        TextView display_username=(TextView)findViewById(R.id.comment_display_username);
        display_headpartrait=(ImageView)findViewById(R.id.comment_display_headpartrait);
        //填充listview数据
        //获取前三十行的数据
        comments=server.getComments(0,30);
        fillData();
        //滚动到顶端
        scrollToTop();
        User t=CurrentUser.getUser();
        String username=null;
        Bundle data=this.getIntent().getExtras();
        if(data!=null)
        {

            String uid=data.getString("uid");
            username=server.getUserNameByUid(uid);
            //把当前用户保存到全局变量中
            User u=new User();
            u.setUid(server.getUidByUserName(username));
            u.setUsername(username);
            CurrentUser.setUser(u);
        }
        else
        {
            username=t.getUsername();
        }
        display_username.setText(username);
        Thread thread=new Thread(thread_downloadHeadpartrait);
        thread.start();
    }

    public void onResume() {
        super.onResume();
    }

    protected  void onActivityResult(int requestCode,int resultCode,Intent data) {
        if(requestCode==RESULT_WRITED)
        {
            if(resultCode==RESULT_OK)
            {
                fillData();
            }
        }
    }


    public void fillData()
    {
        //填充listview数据
        list=(CompatiableListView)findViewById(R.id.data_list);
        List<CommitItem> arraylist =new ArrayList<CommitItem>();
        for(int i=0;i<comments.size();i++)
        {
            String s_username=comments.get(i).getUsername();
            String s_context=comments.get(i).getContext();
            String s_images_url=comments.get(i).getImages_url();
            String s_look_through_num=comments.get(i).getLookThroughNum();
            String s_approver=comments.get(i).getApprover();
            String s_approverCount=comments.get(i).getApproverCouner();
            String s_id=comments.get(i).getCommentId();
            String s_discuss=comments.get(i).getDiscuss();
            String s_location=comments.get(i).getLocation();
            String s_tag=comments.get(i).getTag();
            CommitItem t=new CommitItem();
            t.setUsername(s_username);
            t.setText(s_context);
            t.setLookThroughNum((s_look_through_num));
            t.setApprover(s_approver);
            t.setApproverCount(s_approverCount);
            t.setId(s_id);
            t.setDiscuss(s_discuss);
            t.setLocation(s_location);
            t.setTag(s_tag);
            if(s_images_url.equals(""))
                t.setImageUri(null);
            else
                t.setImageUri(s_images_url.split("\n"));
            arraylist.add(t);
        }

        commentAdapter=new CommentAdapter(this,R.layout.comment_item,arraylist);
        commentAdapter.setListView(list);
        list.setAdapter(commentAdapter);
    }


    public void scrollToTop()
    {
        //滚动到顶端
        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView_comment_total);
        scrollView.smoothScrollTo(0,0);
    }
}
