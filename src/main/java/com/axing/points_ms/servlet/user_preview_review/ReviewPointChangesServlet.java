package com.axing.points_ms.servlet.user_preview_review;

import com.axing.points_ms.model.dto.MeetingPreview;
import com.axing.points_ms.model.dto.NoMeetingPreview;
import com.axing.points_ms.utils.ObtainData;
import com.axing.points_ms.utils.OperateDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.user_preview_review
 * @className: ReviewPointChangesServlet
 * @author: Axing
 * @description: 预览多条积分变动信息（可以根据user_id，select,check查看大会期间及闭会期间的积分变动信息）(select:1大会期间，0闭会期间)
 * @date: 2023/7/20 15:23
 * @version: 1.0
 */
@WebServlet("/ReviewPointChangesServlet")
public class ReviewPointChangesServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(ReviewPointChangesServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Map<String, Object> mapReturn = new HashMap<>();
        Map<String, String> mapReceive;
        Gson gson = new Gson();
        String receiveData;
        ResultSet rs;

//        接收前端数据 user_id , check
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String user_id = mapReceive.get("user_id");
        int check = Integer.parseInt(mapReceive.get("check"));
        int select = Integer.parseInt(mapReceive.get("select"));
        logger.info("获取前端数据成功");


//        查找指定数据集
//        (select:1大会期间，0闭会期间)
        if (select == 1) {
            rs = operateDB.select_meeting_points(user_id, check);
            try {
                while (rs.next()) {
                    MeetingPreview meetingPreview = new MeetingPreview();
                    String id = rs.getString("id");
                    meetingPreview.setId(id);
                    meetingPreview.setSession(rs.getString("session"));
                    meetingPreview.setOrdinal(rs.getString("ordinal"));
                    meetingPreview.setAttend(rs.getInt("attend"));

                    meetingPreview.setConsider(rs.getInt("consider"));
                    meetingPreview.setSupplement_consider(rs.getString("supplement_consider"));

                    meetingPreview.setRecommendation(rs.getInt("recommendation"));
                    meetingPreview.setSupplement_recommendation(rs.getString("supplement_recommendation"));

                    meetingPreview.setBill(rs.getInt("bill"));
                    meetingPreview.setSupplement_bill(rs.getString("supplement_bill"));

                    meetingPreview.setQuestion(rs.getInt("question"));
                    meetingPreview.setSupplement_question(rs.getString("supplement_question"));

                    meetingPreview.setTotal(rs.getString("total"));
                    meetingPreview.setNickname(rs.getString("nickname"));
                    meetingPreview.setReject_reason(rs.getString("reject_reason"));
                    meetingPreview.setPicture(rs.getString("picture"));

                    mapReturn.put("meeting" + id, meetingPreview);
                }
                logger.info("查找指定数据集成功");
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("查找指定数据集失败");
                throw new RuntimeException(e);
            }
        } else if (select == 0) {
            rs = operateDB.select_no_meeting_points(user_id, check);
            try {
                while (rs.next()) {
                    NoMeetingPreview noMeetingPreview = new NoMeetingPreview();
                    String id = rs.getString("id");
                    noMeetingPreview.setId(id);
                    noMeetingPreview.setTry_(rs.getInt("try"));
                    noMeetingPreview.setSupplement_try(rs.getString("supplement_try"));
                    noMeetingPreview.setInspect(rs.getInt("inspect"));
                    noMeetingPreview.setSupplement_inspect(rs.getString("supplement_inspect"));
                    noMeetingPreview.setActivity(rs.getInt("activity"));
                    noMeetingPreview.setSupplement_activity(rs.getString("supplement_activity"));
                    noMeetingPreview.setActivity2(rs.getInt("activity2"));
                    noMeetingPreview.setSupplement_activity2(rs.getString("supplement_activity2"));
                    noMeetingPreview.setGroup_work(rs.getInt("group_work"));
                    noMeetingPreview.setSupplement_group_work(rs.getString("supplement_group_work"));
                    noMeetingPreview.setSurvey(rs.getInt("survey"));
                    noMeetingPreview.setSupplement_survey(rs.getString("supplement_survey"));
                    noMeetingPreview.setMaterial(rs.getInt("material"));
                    noMeetingPreview.setSupplement_material(rs.getString("supplement_material"));
                    noMeetingPreview.setDuty(rs.getInt("duty"));
                    noMeetingPreview.setSupplement_duty(rs.getString("supplement_duty"));
                    noMeetingPreview.setSolve(rs.getInt("solve"));
                    noMeetingPreview.setSupplement_solve(rs.getString("supplement_solve"));
                    noMeetingPreview.setComplete(rs.getInt("complete"));
                    noMeetingPreview.setSupplement_complete(rs.getString("supplement_complete"));

                    noMeetingPreview.setNickname(rs.getString("nickname"));
                    noMeetingPreview.setTotal(rs.getInt("total"));
                    noMeetingPreview.setReject_reason(rs.getString("reject_reason"));
                    noMeetingPreview.setTime(rs.getString("time"));
                    noMeetingPreview.setPicture(rs.getString("picture"));
                    mapReturn.put("no_meeting" + id, noMeetingPreview);

                }
                logger.info("查找指定数据集成功");
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("查找指定数据集失败");
                throw new RuntimeException(e);
            }
        }

//        返回数据
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
        operateDB.closeDB();
    }
}
