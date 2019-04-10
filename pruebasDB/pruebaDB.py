import serial, time, firebase_admin, datetime


ser = serial.Serial('COM2', 9600)
while True:

    inf = ser.readline()

    print(inf)
    
    
    time.sleep(1)
ser.close()
