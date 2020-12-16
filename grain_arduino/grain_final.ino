#include <SPI.h>
#include <Phpoc.h>
#include <Servo.h>

//접속주소.
char host[] = "lccandol.cafe24.com";
PhpocClient client;

//플래그. - 인터넷 접속할때까지 대기 위해 필요.
int send_state_flag = 1;
int download_state_flag = 1;
int ultrasonic_flag = 1;
int working_state_flag = 1;

/*
   0:폰에서 작업시작 명령 가능(대기상태, 2일때 폰에서 알림을 울리고 0으로 변경)
   1:작업시작(1일때 아두이노 동작)
   2:작업 완료(작업이 끝나면 2로 변경.)
*/
//상태 플래그.
int state_flag = 0;


//가져온 데이터.
String stringOne = "0000";


Servo myservo1;
Servo myservo2;
Servo myservo3;

//핀 설정.
const int servo1Pin = 5;
const int servo2Pin = 6;
const int servo3Pin = 7;

//벨브
const int valve1Pin = 1;
const int valve2Pin = 2;

//방수모터
const int motorPin = 3;

//회전모터
const int rotationPin = 4;

void setup() {


  //쌀통 측정 핀 설정.
  for (int i = 14; i < 20 ; i++) {
    if (i % 2 == 0) {
      pinMode(i, INPUT);
    } else {
      pinMode(i, OUTPUT);
    }
  }



  //인터넷 접속 시작.
  //Serial.println("Sending GET request to web server");
  //Phpoc.begin(PF_LOG_SPI | PF_LOG_NET);
  Phpoc.begin();
  delay(1000);


  //서보모터..3
  myservo1.attach(servo1Pin);
  myservo2.attach(servo2Pin);
  myservo3.attach(servo3Pin);

  //0도로 초기화.
  myservo1.write(0);
  myservo2.write(6);
  myservo3.write(0);


  //벨브
  pinMode(valve1Pin, OUTPUT);
  digitalWrite(valve1Pin, 1);
  pinMode(valve2Pin, OUTPUT);
  digitalWrite(valve2Pin, 1);

  //방수모터
  pinMode(motorPin, OUTPUT);
  digitalWrite(motorPin, 1);

  //회전모터
  pinMode(rotationPin, OUTPUT);
  digitalWrite(rotationPin, 1);



}

void loop() {



  //쌀통 높이 측정 및 데이터 전송.
  sendUltrasonicData(ultrasonic(14, 15), ultrasonic(16, 17), ultrasonic(18, 19));

  //1초 휴지.
  delay(1000);

  //상태 파악후 동작.
  switch (getStateData()) {

    case 0: {

        //대기상태, 동작 없음.
        break;
      }
    case 1: {

        //작업상태, 모터및 씻기 동작.

        //작업 data 받아오기.
        getState2WorkingData();


        int getCount = topServoMotorWorkingAndGetCount();
        //2초 쉬고.
        delay(2000);

        for (int i = 0; i < getCount; i++) {
          valveAndBottomMotorWorking();
          delay(2000);
        }





        //작업 완료 상태로 변경.
        state_flag = 2;
        //쌀 정보 초기화.
        stringOne = "0000";
        //상태정보 전송하기.
        sendStateData(state_flag);

        break;
      }
    case 2: {

        //작업완료, 동작없음.

        break;
      }
  }





  //2초 휴지.
  delay(2000);



}

