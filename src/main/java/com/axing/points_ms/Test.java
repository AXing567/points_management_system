package com.axing.points_ms;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.sql.*;

public class Test {
    public static void main(String[] args) {
        int userId = 19; // 用户ID

        try {
            // 连接数据库
            Connection conn;
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/points_management_system";
            conn = DriverManager.getConnection(url, "root", "123");
            // 查询用户信息
            String sql = "SELECT * FROM user_info WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

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

                System.out.println("用户文档生成成功！");
            } else {
                System.out.println("找不到对应的用户信息！");
            }

            // 关闭资源
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
