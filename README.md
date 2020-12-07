# _캡스톤디자인_
## KPU
- 안드로이드 어플리케이션 개발환경
    |구분|내용|
    |:--------:|:----------:|
    |OS|Windows10 education|
    |Language|Java, php|
    |IDE|Android Studio 4.0|
    |서버 호스팅|닷홈|
    |Database|mysql|
    |장비|HP PAVILION 13|
- RegisterRequest<br>
회원등록요청부분 - Register.php파일과 연동
- LoginRequest<br>
로그인요청부분 - Login.php파일과 연동
- SeatreActivity<br>
자리예약
- MainActivity<br>
로그인
- RegisterActivity<br>
회원가입
- LoginSucActivity<br>
로그인 완료되어 열람실예약 버튼이 띄움
- OutActivity<br>
반납되면 푸시알람

## re_arduino
- 아두이노 개발환경
    |구분|내용|
    |:---------:|:------------------:|
    |OS|Windows 10 education|
    |Language|C|
    |IDE|Arduino IDE|
    |장비|LG그램|
- 작동
  1. 안드로이드 어플리케이션에서 수신된 값('a')이 유효하면 적외선센서 작동
  2. 적외선값이 유효하고 일정무게가 감지되면 예약완료
  3. 예약완료됨과 동시에 while()로 인해 계속해서 안드로이드로 값 전송
  3-1. 안드로이드에서 외출버튼을 클릭할 시 'b'라는 메세지를 받아 외출카운터 작동
  3-2. 무게감지값이 무효값이 되면 자동 반납카운터 작동 후 반납
  4. 외출카운터가 종료시 반납카운터 작동 후 반납 / 외출카운터 종료전 무게가 감지되면 다시 자동 예약(안드로이드로 'b'전송)
  