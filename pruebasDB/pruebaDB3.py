import serial, datetime, psycopg2, sys, time, os

con = None

def connectionDB():
    try:
        return psycopg2.connect("host='10.73.74.159' dbname='aplicacion' user='casa_solar' password='casa_solar'")
    except Exception as error:
        return error

def data(ser, fechaActual, fechaAnterior):
    print(ser)
    try:
        con = connectionDB()
        cur = con.cursor()
        
        try:
            if(fechaActual != fechaAnterior):
                cur.execute("CREATE TABLE "+fechaActual+"(fecha_hora timestamp, voltaje decimal, irradiancia decimal, corrientePanel decimal, corrienteBateria decimal, corrienteCarga decimal, voltajePanel decimal, voltajeBateria decimal, voltajeCarga decimal )")
                cur.execute("INSERT INTO "+fechaActual+" VALUES(now(),"+ser+")")
                con.commit()
            else:
                cur.execute("INSERT INTO "+fechaActual+" VALUES(now(),"+ser+")")
                con.commit()
        except Exception as e:
            print("Error: ", e)
    except serial.serialutil.SerialException:
        except_counter +=1
        if except_counter == 5:
            print("Es igual a 5")

fechaAnterior = ""
while True:
    fechaNow = datetime.datetime.now()
    dia = str(fechaNow.day)
    mes = str(fechaNow.month)
    anno = str(fechaNow.year)
        
    fechaActual = "D"+dia+"M"+mes+"A"+anno
    
    if(fechaActual != fechaAnterior):
        try:
            if os.path.exists('/dev/ttyACM0'):
                print(">>> Usando ttyACM0")
                ser = serial.Serial('/dev/ttyACM0', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
            elif os.path.exists('/dev/ttyACM1'):
                print(">>> Usando ttyACM1")
                ser = serial.Serial('/dev/ttyACM1', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
            elif os.path.exists('/dev/ttyACM2'):
                print(">>> Usando ttyACM2")
                ser = serial.Serial('/dev/ttyACM2', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
            elif os.path.exists('/dev/ttyACM3'):
                print(">>> Usando ttyACM3")
                ser = serial.Serial('/dev/ttyACM3', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
            else:
                ser = serial.Serial('COM24', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
                print("Desconectado o cambio de puerto.")
                time.sleep(2)
        except Exception as e:
            print("Error: ", e)
        fechaAnterior = fechaActual
    else:
        try:
            if os.path.exists('/dev/ttyACM0'):
                print(">>> Usando ttyACM0")
                ser = serial.Serial('/dev/ttyACM0', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
            elif os.path.exists('/dev/ttyACM1'):
                print(">>> Usando ttyACM1")
                ser = serial.Serial('/dev/ttyACM1', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
            elif os.path.exists('/dev/ttyACM2'):
                print(">>> Usando ttyACM2")
                ser = serial.Serial('/dev/ttyACM2', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
            elif os.path.exists('/dev/ttyACM3'):
                print(">>> Usando ttyACM3")
                ser = serial.Serial('/dev/ttyACM3', 115200)
                data(ser.readline(), fechaActual, fechaAnterior)
            else:
                print("Desconectado o cambio de puerto.")
                time.sleep(2)
        except Exception as e:
            print("Error: ", e)
        fechaAnterior = fechaActual
