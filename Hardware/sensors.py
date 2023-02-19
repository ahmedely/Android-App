import RPi.GPIO as GPIO
from time import sleep

movement=21 #sensor pin
gate=18 #audio pin
counter=int(0)

GPIO.setmode(GPIO.BCM)
GPIO.setup(movement,GPIO.IN)
GPIO.setup(gate,GPIO.IN)
try:
  while True:
    readSound=GPIO.input(gate)
    getMove=GPIO.input(movement)
    if readSound==1:
      print("Sound detected")
    else:
      print("No sound")
    if getMove==0:
      print("Movement not detected")
      sleep(2)
      counter=0
    else:
      print("Motion detected")
      sleep(2)
