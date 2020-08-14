# ---------- HEADER ---------- ##
# calibrateCamera.py
# Steven Blevins
# 09/05/2017
## --------------------------- ##

## ---------- MAIN CODE ---------- ##

# Provides a 10 second video loop. Used during the camera calibration routine.

#Imports
import picamera
import time

# Instantiate the camera
camera = picamera.PiCamera()

# Start a 'preview' which displays live feed and stop after the specified sleep amount -- hardcoded to 10 seconds for now
camera.start_preview()
time.sleep(10)
camera.stop_preview()