import RPi.GPIO as GPIO
import time
movement=21
GPIO.setmode(GPIO.BCM)
GPIO.setup(movement,GPIO.IN)
while True:
  readVal=GPIO.input(movement)
  if readVal==0:
    print("Motion not detected")
  else:
    print("Motion detected")
GPIO.cleanup()
