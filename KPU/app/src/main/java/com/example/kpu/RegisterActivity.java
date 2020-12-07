package com.example.kpu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextNumber, editTextTextPassword;
    private Button button9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join); //activity_join 레이아웃 띄우기
        editTextNumber = findViewById(R.id.editText_ID); //editText_ID값을 editTextNumber에 저장
        editTextTextPassword = findViewById(R.id.editText_pass); //editText_pass 값을 editTextTextPassword에 저장

        button9 = findViewById(R.id.button9); //button9값을 button9에 저장 후 그 버튼을 누를 시(회원가입 완료 버튼 누를 시)
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = editTextNumber.getText().toString(); //ID, Password 스트링값으로 저장
                String userPassword = editTextTextPassword.getText().toString();
                Log.d("ID : ", userID); //log - 확인하는 메세지 정도로 생각(ex : c언어에서 print사용하여 어디서 오류나는지 확인)
                Log.d("Password : ", userPassword);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.substring(3)); //3자리를 제외하고 String response를 JSONObject로 저장 - 제외이유 : 쓰레기값을 포함시켜서 그걸 제외시키는 것
                            //즉 String -> JSON
                            boolean success = jsonObject.getBoolean("success"); //success의 bool값을 불러옴 = true
                            if (success) { //true라면 - 즉 ID, Password를 DB로 저장했다면
                                Log.d("회원가입 : ", "회원가입 성공" ); //회원가입 성공했다는 log값 출력
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show(); //회원가입 성공이라는 토스트메시지 출력
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class); //회원가입성공과 동시에 바로 MainActivity 화면전환
                                startActivity(intent);
                            } else {
                                Log.d("회원가입 : ", "회원가입 실패" );
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                //서버로 Volley를 이용해서 요청
                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}