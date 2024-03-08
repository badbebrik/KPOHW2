package org.example;

import org.example.model.User;

public interface MenuI {
    void showMenu();
    void run();
    void setCurrentUser(User currentUser);
}
