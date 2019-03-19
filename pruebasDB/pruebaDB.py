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

    fechaNow = datetime.datetime.now()
    dia = str(fechaNow.day)
    mes = str(fechaNow.month)
    anno = str(fechaNow.year)
    hora = str(fechaNow.strftime("%I"))+":"+str((fechaNow.strftime("%M")))+" "+str((fechaNow.strftime("%p")))
    fechaActual = dia+"-"+mes+"-"+anno
    inf = ser.readline()

    try:
        dato2= str(inf).split(": ")[2][0:5]
        dato1= str(inf).split(": ")[1][0:5]
        #print(dato1+"asd"+dato2)
        print(inf)
        ref.child(str(i)).set({
            'fecha_dato':fechaActual,
            'hora':hora,
            'temperatura':dato1,
            'humedad':dato2
        })
        i=i+1
    except Exception as e:
        pass
    
    
    time.sleep(10)
ser.close()
