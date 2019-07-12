import serial, time

ser = serial.Serial('COM2', 11500)
while True:
    inf = ser.readline()
    print(inf)
    time.sleep(3)
ser.close()