//쌀 횟수및 세척 횟수 받아오기.
void getState2WorkingData() {

  String working = "";

  //연결 성공시. get requeset.
  if (client.connect(host, 80))
  {

    client.print("GET /download_state_working.php?id_num=2018");
    client.println(" HTTP/1.1");
    client.println("Host: lccandol.cafe24.com\n");


    //데이터 받아오기.
    while (working_state_flag) {
      while (client.available())
      {
        //한글자씩 받아오기.
        char c = client.read();
        //Serial.print(c);
        if (c == "\n")
          working = "";

        working += c;

        //모든데이터를 다 받았을때.
        if (!client.available()) {
          //해당 루프안에서 c 가 받아온 값.

          stringOne = working.substring(working.length() - 4, working.length());


        }
        working_state_flag = 0;
      }

    }

    //데이터 받기 끝나면 접속종료.
    client.stop();
    working_state_flag = 1;


  }


}
//상태정보 변경하기.
void sendStateData(int updateState) {
  //연결 성공시. get requeset.
  if (client.connect(host, 80))
  {

    client.print("GET /state_upload.php?id_num=2018&state=");
    client.print(updateState);
    client.println(" HTTP/1.1");
    client.println("Host: lccandol.cafe24.com\n");


    //데이터 받아오기.
    while (send_state_flag) {
      while (client.available())
      {
        //한글자씩 받아오기.
        char c = client.read();
        //Serial.print(c);

        /*모든데이터를 다 받았을때.
          if (!client.available()) {
          //해당 루프안에서 c 가 받아온 값.
          Serial.print("resultData : ");
          Serial.println(c);

          //값이 1인경우. - ok
          if (strcmp(c, '1') == 0)
            Serial.println("ok");
          else
            Serial.println("no");
          }*/
        send_state_flag = 0;
      }

    }

    //데이터 받기 끝나면 접속종료.
    client.stop();
    send_state_flag = 1;


  }

}
//상태정보 받아오기.
int getStateData() {

  //연결 성공시. get requeset.
  if (client.connect(host, 80))
  {

    client.print("GET /download_state.php?id_num=2018");
    client.println(" HTTP/1.1");
    client.println("Host: lccandol.cafe24.com\n");


    //데이터 받아오기.
    while (download_state_flag) {
      while (client.available())
      {
        //한글자씩 받아오기.
        char c = client.read();
        //Serial.print(c);

        //모든데이터를 다 받았을때.
        if (!client.available()) {
          //해당 루프안에서 c 가 받아온 값.

          //상태 설정. 한자리수는 '0'을 빼면 int로 변환.
          state_flag = c - '0';
          //Serial.println(state_flag);

        }
        download_state_flag = 0;
      }

    }

    //데이터 받기 끝나면 접속종료.
    client.stop();
    download_state_flag = 1;


  }


  //TODO:상태정보 리턴..
  //test : return 1;
  return state_flag;
}
//쌀통 깊이 정보 전송.
void sendUltrasonicData(int box1, int box2, int box3) {

  //연결 성공시. get requeset.
  if (client.connect(host, 80))
  {

    client.print("GET /ultrasonic_upload.php?id_num=2018&box1=");
    client.print(box1);
    client.print("&box2=");
    client.print(box2);
    client.print("&box3=");
    client.print(box3);
    client.println(" HTTP/1.1");
    client.println("Host: lccandol.cafe24.com\n");


    //데이터 받아오기.
    while (ultrasonic_flag) {
      while (client.available())
      {
        //한글자씩 받아오기.
        char c = client.read();
        //Serial.print(c);

        /*모든데이터를 다 받았을때.
          if (!client.available()) {
          //해당 루프안에서 c 가 받아온 값.
          Serial.print("result : ");
          Serial.println(c);

          //값이 1인경우.
          if (strcmp(c, '1') == 0)
            Serial.println("ok");
          else
            Serial.println("no");
          }*/
        ultrasonic_flag = 0;
      }

    }

    //데이터 받기 끝나면 접속종료.
    client.stop();
    ultrasonic_flag = 1;


  }

}
//쌀통 깊이 층정.
int ultrasonic (int echo, int trig) {
  //14,15 ; 16,17 ; 18,19

  int dur, dis;

  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);

  dur = pulseIn(echo, HIGH);
  dis = dur / 58;

  //1초 stop
  delay(1000);


  return dis;
}
//상단 서보모터 동작.
int topServoMotorWorkingAndGetCount() {

  //서보모터 회전 - 쌀 옮기기.
  int cycleCount = setServoRotateAndGetCount();

  return cycleCount;

}
//벨브및 세척모터 동작.
void valveAndBottomMotorWorking() {

  //2번 벨브 동작.
  //0일때 동작.
  digitalWrite(valve1Pin, 0);
  delay(17000);
  //1일때 멈춤
  digitalWrite(valve1Pin, 1);
  delay(2000);


  //모터동작.
  //0일때 동작.
  //회전 모터 동작.
  for (int i = 0 ; i < 2 ; i++) {
    digitalWrite(motorPin, 0);
    delay(5000);
    //1일때 멈춤
    digitalWrite(motorPin, 1);
    delay(2000);
    digitalWrite(rotationPin, 0);
    delay(3000);
    digitalWrite(rotationPin, 1);
    delay(2000);
  }
  delay(2000);

  //3번 벨브 동작.
  //0일때 동작.
  digitalWrite(valve2Pin, 0);
  delay(30000);
  //1일때 멈춤
  digitalWrite(valve2Pin, 1);
  delay(2000);




}
//서보모터 회전, 씻기 횟수 리턴.
int setServoRotateAndGetCount() {

  //가져온 값. stringOne

  String box1 = stringOne.substring(0, 1);
  String box2 = stringOne.substring(1, 2);
  String box3 = stringOne.substring(2, 3);
  String box4 = stringOne.substring(3, 4);


  int x = box1.toInt();
  int y = box2.toInt();
  int z = box3.toInt();
  int count = box4.toInt(); //씻기 횟수.


  //서보모터 회전 - 쌀 옮기기.
  roateMoto(x, myservo1,0);
  delay(1500);
  roateMoto(y, myservo2,6);
  delay(1500);
  roateMoto(z, myservo3,0);
  delay(1500);

  return count;
}
//서보모터 회전.
void roateMoto(int count, Servo moto, int degree) {

  for (int i = 0 ; i < count; i++) {

    moto.write(180);
    delay(1000);

    moto.write(degree);
    
    delay(1000);

  }

}


