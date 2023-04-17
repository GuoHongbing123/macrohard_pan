package com.vpsair.modules.pan.dao;

import com.vpsair.common.utils.Result;
import com.vpsair.modules.pan.entity.UserinfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 网盘用户 DAO
 * @author Shen
 * @email syf0412@qq.com
 * @date 2021-10-30 23:15:10
 */
@Mapper
public interface UserinfoDao extends BaseMapper<UserinfoEntity> {

    int update(Long sysId, String nickname, String mobile, String mail, String avatar, String remark);

    int changePassword(Long userId, String newPassword);

    int forgetPassword(String newPassword,String username,String mail);

    int checkPassword(String password);

    int checkUser(String username, String mail);

}
