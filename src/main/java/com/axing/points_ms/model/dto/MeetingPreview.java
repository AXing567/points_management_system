package com.axing.points_ms.model.dto;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.model.dto
 * @className: MeetingPreview
 * @author: Axing
 * @description: 大会期间积分变动信息（预览）
 * @date: 2023/7/20 9:38
 * @version: 1.0
 */
public class MeetingPreview {
    private String id;
    private String total;
    private String session;
    private String ordinal;
    private String activity;
    private String nickname;
    private String reject_reason;
    private int attend;
    private int consider;
    private String supplement_consider;
    private int recommendation;
    private String supplement_recommendation;
    private int bill;
    private String supplement_bill;
    private int question;
    private String supplement_question;

    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSupplement_consider() {
        return supplement_consider;
    }

    public void setSupplement_consider(String supplement_consider) {
        this.supplement_consider = supplement_consider;
    }

    public String getSupplement_recommendation() {
        return supplement_recommendation;
    }

    public void setSupplement_recommendation(String supplement_recommendation) {
        this.supplement_recommendation = supplement_recommendation;
    }

    public String getSupplement_bill() {
        return supplement_bill;
    }

    public void setSupplement_bill(String supplement_bill) {
        this.supplement_bill = supplement_bill;
    }

    public String getSupplement_question() {
        return supplement_question;
    }

    public void setSupplement_question(String supplement_question) {
        this.supplement_question = supplement_question;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public int getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(int recommendation) {
        this.recommendation = recommendation;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
