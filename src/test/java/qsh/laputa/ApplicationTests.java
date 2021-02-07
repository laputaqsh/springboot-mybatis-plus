package qsh.laputa;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.mysql.jdbc.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import qsh.laputa.entity.User;
import qsh.laputa.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class ApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void select() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void insert() {
        User user = new User();
        user.setName("不撞墙");
        user.setAge(20);
        user.setEmail("bzq@baomidou.com");
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        int rows = userMapper.insert(user);
        System.out.println("影响记录数：" + rows);
    }

    @Test
    public void selectById() {
        User user = userMapper.selectById(1094590409767661570L);
        System.out.println(user);
    }

    @Test
    public void selectByIds() {
        List<Long> ids = Arrays.asList(1088248166370832385L, 1088250446457389058L, 1094592041087729666L, 1357167922933452801L);
        List<User> userList = userMapper.selectBatchIds(ids);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("manager_id", 1088248166370832385L);//应为列名，而非字段名
        columnMap.put("age", 31);
        List<User> userList = userMapper.selectByMap(columnMap);
        userList.forEach(System.out::println);
    }

    /**
     * 1、名字中包含雨并且年龄小于40
     * name like '%雨%' and age<40
     */
    @Test
    public void selectByWrapper1() {
        //QueryWrapper<User> query = Wrappers.<User>query();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨").lt("age", 40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 2、名字中包含雨年并且年龄大于等于20且小于等于40并且email不为空
     * name like '%雨%' and age between 20 and 40 and email is not null
     */
    @Test
    public void selectByWrapper2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "雨").between("age", 31, 32).isNotNull("email");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 3、名字为王姓或者年龄大于等于30，按照年龄降序排列，年龄相同按照id升序排列
     * name like '王%' or age>=30 order by age desc,id asc
     */
    @Test
    public void selectByWrapper3() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王").or().ge("age", 30)
                .orderByDesc("age").orderByAsc("id");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 4、创建日期为2021年1月14日并且直属上级为名字为王姓
     * date_format(create_time,'%Y-%m-%d')='2021-01-14' and manager_id in (select id from user where name like '王%')
     */
    @Test
    public void selectByWrapper4() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2021-01-14")
                .inSql("manager_id", "select id from user where name like '王%'");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 5、名字为刘姓并且（年龄小于32或邮箱不为空）
     * name like '刘%' and (age<32 or email is not null)
     */
    @Test
    public void selectByWrapper5() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "刘")
                .and(qw -> qw.lt("age", 32).or().isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 6、名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
     * name like '王%' or (age<40 and age>20 and email is not null)
     */
    @Test
    public void selectByWrapper6() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王")
                .or(qw -> qw.lt("age", 40).gt("age", 20).isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 7、（年龄小于40或邮箱不为空）并且名字为王姓
     * (age<40 or email is not null) and name like '王%'
     */
    @Test
    public void selectByWrapper7() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "王");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 8、年龄为30、31、34、35
     * age in (30、31、34、35)
     */
    @Test
    public void selectByWrapper8() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age", Arrays.asList(30, 31, 34, 35));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 9、只返回满足条件的其中一条语句即可
     * limit 1
     */
    @Test
    public void selectByWrapper9() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age", Arrays.asList(30, 31, 34, 35)).last("limit 1");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 10、名字中包含雨并且年龄小于40(id,name)【选取字段】
     */
    @Test
    public void selectByWrapper10() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name").like("name", "雨").lt("age", 40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 10、名字中包含雨并且年龄小于40(id,name,age,email)【排除字段】
     */
    @Test
    public void selectByWrapper11() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(User.class, info -> !info.getColumn().equals("manager_id") &&
                !info.getColumn().equals("create_time")).like("name", "雨").lt("age", 40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 10、名字中包含雨并且年龄小于40(id,name,age,email)【排除字段】
     */
    @Test
    public void selectWithCondition() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String name = "王";
        String email = "";
//        if (!StringUtils.isNullOrEmpty(name)) {
//            queryWrapper.like("name", name);
//        }
//        if (!StringUtils.isNullOrEmpty(email)) {
//            queryWrapper.like("email", email);
//        }
        queryWrapper.like(!StringUtils.isNullOrEmpty(name), "name", name)
                .like(!StringUtils.isNullOrEmpty(email), "email", email);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperEntity() {
        User whereUser = new User();
        whereUser.setName("刘红雨");
        whereUser.setAge(32);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(whereUser);
        //queryWrapper.like("name", "雨").lt("age", 40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperAllEq() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", null);
        //queryWrapper.allEq(params, false);
        queryWrapper.allEq((k, v) -> !k.equals("name"), params);

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperMaps() {//当表字段特别多，且只需查其中少数几列的时候，可以选用这种方式
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name").like("name", "雨").lt("age", 40);

        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 11、按照直属上级分组，查询每组的平均年龄、最小年龄、最大年龄。
     * 并且只取年龄总和小于500的组。
     * select avg(age) avg_age,min(age) min_age,max(age) max_age
     * from user
     * group by manager_id
     * having sum(age) < 500
     */
    @Test
    public void selectByWrapperMaps2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
                .groupBy("manager_id").having("sum(age)<{0}", 500);

        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperObjs() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name").like("name", "雨").lt("age", 40);

        List<Object> userList = userMapper.selectObjs(queryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectByWrapperCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").like("name", "雨").lt("age", 40);

        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println("记录数：" + count);
    }

    @Test
    public void selectByWrapperOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", "刘红雨").lt("age", 40);

        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    @Test
    public void selectByLambda() {// 防止列名误写
        //LambdaQueryWrapper<User> lambdaQueryWrapper = new QueryWrapper<User>().lambda();
        //LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.like(User::getName, "雨").lt(User::getAge, "40");

        List<User> userList = userMapper.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 5、名字为王姓并且（年龄小于40或邮箱不为空）
     * name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void selectByLambda2() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.likeRight(User::getName, "王")
                .and(qw -> qw.lt(User::getAge, 40).or().isNotNull(User::getEmail));

        List<User> userList = userMapper.selectList(lambdaQueryWrapper);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectByLambda3() {
        List<User> userList = new LambdaQueryChainWrapper<>(userMapper)
                .like(User::getName, "雨").ge(User::getAge, 20).list();
        userList.forEach(System.out::println);
    }

}
