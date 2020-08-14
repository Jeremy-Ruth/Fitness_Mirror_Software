## ---------- HEADER ---------- ##
# takeDailyImage.py
# Steven Blevins
# 09/05/2017
## --------------------------- ##

## ---------- MAIN CODE ---------- ##

# This script will run when the main java code determines the user wants to take a picture with a user-defined
# time delay. The script assumes the jave code will any other behaviors.

# Imports
import sys
import picamera
import time
import datetime

# Main function that runs when the script is called
def main( std ):
	
	# Have the operating system wait the specified time in seconds - this assumes the 
	# a valid integer for std was passed
	time.sleep(std)
	
	# Define the camera
	camera = picamera.PiCamera()
	
	# Acquire the timestamp
	ts        = time.time()
	timeStamp = datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')
	
	# Take the picture using a timestamp as the filename
	filename = timeStamp + '.jpg'
	camera.capture(filename)
	
	return;

# Get the time delay amount from the command line - user will pass this in the java code using the java Runtime class 
# Convert the command-line passed value to a float.
std = float(sys.argv[1])

# Call the main function upon calling this script and pass the user-specified set time amount (std)
if __name__ == '__main__':
	main(std)

