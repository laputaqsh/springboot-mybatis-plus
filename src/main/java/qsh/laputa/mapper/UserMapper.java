package qsh.laputa.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import qsh.laputa.entity.User;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    //可用于多表查询
    Page<User> selectCustomPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper<User> wrapper);

}
