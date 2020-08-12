# Fitness_Mirror_Software Alpha Ver 1

The "Fitness Mirror" is basically a specialized smart mirror build. A "smart mirror" is at its most basic a 2-way mirror that has a computer display behind it. When the display is off or asleep the front side of the mirror is the dominant light source and so the mirror reflects images like a normal mirror would. When the display is on, the monitor becomes the dominant light source and so the images shown on the monitor project through the mirror. While they are becoming more popular, most smart mirrors aren't sold, but instead have to be built. There are smart mirrors on the market, but many of them only mean "smart" in that they offer some form of app controlled lighting options, and the ability to show the current weather. Displaying the weather or email are the most commonly implemented smart mirror options for DIY builds as well. Most of these builds, DIY or commercial do not store data on the mirror, but instead sync with a smartphone for example to display the requested information.

This smart mirror build is more ambitious and is intended to focus on something different. The idea was to offer the user a centralized way to manage and monitor their fitness and weight goals. Just as important was the goal to have it be relatively transparent to a users life. For most people who are watching their weight or trying to get in shape, they often look at themselves in a mirror anyway to try to detect differences, weigh themselves with the bathroom scale, etc. It made sense to try to enhance this process in a location where much of it already takes place, aka the bathroom. 

To that end this smart mirror build focuses on interfacing with health data collection sensors such as a bathroom scale, getting user input data and controls via a mobile device, and an integrated GUI and database in the mirror itself to interact with stored information. This is most defintely a DIY build and not an easy one at that. The version presented here should be taken as an early protoype of the concept. The core of the software is included in this repository, but there is still a fair amount of cleanup work that would need to be done for a nice polished user experience. For those who have no background in electronics, preparing the hardware for this build is also a big challenge. If that sounds appealing to you however, I hope that this project will offer you a good start. Or at least an example of how to get started on building your own fitness mirror.

I did complete a physical prototype model of this at one point and when it started coming together it was very cool to see! At the time there was a fair amount of technology limitations (and limitations in personal knowledge). I will likely pick this project back up at some point and try to complete it. Newer technology always presents better opportunities and things may have progressed far enough along to pull off the concept in the way it was envisioned.

One final note of fair warning. If you plan to build this and want a high quality end product it is NOT a cheap project. The software is all open source and readily available. Components like the monitor and 2-way mirror that are critical to the final product are, as of yet, not very cost effective when going for the ideal options. I will be adding more information in the docs section about hardware details and considerations. I will also try to discuss some of the problems I ran into to help others avoid some of the headaches where possible. I hope you enjoy this project!

## Prerequisites 

This software is not meant to be run all on the same system. It may be possible to emulate the various pieces of hardware in order to test code and for further development, but in order to have a finished end product for the existing code base you will need:

### Hardware:
* **Mini-PC** - This is somewhat subjective depending on your needs. The mini-pc is intended to mount on or near the mirror frame. It is where the user GUI and database reside. I used a Raspberry Pi3 at the time because it was the most recent version. I would definitely go with a Pi 4 or something more powerful if you are able.
* **Mobile Device** - The controls for the smart mirror are a GUI based phone app. This allows a user to control the mirror and to enter health data manually when needed.
* **Sensors** - The source code provided only includes code for a bathroom scale and scripts for a Raspberry Pi camera. The location and nature of these devices are going to be heavily dependent on your build and needs. In the prototype I built, the camera was mounted on the mirror frame and the bathroom scale was a commercial scale that was modified with a custom wireless circuit in order to send weight data to the mini-pc. The Arduino code for the scale circuit is included in the source code.

