package org.example;

public class Visitor extends User {
    public Visitor(int id, String login, String password, boolean isAdmin) {
        super(id, login, password, false);
    }

    public Visitor(String login, String password) {
        super(login, password, false);
    }
}
