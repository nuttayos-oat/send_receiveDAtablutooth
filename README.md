# smart door project
This is a first times for me to explain about my project and make this article. If something wrong, I apologize about that. 
Today I will create a smartdoor project so in this project is consist of  3 parts, the first part is an application for user, the second part is raspberry pi sever, the last is a sensor device. 

Equipment
1.esp8266
2.raspberry pi
3.magnetic door lock
4.relay 5 v
5.switching power supply 12 v

Let start with overview of this project.I will create system which you can control your door lock from andorid application and save any changing status in raspberry pi. For this system I use 2 type communications, are bluetooth and wifi protocol, bluetooth is used for communication between smart phone and raspberry pi, the next one use between esp8266(control magnetic door lock) and raspberry pi. I will set up raspberry pi as a mqtt server for reciving status from sensor and then create an android application for controlling sensor. 

![system](https://user-images.githubusercontent.com/58799316/88128209-9f6c5f00-cbff-11ea-8554-848a9f5958ff.png)

From this picture, it show overview of this system so we will connect our devices follow this picture as you see in the picture below.

![103403](https://user-images.githubusercontent.com/58799316/88131115-684d7c00-cc06-11ea-86e8-b9c2d9c8381e.jpg) ![103404](https://user-images.githubusercontent.com/58799316/88131127-6daac680-cc06-11ea-8188-59a0a1905e37.jpg)

Then I will try to control my sensor from server through mqtt communication so I use a red LED for demonstration. 

![103402](https://user-images.githubusercontent.com/58799316/88129289-ea877180-cc01-11ea-8c89-46a226bd0c27.jpg)

A picture above show you about connection status between server and client and next step I will pretend to send message "open" from server and client should receive it moreover it has to reply back and turn LED on as well.

![103400](https://user-images.githubusercontent.com/58799316/88131954-5e2c7d00-cc08-11ea-9539-30830f5761c9.jpg)
![103398](https://user-images.githubusercontent.com/58799316/88132046-a8156300-cc08-11ea-97dc-318c924be638.jpg)

After that send message "close" so LED should turn off follow pictures below.

![103397](https://user-images.githubusercontent.com/58799316/88132241-31c53080-cc09-11ea-9e8f-96b82869de23.jpg)
![103395](https://user-images.githubusercontent.com/58799316/88132280-473a5a80-cc09-11ea-9875-5069ad4e9513.jpg)

In an application part, I will creat android application for controlling this sensor you can reach source code in seperate files. And this connectivity, I really appreciate blueterm application that provide very helpful source code for this project. 

<img src="https://user-images.githubusercontent.com/58799316/88132685-67b6e480-cc0a-11ea-9059-aefa2ac64868.jpg" width="240"> 
pair devices.

<img src="https://user-images.githubusercontent.com/58799316/88133465-54a51400-cc0c-11ea-892c-61342fdf3eae.jpg" width="240"> 

You can start communication between server and client by clicking starthread buttom, the socket will be connected. Now you can order your magnetic door on  buttom open and close, every status will keeped as log in the raspberry pi.

![103939](https://user-images.githubusercontent.com/58799316/88133554-93d36500-cc0c-11ea-99dd-7d5b43c0229a.jpg)
![103940](https://user-images.githubusercontent.com/58799316/88133557-9635bf00-cc0c-11ea-983f-04e2436f15aa.jpg)

![103413](https://user-images.githubusercontent.com/58799316/88133871-4efbfe00-cc0d-11ea-84c8-4a0c3dd82d7d.jpg)
![103412](https://user-images.githubusercontent.com/58799316/88133868-4b687700-cc0d-11ea-8d68-e5629c0b4f02.jpg)

