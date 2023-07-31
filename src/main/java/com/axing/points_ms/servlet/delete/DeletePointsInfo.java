package com.axing.points_ms.servlet.delete;

import com.axing.points_ms.model.dto.Result;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/DeletePointsInfo")
public class DeletePointsInfo extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(ReviewPointChangesServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String, Object> mapReturn = new HashMap<>();
        Map<String, String> mapReceive;
        Gson gson = new Gson();
        String receiveData;

//        接收前端数据 id , check
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String id = mapReceive.get("id");
        int select = Integer.parseInt(mapReceive.get("select"));
        logger.info("获取前端数据成功");

//      处理数据
        if (operateDB.delete_points_info(id, select == 1)) {
            result.setMessage("删除成功");
            result.setSuccess(true);
            logger.info("删除成功");
        } else {
            result.setMessage("删除失败");
            result.setSuccess(false);
            logger.error("删除失败");
        }
//        返回数据
        mapReturn.put("result", result);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");


    }
}
