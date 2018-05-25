//Libreria para accder a la memoria EEPROM del Arduino
#include <EEPROM.h>
#include <Servo.h>

//---SERVO---
Servo servo_A, servo_B, servo_C;
int val_A, val_B, val_C;

//---A PASOS---
int retardo = 5;        // Tiempo de retardo en milisegundos (Velocidad del Motor)
int dato_rx;            // Valor recibido en grados
int numero_pasos = 0;   // Valor en grados donde se encuentra el motor
char c;

//---EEPROM---
int contador = 0;
String cadena;

void setup() {                
    Serial.begin(9600);     // inicializamos el puerto serie a 9600 baudios
    //---SERVO---
    servo_A.attach(3);
    servo_B.attach(4);
    servo_C.attach(5);
    //---A PASOS---
    pinMode(11, OUTPUT);    // Pin 11 conectar a IN4
    pinMode(10, OUTPUT);    // Pin 10 conectar a IN3
    pinMode(9, OUTPUT);     // Pin 9 conectar a IN2
    pinMode(8, OUTPUT);     // Pin 8 conectar a IN1
    //led
    pinMode(2, INPUT_PULLUP);
    attachInterrupt(digitalPinToInterrupt(2), blink, CHANGE);
    pinMode(6, OUTPUT);
    pinMode(7, OUTPUT);
    pinMode(12, OUTPUT);
    pinMode(13, OUTPUT);
    
}

void loop() {
    while (Serial.available()) {    // Leer el valor enviado por el Puerto serial
        c  = Serial.read();
        cadena += c;
        dato_rx = Serial.parseInt();   // Convierte Cadena de caracteres a Enteros
    } 
    switch(c){
        //---SERVO---
		//recibe la orden 'A' por parte de la interfaz o el modulo bluetooth, enciende el led y gira el servo
        case 'A':
            val_A = dato_rx;
            digitalWrite(6,HIGH);
            digitalWrite(7,LOW);
            digitalWrite(12,LOW);
            digitalWrite(13,LOW);
            EEPROM.write(contador, cadena);
            contador++;
            break;
		//recibe la orden 'B' por parte de la interfaz o el modulo bluetooth, enciende el led y gira el servo
        case 'B':
            val_B = dato_rx;
            digitalWrite(6,LOW);
            digitalWrite(7,HIGH);
            digitalWrite(12,LOW);
            digitalWrite(13,LOW);
            EEPROM.write(contador, cadena);
            contador++;
            break;
		//recibe la orden 'C' por parte de la interfaz o el modulo bluetooth, enciende el led y gira el servo
        case 'C':
            val_C = dato_rx;
            digitalWrite(6,LOW);
            digitalWrite(7,LOW);
            digitalWrite(12,HIGH);
            digitalWrite(13,LOW);
            EEPROM.write(contador, cadena);
            contador++;
            break;
        //---A PASOS---  
		//recibe l orden 'D' por parte de la interfaz o el modulo bluetooth, enciende el led y gira el motor a pasos
        case 'D':
            delay(retardo);
            digitalWrite(6,LOW);
            digitalWrite(7,LOW);
            digitalWrite(12,LOW);
            digitalWrite(13,HIGH);
            while (dato_rx > numero_pasos){   // Giro hacia la izquierda en grados
                paso_izq();
                numero_pasos = numero_pasos + 1;
            }
            while (dato_rx < numero_pasos){   // Giro hacia la derecha en grados
                paso_der();
                numero_pasos = numero_pasos -1;
            }
			escribe movimiento en la EEPROM
            EEPROM.write(contador, cadena);
            contador++;
            break;
		//Regresa el brazo a su posicion original
        case 'E':
                val_A = 0;
                delay(2000);
                val_B = 0;
                delay(2000);
                val_C = 0;
                delay(2000);
                c = 'D';
                dato_rx = 0;
                break;
		//Lee la memoria EEPROM del arduino para jeecutar las ultimas ordenes recibidas de manera manual
        case 'F':
                for(int i = 0; i < EEPROM.length(); i++){
                    switch(EEPROM.read(i).substring(0)){
                        //---SERVO---
                        case 'A':
                            val_A = EEPROM.read(i);
                            digitalWrite(6,HIGH);
                            digitalWrite(7,LOW);
                            digitalWrite(12,LOW);
                            digitalWrite(13,LOW);
                            break;
                        case 'B':
                            val_B = EEPROM.read(i);
                            digitalWrite(6,LOW);
                            digitalWrite(7,HIGH);
                            digitalWrite(12,LOW);
                            digitalWrite(13,LOW);
                            break;
                        case 'C':
                            val_C = EEPROM.read(i);
                            digitalWrite(6,LOW);
                            digitalWrite(7,LOW);
                            digitalWrite(12,HIGH);
                            digitalWrite(13,LOW);
                            break;
                        //---A PASOS---    
                        case 'D':
                            delay(retardo);
                            digitalWrite(6,LOW);
                            digitalWrite(7,LOW);
                            digitalWrite(12,LOW);
                            digitalWrite(13,HIGH);
                            while (EEPROM.read(i) > numero_pasos){   // Giro hacia la izquierda en grados
                                paso_izq();
                                numero_pasos = numero_pasos + 1;
                            }
                            while (EEPROM.read(i)< numero_pasos){   // Giro hacia la derecha en grados
                                paso_der();
                                numero_pasos = numero_pasos -1;
                            }
                            break;
                    }
                        servo_A.write(val_A);  
                        servo_B.write(val_B); 
                        servo_C.write(val_C);
                        apagado();
                }

    }
    //---SERVO---
	//manda a cada servo el vlor en grados para girar
    servo_A.write(val_A);  
    servo_B.write(val_B); 
    servo_C.write(val_C);
    apagado();
}  

void paso_der(){         // Pasos a la derecha
    digitalWrite(11, LOW); 
    digitalWrite(10, LOW);  
    digitalWrite(9, HIGH);  
    digitalWrite(8, HIGH);  
    delay(retardo); 
    digitalWrite(11, LOW); 
    digitalWrite(10, HIGH);  
    digitalWrite(9, HIGH);  
    digitalWrite(8, LOW);  
    delay(retardo); 
    digitalWrite(11, HIGH); 
    digitalWrite(10, HIGH);  
    digitalWrite(9, LOW);  
    digitalWrite(8, LOW);  
    delay(retardo); 
    digitalWrite(11, HIGH); 
    digitalWrite(10, LOW);  
    digitalWrite(9, LOW);  
    digitalWrite(8, HIGH);  
    delay(retardo);  
}

void paso_izq() {        // Pasos a la izquierda
    digitalWrite(11, HIGH); 
    digitalWrite(10, HIGH);  
    digitalWrite(9, LOW);  
    digitalWrite(8, LOW);  
    delay(retardo); 
    digitalWrite(11, LOW); 
    digitalWrite(10, HIGH);  
    digitalWrite(9, HIGH);  
    digitalWrite(8, LOW);  
    delay(retardo); 
    digitalWrite(11, LOW); 
    digitalWrite(10, LOW);  
    digitalWrite(9, HIGH);  
    digitalWrite(8, HIGH);  
    delay(retardo); 
    digitalWrite(11, HIGH); 
    digitalWrite(10, LOW);  
    digitalWrite(9, LOW);  
    digitalWrite(8, HIGH);  
    delay(retardo); 
}
        
void apagado() {         // Apagado del Motor
    digitalWrite(11, LOW); 
    digitalWrite(10, LOW);  
    digitalWrite(9, LOW);  
    digitalWrite(8, LOW);  
}

void blink(){
  servo_A.write(0);
}

