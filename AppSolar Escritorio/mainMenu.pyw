import sys, serial,traceback, datetime,firebase_admin,socket
from PyQt5 import uic
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
from PyQt5.QtCore import *

from firebase_admin import credentials
from firebase_admin import db

ser = serial.Serial('COM2', 115200)

cred = credentials.Certificate("datoscasasolar-firebase-adminsdk-42r7y-aaa0af8036.json")
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://datoscasasolar.firebaseio.com/'
})

ref = db.reference('tiempoReal')
refData = db.reference('datos')
snapshot= ref.get()

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

            testConn = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
            host = socket.gethostbyname("www.google.com")

            try:
                s = socket.create_connection((host, 80), 2)
                comInt=True
                print ("Estamos on-line.")
            except Exception as ea:
                comInt=False
                print ("Lo siento, pero no se ha podido establecer la conexión: "+str(ea))
                self.txtConsole.appendPlainText("Lo siento, pero no se ha podido establecer la conexión.")


            if comInt:
                
                dato1= str(inf).split(": ")[1].split(" ")[0]
                dato2= str(inf).split(": ")[2].split(",")[0]
                dato3= str(inf).split(": ")[3].split(" ")[0]
                dato4= str(inf).split(": ")[4].split(" ")[0]
                dato5= str(inf).split(": ")[5].split(";")[0]
                print("1")
                
                ref.child(str(i)).set({
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
