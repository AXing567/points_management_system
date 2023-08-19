package com.axing.points_ms.model.dto;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.model.dto
 * @className: PasswordReview
 * @author: Axing
 * @description: TODO
 * @date: 2023/8/19 15:35
 * @version: 1.0
 */
public class PasswordReview {
    private String id;
    private String username;
    private String nickname;
    private String password;
    private String check_find;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheck_find() {
        return check_find;
    }

    public void setCheck_find(String check_find) {
        this.check_find = check_find;
    }
}