### Software:
As implemented the software components include a database, Java code for the phone and mirror side applications, and an arduino sketch for the bathroomm scale. If you want to use the same tools I did the list is:
* **Java IDE** - This is for the mirror side code. I like IntelliJ, but any Java IDE will be great. I used JavaFX for the GUI so you may want to make sure you're IDE supports working with JavaFX easily.
* **Android IDE** - This is for the phone side code. I used Android Studio. It is probably the safest bet to work with the project I have uploaded, but if you prefer another IDE you should be able to extract the source code from the upladed project
* **MariaDB** - This is an open source, MySQL relational style database software. I have included all of the files you should need to get going for this specific project, but getting everything installed and ready will take a little digging into MariaDB if you have never used it. Get it [here](https://mariadb.org/download/). Info about getting started can be found [here](https://mariadb.com/get-started-with-mariadb/), and if you decide you want to use a Raspberry Pi, installation instructions can be found [here](https://r00t4bl3.com/post/how-to-install-mysql-mariadb-server-on-raspberry-pi).
* **Arduino IDE** - You can use almost any IDE that supports C/C++ as this is the underlying language of the Arduino. Since what was needed in this case was relatively simple however, I just made a sketch using the basic Arduino IDE as found [here](https://www.arduino.cc/en/main/software).

## Getting Started / Installation

There is no simple place to start with this project to be honest. I would recommend as the initial step perhaps planning the scope of features you want to include in your fitness mirror. This will have a large effect on what you will need to start your build. There are a lot of creative ways to add sensors and features. Some common sensors that could be considered for integration in addition to a bathroom scale would be a blood pressure cuff and oxygen sensor. Infrared cameras can even be used to estimate BMI with the right approach and programming. One cost effective way to approach this is using an XBox Kinect sensor bar. There are some guides about how to use a Kinect for BMI calculations online. Depending on your skill set, information available, and tools on hand you may also want to think about whether you prefer to try to add sensors by reverse engineering hardware and adding communication components, reverse engineer software from existing smart devices to try to grab the signal they transfer with your mirror, or build a device from scratch so you have complete control over every aspect of it. None of these are easy and require a lot of patience to be successful. It wasn't the case at the time I built my prototype, but there may even be more DIY friendly health sensors on the market now that provide a built in means of integrating their devices into custom setups.

Once you think you know where you want to go with your build, I can only suggest that you look through the code that can be found in the SRC folder for all of the various aspects of the software. I cannot provid a ready to go option because so much of this process was custom and specific to the exact hardware being used at the time. Once you have a solid understanding of the provided code however, you will begin to have an idea of what you might need to change in order to work with your specific hardware. Almost anything can be changed out if you're willing to put the time into it. A lot of cost effective off the shelf options were chosen for the original build as I wasn't even sure it would be possible for me to do what I wanted. Many were not the best choice, and there are likely even more options now. If you are having great success with your build, it may even be worth thinking about ways to abstract some of the code provided so that it is more versatile and flexible as you grow the health monitoring system.

The most important advice I can give before beginning is to remember to have fun with this, and mentally prepare for a large time investment. There is a lot of cool stuff to learn if you are a tinkerer at heart both on the hardware and software ends. Even if you aren't ultimately successful with everything you want to do, I can all but gaurantee you'll learn a lot just by trying.

## Testing

I have left most of my bug testing snippets in the code available here on Github. Much of it is still active within the code since it is console print statements meant to track what is happening at a given time. It was transparent during use, but gave me a constant way to see what was going on when needed. There are also some sections of code that are commented out that are useful for testing under certain conditions. It can be useful to output the console statements to a log file instead once you have enough of the system running. The major aspects of the program are all multi-threaded, so having a plan of how to track what is really going on when something isn't working properly can be tricky but is important.

When putting the pieces together I found it was also very helpful to have emulation for the phone on my main development system so that I could run the phone app and main program together while working with the code and debuggers. My Windows based desktop had some different requirements for communication between the phone and main software as well as beween the main software and database. Whatever direction you take it is worthwile to get your development system set up in tandem with any of your "live" systems, if not before. I left my alternate setup in the parts of the code and commented out the section I wasn't using at the time. These should help you get an idea of the approach.

Lastly, in the "test" folder I have left some generic image items and other files that may be useful. They are extremely basic, but can be useful in testing things like data retrieval and time lapse features rather than having to take a bunch personal pictures or grab them from all over the web. Anything else I can think of that might help I will also include in the test or docs folders along with some information about how to use the files where meaningful to do so.

## Final Thoughts

If you made it this far and still think this project sounds worth a try, let me know if you get started and need some help! I may not be able to help you with the specifics of the problems you face, but I may have some insight into a good approach, what to try, or what tools you may need from my own experiences. Also, I do plan to return to this project when time allows. I would like to take what I have learned in the initial attempt to come up with something more practical as a user device, and that has more of the features that would make it what it was mean to be. If that happens then I will update this repository with my new findings and results. Good luck!

## Version History

* **Alpha Version 1** - Basic implementation of core software features and minimal external sensor devices (a bathroom scale and camera).

## Author(s)

Original concept: 
**Jeremy Ruth, Steven Blevins, Vanessa Thompson, Matt Hayes**

Software: 
**Jeremy Ruth**

## License

This project is licensed under the GNU General Public License v3.0 license. See [License](https://github.com/Jeremy-Ruth/Fitness_Mirror_Software/blob/master/LICENSE) for more details.
