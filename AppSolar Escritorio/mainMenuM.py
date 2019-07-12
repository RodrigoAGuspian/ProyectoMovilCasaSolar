import sys, serial,traceback, datetime,firebase_admin,socket

import serial
import threading

from PyQt5 import uic, QtCore, QtWidgets
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *

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

ref1 = db.reference('tiempoReal',app1)
refData = db.reference('datos',app1)

ref2 = db.reference('proyectoIrradiancia',app2).child('tiempoReal')
refData2 = db.reference('proyectoIrradiancia',app2).child('datos')

snapshot= ref1.get()

i=0
inf=""
snapshot= ref1.get()

try:
    print(str(i))
    print(len(snapshot))
    i=len(snapshot)
    if i==60:
        i=0
        pass
except Exception as e:
    print("Error de datos")
    pass

banderaP = False;
class Helper(QtCore.QObject):
    textSignal = QtCore.pyqtSignal(str)

def leer_puerto(port, baudrate, helper):
    global inf
    arduino = serial.Serial(port, baudrate)
    while arduino.isOpen():
        rawString = arduino.readline()
        inf=rawString
        helper.textSignal.emit(str(rawString)[7:-5])


class Ventana(QMainWindow):
    def __init__ (self, parent=None):
        super(Ventana, self).__init__(parent)
        uic.loadUi('MainMenu.ui', self)
        global banderaP
        self.timer = QtCore.QTimer(self)
        self.timer.timeout.connect(self.tick)
        self.timer.start(2000)
        self.btnIniciarTD.clicked.connect(self.aceptar)
        self.btnDetenerTD.clicked.connect(self.denegar)
        port = "COM2"
        self.helper = Helper()
        threading.Thread(target=leer_puerto, args=(port, 115000, self.helper), daemon=True).start()

    def aceptar(self):
        global banderaP
        banderaP=True

    def denegar(self):
        global banderaP
        banderaP=False

    @QtCore.pyqtSlot()
    def tick(self):
        global banderaP
        if banderaP:
            global i
            global inf
            comInt = True
            tiempo=0;
            fechaNow = datetime.datetime.now()
            dia = str(fechaNow.day)
            mes = str(fechaNow.month)
            anno = str(fechaNow.year)
            hora = str(fechaNow.strftime("%I"))+":"+str((fechaNow.strftime("%M")))+" "+str((fechaNow.strftime("%p")))
            fechaActual = dia+"-"+mes+"-"+anno
            fechaActual1 =dia+"-"+mes+"-"+anno+" "+str(fechaNow.strftime("%H"))+":"+str((fechaNow.strftime("%M")))+":"+str((fechaNow.strftime("%S")))
            try:
                if True:    
                    try:
                        dato1= str(inf).split(": ")[1].split(" ")[0]
                        dato2= str(inf).split(": ")[2].split(",")[0]
                        dato3= str(inf).split(": ")[3].split(" ")[0]
                        dato4= str(inf).split(": ")[4].split(" ")[0]
                        dato5= str(inf).split(": ")[5].split(";")[0]
                        tmpVoltaje = float(dato5);
                        if  tmpVoltaje>= 0.5:
                            print("1")
                            ref1.child(str(i)).set({
                                'fechaActual':fechaActual,
                                'hora':hora,
                                'fechaActual1':fechaActual1,
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
                            ref2.child(str(i)).set({
                                'fechaActual':fechaActual,
                                'hora':hora,
                                'fechaActual1':fechaActual1,
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
                            self.txtConsole.appendPlainText(str(inf))
                            i=i+1
                            if i==60:
                                nowRef = refData.child("y"+anno).child("m"+mes).child("d"+dia)
                                nowRef2 = refData2.child("y"+anno).child("m"+mes).child("d"+dia)
                                i=0
                                j=0
                                k=0
                                try:
                                    j= len(nowRef.get())
                                    k= len(nowRef2.get())
                                    pass
                                except Exception as a:
                                    print ("Lo siento, no se ha podido comunicar con la base de datos, espere: "+str(a))
                                    self.txtConsole.appendPlainText("Lo siento, no se ha podido comunicar con la base de datos, espere.")
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

                                nowRef2.child(str(k)).set({
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
                            print ("Estamos en la noche.")
                            self.txtConsole.appendPlainText("Estamos en la noche.")
                            pass

                    except Exception as e:
                        print ("Lo siento, no se ha podido comunicar con la base de datos, espere: "+str(e))
                        self.txtConsole.appendPlainText("Lo siento, no se ha podido comunicar con la base de datos, espere.")
                    else:
                        pass

            except Exception as e:
                print("Error de datos: "+str(e))
                pass



app = QApplication(sys.argv)
newVentana = Ventana()
newVentana.show()
app.exec_()
