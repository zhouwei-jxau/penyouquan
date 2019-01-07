package com.example.zhouwei.comments;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by zhouwei on 2018/12/16.
 */

public class ThumbUpTextView extends android.support.v7.widget.AppCompatTextView {
    private int approverCount=0;

    public int getApproverCount() {
        return approverCount;
    }

    public void setApproverCount(int approverCount) {
        this.approverCount = approverCount;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    private String approver;

    public ThumbUpTextView(Context context) {
        super(context);
    }

    public ThumbUpTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThumbUpTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText()
    {
        this.setText(approver);
        if(this.approverCount>10)
        {
            this.setText(this.getText().toString()+"等");
        }

        this.setText(this.getText().toString()+this.getApproverCount()+"人觉得很赞");
    }
}
