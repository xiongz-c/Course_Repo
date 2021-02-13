# 2.4G Communication

## Objective 

The main purpose of this term project is to implement two MiniSTM32 boards to communicate with each other through the NRF24L01 module. The specific communication method is entering message from serial port debugging assistant through the Universal Asynchronous Receiver/Transmitter(UART) on the STM32 board and transmitting the message to another board by 2.4G channel.

## Project Logic

### Display Interface

Four GUI design. See report

### LED status

We define four state of LED to show our state of connection. 

* **all lights closed**, Communication system closed. 

* **Red light** is always on, the communication system is on, but not connected. 

* **Green light** is always on, the communication system is on, but not connected. Flashing red, 2.4G module not found. 

  We also control the LEDs to show the connection status by GPIO. If the connection is closed, the LED will turn Red like the following figure.

### Communication Logic

#### Channel setting

2.4G communication is based on channel broadcast messages.  We provide channel setting options by modifying some source code of library functions.

#### Heart-beat

To make sure your partner is still alive, we send  a special heartbeat detection packet "\0".

#### TX & RX

In terms of messaging, our team uses library functions that have been changed according to our team’s needs. In each cycle, a judgment of receiving and sending is performed.

* If a message is received, we will determine whether it is a heartbeat detection package, 
  * if it is, ignore it, 
  * otherwise it will be printed on the LCD display.

### Operating instructions

See report