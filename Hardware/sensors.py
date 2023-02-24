import busio
import digitalio
import board
import adafruit_mcp3xxx.mcp3008 as MCP
from adafruit_mcp3xxx.analog_in import AnalogIn
import RPi.GPIO as GPIO
from time import sleep


spi=busio.SPI(clock=board.SCK,MISO=board.MISO,MOSI=board.MOSI)
cs=digitalio.DigitalInOut(board.D5)
mcp=MCP.MCP3008(spi,cs)

movement=21
audio=AnalogIn(mcp,MCP.P0)

GPIO.setmode(GPIO.BCM)
GPIO.setup(movement,GPIO.IN)

average=0
counter=1
print('Stay Quit! Adapting to your environment!')
answer=input('Ready? (y/n): ')
while answer!='y':
	answer=input('Ready? (y/n): ')
while counter!=24:
	if audio.voltage>average:
		average=audio.voltage
	counter=counter+1
average=average+0.02
counter=1
counterM=0
pos1=0
pos2=0
pos3=0
pos1M=0
pos2M=0
pos3M=0
try:
	while True:
		sleep(0.05)
		getMove=GPIO.input(movement)
		if audio.voltage>average:
			if pos1==0:
				pos1=1
			else:
				if pos2==0:
					pos2=counter
				elif pos3==0:
					pos3=counter
			if pos2-pos1<=12 and pos3-pos2<=12:
				print('SOUND Alert sent to the app!')
				pos1=0
				pos2=0
				pos3=0
		else:
			if pos1==1:
				counter=counter+1
			print('No sound')

		if getMove==0:
			if pos1M==1:
				counterM=counterM+1
			print('No movement')
		else:
			if pos1M==0:
				pos1M=1
			else:
				if pos2M==0:
					pos2M=counterM
				elif pos3M==0:
					pos3M=counterM
			if pos1M==1 and pos2M!=0 and pos3M!=0:
					print('Movement Alert sent to the app!')
					pos1M=0
					pos2M=0
					pos3M=0

except KeyboardInterrupt:
	GPIO.cleanup()
