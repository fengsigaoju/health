package com.fengsigaoju.health.friend.util;

import com.fengsigaoju.health.friend.domain.dto.UserDTO;

import java.util.Map;

/**
 * @author yutong song
 * @date 2018/5/8
 */
public class RestUtil {

    public static UserDTO convertToUserDTO(Map<String,Object>map){
        UserDTO userDTO=new UserDTO();
        userDTO.setUsername((String)map.get("username"));
        userDTO.setUserId(Long.valueOf(map.get("userId").toString()));
        userDTO.setUserStatus((String)map.get("userStatus"));
        userDTO.setGmtCreate((String)(map.get("gmtCreate")));
        userDTO.setGmtModified((String)map.get("gmtModified"));
        userDTO.setLinkPhone(Long.valueOf(map.get("linkPhone").toString()));
        userDTO.setPictureUrl((String)map.get("pictureUrl"));
        return userDTO;
    }
}
