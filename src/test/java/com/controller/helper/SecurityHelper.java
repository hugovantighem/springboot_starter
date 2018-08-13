package com.controller.helper;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SecurityHelper {
    public static final String LOGIN_BODY = "username={0}&password={1}";

    public static MultiValueMap<String, String> loginForm(String username, String password){
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        return map;
    }

}
