import serial, time, firebase_admin, datetime
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate("datoscasasolar-firebase-adminsdk-42r7y-aaa0af8036.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://datoscasasolar.firebaseio.com/'
})


ref = db.reference('tiempoReal')
refData = db.reference('datos')
snapshot= ref.get()

i=0
try:
    print(str(i))
    print(len(snapshot))
    i=len(snapshot)
    if i==60:
        i=0
        pass
except Exception as e:
    pass



ser = serial.Serial('COM2', 9600)
while True:
    tiempo=0;
    fechaNow = datetime.datetime.now()
    dia = str(fechaNow.day)
    mes = str(fechaNow.month)
    anno = str(fechaNow.year)
    hora = str(fechaNow.strftime("%I"))+":"+str((fechaNow.strftime("%M")))+" "+str((fechaNow.strftime("%p")))
    fechaActual = dia+"-"+mes+"-"+anno
    fechaActual1 =dia+"-"+mes+"-"+anno+" "+str(fechaNow.strftime("%H"))+":"+str((fechaNow.strftime("%M")))+":"+str((fechaNow.strftime("%S")))
    inf = ser.readline()
    try:
        dato1= str(inf).split(": ")[1].split(" ")[0]
        dato2= str(inf).split(": ")[2].split("T")[0]
        dato3= str(inf).split(": ")[3].split(",")[0]
        dato4= str(inf).split(": ")[4].split(" ")[0]
        dato5= str(inf).split(": ")[5].split(";")[0]
        #print(dato1+"asd"+dato2)
        print(inf)
        ref.child(str(i)).set({
            'fechaActual':fechaActual,
            'hora':hora,
            'fechaActual1':fechaActual1,
            'temperatura':dato3,
            'humedad':dato4,
            "corrienteBateria" : "0.0",
            "corrienteCargas" : "0.0",
            "corrientePanel" : dato1,
            "irradiancia" : dato2,
            "voltajeBateria" : "0.0",
            "voltajeCargas" : "0.0",
            "voltajePanel" : dato5,

        })
        
        if i==59:
            nowRef = refData.child("y"+anno).child("m"+mes).child("d"+dia)
            i=0
            j=0
            try:
                j= len(nowRef.get())
                pass
            except Exception as a:
                pass
            nowRef.child(str(j)).set({
                'hora':hora,
                'temperatura':dato2,
                'humedad':dato3,
                "corrienteBateria" : "0.0",
                "corrienteCargas" : "0.0",
                "corrientePanel" : dato1,
                "irradiancia" : dato4,
                "voltajeBateria" : "0.0",
                "voltajeCargas" : "0.0",
                "voltajePanel" : dato5,

            })
        else:
            i=i+1



        
    except Exception as e:
        pass
    
    
    time.sleep(1)
ser.close()
