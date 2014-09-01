package com.taskadapter.redmineapi.bean;

import java.util.Date;

/**
 * 
 **/
public class WikiPage {

    private WikiPage parent;
    private String title;
    private Integer version;

    private Date createdOn;
    private Date updatedOn;

    private String text;
    private User user;

    public WikiPage getParent() {
        return parent;
    }

    public void setParent(WikiPage parent) {
        this.parent = parent;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getTitle() {
        return title;
    }

    public Integer getVersion() {
        return version;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

}
