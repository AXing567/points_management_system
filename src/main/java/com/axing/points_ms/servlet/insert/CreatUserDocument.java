package com.axing.points_ms.servlet.insert;

import com.axing.points_ms.model.dto.Result;
import com.axing.points_ms.servlet.user_preview_review.ReviewPointChangesServlet;
import com.axing.points_ms.utils.ObtainData;
import com.axing.points_ms.utils.OperateDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/CreatUserDocument")
public class CreatUserDocument extends HttpServlet {
    Logger logger = LoggerFactory.getLogger(ReviewPointChangesServlet.class);

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("被调用");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf8");
        OperateDB operateDB = new OperateDB();
        operateDB.connect2();
        Result result = new Result();
        Map<String, Object> mapReturn = new HashMap<String, Object>();
        Map<String, String> mapReceive = new HashMap<String, String>();
        Gson gson = new Gson();
        String receiveData = null;
        ResultSet rs = null;

//        接收前端数据 id , check
        receiveData = ObtainData.obtain_data(request);
        mapReceive = gson.fromJson(receiveData, new TypeToken<Map<String, String>>() {
        }.getType());
        String userId = mapReceive.get("userId");
        logger.info("获取前端数据成功");

//        根据id查询用户信息
        rs = operateDB.select_user_info(userId);
        try {
            if (rs.next()) {
                // 创建 Word 文档对象
                XWPFDocument document = new XWPFDocument();

                // 创建段落
                XWPFParagraph paragraph = document.createParagraph();

                // 创建文本运行
                XWPFRun run = paragraph.createRun();

                // 设置文本内容
                run.setText("姓名: " + rs.getString("nick_name"));
                run.addBreak();
                run.setText("性别: " + rs.getString("gender"));
                run.addBreak();
                run.setText("民族: " + rs.getString("ethnicity"));
                run.addBreak();
                run.setText("出生年月: " + rs.getDate("birthday"));
                run.addBreak();
                run.setText("籍贯: " + rs.getString("native_place"));
                run.addBreak();
                run.setText("党派: " + rs.getString("party"));
                run.addBreak();
                run.setText("参加工作年月: " + rs.getDate("work_start_date"));
                run.addBreak();
                run.setText("健康状况: " + rs.getString("health_status"));
                run.addBreak();
                run.setText("学历: " + rs.getString("education"));
                run.addBreak();
                run.setText("毕业院校及专业: " + rs.getString("graduate_school_major"));
                run.addBreak();
                run.setText("学位或技术职称: " + rs.getString("degree_technical_title"));
                run.addBreak();
                run.setText("专长: " + rs.getString("expertise"));
                run.addBreak();
                run.setText("身份证号: " + rs.getString("id_card_number"));
                run.addBreak();
                run.setText("工作单位: " + rs.getString("employer"));
                run.addBreak();
                run.setText("现任职务: " + rs.getString("current_position"));
                run.addBreak();
                run.setText("联系电话: " + rs.getString("phone_number"));
                run.addBreak();
                run.setText("邮编: " + rs.getString("postal_code"));
                run.addBreak();
                run.setText("简历: " + rs.getString("resume"));
                run.addBreak();
                run.setText("市: " + rs.getString("city"));
                run.addBreak();
                run.setText("区: " + rs.getString("district"));

                // 保存为 Word 文档
                FileOutputStream out = new FileOutputStream(
                        "D:\\points_management_system\\points_management_system\\src\\main\\webapp\\download\\"
                                + userId + "\\user_document.docx");
                document.write(out);
                out.close();
                result.setMessage("用户文档生成成功");
                result.setSuccess(true);
                result.setPath(userId + "/user_document.docx");
                result.setFullPath("https://w204882q59.zicp.fun/download/" + userId + "/user_document.docx");
                logger.info("用户文档生成成功");
                logger.info("fullPath" + "https://w204882q59.zicp.fun/download/" + userId + "/user_document.docx");
            } else {
                result.setMessage("找不到对应的用户信息");
                result.setSuccess(false);
                logger.error("找不到对应的用户信息！");
            }
        } catch (SQLException e) {
            result.setMessage("打印用户信息异常");
            result.setSuccess(false);
            logger.error("打印用户信息异常！");
        }


//        返回数据
        mapReturn.put("result", result);
        response.getWriter().write(gson.toJson(mapReturn));
        logger.info("返回数据成功");


    }
}
