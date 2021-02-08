package qsh.laputa.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import qsh.laputa.entity.User;
import qsh.laputa.mapper.UserMapper;
import qsh.laputa.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
