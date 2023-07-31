package com.axing.points_ms.model.dto;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.model.dto
 * @className: Result
 * @author: Axing
 * @description: 返回结果类
 * @date: 2023/7/5 17:55
 * @version: 1.0
 */
public class Result {
    private boolean success; // 是否成功
    private String message; // 登录信息
    private String power; // 用户权限
    private String token; // 用户token
    private String id; // 用户id
    private String fullPath; // 完整文件路径
    private String path; // 文件路径
    private String fileName; // 文件名
    private String md5FileName; // md5运算后的文件名

    public String getMd5FileName() {
        return md5FileName;
    }

    public void setMd5FileName(String md5FileName) {
        this.md5FileName = md5FileName;
    }

    public Result setLoginRegisterResult(boolean success, String message, String permission, String token) {
        this.success = success;
        this.message = message;
        this.power = permission;
        this.token = token;
        return this;
    }

    public Result() {
    }

    public String toString() {
        return "LoginResult [success=" + success + ", message=" + message + ", permission=" + power + "]";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPower(String power) {
        this.power = power;
    }

}
