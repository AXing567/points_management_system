package com.axing.points_ms.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms.utils
 * @className: OperateDB
 * @author: Axing
 * @description: 对数据库进行操作
 * @date: 2023/7/5 18:00
 * @version: 1.0
 */
public class OperateDB {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;
    /*日志记录器*/
    static Logger logger = LoggerFactory.getLogger(OperateDB.class);

    /**
     * @author Axing
     * @description 普通方法链接数据库
     * @date 2023/7/5 18:11
     */
    public void connect() {
        try {
            logger.info("数据库准备连接");
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/points_management_system";
            conn = DriverManager.getConnection(url, "root", "123");
            logger.info("数据库连接成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Axing
     * @description 使用Druid连接池连接数据库
     * @date 2023/7/10 15:16
     */
    public void connect2() {
        //创建Properties对象，用于加载配置文件
        Properties pro = new Properties();
        try {
            //加载配置文件
            pro.load(Files.newInputStream(Paths.get("D:/points_management_system/points_management_system/src/main/resources/druid.properties")));
            //获取数据库连接池对象
            DataSource ds = DruidDataSourceFactory.createDataSource(pro);
            //获取数据库连接对象
            conn = ds.getConnection();
            logger.info("数据库连接成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("数据库连接失败");
        }


    }

    /* *****************************************************************************************************************
     * 增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增  *
     * 增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增  *
     **************************************************************************************************************** */


    /**
     * @param nickname:
     * @param username:
     * @param password:
     * @return boolean
     * @author Axing
     * @description 管理员注册用户（将用户信息插入到三个表中）
     * @date 2023/7/16 19:25
     */
    public boolean insert_user_all(String nickname, String username, String password) {
//        将用户信息插入到user_review表中
        boolean check;
        check = insert_user_review(nickname, username, password);
//        查看此用户的user_id
        String user_id = select_user_review_id(username);
//        在user_review表中通过此用户
        check &= update_user_review(user_id, 1);
//        在user表中插入此用户
        check &= insert_user(user_id);
//        在user_info表中插入此用户
        check &= insert_user_info(user_id);
        if (check) {
            logger.info("用户注册成功");
            return true;
        } else {
            logger.error("用户注册失败");
            return false;
        }
    }

    /**
     * @param user_id:
     * @return boolean
     * @author Axing
     * @description 将指定user_id的用户插入到user表中
     * @date 2023/7/16 9:34
     */
    public boolean insert_user(String user_id) {
        try {
//            查找指定user_id的用户并取得其username、nickname、password、salt
            pstmt = conn.prepareStatement("select * from user_review where user_id = ?");
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
            String username;
            String nickname;
            String password;
            String salt;
            if (rs.next()) {
                username = rs.getString("username");
                nickname = rs.getString("nickname");
                password = rs.getString("password");
                salt = rs.getString("salt");
            } else {
                logger.error("指定用户id不存在用户审核表（user_review)中");
                return false;
            }
//            将指定用户的username、nickname、password、salt插入到user表中
            pstmt = conn.prepareStatement("insert into user(username,nickname,password,salt,user_id) values(?,?,?,?,?)");
            pstmt.setString(1, username);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            pstmt.setString(4, salt);
            pstmt.setString(5, user_id);
            if (pstmt.executeUpdate() == 1) {
                logger.info("指定user_id的用户插入到user表中成功");
                return true;
            } else {
                logger.error("指定user_id的用户插入到user表中失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("boolean insert_user(String user_id)函数出现异常");
            return false;
        }
    }

    /**
     * @param user_id:
     * @return boolean
     * @author Axing
     * @description 插入user_id和nick_name到user_info表中
     * @date 2023/7/16 9:46
     */
    public boolean insert_user_info(String user_id) {
        try {
//            查找指定user_id的用户并取得其nickname
            pstmt = conn.prepareStatement("select nickname from user_review where user_id = ?");
            pstmt.setString(1, user_id);
            rs = pstmt.executeQuery();
            String nickname;
            if (rs.next()) {
                nickname = rs.getString("nickname");
            } else {
                logger.error("指定用户id不存在用户审核表（user_review)中");
                return false;
            }
//            将指定用户的username、nickname、password、salt插入到user表中
            pstmt = conn.prepareStatement("insert into user_info(user_id,nick_name) values(?,?)");
            pstmt.setString(1, user_id);
            pstmt.setString(2, nickname);
            if (pstmt.executeUpdate() == 1) {
                logger.info("指定user_id的用户插入到user_info表中成功");
                return true;
            } else {
                logger.error("指定user_id的用户插入到user_info表中失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("boolean insert_user_info(String user_id)函数出现异常");
            return false;
        }
    }


    /**
     * @param session:
     * @param ordinal:
     * @param attend:
     * @param consider:
     * @param supplement_consider:
     * @param recommendation:
     * @param supplement_recommendation:
     * @param bill:
     * @param supplement_bill:
     * @param question:
     * @param supplement_question:
     * @param nickname:
     * @param add_id:
     * @param picture:
     * @param user_id:
     * @param total:
     * @return boolean 插入成功返回true，否则返回false
     * @author Axing
     * @description 插中入出席大会数据：第几次大会，是否出席大会，审议报告决议并积极发言，提出建议意见，提出议案，提出质询及以上内容的补充说明至待审核的表
     * @date 2023/7/20 10:42
     */
    public boolean insert_meeting(String session, String ordinal, int attend, int consider, String supplement_consider,
                                  int recommendation, String supplement_recommendation, int bill,
                                  String supplement_bill, int question, String supplement_question,
                                  String nickname, String add_id, String picture, String user_id, int total) {
        try {
            pstmt = conn.prepareStatement("insert into meeting" +
                    "(session,ordinal,attend,consider,supplement_consider,recommendation,supplement_recommendation,bill,supplement_bill," +
                    "question,supplement_question,nickname,add_id,picture,user_id, total,time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, session);
            pstmt.setString(2, ordinal);
            pstmt.setInt(3, attend);
            pstmt.setInt(4, consider);
            pstmt.setString(5, supplement_consider);
            pstmt.setInt(6, recommendation);
            pstmt.setString(7, supplement_recommendation);
            pstmt.setInt(8, bill);
            pstmt.setString(9, supplement_bill);
            pstmt.setInt(10, question);
            pstmt.setString(11, supplement_question);
            pstmt.setString(12, nickname);
            pstmt.setString(13, add_id);
            pstmt.setString(14, picture);
            pstmt.setString(15, user_id);
            pstmt.setInt(16, total);
            pstmt.setString(17, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));


            if (pstmt.executeUpdate() == 1) {
                logger.info("新建数据成功");
                return true;
            } else {
                logger.error("新建数据失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("新建数据失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param receiveGson: 接收到的json数据
     * @return boolean 插入非大会相关积分变动数据是否成功
     * @author Axing
     * @description 插入非大会相关积分变动数据(仅在此函数中使用了getOrDefault方法以确保可以有部分变量不向后端传递 ）
     * @date 2023/7/13 9:52
     */
    public boolean insert_no_meeting(String receiveGson) {
        Map<String, String> map = new Gson().fromJson(receiveGson, new TypeToken<Map<String, String>>() {
        }.getType());
        String user_id = map.get("user_id");
        String add_id = map.get("add_id");
        String nickname = map.get("nickname");
        String time = map.get("time");
        String picture = map.get("picture");
        int try1 = Integer.parseInt(map.getOrDefault("try1", "0"));
        String supplement_try = map.getOrDefault("supplement_try", "");
        int inspect = Integer.parseInt(map.getOrDefault("inspect", "0"));
        String supplement_inspect = map.getOrDefault("supplement_inspect", "");
        int activity = Integer.parseInt(map.getOrDefault("activity", "0"));
        String supplement_activity = map.getOrDefault("supplement_activity", "");
        int activity2 = Integer.parseInt(map.getOrDefault("activity2", "0"));
        String supplement_activity2 = map.getOrDefault("supplement_activity2", "");
        int group_work = Integer.parseInt(map.getOrDefault("group_work", "0"));
        String supplement_group_work = map.getOrDefault("supplement_group_work", "");
        int survey = Integer.parseInt(map.getOrDefault("survey", "0"));
        String supplement_survey = map.getOrDefault("supplement_survey", "");
        int material = Integer.parseInt(map.getOrDefault("material", "0"));
        String supplement_material = map.getOrDefault("supplement_material", "");
        int duty = Integer.parseInt(map.getOrDefault("duty", "0"));
        String supplement_duty = map.getOrDefault("supplement_duty", "");
        int solve = Integer.parseInt(map.getOrDefault("solve", "0"));
        String supplement_solve = map.getOrDefault("supplement_solve", "");
        int complete = Integer.parseInt(map.getOrDefault("complete", "0"));
        String supplement_complete = map.getOrDefault("supplement_complete", "");
        int total = try1 + inspect + activity + activity2 + group_work + survey + material + duty + solve + complete;
        try {
            pstmt = conn.prepareStatement("insert into no_meeting" +
                    "(user_id,nickname,time,try,supplement_try,inspect,supplement_inspect,activity,supplement_activity,activity2,supplement_activity2," +
                    "group_work,supplement_group_work,survey,supplement_survey,material,supplement_material,duty,supplement_duty,solve,supplement_solve," +
                    "complete,supplement_complete,total,add_id,picture) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, user_id);
            pstmt.setString(2, nickname);
            pstmt.setString(3, time);
            pstmt.setInt(4, try1);
            pstmt.setString(5, supplement_try);
            pstmt.setInt(6, inspect);
            pstmt.setString(7, supplement_inspect);
            pstmt.setInt(8, activity);
            pstmt.setString(9, supplement_activity);
            pstmt.setInt(10, activity2);
            pstmt.setString(11, supplement_activity2);
            pstmt.setInt(12, group_work);
            pstmt.setString(13, supplement_group_work);
            pstmt.setInt(14, survey);
            pstmt.setString(15, supplement_survey);
            pstmt.setInt(16, material);
            pstmt.setString(17, supplement_material);
            pstmt.setInt(18, duty);
            pstmt.setString(19, supplement_duty);
            pstmt.setInt(20, solve);
            pstmt.setString(21, supplement_solve);
            pstmt.setInt(22, complete);
            pstmt.setString(23, supplement_complete);
            pstmt.setInt(24, total);
            pstmt.setString(25, add_id);
            pstmt.setString(26, picture);
            if (pstmt.executeUpdate() == 1) {
                logger.info("非大会相关积分变动数据插入成功");
                return true;
            } else {
                logger.error("非大会相关积分变动数据插入失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("非大会相关积分变动数据插入失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param username:
     * @param nickname:
     * @param password:
     * @return boolean
     * @author Axing
     * @description 插入找回密码数据
     * @date 2023/7/13 13:29
     */
    public boolean insert_find_password(String username, String nickname, String password ) {
        try {
            pstmt = conn.prepareStatement
                    ("insert into find_password_review(username,nickname,password) values(?,?,?)");
            pstmt.setString(1, username);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            if (pstmt.executeUpdate() == 1) {
                logger.info("用户"+nickname+"已提交新密码审核");
                return true;
            } else {
                logger.error("用户"+nickname+"提交新密码审核失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("用户"+nickname+"提交新密码审核失败（SQLException）");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param nickname: 姓名
     * @param username: 账号（手机号）
     * @param password: 密码
     * @return boolean
     * @author Axing
     * @description 新增用户。在用户审核表中插入数据
     * @date 2023/7/9 15:36
     */
    public boolean insert_user_review(String nickname, String username, String password) {
        String salt = Encipher.getStringRandom(6);
        password = Encipher.md5(Encipher.md5(password) + salt);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        System.out.println("Current time: " + formattedDateTime);
        try {
            pstmt = conn.prepareStatement("insert into user_review(nickname,username,password,salt,register_time) values(?,?,?,?,?)");
            pstmt.setString(1, nickname);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, salt);
            pstmt.setString(5, formattedDateTime);
            if (pstmt.executeUpdate() == 1) {
                logger.info("账号注册已提交审核");
                return true;
            } else {
                logger.error("账号注册提交审核失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("账号注册提交审核失败");
            throw new RuntimeException(e);
        }
    }

    /* ******************************************************************************************************************
     * 删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删  *
     * 删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删删  *
     **************************************************************************************************************** */

    /**
     * @param id:
     * @param select:
     * @return boolean
     * @author Axing
     * @description 根据积分表的id删除积分变动数据（select为true时删除大会相关积分变动数据，为false时删除非大会相关积分变动数据）
     * @date 2023/7/30 12:59
     */
    public boolean delete_points_info(String id, boolean select) {
        try {
            if (select) {
                pstmt = conn.prepareStatement("delete from meeting where id=?");
            } else {
                pstmt = conn.prepareStatement("delete from no_meeting where id=?");
            }
            pstmt.setString(1, id);
            if (pstmt.executeUpdate() == 1) {
                logger.info("积分变动数据删除成功");
                return true;
            } else {
                logger.error("积分变动数据删除失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("积分变动数据删除失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param userId:
     * @return boolean
     * @author Axing
     * @description 根据用户id删除用户
     * @date 2023/7/31 21:29
     */
    public boolean delete_user(String userId) {
        try {
            pstmt = conn.prepareStatement("delete from user where user_id=?");
            pstmt.setString(1, userId);
            if (pstmt.executeUpdate() == 1) {
                logger.info("用户删除成功");
                return true;
            } else {
                logger.error("用户删除失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("用户删除失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param userId:
     * @return boolean
     * @author Axing
     * @description 根据用户id删除用户信息
     * @date 2023/7/31 21:30
     */
    public boolean delete_user_info(String userId) {
        try {
            pstmt = conn.prepareStatement("delete from user_info where user_id=?");
            pstmt.setString(1, userId);
            if (pstmt.executeUpdate() == 1) {
                logger.info("用户信息删除成功");
                return true;
            } else {
                logger.error("用户信息删除失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("用户信息删除失败");
            throw new RuntimeException(e);
        }
    }
    /* ******************************************************************************************************************
     * 改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改  *
     * 改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改  *
     **************************************************************************************************************** */

    /**
     * @param username:
     * @param check:
     * @return boolean
     * @author Axing
     * @description 修改指定username的所有记录的check_find字段为check
     * @date 2023/8/19 15:54
     */
    public boolean update_find_password_check(String username , String check){
        try {
            pstmt = conn.prepareStatement("update find_password_review set check_find=? where username= ?");
            pstmt.setString(1, check);
            pstmt.setString(2, username);
            if (pstmt.executeUpdate() >0) {
                logger.info("找回密码验证码更新成功");
                return true;
            } else {
                logger.error("找回密码验证码更新失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("找回密码验证码更新失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param username:
     * @param password:
     * @return boolean
     * @author Axing
     * @description 在user表中修改指定username的密码为password（加密后的密码）
     * @date 2023/8/19 15:55
     */
    public boolean update_user_password(String username , String password){
        String salt = Encipher.getStringRandom(6);
        password = Encipher.md5(Encipher.md5(password) + salt);
        try {
            pstmt = conn.prepareStatement("update user set password=? , salt = ? where username=?");
            pstmt.setString(1, password);
            pstmt.setString(2, salt);
            pstmt.setString(3, username);
            if (pstmt.executeUpdate() == 1) {
                logger.info("用户密码更新成功");
                return true;
            } else {
                logger.error("用户密码更新失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("用户密码更新失败");
            throw new RuntimeException(e);
        }
    }
    public boolean update_no_meeting(String receiveGson) {
        Map<String, String> mapReceive = new Gson().fromJson(receiveGson, new TypeToken<Map<String, String>>() {
        }.getType());
        int id = Integer.parseInt(mapReceive.get("id"));
        int user_id = Integer.parseInt(mapReceive.get("user_id"));
        String nickname = mapReceive.get("nickname");
        String time = mapReceive.get("time");
        int tryScore = Integer.parseInt(mapReceive.get("try"));
        String supplementTry = mapReceive.get("supplement_try");
        int inspectScore = Integer.parseInt(mapReceive.get("inspect"));
        String supplementInspect = mapReceive.get("supplement_inspect");
        int activityScore = Integer.parseInt(mapReceive.get("activity"));
        String supplementActivity = mapReceive.get("supplement_activity");
        int activity2Score = Integer.parseInt(mapReceive.get("activity2"));
        String supplementActivity2 = mapReceive.get("supplement_activity2");
        int groupWorkScore = Integer.parseInt(mapReceive.get("group_work"));
        String supplementGroupWork = mapReceive.get("supplement_group_work");
        int surveyScore = Integer.parseInt(mapReceive.get("survey"));
        String supplementSurvey = mapReceive.get("supplement_survey");
        int materialScore = Integer.parseInt(mapReceive.get("material"));
        String supplementMaterial = mapReceive.get("supplement_material");
        int dutyScore = Integer.parseInt(mapReceive.get("duty"));
        String supplementDuty = mapReceive.get("supplement_duty");
        int solveScore = Integer.parseInt(mapReceive.get("solve"));
        String supplementSolve = mapReceive.get("supplement_solve");
        int completeScore = Integer.parseInt(mapReceive.get("complete"));
        String supplementComplete = mapReceive.get("supplement_complete");
        int totalScore = tryScore + inspectScore + activityScore + activity2Score + groupWorkScore + surveyScore +
                materialScore + dutyScore + solveScore + completeScore;
        String addId = mapReceive.get("add_id");
        int check = Integer.parseInt(mapReceive.get("check"));
        String picture = mapReceive.get("picture");
        String rejectReason = mapReceive.get("reject_reason");
        try {
            String updateQuery = "UPDATE no_meeting SET user_id = ?, nickname = ?, time = ?, try = ?, " +
                    "supplement_try = ?, inspect = ?, supplement_inspect = ?, activity = ?, supplement_activity = ?, " +
                    "activity2 = ?, supplement_activity2 = ?, group_work = ?, supplement_group_work = ?, survey = ?, " +
                    "supplement_survey = ?, material = ?, supplement_material = ?, duty = ?, supplement_duty = ?, " +
                    "solve = ?, supplement_solve = ?, complete = ?, supplement_complete = ?, total = ?, add_id = ?, " +
                    "`check` = ?, picture = ?, reject_reason = ? WHERE id = ?";
            pstmt = conn.prepareStatement(updateQuery);

            pstmt.setInt(1, user_id);
            pstmt.setString(2, nickname);
            pstmt.setString(3, time);
            pstmt.setInt(4, tryScore);
            pstmt.setString(5, supplementTry);
            pstmt.setInt(6, inspectScore);
            pstmt.setString(7, supplementInspect);
            pstmt.setInt(8, activityScore);
            pstmt.setString(9, supplementActivity);
            pstmt.setInt(10, activity2Score);
            pstmt.setString(11, supplementActivity2);
            pstmt.setInt(12, groupWorkScore);
            pstmt.setString(13, supplementGroupWork);
            pstmt.setInt(14, surveyScore);
            pstmt.setString(15, supplementSurvey);
            pstmt.setInt(16, materialScore);
            pstmt.setString(17, supplementMaterial);
            pstmt.setInt(18, dutyScore);
            pstmt.setString(19, supplementDuty);
            pstmt.setInt(20, solveScore);
            pstmt.setString(21, supplementSolve);
            pstmt.setInt(22, completeScore);
            pstmt.setString(23, supplementComplete);
            pstmt.setInt(24, totalScore);
            pstmt.setString(25, addId);
            pstmt.setInt(26, check);
            pstmt.setString(27, picture);
            pstmt.setString(28, rejectReason);
            pstmt.setInt(29, id);
            if (pstmt.executeUpdate() == 1) {
                logger.info("更新no_meeting表成功");
                return true;
            } else {
                logger.error("更新no_meeting表失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("更新no_meeting表失败");
        }
        logger.error("更新no_meeting表失败");
        return false;
    }

    /**
     * @param receiveGson:
     * @return boolean
     * @author Axing
     * @description 更新meeting表中除id, user_id, nickname之外的所有数据。下面为创建此函数时meeting表中所有的字段
     * id: int
     * user_id: int
     * nickname: char(16)
     * time: char(20)
     * session: char(2)
     * ordinal: char(10)
     * attend: int(2)
     * consider: int(2)
     * supplement_consider: char(225)
     * recommendation: int(2)
     * supplement_recommendation: char(225)
     * bill: int(2)
     * supplement_bill: char(225)
     * question: int(2)
     * supplement_question: char(225)
     * total: int(2)
     * add_id: char(16)
     * check: tinyint(1)
     * picture: char(255)
     * reject_reason: char(255)
     * @date 2023/7/22 20:37
     */
    public boolean update_meeting(String receiveGson) {
        Map<String, String> mapReceive = new Gson().fromJson(receiveGson, new TypeToken<Map<String, String>>() {
        }.getType());
        String id = mapReceive.get("id");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String session = mapReceive.get("session");
        String ordinal = mapReceive.get("ordinal");
        int attend = Integer.parseInt(mapReceive.get("attend"));
        int consider = Integer.parseInt(mapReceive.get("consider"));
        String supplement_consider = mapReceive.get("supplement_consider");
        int recommendation = Integer.parseInt(mapReceive.get("recommendation"));
        String supplement_recommendation = mapReceive.get("supplement_recommendation");
        int bill = Integer.parseInt(mapReceive.get("bill"));
        String supplement_bill = mapReceive.get("supplement_bill");
        int question = Integer.parseInt(mapReceive.get("question"));
        String supplement_question = mapReceive.get("supplement_question");
        int total = attend + consider + recommendation + bill + question;
        String add_id = mapReceive.get("add_id");
        int check = Integer.parseInt(mapReceive.get("check"));
        String picture = mapReceive.get("picture");
        String reject_reason = mapReceive.get("reject_reason");
        try {
            String updateQuery = "UPDATE meeting SET time=?, session=?, ordinal=?, attend=?, consider=?, supplement_consider=?, " +
                    "recommendation=?, supplement_recommendation=?, bill=?, supplement_bill=?, question=?, " +
                    "supplement_question=?, total=?, add_id=?, `check`=?, picture=?, reject_reason=? WHERE id=?";
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, time);
            pstmt.setString(2, session);
            pstmt.setString(3, ordinal);
            pstmt.setInt(4, attend);
            pstmt.setInt(5, consider);
            pstmt.setString(6, supplement_consider);
            pstmt.setInt(7, recommendation);
            pstmt.setString(8, supplement_recommendation);
            pstmt.setInt(9, bill);
            pstmt.setString(10, supplement_bill);
            pstmt.setInt(11, question);
            pstmt.setString(12, supplement_question);
            pstmt.setInt(13, total);
            pstmt.setString(14, add_id);
            pstmt.setInt(15, check);
            pstmt.setString(16, picture);
            pstmt.setString(17, reject_reason);
            pstmt.setString(18, id);

            pstmt.executeUpdate();
            if (pstmt.executeUpdate() == 1) {
                logger.info("会议期间积分变动数据更新成功");
                return true;
            } else {
                logger.error("会议期间积分变动数据更新失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("会议期间积分变动数据更新失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id:
     * @param reason:
     * @param select:
     * @return boolean
     * @author Axing
     * @description 根据select选择表（true为meeting表；反之no_meeting表），根据积分表中的记录id更新原因，
     * @date 2023/7/22 17:31
     */
    public boolean update_reason(String id, String reason, boolean select) {
        try {
//            select 为true 时插入到meeting表中反之插入到no_meeting表中
            if (select) {
                pstmt = conn.prepareStatement("update meeting set reject_reason = ? where id = ?");
            } else {
                pstmt = conn.prepareStatement("update no_meeting set reject_reason = ? where id = ?");
            }
            pstmt.setString(1, reason);
            pstmt.setString(2, id);
            if (pstmt.executeUpdate() == 1) {
                logger.info("更新原因成功");
                return true;
            } else {
                logger.error("更新原因失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("更新原因失败");
            return false;
        }

    }

    /**
     * @param id:
     * @param check:
     * @param select: true为meeting表，false为no_meeting表
     * @return boolean
     * @author Axing
     * @description 根据select选择更新meeting或no_meeting表，根据id更新表中的check字段
     * @date 2023/7/22 10:34
     */
    public boolean update_meeting_or_no_meeting_check(String id, int check, boolean select) throws SQLException {
        if (select) {
            pstmt = conn.prepareStatement("update meeting set `check` = ? where id = ?");
            logger.info("更新大会表中的check字段");
        } else {
            pstmt = conn.prepareStatement("update no_meeting set `check` = ? where id = ?");
        }
        pstmt.setInt(1, check);
        pstmt.setString(2, id);
        if (pstmt.executeUpdate() == 1) {
            logger.info("更新积分表中的check字段成功");
            return true;
        } else {
            logger.error("更新积分表中的check字段失败");
            return false;
        }
    }


    /**
     * @param user_id:
     * @param token:
     * @return boolean 是否更新成功user表中的token字段
     * @author Axing
     * @description 根据user_id更新用户token
     * @date 2023/7/18 10:45
     */
    public boolean update_user_token(String token, String user_id) {
        try {
            pstmt = conn.prepareStatement("update user set `token` = ? where user_id = ?");
            pstmt.setString(1, token);
            pstmt.setString(2, user_id);
            if (pstmt.executeUpdate() == 1) {
                logger.info("更新user表中的token字段成功");
                return true;
            } else {
                logger.error("更新user表中的token字段失败");
                return false;
            }
        } catch (SQLException e) {
            logger.error("更新user表中的token字段失败（SQLException）");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param receiveGson 从前端接收的json字符串
     * @return boolean 成功返回true，失败返回false（已存在该账号）（SQLException）
     * @author Axing
     * @description 更新用户具体信息
     * @date 2023/7/13 16:11
     */
    public boolean update_user_info(String receiveGson) {
        Map<String, String> map;
        Gson gson = new Gson();
        map = gson.fromJson(receiveGson, new TypeToken<Map<String, String>>() {
        }.getType());
        String user_id = map.get("user_id");
        String nickname = map.get("nickname");
        String gender = map.get("gender");
        String ethnicity = map.get("ethnicity");
        String birthday = map.get("birthday");
        String native_place = map.get("native_place");
        String party = map.get("party");
        String work_start_date = map.get("work_start_date");
        String health_status = map.get("health_status");
        String education = map.get("education");
        String graduate_school_major = map.get("graduate_school_major");
        String degree_technical_title = map.get("degree_technical_title");
        String expertise = map.get("expertise");
        String id_card_number = map.get("id_card_number");
        String employer = map.get("employer");
        String current_position = map.get("current_position");
        String phone_number = map.get("phone_number");
        String postal_code = map.get("postal_code");
        String resume = map.get("resume");
        String city = map.get("city");
        String district = map.get("district");

        try {
            String sql = "UPDATE user_info SET nick_name=?, gender=?, ethnicity=?, birthday=?, native_place=?, party=?, " +
                    "work_start_date=?, health_status=?, education=?, graduate_school_major=?, degree_technical_title=?," +
                    " expertise=?, id_card_number=?, employer=?, current_position=?, phone_number=?, postal_code=?, " +
                    "resume=?, city = ? , district = ? WHERE user_id=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nickname);
            pstmt.setString(2, gender);
            pstmt.setString(3, ethnicity);
            pstmt.setString(4, birthday);
            pstmt.setString(5, native_place);
            pstmt.setString(6, party);
            pstmt.setString(7, work_start_date);
            pstmt.setString(8, health_status);
            pstmt.setString(9, education);
            pstmt.setString(10, graduate_school_major);
            pstmt.setString(11, degree_technical_title);
            pstmt.setString(12, expertise);
            pstmt.setString(13, id_card_number);
            pstmt.setString(14, employer);
            pstmt.setString(15, current_position);
            pstmt.setString(16, phone_number);
            pstmt.setString(17, postal_code);
            pstmt.setString(18, resume);
            pstmt.setString(19, city);
            pstmt.setString(20, district);
            pstmt.setString(21, user_id);

            if (pstmt.executeUpdate() == 1) {
                logger.info("新增用户信息成功");
                return true;
            } else {
                logger.error("新增用户信息失败");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("新增用户信息失败");
        }
        return false;
    }

    /**
     * @param user_id:
     * @param check:
     * @return boolean user_review表中check字段是否修改成功
     * @author Axing
     * @description 对指定用户的审核状态进行修改(可指定修改为通过或不通过)
     * @date 2023/7/15 15:05
     */
    public boolean update_user_review(String user_id, int check) {
        try {
            pstmt = conn.prepareStatement("update user_review set `check` = ? where user_id = ?");
            pstmt.setInt(1, check);
            pstmt.setString(2, user_id);
            if (pstmt.executeUpdate() == 1) {
                logger.info("修改用户审核状态成功");
                return true;
            } else {
                logger.error("修改用户审核状态失败");
                return false;
            }
        } catch (SQLException e) {
            logger.error("修改用户审核状态失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /* ******************************************************************************************************************
     * 查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查  *
     * 查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查  *
     **************************************************************************************************************** */

    /**
     * @param :
     * @return ResultSet
     * @author Axing
     * @description 查找find_password_review表中每个用户最新密码修改记录
     * @date 2023/8/19 15:30
     */
    public ResultSet select_find_password_review(){
        try {
            pstmt = conn.prepareStatement("SELECT * FROM find_password_review WHERE (username, id) IN" +
                    " ( SELECT username, MAX(id) FROM find_password_review GROUP BY username);");
            rs = pstmt.executeQuery();
            logger.info("查询find_password_review表成功");
            return rs;
        } catch (SQLException e) {
            logger.error("查询find_password_review表失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id:
     * @param select: 为真时查询meeting表，为假时查询no_meeting表
     * @return ResultSet
     * @author Axing
     * @description 查询meeting表或no_meeting表中指定id和check的全部信息
     * @date 2023/7/22 18:23
     */
    public ResultSet select_points(String id, boolean select) {
        try {
            if (select) {
                pstmt = conn.prepareStatement("SELECT * FROM meeting WHERE id = ?");
            } else {
                pstmt = conn.prepareStatement("SELECT * FROM no_meeting WHERE id = ?");
            }
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            logger.info("查询" + (select ? "meeting" : "no_meeting") + "表积分变动信息成功");
            return rs;
        } catch (SQLException e) {
            logger.error("查询" + (select ? "meeting" : "no_meeting") + "表积分变动信息异常");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param user_id:
     * @param check:
     * @return ResultSet id,try,inspect,activity,activity2,group_work,survey,material, duty,solve,complete,
     * nickname,total,reject_reason
     * @author Axing
     * @description 根据user_id和check字段查询no_meeting表中的积分记录（用于用户和管理员查看积分记录）
     * @date 2023/7/22 10:30
     */
    public ResultSet select_no_meeting_points(String user_id, int check, boolean all) {
        try {
            if (all) {
                pstmt = conn.prepareStatement("SELECT * FROM no_meeting WHERE `check` = ?");
                pstmt.setInt(1, check);

            } else {
                pstmt = conn.prepareStatement("SELECT * FROM no_meeting WHERE user_id = ? AND `check` = ?");
                pstmt.setString(1, user_id);
                pstmt.setInt(2, check);
            }
            rs = pstmt.executeQuery();
            logger.info("查询非会议期间指定用户、指定check字段的记录成功");
            return rs;
        } catch (SQLException e) {
            logger.error("查询非会议期间指定用户、指定check字段的记录异常");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param user_id:
     * @param check:
     * @return ResultSet 返回积分变动id, nickname, session, ordinal,attend,consider, recommendation, bill, question,
     * total, reject_reason 的结果集
     * @author Axing
     * @description 查询会议期间指定user_id、check字段的记录
     * @date 2023/7/20 15:41
     */
    public ResultSet select_meeting_points(String user_id, int check, boolean all) {
        try {
            if (all) {
                pstmt = conn.prepareStatement("SELECT * FROM meeting WHERE `check` = ?");
                pstmt.setInt(1, check);
            } else {
                pstmt = conn.prepareStatement("SELECT * FROM meeting WHERE user_id = ? AND `check` = ?");
                pstmt.setString(1, user_id);
                pstmt.setInt(2, check);
            }
            rs = pstmt.executeQuery();
            logger.info("查询会议期间指定用户、指定check字段的记录成功");
            return rs;
        } catch (SQLException e) {
            logger.error("查询会议期间指定用户、指定check字段的记录异常");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param token:
     * @return String user_id
     * @author Axing
     * @description 在user表中查询token字段，返回user_id，若不存在则返回空字符串（“”）
     * @date 2023/7/19 19:57
     */
    public String select_token(String token) {
        try {
            pstmt = conn.prepareStatement("SELECT user_id FROM user WHERE token = ?");
            pstmt.setString(1, token);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("查询token成功");
                return rs.getString("user_id");
            } else {
                logger.error("查询token失败");
                return "";
            }
        } catch (SQLException e) {
            logger.error("查询token失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param token:
     * @return boolean
     * @author Axing
     * @description 根据token查询user表，若存在则返回true，否则返回false
     * @date 2023/7/30 12:21
     */
    public boolean select_boolean_token(String token) {
        try {
            pstmt = conn.prepareStatement("SELECT token FROM user WHERE token = ?");
            pstmt.setString(1, token);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("查询token成功");
                return true;
            } else {
                logger.error("查询token失败");
                return false;
            }
        } catch (SQLException e) {
            logger.error("查询token失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param username:
     * @return ResultSet
     * @author Axing
     * @description 模糊查询username字段，返回一个结果集
     * @date 2023/7/17 14:51
     */
    public ResultSet select_username(String username) {
        try {
            pstmt = conn.prepareStatement("SELECT user_id,nickname FROM user WHERE username like ?");
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            logger.info("查询用户名成功");
            return rs;
        } catch (SQLException e) {
            logger.error("查询用户名失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param city:
     * @param district:
     * @return ResultSet
     * @author Axing
     * @description 根据市、区返回指定代表信息集（user_id,nickname)
     * @date 2023/7/17 14:25
     */
    public ResultSet select_city_district(String city, String district) {
        try {
            pstmt = conn.prepareStatement("SELECT user_id,nick_name FROM user_info WHERE city = ? AND district like ?");
            pstmt.setString(1, city);
            pstmt.setString(2, district);
            rs = pstmt.executeQuery();
            logger.info("查询城市成功");
            return rs;
        } catch (SQLException e) {
            logger.error("查询城市失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param nickname:
     * @return ResultSet
     * @author Axing
     * @description 根据姓氏查询用户信息表中的用户信息
     * @date 2023/7/16 19:31
     */
    public ResultSet select_nickname(String nickname) {
        try {
            pstmt = conn.prepareStatement("SELECT user_id,nickname FROM user WHERE nickname like ?");
            pstmt.setString(1, nickname);
            rs = pstmt.executeQuery();
            logger.info("查询姓氏成功");
            return rs;
        } catch (SQLException e) {
            logger.error("查询姓氏失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param userId:
     * @return String
     * @author Axing
     * @description 根据user_id查询用户昵称
     * @date 2023/7/27 20:49
     */
    public String select_nickname(int userId) {
        try {
            pstmt = conn.prepareStatement("SELECT nickname FROM user WHERE user_id = ?");
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("查询用户昵称成功");
                return rs.getString("nickname");
            } else {
                logger.error("查无此人");
                return null;
            }
        } catch (SQLException e) {
            logger.error("查询用户昵称失败（SQLException）");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param username:
     * @return String
     * @author Axing
     * @description 根据username（账号，也是手机号）查询用户审核信息表中的user_id
     * @date 2023/7/16 15:51
     */
    public String select_user_review_id(String username) {
        try {
            pstmt = conn.prepareStatement("SELECT user_id FROM user_review WHERE username = ?");
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("查询用户审核信息表中的user_id成功");
                return rs.getString("user_id");
            } else {
                logger.error("查询用户审核信息表中的user_id失败");
                return null;
            }
        } catch (SQLException e) {
            logger.error("查询用户审核信息表中的user_id失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param user_id:
     * @return ResultSet
     * @author Axing
     * @description 根据user_id查询用户的具体信息
     * @date 2023/7/16 9:06
     */
    public ResultSet select_user_info(String user_id) {
        try {
            pstmt = conn.prepareStatement("SELECT * FROM user_info WHERE user_id = ?");
            pstmt.setString(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            logger.info("查询用户信息成功");
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户信息失败");
        }
        return null;
    }

    /**
     * @param check: 是0或1（0表示未审核，1表示已审核）
     * @return ResultSet
     * @author Axing
     * @description 查询用户信息审核表中所有check字段为形参的数据
     * @date 2023/7/16 8:09
     */
    public ResultSet select_user_review(String check) {
        try {
            pstmt = conn.prepareStatement("SELECT user_id, username,nickname,register_time FROM user_review WHERE `check` =" + check);
            ResultSet rs = pstmt.executeQuery();
            logger.info("查询用户信息审核表中所有check字段为" + check + "的数据成功");
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户信息审核表中所有check字段为" + check + "的数据失败");
        }
        return null;
    }

    /**
     * @param userName:
     * @return String
     * @author Axing
     * @description 根据userName查询用户id
     * @date 2023/7/13 15:53
     */
    public String selectUserId(String userName) {
        try {
            pstmt = conn.prepareStatement("SELECT user_id FROM user WHERE username = ?");
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("查询用户id成功");
                return rs.getString("user_id");
            }else {
                logger.error("查无此人");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户id失败");
        }
        logger.error("查询用户id失败");
        return null;
    }

    /**
     * @param user_id:
     * @return String
     * @author Axing
     * @description 根据user_id查询用户权限
     * @date 2023/7/13 15:52
     */
    public String select_power(String user_id) {
        try {
            pstmt = conn.prepareStatement("SELECT power FROM user WHERE user_id = ?");
            pstmt.setString(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("power");
            }
            logger.info("查询用户权限成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户权限失败");
        }
        logger.error("查询用户权限失败");
        return null;
    }

    /**
     * @param username: 账号
     * @param password: 密码（加密：md5（md5（原密码）+salt））
     * @return String user_id
     * @author Axing
     * @description 根据账号密码查看账号密码是否正确，正确返回user_id，错误返回null
     * @date 2023/7/13 14:41
     */
    public String select_username_password(String username, String password) {
        try {
            pstmt = conn.prepareStatement("SELECT user_id FROM user WHERE username = ? AND password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("user_id");
            } else {
                logger.info("账号密码错误");
                return "";
            }
        } catch (SQLException e) {
            logger.error("判断账号密码正确时出现异常");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param userName:
     * @return boolean
     * @author Axing
     * @description 查询是否有此账号
     * @date 2023/7/6 13:56
     */
    public boolean selectUser(String userName) {
        try {
            pstmt = conn.prepareStatement("SELECT username FROM user WHERE username = ?");
            pstmt.setString(1, userName);
            return pstmt.executeQuery().next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param username:
     * @return String
     * @author Axing
     * @description 根据账号查密码盐
     * @date 2023/7/6 14:29
     */
    public String selectSalt(String username) {
        try {
            pstmt = conn.prepareStatement("SELECT salt FROM user WHERE username = ?");
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) return rs.getString("salt");
            else {
                logger.info("查询密码盐失败");
                return null;
            }
        } catch (Exception e) {
            logger.error("查询密码盐失败");
            e.printStackTrace();
        }
        return null;
    }

    public void closeDB() {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
            logger.info("数据库关闭成功");
        } catch (Exception e) {
            logger.error("数据库关闭失败");
            e.printStackTrace();
        }
    }

}
