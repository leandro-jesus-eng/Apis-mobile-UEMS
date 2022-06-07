package com.apis.features;

import android.util.Log;
import com.apis.model.User;

public final class ManageUser {

    static User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User currentUser) {
        user = currentUser;
        Log.i("Teste", user.getEmail());
    }
}
