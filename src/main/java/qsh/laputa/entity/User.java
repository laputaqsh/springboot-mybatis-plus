package qsh.laputa.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;

    //@TableField(condition = SqlCondition.LIKE)
    private String name;

    //@TableField(condition = "%s&lt;#{%s}")
    private Integer age;

    private String email;

    private Long managerId;//上级ID

    private LocalDateTime createTime;//创建时间

    /**
     * 将某字段不对应到数据库中
     * 1、transient（无法序列化）
     * 2、static（整个类对应一个）
     * 3、@TableField(exist = false)
     */
}
