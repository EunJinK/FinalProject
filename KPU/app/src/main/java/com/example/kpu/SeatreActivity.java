package com.example.kpu;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class SeatreActivity extends AppCompatActivity {
    NotificationManager notificationManager;
    PendingIntent intent;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    String seat = "a"; //예약되었을 때 아두이노로 보낼 메세지
    String seatout = "b"; //외출되었을 때 아두이노로 보낼 메세지
    String cc = "go";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seatre); //activity_seatre레이아웃 띄우기

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //블루투스 어댑터

        final Button bt_list = (Button) findViewById(R.id.bt_list); //블루투스 연결버튼
        final Button bt_on = (Button) findViewById(R.id.bt_on); //블루투스 켜기
        final Button bt_off = (Button) findViewById(R.id.bt_off); //블루투스 끄기
        final Button button1 = (Button) findViewById(R.id.button1); //1번자리 - 1번자리 버튼 값을 찾아와 button1에 저장
        final Button button2 = (Button) findViewById(R.id.button2); //2번자리
        final Button button3 = (Button) findViewById(R.id.button3); //3번자리
        final Button button4 = (Button) findViewById(R.id.button4); //4번자리
        final Button button5 = (Button) findViewById(R.id.button5); //5번자리
        final Button button6 = (Button) findViewById(R.id.button6); //6번자리
        final Button button7 = (Button) findViewById(R.id.button7); //7번자리
        final Button button8 = (Button) findViewById(R.id.button8); //8번자리

        final SharedPreferences out = getSharedPreferences("outcheck", 0); //공유구문 ~4줄
        cc = out.getString("check", "go");
        //SharePreferences 이름 = getSharePreferences(SharePre의 네임, 모드)
        //이름에서 키값을 읽어온다. defValue(키값이 NULL일시 받아올 값)
        //check키값에 공유된 값을 string으로 읽어와서 cc에 저장(NULL일시 go1를 읽어옴)
        //★ 자리가 예약, 반납, 외출 여부를 액티비티가 바뀌어도 유지시키기 위해 쓰는 데이터공유 ★

        if (cc == "input1") { //cc가 input1일 경우 - 1번자리 예약
            button1.setBackgroundColor(Color.rgb(255, 0, 0)); //빨간색 버튼 변화
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
        } else if (cc == "out1") { //cc가 out1일 경우 - 1번자리 외출
            button1.setBackgroundColor(Color.rgb(255, 255, 0)); //노란색 버튼 변화
            button2.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
        } else if (cc == "input2") {
            button2.setBackgroundColor(Color.rgb(255, 0, 0));
            button1.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
        } else if (cc == "out2") {
            button2.setBackgroundColor(Color.rgb(255, 255, 0));
            button1.setEnabled(false);
            button3.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
        } else if (cc == "input 3") {
            button3.setBackgroundColor(Color.rgb(255, 0, 0));
            button2.setEnabled(false);
            button1.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
        } else if (cc == "out3") {
            button3.setBackgroundColor(Color.rgb(255, 255, 0));
            button2.setEnabled(false);
            button1.setEnabled(false);
            button4.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
        } else if (cc == "input4") {
            button4.setBackgroundColor(Color.rgb(255, 0, 0));
            button2.setEnabled(false);
            button3.setEnabled(false);
            button1.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
        } else if (cc == "out4") {
            button4.setBackgroundColor(Color.rgb(255, 255, 0));
            button2.setEnabled(false);
            button3.setEnabled(false);
            button1.setEnabled(false);
            button5.setEnabled(false);
            button6.setEnabled(false);
            button7.setEnabled(false);
            button8.setEnabled(false);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //처음에 원하는 자리를 누르면
                AlertDialog.Builder builder = new AlertDialog.Builder(SeatreActivity.this); //Dialog출력
                builder.setTitle("1번 자리"); //Dialog 타이틀은 1번자리
                builder.setPositiveButton("예약", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int id) { //Dialog 버튼 중 하나 예약버튼
                        SharedPreferences out = getSharedPreferences("outcheck", 0); //공유구문 ~4줄
                        SharedPreferences.Editor outche = out.edit();
                        outche.putString("check", "input1"); //check라는 키값에 input1을 넣어 공유
                        outche.commit(); //공유완료
                        mThreadConnectedBluetooth.write(seat);
                        in(button1);
                        button2.setEnabled(false); //나머지 버튼 비활성화
                        button3.setEnabled(false);
                        button4.setEnabled(false);
                        deact(button5, button6, button7, button8);
                    }
                });
                builder.setNegativeButton("반납", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        out(button1);
                        SharedPreferences out = getSharedPreferences("outcheck", 0);
                        SharedPreferences.Editor outche = out.edit();
                        outche.putString("check", "go");
                        outche.commit();
                        button2.setEnabled(true);
                        button3.setEnabled(true);
                        button4.setEnabled(true);
                        react(button5, button6, button7, button8);
                    }
                });
                builder.setNeutralButton("외출", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String cc = out.getString("check", "go1"); // 키값을 다시 읽어온다.
                        if (cc != "input1") { //읽어온 키값이 input1이 아니라면 → 아직 예약이 안되어있는 상태라면
                            Toast.makeText(getApplicationContext(), "예약을 먼저 진행해주세요.", Toast.LENGTH_SHORT).show(); //예약먼저 진행해달라는 메시지 출력
                        } else if (cc == "input1") { //읽어온 키값이 input1이라면 → 예약이 되어있는 상태라면
                            //외출기능 실행
                            rest(button1);
                            SharedPreferences out = getSharedPreferences("outcheck", 0);
                            SharedPreferences.Editor outche = out.edit();
                            outche.putString("check", "out1");
                            outche.commit();
                            mThreadConnectedBluetooth.write(seatout); //seatout 스트링값 아두이노로 전송
                            button2.setEnabled(false);
                            button3.setEnabled(false);
                            button4.setEnabled(false);
                            deact(button5, button6, button7, button8); //버튼 비활성화
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SeatreActivity.this);
                builder.setTitle("2번 자리");
                builder.setPositiveButton("예약", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        in(button2);
                        SharedPreferences out = getSharedPreferences("outcheck", 0); //공유구문 ~4줄
                        SharedPreferences.Editor outche = out.edit();
                        outche.putString("check", "input2"); //check라는 키값에 input1을 넣어 공유
                        outche.commit(); //공유완료
                        mThreadConnectedBluetooth.write(seat);
                        button1.setEnabled(false);
                        button3.setEnabled(false);
                        button4.setEnabled(false);
                        deact(button5, button6, button7, button8);
                    }
                });

                builder.setNegativeButton("반납", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        out(button2);
                        SharedPreferences out = getSharedPreferences("outcheck", 0);
                        SharedPreferences.Editor outche = out.edit();
                        outche.putString("check", "go");
                        outche.commit();
                        button1.setEnabled(true);
                        button3.setEnabled(true);
                        button4.setEnabled(true);
                        react(button5, button6, button7, button8);
                    }
                });
                builder.setNeutralButton("외출", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String cc = out.getString("check", "go2"); // 키값을 다시 읽어온다.
                        if (cc != "input2") { //읽어온 키값이 input2이 아니라면 → 아직 예약이 안되어있는 상태라면
                            Toast.makeText(getApplicationContext(), "예약을 먼저 진행해주세요.", Toast.LENGTH_SHORT).show(); //예약먼저 진행해달라는 메시지 출력
                        } else if (cc == "input2") { //읽어온 키값이 input2이라면 → 예약이 되어있는 상태라면
                            //외출기능 실행
                            rest(button2);
                            SharedPreferences out = getSharedPreferences("outcheck", 0);
                            SharedPreferences.Editor outche = out.edit();
                            outche.putString("check", "out2");
                            outche.commit();
                            mThreadConnectedBluetooth.write(seatout); //seatout 스트링값 아두이노로 전송
                            button1.setEnabled(false);
                            button3.setEnabled(false);
                            button4.setEnabled(false);
                            deact(button5, button6, button7, button8);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SeatreActivity.this);
                builder.setTitle("3번 자리");
                builder.setPositiveButton("예약", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        in(button3);
                        SharedPreferences out = getSharedPreferences("outcheck", 0); //공유구문 ~4줄
                        SharedPreferences.Editor outche = out.edit();
                        outche.putString("check", "input3"); //check라는 키값에 input1을 넣어 공유
                        outche.commit(); //공유완료
                        mThreadConnectedBluetooth.write(seat);
                        button2.setEnabled(false);
                        button1.setEnabled(false);
                        button4.setEnabled(false);
                        deact(button5, button6, button7, button8);
                    }
                });

                builder.setNegativeButton("반납", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        out(button3);
                        SharedPreferences out = getSharedPreferences("outcheck", 0);
                        SharedPreferences.Editor outche = out.edit();
                        outche.putString("check", "go");
                        outche.commit();
                        button2.setEnabled(true);
                        button1.setEnabled(true);
                        button4.setEnabled(true);
                        react(button5, button6, button7, button8);
                    }
                });
                builder.setNeutralButton("외출", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String cc = out.getString("check", "go3"); // 키값을 다시 읽어온다.
                        if (cc != "input3") { //읽어온 키값이 input3이 아니라면 → 아직 예약이 안되어있는 상태라면
                            Toast.makeText(getApplicationContext(), "예약을 먼저 진행해주세요.", Toast.LENGTH_SHORT).show(); //예약먼저 진행해달라는 메시지 출력
                        } else if (cc == "input3") { //읽어온 키값이 input3이라면 → 예약이 되어있는 상태라면
                            //외출기능 실행
                            rest(button3);
                            SharedPreferences out = getSharedPreferences("outcheck", 0);
                            SharedPreferences.Editor outche = out.edit();
                            outche.putString("check", "out3");
                            outche.commit();
                            mThreadConnectedBluetooth.write(seatout); //seatout 스트링값 아두이노로 전송
                            button2.setEnabled(false);
                            button1.setEnabled(false);
                            button4.setEnabled(false);
                            deact(button5, button6, button7, button8);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SeatreActivity.this);
                builder.setTitle("4번 자리");
                builder.setPositiveButton("예약", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        in(button4);
                        SharedPreferences out = getSharedPreferences("outcheck", 0); //공유구문 ~4줄
                        SharedPreferences.Editor outche = out.edit();
                        outche.putString("check", "input4"); //check라는 키값에 input1을 넣어 공유
                        outche.commit(); //공유완료
                        mThreadConnectedBluetooth.write(seat);
                        button2.setEnabled(false);
                        button3.setEnabled(false);
                        button1.setEnabled(false);
                        deact(button5, button6, button7, button8);
                    }
                });

                builder.setNegativeButton("반납", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        out(button4);
                        SharedPreferences out = getSharedPreferences("outcheck", 0);
                        SharedPreferences.Editor outche = out.edit();
                        outche.putString("check", "go");
                        outche.commit();
                        button2.setEnabled(true);
                        button3.setEnabled(true);
                        button1.setEnabled(true);
                        react(button5, button6, button7, button8);
                    }
                });
                builder.setNeutralButton("외출", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        final String cc = out.getString("check", "go4"); // 키값을 다시 읽어온다.
                        if (cc != "input4") { //읽어온 키값이 input4이 아니라면 → 아직 예약이 안되어있는 상태라면
                            Toast.makeText(getApplicationContext(), "예약을 먼저 진행해주세요.", Toast.LENGTH_SHORT).show(); //예약먼저 진행해달라는 메시지 출력
                        } else if (cc == "input4") { //읽어온 키값이 input4이라면 → 예약이 되어있는 상태라면
                            //외출기능 실행
                            rest(button4);
                            SharedPreferences out = getSharedPreferences("outcheck", 0);
                            SharedPreferences.Editor outche = out.edit();
                            outche.putString("check", "out4");
                            outche.commit();
                            mThreadConnectedBluetooth.write(seatout); //seatout 스트링값 아두이노로 전송
                            button2.setEnabled(false);
                            button3.setEnabled(false);
                            button1.setEnabled(false);
                            deact(button5, button6, button7, button8);
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        bt_list.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPairedDevices();
            }
        });

        bt_on.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothOn();
            }
        });

        bt_off.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothOff();
            }
        });

        mBluetoothHandler = new
                Handler() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @SuppressLint("HandlerLeak")
                    public void handleMessage(Message msg) {
                        SharedPreferences out = getSharedPreferences("outcheck", 0);
                        final String cc = out.getString("check", "go1");
                        if (msg.what == BT_MESSAGE_READ) {
                            String readMessage = null;
                            String a = "a";
                            String b = "b";
                            byte[] readbuf = (byte[]) msg.obj;
                            readMessage = new String(readbuf, 0, msg.arg1);
                            Toast.makeText(getApplicationContext(), "msg: " + readMessage, Toast.LENGTH_SHORT).show();
                            if (readMessage.equals(a)) { //받은 메세지가 a일때
                                if (cc == "input1") { //1번자리가 예약되어있다면
                                    out(button1);
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "go");
                                    outche.commit();
                                    button2.setEnabled(true);
                                    button3.setEnabled(true);
                                    button4.setEnabled(true);
                                    react(button5, button6, button7, button8);
                                    push();
                                } else if (cc == "input2") { //2번자리가 예약되어있다면
                                    out(button2);
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "go");
                                    outche.commit();
                                    button1.setEnabled(true);
                                    button3.setEnabled(true);
                                    button4.setEnabled(true);
                                    react(button5, button6, button7, button8);
                                    push();
                                } else if (cc == "input3") {// 3번자리가 예약되어있다면
                                    out(button3);
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "go");
                                    outche.commit();
                                    button2.setEnabled(true);
                                    button1.setEnabled(true);
                                    button4.setEnabled(true);
                                    react(button5, button6, button7, button8);
                                    push();
                                } else if (cc == "input4") { //4번자리가 예약되어있다면
                                    out(button4);
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "go");
                                    outche.commit();
                                    button2.setEnabled(true);
                                    button3.setEnabled(true);
                                    button1.setEnabled(true);
                                    react(button5, button6, button7, button8);
                                    push();
                                }
                                else if (cc == "out1") { //1번자리가 외출되어있다면
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "go"); //check라는 키값에 input1을 넣어 공유
                                    outche.commit(); //공유완료
                                    out(button1);
                                    button2.setEnabled(true);
                                    button3.setEnabled(true);
                                    button4.setEnabled(true);
                                    react(button5, button6, button7, button8);
                                    push();
                                } else if (cc == "out2") { //2번자리가 외출되어있다면
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "go"); //check라는 키값에 input2을 넣어 공유
                                    outche.commit(); //공유완료
                                    out(button2);
                                    button1.setEnabled(true);
                                    button3.setEnabled(true);
                                    button4.setEnabled(true);
                                    react(button5, button6, button7, button8);
                                    push();
                                } else if (cc == "out3") { //3번자리가 외출되어있다면
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "go"); //check라는 키값에 input3을 넣어 공유
                                    outche.commit(); //공유완료
                                    out(button3);
                                    button2.setEnabled(true);
                                    button1.setEnabled(true);
                                    button4.setEnabled(true);
                                    react(button5, button6, button7, button8);
                                    push();
                                } else if (cc == "out4") { //4번자리가 외출되어있다면
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "go"); //check라는 키값에 input4을 넣어 공유
                                    outche.commit(); //공유완료
                                    out(button4);
                                    button2.setEnabled(true);
                                    button3.setEnabled(true);
                                    button1.setEnabled(true);
                                    react(button5, button6, button7, button8);
                                    push();
                                }
                            } else if (readMessage.equals(b)) { //받은 메세지가 b일때
                                if (cc == "out1") { //1번자리가 외출되어있다면
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "input1"); //check라는 키값에 input1을 넣어 공유
                                    outche.commit(); //공유완료
                                    in(button1);
                                    button2.setEnabled(false); //나머지 버튼 비활성화
                                    button3.setEnabled(false);
                                    button4.setEnabled(false);
                                    deact(button5, button6, button7, button8);
                                } else if (cc == "out2") { //2번자리가 외출되어있다면
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "input2"); //check라는 키값에 input2을 넣어 공유
                                    outche.commit(); //공유완료
                                    in(button2);
                                    button1.setEnabled(false); //나머지 버튼 비활성화
                                    button3.setEnabled(false);
                                    button4.setEnabled(false);
                                    deact(button5, button6, button7, button8);
                                } else if (cc == "out3") { //3번자리가 외출되어있다면
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "input3"); //check라는 키값에 input3을 넣어 공유
                                    outche.commit(); //공유완료
                                    in(button3);
                                    button2.setEnabled(false); //나머지 버튼 비활성화
                                    button1.setEnabled(false);
                                    button4.setEnabled(false);
                                    deact(button5, button6, button7, button8);
                                } else if (cc == "out4") { //4번자리가 외출되어있다면면
                                    SharedPreferences.Editor outche = out.edit();
                                    outche.putString("check", "input4"); //check라는 키값에 input4을 넣어 공유
                                    outche.commit(); //공유완료
                                    in(button4);
                                    button2.setEnabled(false); //나머지 버튼 비활성화
                                    button3.setEnabled(false);
                                    button1.setEnabled(false);
                                    deact(button5, button6, button7, button8);
                                }
                            }
                        }
                    }
                };
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void push() {
        intent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), OutActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.desk) // 아이콘 설정하지 않으면 오류남
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("KPU 열람실") // 제목 설정
                .setContentText("예약하신 자리가 반납되었습니다.") // 내용 설정
                .setTicker("열람실 자리 반납") // 상태바에 표시될 한줄 출력
                .setAutoCancel(true)
                .setContentIntent(intent);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    void deact(Button button5, Button button6, Button button7, Button button8) { //5-8번 버튼 비활성화
        button5.setEnabled(false);
        button6.setEnabled(false);
        button7.setEnabled(false);
        button8.setEnabled(false);
    }

    void react(Button button5, Button button6, Button button7, Button button8) { //5-8번 버튼 활성화
        button5.setEnabled(true);
        button6.setEnabled(true);
        button7.setEnabled(true);
        button8.setEnabled(true);
    }

    void in(Button button) { //예약완료 버튼 변화
        button.setBackgroundColor(Color.rgb(255, 0, 0)); //버튼 배경 빨강색으로 변화
        Toast.makeText(getApplicationContext(), "예약완료", Toast.LENGTH_SHORT).show(); //예약완료라는 토스트메시지 출력
    }

    void out(Button button) { //반납완료 버튼 변화
        button.setBackgroundColor(Color.rgb(220, 220, 220)); //회색
        Toast.makeText(getApplicationContext(), "반납완료", Toast.LENGTH_SHORT).show();
    }

    void rest(Button button) { //외출완료 버튼 변화
        button.setBackgroundColor(Color.rgb(255, 255, 0));
        Toast.makeText(getApplicationContext(), "외출완료", Toast.LENGTH_SHORT).show();
    }

    void bluetoothOn() { //블루투스 활성화
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }
        }
    }

    void bluetoothOff() { //블루투스 비활성화
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("자리번호에 맞는 번호를 선택하세요.");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int item) {
                        try {
                            connectSelectedDevice(items[item].toString());//, isThread);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    void connectSelectedDevice(final String selectedDeviceName) throws IOException {//, final boolean isThread) {
        Log.d("selectedDeviceName", "--------" + selectedDeviceName + "--------");
        Log.d("connectSelectedDevice", "-------디바이스 선택하고 연결하는 함수 실행-------");
        for (BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
            }
        }
        try {
            Log.d("connecting", "--------디바이스 연결 중--------");
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            Toast.makeText(getApplicationContext(), selectedDeviceName+"가 연결되었습니다.", Toast.LENGTH_SHORT).show();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 사용 오류", Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}