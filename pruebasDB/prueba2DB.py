import serial, time, firebase_admin, datetime
from firebase_admin import credentials
from firebase_admin import db

cred1 = credentials.Certificate("datoscasasolar-firebase-adminsdk-42r7y-aaa0af8036.json")
cred2 = credentials.Certificate("innovatecfrontend-firebase-adminsdk-6v5zs-413fbc2db0.json")

app1 = firebase_admin.initialize_app(cred1, {
    'databaseURL': 'https://datoscasasolar.firebaseio.com/'
})

app2 = firebase_admin.initialize_app(cred2, {
    'databaseURL': 'https://innovatecfrontend.firebaseio.com/'
},"app2")

#sfirebase_admin.db.reference(path='/', app=None, url=None)

ref1 = db.reference('tiempoReal1',app1)
ref2 = db.reference('tiempoReal2',app2)


ref1.child(str(0)).set({
            'fechaActual':"1.1",
            'hora':"1.1",
            'fechaActual1':"2.0",
            'temperatura':"0.0",
            'humedad':"0.123",
            "corrienteBateria" : "0.0",
            "corrienteCargas" : "0.0",
            "corrientePanel" : "0.0",
            "irradiancia" : "0.0",
            "voltajeBateria" : "0.0",
            "voltajeCargas" : "0.0",
            "voltajePanel" : "0.0",

        })

ref2.child(str(0)).set({
            'fechaActual':"1.1",
            'hora':"1.1",
            'fechaActual1':"2.0",
            'temperatura':"0.0",
            'humedad':"0.0",
            "corrienteBateria" : "0.0",
            "corrienteCargas" : "0.0",
            "corrientePanel" : "0.0",
            "irradiancia" : "0.0",
            "voltajeBateria" : "0.0",
            "voltajeCargas" : "0.0",
            "voltajePanel" : "0.123",

        })