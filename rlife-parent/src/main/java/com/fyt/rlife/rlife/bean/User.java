package com.fyt.rlife.rlife.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id; //用户Id
    private String username; //用户名
    private String userpswd; //密码
    private String email; //邮箱
    private String phone; //电话
    private String sex; //性别
    private Date birthday; //生日
    private Date lastDate; //最后登陆时间
    private Date regDate; //注册时间
    private Integer userLeave; //等级
    private Integer integral; //轮回积分

    //角色集合
    //@Transient
    //private List<Role> roles;
    //道具集合
}
