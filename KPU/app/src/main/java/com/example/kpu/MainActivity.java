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

public class MainActivity extends AppCompatActivity {

    private EditText ed_id, ed_password;
    private Button button, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_id = findViewById(R.id.editText); //editText에 써지는 ID값 읽어서 ed_id에 저장
        ed_password = findViewById(R.id.editText2); //editText2에 써지는 Password값 읽어서 ed_password에 저장

        Button button = (Button) findViewById(R.id.button); //로그인 버튼누를시 값 button에 저장
        button.setOnClickListener(new View.OnClickListener(){ //로그인 버튼 누를시
            public void onClick(View v) {
                String userID = ed_id.getText().toString(); //ed_id에 쓰여진 id를 스트링으로 저장
                String userPassword = ed_password.getText().toString(); //ed_password에 쓰여진 password를 스트링으로 저장

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response.substring(3)); //response에서 3글자예외하고(스레기값) 스트링값으로 JSONObject 저장
                            boolean success = jsonObject.getBoolean("success"); //success에 boolean값을 success에저장
                            if(success) //true라면
                            {
                                String userID = jsonObject.getString("userID"); //DB에 저장된 ID, Password 스트링으로 불러옴(?)
                                String userPassword = jsonObject.getString("userPassword");
                                Log.d("ACTIVITY_LC","로그인 되었습니다."); //로그인 되었다는 Log값을 Logcat에 출력 - 그저 작동되는지 알아보기위한 구문
                                Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show(); //로그인되었다는 토스트알림창 띄우기
                                Intent intent = new Intent(getApplicationContext(), LoginSucActivity.class); //다음 액티비티인 LoginSucActivity 띄우기
                                startActivity(intent); // 메소드를 이용하여 새로운 액티비티를 띄움
                            }
                            else //로그인 실패 시
                            {
                                Log.d("ACTIVITY_ELC","로그인 실패하였습니다.");
                                Toast.makeText( getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG ).show();
                                return;
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
                RequestQueue queue = Volley.newRequestQueue( MainActivity.this );
                queue.add(loginRequest);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2); //회원가입 버튼 누를 시
        button2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Log.d("JOIN_US", "아이디와 비밀번호를 입력하세요.");
                Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent); //메소드를 이용하여 회원가입 액티비티를 띄움
            }
        });
    }
}