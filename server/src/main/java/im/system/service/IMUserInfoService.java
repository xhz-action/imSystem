package im.system.service;

import im.system.model.IMUserInfo;
import im.system.dao.IMUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IMUserInfoService {

    @Resource
    private IMUserMapper userMapper;

    /**
     * 查询所有用户信息
     * @return
     */
    public List<IMUserInfo> getUserInfoList(){
        return userMapper.selectList(null);
    }
}
