byte ledPin = 13;
byte inputPin = 2;
int delayBarrier = 6000;
boolean barrierBroken = false;
byte incomingByte;

void setup() {
  pinMode(ledPin, OUTPUT); //инициализация 13 пина как порт вывода
  pinMode(inputPin, INPUT);//инициализация 2 пина как порт ввода
  Serial.begin(9600);      //установка скорости последовательного порта
}

void loop() {
  if (Serial.available() > 0) {
    incomingByte = Serial.read(); //считывание команды с сервера/ПК
    if(incomingByte == '2'){
      digitalWrite(ledPin, HIGH); //включение светодиода   

      do{
        delay(delayBarrier);              //задержка на проезд автомобиля (необходимо корректировать)
      }
      while(isBarrierBroken());   //цикл пока барьер разорван
      delay(delayBarrier);
      digitalWrite(ledPin, LOW);
    }
  }

  boolean bb = isBarrierBroken(); 
  if(bb && (barrierBroken != bb)){
    Serial.write("1\n");
  }

  barrierBroken = bb;
}

boolean isBarrierBroken(){
  boolean temp = (digitalRead(inputPin) == LOW);//High если барьер нарушен
  delay(10);                                    //Программная защита 
  return temp & (digitalRead(inputPin) == LOW); //от дребезга контактов
}




