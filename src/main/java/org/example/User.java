package org.example;

import lombok.Getter;
import lombok.Setter;


@Getter
public class User {
    private int id;
    private String login;
    private String password;
    private boolean isAdmin;

    public User(int id, String login, String password, boolean isAdmin) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(String login, String password, boolean isAdmin) {
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}
