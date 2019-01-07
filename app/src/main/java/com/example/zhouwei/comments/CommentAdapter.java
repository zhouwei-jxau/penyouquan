package com.example.zhouwei.comments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.network.zhouwei.http_network.Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhouwei on 2018/12/4.
 */

class UserUri{
    String uri;
    String username;
    int position;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTag()
    {
        return position+username+"_"+uri;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

public class CommentAdapter extends ArrayAdapter{
    private int resourceId=-1;

    private Bitmap g_bitmap=null;
    private ListView listView=null;
    private List<UserUri> uriList=new ArrayList<UserUri>();
    private List<String> headpartraitList=new ArrayList<String>();
    private Thread currentGetBitmapThread=null;
    private Thread currentGetHeadpartraitThread=null;
    private HashMap<String,Bitmap> bitmaps=new HashMap<String,Bitmap>();
    private HashMap<String,Bitmap> headpatraits=new HashMap<String,Bitmap>();

    final int UPDATE_IMAGE=1;
    final int UPDATE_HEADPARTRAIT=2;

    private Handler handler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            if(msg.arg1==UPDATE_IMAGE)
            {
                String key=(String) msg.obj;
                Bitmap bm=bitmaps.get(key);
                ImageView view=((ImageView)listView.findViewWithTag(key));
                if(bm!=null)
                    if(view!=null)
                    view.setImageBitmap(bm);
            }

            if(msg.arg1==UPDATE_HEADPARTRAIT)
            {
                String key=(String) msg.obj;
                Bitmap bm=headpatraits.get(key);
                ImageView view=((ImageView)listView.findViewWithTag(key));
                if(bm!=null)
                    if(view!=null)
                        view.setImageBitmap(bm);
            }
        }
    };

    private Thread thread_getHeadpartrait=new Thread()
    {
        public void run()
        {
            while(headpartraitList.size()>0)
            {
                String key=headpartraitList.get(0);
                if(headpatraits.get(key)==null)
                {
                    Server server=new Server();
                    String[] t=key.split("_");
                    Bitmap tBitmap=server.imageFileDownload(t[1]);
                    headpatraits.put(key,tBitmap);
                    headpartraitList.remove(0);
                    Message tMessage=new Message();
                    tMessage.arg1=UPDATE_HEADPARTRAIT;
                    tMessage.obj=key;
                    handler.sendMessage(tMessage);
                }
                else
                {
                    headpartraitList.remove(0);
                }
            }
        }

    };

