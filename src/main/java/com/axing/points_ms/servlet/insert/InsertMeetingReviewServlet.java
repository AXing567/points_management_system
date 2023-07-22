package com.axing.points_ms.servlet.insert;

import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.utils.ObtainData;
import com.axing.points_ms.utils.OperateDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.insert
 * @className: InsertMeetingReview
 * @author: Axing
 * @description: 插入大会相关的积分变动信息
 * @date: 2023/7/10 16:04
 * @version: 1.0
 */
@WebServlet("/insertMeetingReview")

public class InsertMeetingReviewServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(InsertMeetingReviewServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        logger.info("已启动");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Map<String, String> mapReceive;
        Map<String, Object> mapReturn = new HashMap<>();
        Gson gson = new Gson();
        Result result = new Result();
        ObtainData obtainData = new ObtainData();
        String receiveData;


        try {
            logger.info("获取前端传来的数据");
//            获取前端传来的数据
            receiveData = ObtainData.obtain_data(request);
            mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
            }.getType());
            String ordinal = mapReceive.get("ordinal");
            String session = mapReceive.get("session");
            int attend = Integer.parseInt(mapReceive.get("attend"));
            int consider = Integer.parseInt(mapReceive.get("consider"));
            String supplement_consider = mapReceive.get("supplement_consider");
            int recommendation = Integer.parseInt(mapReceive.get("recommendation"));
            String supplement_recommendation = mapReceive.get("supplement_recommendation");
            int bill = Integer.parseInt(mapReceive.get("bill"));
            String supplement_bill = mapReceive.get("supplement_bill");
            int question = Integer.parseInt(mapReceive.get("question"));
            String supplement_question = mapReceive.get("supplement_question");
            String nickname = mapReceive.get("nickname");
            String add_id = mapReceive.get("add_id");
            String picture = mapReceive.get("picture");
            String user_id = mapReceive.get("user_id");
            int total = attend + consider + recommendation + bill + question;


//        存储到数据库
            logger.info("存储到数据库");
            boolean check = operateDB.insert_meeting(session, ordinal, attend, consider, supplement_consider,
                    recommendation, supplement_recommendation, bill, supplement_bill, question, supplement_question,
                    nickname, add_id, picture, user_id, total);
            if (check) {
                logger.info("插入成功");
                result.setMessage("插入成功");
                result.setSuccess(true);
            } else {
                logger.info("插入失败");
                result.setMessage("插入失败");
                result.setSuccess(false);
            }

//        返回结果
            mapReturn.put("result", result);
            String jsonReturn = gson.toJson(mapReturn);
            response.getWriter().write(jsonReturn);
            logger.info("用户状态信息已返回（插入大会相关信息）");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
