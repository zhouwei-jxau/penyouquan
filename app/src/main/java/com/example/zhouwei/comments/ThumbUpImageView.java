package com.example.zhouwei.comments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.network.zhouwei.http_network.Server;

/**
 * Created by zhouwei on 2018/12/16.
 */

public class ThumbUpImageView extends android.support.v7.widget.AppCompatImageView {

    private  int uid;
    private int commenitid;
    private ThumbUpTextView thumbUpTextView;
    private  boolean thumbUped=false;

    public ThumbUpImageView(Context context) {
        super(context);
    }

    public ThumbUpImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThumbUpImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCommenitid() {
        return commenitid;
    }

    public void setCommenitid(int commenitid) {
        this.commenitid = commenitid;
    }


    public ThumbUpTextView getTextView() {
        return thumbUpTextView;
    }

    public void setTextView(ThumbUpTextView textView) {
        this.thumbUpTextView = textView;
    }


    public void thumbUp()
    {
        if(thumbUped)
            return;
        Server server=new Server();
        server.thumbUp(this.getUid(),this.getCommenitid());
        this.thumbUpTextView.setApproverCount(this.thumbUpTextView.getApproverCount()+1);
        this.thumbUpTextView.setApprover(CurrentUser.getUser().getUsername()+"、"+this.thumbUpTextView.getApprover());
        this.thumbUpTextView.setText();
        this.setImageResource(R.drawable.thumbuped);
        thumbUped=true;
    }

}
