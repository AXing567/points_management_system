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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
     * @description 链接数据库
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
     * @param :
     * @return void
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

    /*******************************************************************************************************************
     * 增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增  *
     * 增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增增  *
     **************************************************************************************************************** */


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
            String username = null;
            String nickname = null;
            String password = null;
            String salt = null;
            if (rs.next()) {
                username = rs.getString("username");
                nickname = rs.getString("nickname");
                password = rs.getString("password");
                salt = rs.getString("salt");
            }else {
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
            String nickname = null;
            if (rs.next()) {
                nickname = rs.getString("nickname");
            }else {
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
     * @param content:
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
     * @return boolean
     * @author Axing
     * @description 插中入出席大会数据：第几次大会，是否出席大会，审议报告决议并积极发言，提出建议意见，提出议案，提出质询及以上内容的补充说明至待审核的表
     * @date 2023/7/12 19:56
     */
    public boolean insert_meeting_review(String content, int attend, int consider, String supplement_consider,
                                         int recommendation, String supplement_recommendation, int bill,
                                         String supplement_bill, int question, String supplement_question,
                                         String nickname, String add_id, String picture, String user_id, int total) {
        try {
            pstmt = conn.prepareStatement("insert into meeting_review" +
                    "(content,attend,consider,supplement_consider,recommendation,supplement_recommendation,bill,supplement_bill," +
                    "question,supplement_question,nickname,add_id,picture,user_id, total) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, content);
            pstmt.setInt(2, attend);
            pstmt.setInt(3, consider);
            pstmt.setString(4, supplement_consider);
            pstmt.setInt(5, recommendation);
            pstmt.setString(6, supplement_recommendation);
            pstmt.setInt(7, bill);
            pstmt.setString(8, supplement_bill);
            pstmt.setInt(9, question);
            pstmt.setString(10, supplement_question);
            pstmt.setString(11, nickname);
            pstmt.setString(12, add_id);
            pstmt.setString(13, picture);
            pstmt.setString(14, user_id);
            pstmt.setInt(15, total);
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
     * @description 插入非大会相关积分变动数据
     * @date 2023/7/13 9:52
     */
    public boolean insert_no_meeting_review(String receiveGson) {
        Map<String, String> map = new Gson().fromJson(receiveGson, new TypeToken<Map<String, String>>() {
        }.getType());
        String user_id = map.get("user_id");
        String nickname = map.get("nickname");
        String time = map.get("time");
        int try1 = Integer.parseInt(map.get("try1"));
        String supplement_try = map.get("supplement_try");
        int inspect = Integer.parseInt(map.get("inspect"));
        String supplement_inspect = map.get("supplement_inspect");
        int activity = Integer.parseInt(map.get("activity"));
        String supplement_activity = map.get("supplement_activity");
        int activity2 = Integer.parseInt(map.get("activity2"));
        String supplement_activity2 = map.get("supplement_activity2");
        int group_work = Integer.parseInt(map.get("group_work"));
        String supplement_group_work = map.get("supplement_group_work");
        int survey = Integer.parseInt(map.get("survey"));
        String supplement_survey = map.get("supplement_survey");
        int material = Integer.parseInt(map.get("material"));
        String supplement_material = map.get("supplement_material");
        int duty = Integer.parseInt(map.get("duty"));
        String supplement_duty = map.get("supplement_duty");
        int solve = Integer.parseInt(map.get("solve"));
        String supplement_solve = map.get("supplement_solve");
        int complete = Integer.parseInt(map.get("complete"));
        String supplement_complete = map.get("supplement_complete");
        int total = try1 + inspect + activity + activity2 + group_work + survey + material + duty + solve + complete;
        String add_id = map.get("add_id");
        try {
            pstmt = conn.prepareStatement("insert into no_meeting_review" +
                    "(user_id,nickname,time,try,supplement_try,inspect,supplement_inspect,activity,supplement_activity,activity2,supplement_activity2," +
                    "group_work,supplement_group_work,survey,supplement_survey,material,supplement_material,duty,supplement_duty,solve,supplement_solve," +
                    "complete,supplement_complete,total,add_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
    public boolean insert_find_password(String username, String nickname, String password) {
        try {
            pstmt = conn.prepareStatement("insert into find_password_review(username,nickname,password) values(?,?,?)");
            pstmt.setString(1, username);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            if (pstmt.executeUpdate() == 1) {
                logger.info("找回密码成功");
                return true;
            } else {
                logger.error("找回密码失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("找回密码失败（SQLException）");
            throw new RuntimeException(e);
        }
    }

    /**
     * @param nickname: 姓名
     * @param username: 账号（手机号）
     * @param password: 密码
     * @return boolean
     * @author Axing
     * @description 新增用户。在用户审核表和用户详细信息表中插入数据
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

    /**
     * @param nickname:
     * @param username:
     * @return boolean
     * @author Axing
     * @description TODO
     * @date 2023/7/13 19:43
     */
    public boolean insert_user_info(String nickname, String username) {
        try {
            pstmt = conn.prepareStatement("insert into user_info(user_id,nick_name) values(?,?)");
            String user_id = selectUserId(username);
            pstmt.setString(1, user_id);
            pstmt.setString(2, nickname);
            if (pstmt.executeUpdate() == 1) {
                logger.info("新建用户详细信息数据条成功");
                return true;
            } else {
                logger.error("新建用户详细信息数据条失败");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("新建用户详细信息数据条失败");
            throw new RuntimeException(e);
        }
    }


    /*******************************************************************************************************************
     * 改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改  *
     * 改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改改  *
     **************************************************************************************************************** */
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
        String token = map.get("token");
        try {
            String sql = "UPDATE user_info SET nick_name=?, gender=?, ethnicity=?, birthday=?, native_place=?, party=?, " +
                    "work_start_date=?, health_status=?, education=?, graduate_school_major=?, degree_technical_title=?," +
                    " expertise=?, id_card_number=?, employer=?, current_position=?, phone_number=?, postal_code=?, " +
                    "resume=?, token=? WHERE user_id=?";
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
            pstmt.setString(19, token);
            pstmt.setString(20, user_id);

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
     * @return boolean
     * @author Axing
     * @description 对指定用户的审核状态进行修改
     * @date 2023/7/15 15:05
     */
    public boolean update_user_review(String user_id) {
        try {
            pstmt = conn.prepareStatement("update user_review set `check` = 1 where user_id = ?");
            pstmt.setString(1, user_id);
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


    /*******************************************************************************************************************
     * 查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查  *
     * 查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查查  *
     **************************************************************************************************************** */

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
                return rs.getString("user_id");
            }
            logger.info("查询用户id成功");
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
     * @param username:
     * @param password:
     * @return String token
     * @author Axing
     * @description 查看账号密码是否正确，存在返回token，否则返回null
     * @date 2023/7/13 14:41
     */
    public String select_token(String username, String password) {
        try {
            pstmt = conn.prepareStatement("SELECT token FROM user WHERE username = ? AND password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("token");
            } else {
                logger.info("账号密码错误");
                return null;
            }
        } catch (SQLException e) {
            logger.error("判断账号密码正确性出现异常");
            e.printStackTrace();
        }
        return null;
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
