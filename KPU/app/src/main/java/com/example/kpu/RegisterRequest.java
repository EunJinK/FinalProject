package com.example.kpu;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //서버URL 설정
    final static private String URL = "http://kpuhosting.dothome.co.kr/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST,URL,listener,null);

        map = new HashMap<>();
        map.put("userID", userID); //userID를 등록
        map.put("userPassword",userPassword); //userPassword를 등록

    }
    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}