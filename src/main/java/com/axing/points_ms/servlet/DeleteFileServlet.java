package com.axing.points_ms.servlet;

import com.axing.points_ms.model.dto.Result;
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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.servlet
 * @className: DeleteFileServlet
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/18 8:52
 * @version: 1.0
 */
@WebServlet("/deleteFile")
public class DeleteFileServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(DeleteFileServlet.class);

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

//        获取前端传来的数据filePath
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String fileName = mapReceive.get("fileName");
        String user_id = mapReceive.get("user_id");

//        根据fileName删除文件
        String realPath = request.getServletContext().getRealPath("/");
        File fileToDelete = new File(realPath + user_id + "\\" + fileName);
        logger.info("realPath:" + realPath);
        logger.info("fileToDelete:" + fileToDelete);

        if (fileToDelete.delete()) {
            result.setSuccess(true);
            result.setMessage("文件删除成功。");
            logger.info("文件删除成功。");
        } else {
            result.setSuccess(false);
            result.setMessage("文件删除失败。");
            logger.info("文件删除失败。");
        }

//        返回是否成功
        mapReturn.put("result", result);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");

    }
}
