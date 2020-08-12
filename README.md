# Fitness_Mirror_Software Alpha Ver 1

The "Fitness Mirror" is basically a specialized smart mirror build. A "smart mirror" is at its most basic a 2-way mirror that has a computer display behind it. When the display is off or asleep the front side of the mirror is the dominant light source and so the mirror reflects images like a normal mirror would. When the display is on, the monitor becomes the dominant light source and so the images shown on the monitor project through the mirror. While they are becoming more popular, most smart mirrors aren't sold, but instead have to be built. There are smart mirrors on the market, but many of them only mean "smart" in that they offer some form of app controlled lighting options, and the ability to show the current weather. Displaying the weather or email are the most commonly implemented smart mirror options for DIY builds as well. Most of these build do not store data on the mirror, but instead sync with a smartphone for example to display the requested information.

This smart mirror is more ambitious and is intended to focus on something different. The idea was to offer the user a centralized way to manage and monitor their fitness and weight goals. To that end this mirror build focuses on interfacing with health data collection sensors such as a bathroom scale, user input data and controls with a mobile device, and GUI as part of the mirror to be able to interact with stored information. This is most defintely a DIY build and not an easy one at that. The version presented here should be taken as an early protoype of the concept. The core of the software is included in this repository, but there is still a fair amount of cleanup work that would need to be done for a nice polished user experience. For those who have no background in electronics, preparing the hardware for this build is also a big challenge. If that sounds appealing to you however, I hope that this project will offer you a good start. Or at least an example of how to get started on building your own fitness mirror.

I did complete a physical prototype model of this at one point and when it started coming together it was very cool to see! At the time there was a fair amount of technology limitations (and limitations in personal knowledge). I will likely pick this project back up at some point and try to complete it. Newer technology always presents better opportunities and things may have progressed far enough along to pull off the concept in the way it was intended.

One final note of warning. If you plan to build this and want a high quality end product it is NOT a cheap project. The software is all open source and readily available. but components like the monitor and 2-way mirror are critical to the final product and as of yet are not cost effective when getting the ideal options. I will be adding more information in the docs section about hardware details and considerations. I will also try to discuss some of the problems I ran into to help others avoid some of the headaches where possible. I hope you enjoy this project!

## Prerequisites 

This software is not meant to be run all on the same system. It may be possible to emulate the various pieces of hardware in order to test code and for further development, but in order to have a finished end product for the existing code base you will need:

### Hardware:
* **Mini-PC** - This is somewhat subjective depending on your needs. The mini-pc is intended to mount on or near the mirror frame. It is where the user GUI and database reside. I used a Raspberry Pi3 at the time because it was the most recent version. I would definitely go with a Pi 4 or something more powerful if you are able.
* **Mobile Device** - The controls for the smart mirror are a GUI based phone app. This allows a user to control the mirror and to enter health data manually when needed.
* **Sensors** - The source code provided only includes code for a bathroom scale and scripts for a Raspberry Pi camera. The location and nature of these devices are going to be heavily dependent on your build and needs. In the prototype I built, the camera was mounted on the mirror frame and the bathroom scale was a commercial scale that was modified with a custom wirless circuit in order to send weight data to the mini-pc. The Arduino code for the scale circuit is included in the source code.

### Software:
As implemented the software components include a database, Java code for the phone and mirror side applications, and an arduino sketch for the bathroomm scale. If you want to use the same tools I did the list is:
* **Java IDE** - This is for mirror side code. I like IntelliJ, but any Java IDE will be great. I used JavaFX for the GUI so you may want to make sure you're IDE supports working with JavaFX easily.
* **Android IDE** - This is for the phone side code. I used Android Studio. It is probably the safest bet to work with the project I have uploaded, but if you prefer another IDE you should be able to extract the source code from the upladed project
* **MariaDB** - This is an open source, MySQL style, database software. I have included all of files you should need to get going, but getting everything installed and ready will take a little digging into MariaDB if you have never used it. Get it [here](https://mariadb.org/download/). Info about getting started can be found [here](https://mariadb.com/get-started-with-mariadb/), and if you decide you want to use a Raspberry Pi installation instructions can be found [here](https://r00t4bl3.com/post/how-to-install-mysql-mariadb-server-on-raspberry-pi).
* **Arduino IDE** - You can use almost any IDE that supports C/C++ as this is the underlying language of the Arduino. Since what was needed in this case was relatively simple however, I just made a sketch using the basic Arduino IDE as found [here](https://www.arduino.cc/en/main/software).

## Getting Started / Installation

## Testing

## Important Note

## Version History

## Author

Original concept: 
**Jeremy Ruth, Steven Blevins, Vanessa Thompson, Matt Hayes**

Software: 
**Jeremy Ruth**

## License

This project is licensed under the GNU General Public License v3.0 license. See [License](https://github.com/Jeremy-Ruth/Fitness_Mirror_Software/blob/master/LICENSE) for more details.
