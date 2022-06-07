package com.apis.features.usecases;

import com.apis.model.User;

public final class ManageUser {

    static User user = null;

    public User getUser() { return user; }

    public void setUser(User currentUser) { user = currentUser; }
}
