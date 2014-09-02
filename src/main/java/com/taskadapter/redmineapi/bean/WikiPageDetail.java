package com.taskadapter.redmineapi.bean;

/**
 * 
 **/
public class WikiPageDetail extends WikiPage {

    private WikiPageDetail parent;
    private String text;
    private User user;

    public WikiPageDetail getParent() {
        return parent;
    }

    public void setParent(WikiPageDetail parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    

}
