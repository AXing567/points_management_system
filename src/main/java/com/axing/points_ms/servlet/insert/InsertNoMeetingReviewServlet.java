package com.axing.points_ms.servlet.insert;

import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.utils.ObtainData;
import com.axing.points_ms.utils.OperateDB;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet.insert
 * @className: InsertNoMeetingReviewServlet
 * @author: Axing
 * @description: 插入非大会相关的积分变动信息
 * @date: 2023/7/10 20:42
 * @version: 1.0
 */
@WebServlet("/insertNoMeetingReview")
public class InsertNoMeetingReviewServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(InsertNoMeetingReviewServlet.class);
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String , Object> mapReturn = new HashMap<String ,Object>();
        Gson gson = new Gson();

//        获取前端数据
        String receiveData = ObtainData.obtain_data(request);

//        存储数据至数据库
        boolean check = operateDB.insert_no_meeting(receiveData);

//        返回结果至前端
        if(check){
            result.setSuccess(true);
            result.setMessage("数据上传成功");
        }else {
            result.setSuccess(false);
            result.setMessage("数据上传失败");
        }
        mapReturn.put("result", result);
        String jsonReturn = gson.toJson(mapReturn);
        response.getWriter().write(jsonReturn);
        logger.info("返回数据成功");

    }
}
