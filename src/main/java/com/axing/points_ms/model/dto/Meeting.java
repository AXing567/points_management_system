package com.axing.points_ms.model.dto;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.model.dto
 * @className: Meeting
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/22 18:31
 * @version: 1.0
 */
public class Meeting {
    private int id;
    private int userId;
    private String nickname;
    private String time;
    private String session;
    private String ordinal;
    private int attend;
    private int consider;
    private String supplementConsider;
    private int recommendation;
    private String supplementRecommendation;
    private int bill;
    private String supplementBill;
    private int question;
    private String supplementQuestion;
    private int total;
    private String addId;
    private int check;
    private String picture;
    private String rejectReason;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }

    public int getAttend() {
        return attend;
    }

    public void setAttend(int attend) {
        this.attend = attend;
    }

    public int getConsider() {
        return consider;
    }

    public void setConsider(int consider) {
        this.consider = consider;
    }

    public String getSupplementConsider() {
        return supplementConsider;
    }

    public void setSupplementConsider(String supplementConsider) {
        this.supplementConsider = supplementConsider;
    }

    public int getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(int recommendation) {
        this.recommendation = recommendation;
    }

    public String getSupplementRecommendation() {
        return supplementRecommendation;
    }

    public void setSupplementRecommendation(String supplementRecommendation) {
        this.supplementRecommendation = supplementRecommendation;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public String getSupplementBill() {
        return supplementBill;
    }

    public void setSupplementBill(String supplementBill) {
        this.supplementBill = supplementBill;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public String getSupplementQuestion() {
        return supplementQuestion;
    }

    public void setSupplementQuestion(String supplementQuestion) {
        this.supplementQuestion = supplementQuestion;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getAddId() {
        return addId;
    }

    public void setAddId(String addId) {
        this.addId = addId;
    }

    public int isCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }
}
