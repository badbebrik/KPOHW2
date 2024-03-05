package org.example;

public class Admin extends User {
    public Admin(int id, String login, String password, boolean isAdmin) {
        super(id, login, password, true);
    }

    public Admin(String login, String password, boolean isAdmin) {
        super(login, password, true);
    }
}