    private Thread thread_getBitmap=new Thread()
    {
        public void run(){
            String spec="http://119.29.60.170/";

            try {
                while(uriList.size()>0)
                {
                    URL url=new URL(spec+uriList.get(0).getUri());
                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                    conn.setRequestProperty("Charaset","utf-8");
                    conn.connect();
                    if(conn.getResponseCode()==200)
                    {
                        InputStream inputStream=conn.getInputStream();
                        g_bitmap= BitmapFactory.decodeStream(inputStream);
                        bitmaps.put(uriList.get(0).getTag(),g_bitmap);
                        Message message=new Message();
                        message.arg1=UPDATE_IMAGE;
                        message.obj=new String(uriList.get(0).getTag());
                        handler.sendMessage(message);
                        uriList.remove(0);
                }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void getHeadPartraitFromServer(String key)
    {
        headpartraitList.add(key);
        if(currentGetHeadpartraitThread==null||currentGetHeadpartraitThread.isAlive()==false)
        {
            currentGetHeadpartraitThread=new Thread(thread_getHeadpartrait);
            currentGetHeadpartraitThread.start();
        }
    }

    private void getBitmapFromServer(UserUri uri)
    {
        uriList.add(uri);
        if(currentGetBitmapThread==null||currentGetBitmapThread.isAlive()==false)
        {
            currentGetBitmapThread=new Thread(thread_getBitmap);
            currentGetBitmapThread.start();
        }
    }

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<?> objects) {
        super(context, resource, objects);
        this.resourceId=resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        CommitItem item=(CommitItem) this.getItem(position);
        View view=(View) LayoutInflater.from(getContext()).inflate(resourceId,null);
        //设置头像
        ImageView imageView_headpartrait=(ImageView)view.findViewById(R.id.image_headpartrait);
        imageView_headpartrait.setTag("headpartrait"+position+"_"+item.getUsername());
        getHeadPartraitFromServer("headpartrait"+position+"_"+item.getUsername());
        //设置用户名和说说内容
        TextView textView_username=(TextView)view.findViewById(R.id.text_username);
        textView_username.setText(item.getUsername());
        TextView textView_context=(TextView)view.findViewById(R.id.text_context);
        textView_context.setText(item.getText());
        TextView textView_lookThroughNum=(TextView)view.findViewById(R.id.textView_lookThroughNum);
        textView_lookThroughNum.setText("浏览"+item.getLookThroughNum()+"次");
        ThumbUpTextView textView_thumbUp=(ThumbUpTextView)view.findViewById(R.id.textView_thumbUp);
        textView_thumbUp.setApprover(item.getApprover().replace("\n","、"));
        textView_thumbUp.setApproverCount(Integer.parseInt(item.getApproverCount()));
        textView_thumbUp.setText();
        TextView textView_discuss=(TextView)view.findViewById(R.id.textView_discuss);
        textView_discuss.setText(item.getDiscuss());
        DiscussButton discussButton=(DiscussButton)view.findViewById(R.id.button_sendMessage);
        discussButton.setCommentId(Integer.parseInt(item.getId()));
        discussButton.setDiscussMessageEditView((EditText)view.findViewById(R.id.editText_editMessage));
        discussButton.setDiscussTextView((TextView)view.findViewById(R.id.textView_discuss));
        discussButton.setOnClickListener(v->{
            ((DiscussButton)v).discuss();
        });
        //地点和标签
        TextView location=(TextView)view.findViewById(R.id.textView_location);
        if(!item.getLocation().equals("")&&item.getLocation()!=null)
        {
            location.setText("发布于"+item.getLocation());
        }
        else
        {
            location.setText("");
            location.setHeight(0);
            LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.location_layout);
            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.height=0;
            linearLayout.setLayoutParams(layoutParams);
        }
        TextView tag=(TextView)view.findViewById(R.id.textView_tag);
        if(!item.getTag().equals("")&&item.getTag()!=null)
        {
            tag.setText(item.getTag());
        }
        else
        {
            tag.setText("");
            tag.setHeight(0);
            LinearLayout linearLayout=(LinearLayout)view.findViewById(R.id.tag_layout);
            LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.height=0;
            linearLayout.setLayoutParams(layoutParams);
        }
        //点赞
        ThumbUpImageView image_thumb=(ThumbUpImageView)view.findViewById(R.id.imageView_thumbUp);
        image_thumb.setCommenitid(Integer.parseInt(item.getId()));
        User currentUser=CurrentUser.getUser();
        int uid=Integer.parseInt(currentUser.getUid());
        image_thumb.setUid(uid);
        image_thumb.setTextView(textView_thumbUp);
        image_thumb.setOnClickListener(v->{
            ((ThumbUpImageView)v).thumbUp();
        });
        //判断图片数量，选择显示图片
        int image_counter;
        if(item.getImageUri()!=null)
            image_counter=item.getImageUri().length;
        else
            image_counter=0;
        ImageView[] images={(ImageView)view.findViewById(R.id.image1),
                (ImageView)view.findViewById(R.id.image2),
                (ImageView)view.findViewById(R.id.image3),
                (ImageView)view.findViewById(R.id.image4),
                (ImageView)view.findViewById(R.id.image5),
                (ImageView)view.findViewById(R.id.image6),
                (ImageView)view.findViewById(R.id.image7),
                (ImageView)view.findViewById(R.id.image8),
                (ImageView)view.findViewById(R.id.image9)};

        for(int i=0;i<image_counter;i++)
        {
            UserUri u=new UserUri();
            u.setUri(item.getImageUri()[i]);
            u.setUsername(item.getUsername());
            u.setPosition(position);
            images[i].setTag(u.getTag());
            getBitmapFromServer(u);
        }

        for(int i=image_counter;i<9;i++)
        {
            ViewGroup.LayoutParams layoutParams=images[i].getLayoutParams();
            layoutParams.height=0;
            images[i].setLayoutParams(layoutParams);
            images[i].measure(0,0);
        }
        return view;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
