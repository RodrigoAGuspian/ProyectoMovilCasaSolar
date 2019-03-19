import serial, datetime, psycopg2, sys, time, os

con = None

fechaAnterior = ""

def loop():
    data = ""
    try:
        if os.path.exists('/dev/ttyACM0'):
            print(">>> Usando ttyACM0")
            ser = serial.Serial('/dev/ttyACM0', 115200)
            data = ser.readline()
        elif os.path.exists('/dev/ttyACM1'):
            print(">>> Usando ttyACM1")
            ser = serial.Serial('/dev/ttyACM1', 115200)
            data = ser.readline()
        else:
            print("Desconectado o cambio de puerto.")
    except Exception, e:
        print("Error: ", e)
    return data

while True:
    data = loop()
    print(data)
    try:
        con = psycopg2.connect("host='10.73.74.159' dbname='aplicacion' user='casa_solar' password='casa_solar'")
        cur = con.cursor()
        fechaNow = datetime.datetime.now()
        dia = str(fechaNow.day)
        mes = str(fechaNow.month)
        anno = str(fechaNow.year)
        fechaActual = "D"+dia+"M"+mes+"A"+anno
        try:
            if(fechaActual != fechaAnterior):
                cur.execute("CREATE TABLE "+fechaActual+"(fecha_hora timestamp, voltaje decimal, irradiancia decimal )")
                cur.execute("INSERT INTO "+fechaActual+" VALUES(now(),"+data+")")
                con.commit()
                fechaAnterior = fechaActual
            else:
                cur.execute("INSERT INTO "+fechaActual+" VALUES(now(),"+data+")")
                con.commit()
                fechaAnterior = fechaActual
        except Exception, e:
            print("Error: ", e)
    except serial.serialutil.SerialException:
        except_counter +=1
        if except_counter == 5:
            print("Es igual a 5")
    time.sleep(2)
