import serial, time, firebase_admin, datetime
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate("datoscasasolar-firebase-adminsdk-42r7y-aaa0af8036.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://datoscasasolar.firebaseio.com/'
})


ref = db.reference('datos')
snapshot= ref.get()

i=0
try:
    print(str(i))
    print(len(snapshot))
    i=len(snapshot)
except Exception as e:
    pass

ser = serial.Serial('COM2', 9600)
while True:

    inf = ser.readline()
    print(inf)
    dato1= str(inf).split(": ")[1][0:5]
    dato2= str(inf).split(": ")[2][0:5]
    dato3= str(inf).split(": ")[3][0:5]
    dato4= str(inf).split(": ")[4][0:5]

    print(dato1,dato2,dato3,dato4)
    
    
    time.sleep(1)
ser.close()
