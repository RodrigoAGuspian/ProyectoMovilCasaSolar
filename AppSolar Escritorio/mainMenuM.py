import sys, serial,traceback, datetime,firebase_admin,socket
from PyQt5 import uic
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *

from firebase_admin import credentials
from firebase_admin import db

ser = serial.Serial('COM2', 115200)

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
try:
    i=len(snapshot)
    if i==60:
        i=0
        pass
except Exception as e:
    print("Error de datos")
pass


class Ventana(QMainWindow):
    """docstring for ClassName"""
    bandera = True
    bandera1 = False
    counter=0
    
    def __init__(self):
        QMainWindow.__init__(self)
        uic.loadUi("MainMenu.ui",self)
        self.btnIniciarTD.clicked.connect(self.iniciarIngresoDeDatos)
        self.btnDetenerTD.clicked.connect(self.detenerIngresoDeDatos)

        self.threadpool = QThreadPool()
        
            

    def iniciarIngresoDeDatos(self):
        try:
            self.timer = QTimer()
            self.timer.setInterval(3000)
            self.timer.timeout.connect(self.recurring_timer)
            self.timer.start()
            pass
        except Exception as e:
            pass

    def detenerIngresoDeDatos(self):
        self.timer.stop()

    def progress_fn(self, n):
        print("%d%% done" % n)

    def execute_this_fn(self, progress_callback):
        for n in range(0, 5):
            time.sleep(1)
            progress_callback.emit(n*100/4)

        return "Done."

    def print_output(self, s):
        print(s)

    def thread_complete(self):
        print("THREAD COMPLETE!")

    def oh_no(self):
        
        worker = Worker(self.execute_this_fn) # Any other args, kwargs are passed to the run function
        worker.signals.result.connect(self.print_output)
        worker.signals.finished.connect(self.thread_complete)
        worker.signals.progress.connect(self.progress_fn)
        self.threadpool.start(worker)

    def recurring_timer(self):
        global i
        comInt = True
        inf = ser.readline()
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
                        pass

                except Exception as e:
                    print ("Lo siento, no se ha podido comunicar con la base de datos, espere: "+str(e))
                    self.txtConsole.appendPlainText("Lo siento, no se ha podido comunicar con la base de datos, espere.")
                else:
                    pass

        except Exception as e:
            print("Error de datos: "+str(e))
            pass





class Worker(QRunnable):
    def __init__(self, fn, *args, **kwargs):
        super(Worker, self).__init__()

    @pyqtSlot()
    def run(self):
        try:
            result = self.fn(*self.args, **self.kwargs)
        except:
            traceback.print_exc()
            exctype, value = sys.exc_info()[:2]
            self.signals.error.emit((exctype, value, traceback.format_exc()))
        else:
            self.signals.result.emit(result)  # Return the result of the processing
        finally:
            self.signals.finished.emit()  # Done

app = QApplication(sys.argv)
newVentana = Ventana()
newVentana.show()
app.exec_()

