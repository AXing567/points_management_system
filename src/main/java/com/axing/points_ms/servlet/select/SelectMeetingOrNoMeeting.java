package com.axing.points_ms.servlet.select;

import com.axing.points_ms.model.dto.Meeting;
import com.axing.points_ms.model.dto.NoMeeting;
import com.axing.points_ms.servlet.user_preview_review.ReviewPointChangesServlet;
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
 * @package: com.axing.points_ms.servlet.select
 * @className: SelectMeetingOrNoMeeting
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/22 18:10
 * @version: 1.0
 */
@WebServlet("/SelectMeetingOrNoMeeting")
public class SelectMeetingOrNoMeeting extends HttpServlet {
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
        Meeting meeting = new Meeting();
        NoMeeting noMeeting = new NoMeeting();

//        接收前端数据 id , select
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String id = mapReceive.get("id");
        int select = Integer.parseInt(mapReceive.get("select"));
        logger.info("获取前端数据成功");


//        查询数据库
        rs = operateDB.select_points(id, select == 1);
        if(select==1){
            try {
                if (rs.next()) {
                    meeting.setId(rs.getInt("id"));
                    meeting.setUserId(rs.getInt("user_id"));
                    meeting.setNickname(rs.getString("nickname"));
                    meeting.setTime(rs.getString("time"));
                    meeting.setSession(rs.getString("session"));
                    meeting.setOrdinal(rs.getString("ordinal"));
                    meeting.setAttend(rs.getInt("attend"));
                    meeting.setConsider(rs.getInt("consider"));
                    meeting.setSupplementConsider(rs.getString("supplement_consider"));
                    meeting.setRecommendation(rs.getInt("recommendation"));
                    meeting.setSupplementRecommendation(rs.getString("supplement_recommendation"));
                    meeting.setBill(rs.getInt("bill"));
                    meeting.setSupplementBill(rs.getString("supplement_bill"));
                    meeting.setQuestion(rs.getInt("question"));
                    meeting.setSupplementQuestion(rs.getString("supplement_question"));
                    meeting.setTotal(rs.getInt("total"));
                    meeting.setAddId(rs.getString("add_id"));
                    meeting.setCheck(rs.getInt("check"));
                    meeting.setPicture(rs.getString("picture"));
                    meeting.setRejectReason(rs.getString("reject_reason"));
                }
                mapReturn.put("meeting", meeting);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("查询数据库失败");
            }
        }else {
            try {
                while (rs.next()) {
                    noMeeting.setId(rs.getInt("id"));
                    noMeeting.setUserId(rs.getInt("user_id"));
                    noMeeting.setNickname(rs.getString("nickname"));
                    noMeeting.setTime(rs.getString("time"));
                    noMeeting.setTryScore(rs.getInt("try"));
                    noMeeting.setSupplementTry(rs.getString("supplement_try"));
                    noMeeting.setInspectScore(rs.getInt("inspect"));
                    noMeeting.setSupplementInspect(rs.getString("supplement_inspect"));
                    noMeeting.setActivityScore(rs.getInt("activity"));
                    noMeeting.setSupplementActivity(rs.getString("supplement_activity"));
                    noMeeting.setActivity2Score(rs.getInt("activity2"));
                    noMeeting.setSupplementActivity2(rs.getString("supplement_activity2"));
                    noMeeting.setGroupWorkScore(rs.getInt("group_work"));
                    noMeeting.setSupplementGroupWork(rs.getString("supplement_group_work"));
                    noMeeting.setSurveyScore(rs.getInt("survey"));
                    noMeeting.setSupplementSurvey(rs.getString("supplement_survey"));
                    noMeeting.setMaterialScore(rs.getInt("material"));
                    noMeeting.setSupplementMaterial(rs.getString("supplement_material"));
                    noMeeting.setDutyScore(rs.getInt("duty"));
                    noMeeting.setSupplementDuty(rs.getString("supplement_duty"));
                    noMeeting.setSolveScore(rs.getInt("solve"));
                    noMeeting.setSupplementSolve(rs.getString("supplement_solve"));
                    noMeeting.setCompleteScore(rs.getInt("complete"));
                    noMeeting.setSupplementComplete(rs.getString("supplement_complete"));
                    noMeeting.setTotalScore(rs.getInt("total"));
                    noMeeting.setAddId(rs.getString("add_id"));
                    noMeeting.setCheck(rs.getBoolean("check"));
                    noMeeting.setPicture(rs.getString("picture"));
                    noMeeting.setRejectReason(rs.getString("reject_reason"));
                }
                mapReturn.put("noMeeting", noMeeting);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("查询数据库失败");
            }
        }

//        返回数据
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");
        operateDB.closeDB();


    }
}
