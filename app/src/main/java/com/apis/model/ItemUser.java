package com.apis.model;

import java.io.Serializable;

public class ItemUser implements Serializable {
    private boolean selected;
    private User user;

    public ItemUser(boolean selected, User user){
        this.selected = selected;
        this.user = user;
    }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
