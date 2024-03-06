package org.example.model;

import lombok.*;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String login;
    private String password;
    private UserRole role;

    public User(String username, String s, UserRole admin) {
        this.login = username;
        this.password = s;
        this.role = admin;
    }
}
