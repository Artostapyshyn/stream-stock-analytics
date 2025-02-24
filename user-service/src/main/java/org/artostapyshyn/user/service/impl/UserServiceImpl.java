package com.artostapyshyn.user.service.impl;

import lombok.AllArgsConstructor;
import com.artostapyshyn.user.model.UserVO;
import com.artostapyshyn.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public UserVO save(UserVO userVO) {
        String userId = String.valueOf(new Date().getTime());
        userVO.setId(userId);
        userVO.setRole("USER");
        return userVO;
    }
}
