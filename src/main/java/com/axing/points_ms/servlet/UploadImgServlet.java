package com.axing.points_ms.servlet;

import com.axing.points_ms.model.dto.Result;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.axing.points_ms.utils.Encipher.md5;
import static com.axing.points_ms.utils.FileTools.renameFile;


/**
 * @projectName: points_management_system
 * @package: com.example.points_management_system
 * @className: UploadImgServlet
 * @author: Axing
 * @description: 文件上传
 * @date: 2023/7/4 19:16
 * @version: 1.0
 */

@WebServlet("/uploadImg")
@MultipartConfig
public class UploadImgServlet extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(UploadImgServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("被调用");
        Map<String, Object> mapReturn = new HashMap<>();
        Gson gson = new Gson();
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        Result result = new Result();
        try {
//            存储文件至服务器
            Part part = request.getPart("myfile");
            String userId = request.getParameter("userId");
            String fileName = part.getSubmittedFileName();
            String realPath = "D:\\points_management_system\\points_management_system\\src\\main\\webapp\\download\\";
            String fullPath = renameFile(realPath + fileName, userId + "\\" + md5(fileName));
            part.write(fullPath);

//            返回上传结果
            result.setSuccess(true);
            result.setMessage("文件上传成功");
            String path = fullPath.substring(fullPath.indexOf(userId));
            fullPath = "http://cn-hk-bgp-9.openfrp.top:28224/download/" + path;
            result.setPath(path);
            result.setFullPath(fullPath);
            result.setFileName(fileName);
            mapReturn.put("result", result);
            response.getWriter().write(gson.toJson(mapReturn));
            logger.info("文件上传成功");
            logger.info("相对文件路径：" + path);
            logger.info("返回数据成功");
        } catch (ServletException e) {
            result.setSuccess(false);
            result.setMessage("文件上传失败（ServletException）");
            mapReturn.put("result", result);
            response.getWriter().write(gson.toJson(mapReturn));
            logger.info("文件上传失败(ServletException)");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            result.setSuccess(false);
            result.setMessage("文件上传失败（UnsupportedEncodingException）");
            mapReturn.put("result", result);
            response.getWriter().write(gson.toJson(mapReturn));
            logger.info("文件上传失败(UnsupportedEncodingException)");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage("文件上传失败（IOException）");
            mapReturn.put("result", result);
            response.getWriter().write(gson.toJson(mapReturn));
            logger.info("文件上传失败(IOException)");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

