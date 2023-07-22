package com.axing.points_ms.model.dto;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.model.dto
 * @className: NoMeetingPreview
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/21 19:52
 * @version: 1.0
 */
public class NoMeetingPreview {
    private String id;
    private int total;
    private int activity;
    private int try_;
    private int inspect;
    private int activity2;
    private int group_work;
    private int survey;
    private int material;
    private int duty;
    private int solve;
    private int complete;
    private String nickname;
    private String reject_reason;

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getTry_() {
        return try_;
    }

    public void setTry_(int try_) {
        this.try_ = try_;
    }

    public int getInspect() {
        return inspect;
    }

    public void setInspect(int inspect) {
        this.inspect = inspect;
    }

    public int getActivity2() {
        return activity2;
    }

    public void setActivity2(int activity2) {
        this.activity2 = activity2;
    }

    public int getGroup_work() {
        return group_work;
    }

    public void setGroup_work(int group_work) {
        this.group_work = group_work;
    }

    public int getSurvey() {
        return survey;
    }

    public void setSurvey(int survey) {
        this.survey = survey;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public int getDuty() {
        return duty;
    }

    public void setDuty(int duty) {
        this.duty = duty;
    }

    public int getSolve() {
        return solve;
    }

    public void setSolve(int solve) {
        this.solve = solve;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
