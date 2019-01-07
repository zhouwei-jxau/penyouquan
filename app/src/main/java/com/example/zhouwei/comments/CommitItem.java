package com.example.zhouwei.comments;

/**
 * Created by zhouwei on 2018/12/4.
 */

public class CommitItem {

    private String username;
    private String text;
    private String[] imageUri;
    private String lookThroughNum;
    private String approver;
    private String approverCount;
    private String id;
    private String discuss;
    private String location;
    private String tag;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getImageUri() {
        return imageUri;
    }

    public void setImageUri(String[] imageUri) {
        this.imageUri = imageUri;
    }

    public String getLookThroughNum() {
        return lookThroughNum;
    }

    public void setLookThroughNum(String lookThroughNum) {
        this.lookThroughNum = lookThroughNum;
    }


    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproverCount() {
        return approverCount;
    }

    public void setApproverCount(String approverCount) {
        this.approverCount = approverCount;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDiscuss() {
        return discuss;
    }

    public void setDiscuss(String discuss) {
        this.discuss = discuss;
    }
}
