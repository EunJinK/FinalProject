#include "HX711.h" //HX711로드셀 엠프 관련함수 호출
//#include "Timer.h" //시간
#include <SoftwareSerial.h>
#define calibration_factor -50000.0 // 로드셀 스케일 값 선언 초기
#define DOUT  3 //엠프 데이터 아웃 핀 넘버 선언
#define CLK  2  //엠프 클락 핀 넘버 
HX711 scale(DOUT, CLK); //엠프 핀 선언 
SoftwareSerial hc06(4,5); //Tx, Rx

int ledRed = 9;      //  LED 핀 빨간색 (예약상탱 = 사람있음)
int ledGreen = 10;      //  LED 핀 초록색 (빈자리 = 사람없음)
int ledYellow = 11;
int inputPin = 7;     // 센서 신호핀
int pirState = LOW;   // 센서 초기상태는 움직임이 없음을 가정
int val = 0;          // 센서 신호의 판별을 위한 변수
int result;
int result2;
char msg;

int s, m; //시간 변수
int timer; // 타이머
int timer2;
int outcnt = 0; //외출가능한 시간 카운터변수
int yellow = 0; //LED변화 방지를 위한 변수

//a = 예약, b = 외출
void setup(){
    pinMode(ledRed, OUTPUT);    // LED를 출력으로 설정    
    pinMode(ledGreen, OUTPUT);  
    pinMode(ledYellow,OUTPUT);
    pinMode(inputPin, INPUT);    // 센서 Input 설정
    Serial.begin(9600);         // 시리얼 통신, 속도는 9600
    hc06.begin(9600);
    Serial.println("HX711 scale TEST");  
    scale.set_scale(calibration_factor);  //스케일 지정 
    scale.tare();  //스케일 설정
    Serial.println("Readings:");
}
int humancheck(){
  Serial.print("Reading: ");
  float kg = scale.get_units();
  kg*=2;//0.454;
  Serial.print(kg,2); 
  Serial.print(" kg"); 
  Serial.println();  
  delay(500);
  return kg;
}
 
void loop(){
  /*        GREEN ON          */
  digitalWrite(ledRed, LOW);            //초기상태(예약 전)
  digitalWrite(ledGreen, HIGH); 
  digitalWrite(ledYellow, LOW); 
  
  if(hc06.available()){                    // 블루투스값이 있을 때 → 사용자가 예약버튼을 눌렀을 경우
    msg=hc06.read();                       // 블루투스로 핸드폰의 값을 읽어서 msg에 저장
    Serial.write(msg);
    Serial.println();
    if(msg=='a')                                // 예약 버튼 눌렸을때 핸드폰에서 받아온 값이 a라면 
    {
      yellow = 0;
      Serial.println("Hi\n");
      val = digitalRead(inputPin);         // 적외선 센서 신호값을 읽어와서 val에 저장
      if(val==HIGH){                        //적외선 센서의 값이 유효하다면
        while(1){
          delay(2000);                      // 대기시간
          result=humancheck();             // 사용자 정의 함수 : 몸무게를 측정하는 함수
          /* 예약버튼을 누른 후 사람이 감지되지 않았을 때 */
          if(result<=0.5) {          
            Serial.println("Rebooking plz");
            digitalWrite(ledRed, LOW);      
            digitalWrite(ledGreen, HIGH);
            digitalWrite(ledYellow, LOW); 
            break;
          }
          /* 예약버튼을 누른 후 사람이 감지되었을 때 */
          else {               
             /*         예약완료          */
             /*        RED ON         */
             Serial.println("--Reservatioin complete--\n");
             while(1){ //예약된 상태에서 계속 사람감지            
               result=humancheck();
               msg=hc06.read();
               if(yellow == 0)
               {
                 digitalWrite(ledRed, HIGH);      
                 digitalWrite(ledGreen, LOW);
                 digitalWrite(ledYellow, LOW);
               }
               else if(yellow == 1)
               {
                 digitalWrite(ledRed, LOW);      
                 digitalWrite(ledGreen, LOW);
                 digitalWrite(ledYellow, HIGH);
               }
               Serial.print("KPU msg : ");
               Serial.println(msg);
               if(msg == 'b') //핸드폰에서 외출버튼을 눌렀을 시
               {
                outcnt = 0; //외출가능한 시간 카운터 초기화
                while(1)
                {
                  Serial.println("Stand by for 3s . . ."); //3초에 한번씩 사람이 돌아왔는지 체크함
                  /* Yellow ON */
                  digitalWrite(ledRed, LOW);      
                  digitalWrite(ledGreen, LOW); 
                  digitalWrite(ledYellow, HIGH);
                  delay(3000);
                  outcnt++; //외출가능한시간 카운터
                  Serial.print("outcnt = ");
                  Serial.println(outcnt);
                  result2=humancheck();
                  if(outcnt < 5) //외출가능한 시간안에서
                  {
                    if(result2 > 0.5) // 사람이 돌아왔다면
                    {
                      hc06.write('b'); //핸드폰으로 b를 전송
                      Serial.println("come back person ! ! \n");
                      break;
                    }
                  }
                  else if(outcnt >= 5) //외출가능한 시간동안 사람이 돌아오지 않는다면
                  {
                    Serial.println("Exceed leave time, Go Return");
                    yellow = 1; //break하고 나가면 LED가 빨강으로 바뀌는 것을 방지하기 위한 변수(LED노란색 ON)
                    break;
                  }
                }
               }
               else //계속 예약인 상태
               {
                 if (result <= 0.5) //예약이 된 상태에서 사람이 나갔을 시
                 {
                      Serial.println ("----person out----");
                      for(timer=10; timer>=0; timer--){ //타이머 시작
                          delay(1000);
                          m = timer / 6;  
                          s = timer % 6;
                          Serial.print(m);
                          Serial.print(":");
                          Serial.println(s);
                          timer2 = timer;
      
                          result=humancheck();
                          if (result >0.5){
                            hc06.write('b');
                            //외출 → 반납에서 cnt = 5일때 타이머 작동이 되므로 카운터작동 중 다시 사람이 왔을 시 어플상에 외출 → 예약 상태로 바꾸기 위한 데이터 전송
                            yellow = 0; //외출LED에서 카운터로 넘어갔을 때 다시 사람이 와서 무게센서를 감지할 때 LED상태를 예약상태로 변경하기 위한 변수(LED빨강ON)
                            break; //타이머가 작동하는 동안 다시 사람이 돌아왔을 시 타이머 중지(for문 나감)
                          }
                      }
                  
                      if (timer2 == 0) //타이머가 종료됐을 시
                      {
                        digitalWrite(ledRed, LOW);      
                        digitalWrite(ledGreen, HIGH); 
                        digitalWrite(ledYellow, LOW); 
                        Serial.println("Automatic RETURN"); //반납하여 No Person 구간으로 넘어감
                        hc06.write('a');  //핸드폰으로 a를 전송하여 반납기능 수행
                        break;
                      }
                 }    
               }
             }
              ////예약 했는데 사람 사라질 때////
              Serial.println("No Person");
              //hc06.write('a');  //핸드폰으로 a를 전송하여 반납기능 수행
              //////////////////////////////
          }
        }
      }
      else{              //적외선 신호값이 유효하지 않다면
        digitalWrite(ledRed, LOW);      
        digitalWrite(ledGreen, HIGH);
        digitalWrite(ledYellow, LOW);      
      }
    }
  }
}
