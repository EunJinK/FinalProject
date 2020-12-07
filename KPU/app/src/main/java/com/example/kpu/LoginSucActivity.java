package com.example.kpu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginSucActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_suc);
        //ImageButton imbutton1 = (ImageButton) findViewById(R.id.imageButton2); //열람실 내자리확인 -> 현재 구현안하는 중
        //imbutton1.setOnClickListener(new ImageButton.OnClickListener(){
        //    public void onClick(View v) {
        //         Intent intent = new Intent(getApplicationContext(), SeatcfActivity.class);
        //         startActivity(intent); // 메소드를 이용하여 새로운 액티비티를 띄움
        //    }
        //});

        ImageButton imbutton2 = (ImageButton) findViewById(R.id.imageButton3); //열람실 자리예약
        //ImageButton(누를 것의 형태) imbutton2 (변수명 선언)
        imbutton2.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View v) {
                Log.d("ACTIVITY_LC", "원하는 자리를 클릭해주세요.");
                Toast.makeText(getApplicationContext(), "원하는 자리를 클릭해주세요.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SeatreActivity.class); //Intent를 이용하여 버튼 클릭 시 SeatreActivity
                startActivity(intent); // 메소드를 이용하여 새로운 액티비티를 띄움
                Log.d("ACTIVITY_LC", "자리예약화면");
            }
        });
    }
}