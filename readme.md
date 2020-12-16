## Grain : Optional Mixed Grain Mixer

- The project ended in Q2 2018.  
- Organized and written in 2020 for recording.

~~~
- grain_app : Android app for gain.
- grain_arduino : arduino script
- grain_server: deleted(security problem)
~~~

This project is carried out by a team of 5 people.  

**K.T. Kim** : Team management, office, idea, production manager.  
**J.H.Lee** : Development, circuit composition, Manufacture and design.  
**W.S.Yoo** : Circuit composition and manufacture.   
**H.Y.Baek** : Circuit composition and manufacture.  
**D.K.Joo** : Development and manufacture.   

### Concept  
In general, the process of making rice is very difficult.  
Perform a series of procedures to bring an appropriate amount of stored rice, wash it, and put it in a rice cooker.  
With the consumption of rice in an environment where single-person households are rapidly increasing, our team tried to develop a device that effectively stores rice and health-conscious grains and automates cleaning.

### Appearance design
The goal is to design home appliances that efficiently store and clean up weeds.  
the upper part of a large store of rice A certain amount of grain is divided into the middle part of the washing machine and the bottom part of the washing process.
The following figure illustrates the overall exterior design.
| ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/device_inside.png?raw=true) | ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/device_out.png?raw=true) |
|--|--|

### Flow

 1. #### step1
  ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/step1.png?raw=true)
 
 2. #### step2
 ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/step2.png?raw=true)
 
 3. #### step3
 ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/step3.png?raw=true)

### Circuit Design
Based on the Arduino, a complex operation of motors, water pumps, Ultrasonic distance sensor, and Internet communication was required.  
Top side three ultrasonic distance sensor detect how much remained rice is there and arduino update the server data.  
The three motors serve to send a certain amount of rice down through the structure shown below.  
|![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/rotation.png?raw=true)| ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/rotation2.png?raw=true) |
|--|--|

Two water pumps are used to put water in and out of the washing area below, and a motor with good rotational force is used to clean rice like the structure of a washing machine.  
![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/buck.png?raw=true)  

The figure below shows the overall circuit configuration and the shape of the actual prototype.  
|![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/circuit_layout.png?raw=true)| ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/circuit_layout2.jpg?raw=true) |
|--|--|

### App Design
When a user runs a rice wash through the app, the contents are sent to the server and the device receives the information and proceeds with the rice wash.  
The purpose of using the app is to increase convenience by automatically washing in advance in situations such as on the way home from work.  
The picture below shows the picture of the app.
|![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/Artboard%201-100.jpg?raw=true)| ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/Artboard%202-100.jpg?raw=true) | ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/Artboard%203-100.jpg?raw=true)|
|--|--|--|
|![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/Artboard%204-100.jpg?raw=true)| ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/Artboard%205-100.jpg?raw=true) | ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/Artboard%206-100.jpg?raw=true)|

### Production
Products such as acrylic boards were designed with drawings, and the exterior was painted in colors suitable for home appliances.  
The final product is as follows.
|![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/final_product.jpg?raw=true)| ![grain](https://github.com/hololee/Grain-Optional-Mixed-Grain-Mixer/blob/main/after_wash.jpg?raw=true) |
|--|--|