import paho.mqtt.client as mqtt
import time
import csv
import datetime
import bluetooth


mqtt_username = "username"
mqtt_password = "tkcrd1234"
mqtt_topic = "esp_client"
mqtt_broker_ip = "192.168.1.158"
uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

mess = ""
result = ""
state_header = 0

server_socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_socket.bind(("",bluetooth.PORT_ANY))
server_socket.listen(1)

port = server_socket.getsockname()[1]

client = mqtt.Client()
client.username_pw_set(mqtt_username, mqtt_password)

def gettime():
    time = datetime.datetime.now().strftime("%H:%M:%S")
    time = str(time)
    return(time)

def getdate():
    date = datetime.datetime.now().strftime("%Y-%m-%d")
    date = str(date)
    return(date)
    
def write_to_csv():
    print("Im here")
    global state_header
    with open('/home/pi/door_status.csv',mode='a') as csv_file :
        columname = ['date','time','status']
        writer = csv.DictWriter(csv_file,fieldnames=columname)
        if(state_header != 1):
            writer.writeheader()
            state_header = 1
        writer.writerow({'date': getdate(),'time': gettime(),'status': result})
        print(state_header)
    
        
        

def on_connect(client, userdata, flags, rc):
    print("connect", str(rc))
    
    client.subscribe(mqtt_topic)
    
    
def on_message(client, userdata, msg):
    message = ""
    global result
    for i in str(msg.payload) :
       if(i != "b" and i != "'") :
            message = message + i
    result = message
    print("topic:", msg.topic +"\nMessage:" +result)
    write_to_csv()  
    
client.on_connect = on_connect
client.on_message = on_message


client.connect(mqtt_broker_ip, 1883)

client.loop_start()

bluetooth.advertise_service(server_socket, "SampleServer", service_id = uuid, service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS], profiles=[bluetooth.SERIAL_PORT_PROFILE],protocols = [bluetooth.OBEX_UUID])

print("wating for", port)
client_socket,address = server_socket.accept()
print("Accept connection from address",address)

#client.disconnect()
while True :
     data = client_socket.recv(1024)
     if data :
        for i in str(data) :
          i=i.rstrip("\r\n")
          if(i != "b" and i != "'") :
             mess = mess + i
             
        
        if(mess[0] == "1" ):
            print("one")
            mess =""
            client.publish("raspi_server", "open")
        elif(mess[0] == "2"):
            print("two")
            mess=""
            client.publish("raspi_server", "close")
         
        else:
            print("wrong")
            mess=""
    
    

    