package com.example.zhouwei.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.network.zhouwei.http_network.Server;

/**
 * Created by zhouwei on 2018/12/16.
 */

public class DiscussButton extends android.support.v7.widget.AppCompatButton {

    TextView discussTextView=null;

    EditText discussMessageEditView=null;

    int commentId=0;

    public DiscussButton(Context context) {
        super(context);
    }

    public DiscussButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditText getDiscussMessageEditView() {
        return discussMessageEditView;
    }

    public void setDiscussMessageEditView(EditText discussMessageEditView) {
        this.discussMessageEditView = discussMessageEditView;
    }

    public TextView getDiscussTextView() {
        return discussTextView;
    }

    public void setDiscussTextView(TextView discussTextView) {
        this.discussTextView = discussTextView;
    }


    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }


    public void discuss()
    {
        Server server=new Server();
        server.discuss(this.getCommentId(),CurrentUser.getUser().getUsername(),this.getDiscussMessageEditView().getText().toString());

        TextView textView=this.getDiscussTextView();
        textView.setText(textView.getText().toString()+"\n"+CurrentUser.getUser().getUsername()+":"+this.getDiscussMessageEditView().getText().toString());
    }

}
