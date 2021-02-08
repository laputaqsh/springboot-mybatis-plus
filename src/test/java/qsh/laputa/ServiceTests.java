package qsh.laputa;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import qsh.laputa.entity.User;
import qsh.laputa.service.UserService;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class ServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void getOne() {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge, 25), false);
        System.out.println(user);
    }

    @Test
    public void batch() {
        User user1 = new User();
        user1.setName("拆南墙");
        user1.setAge(32);
        User user2 = new User();
        user2.setId(1358612312944033794L);
        user2.setName("拆北墙");
        user2.setAge(33);
        List<User> userList = Arrays.asList(user1, user2);
        boolean res = userService.saveOrUpdateBatch(userList);
        System.out.println(res);
    }

    @Test
    public void chainSelect() {
        List<User> userList = userService.lambdaQuery()
                .gt(User::getAge, 25).like(User::getName, "墙").list();
        userList.forEach(System.out::println);
    }

    @Test
    public void chainUpdate() {
        boolean res = userService.lambdaUpdate()
                .eq(User::getAge, 31).set(User::getAge, 26).update();
        System.out.println(res);
    }

    @Test
    public void chainDelete() {
        boolean res = userService.lambdaUpdate()
                .eq(User::getAge, 33).remove();
        System.out.println(res);
    }

}
