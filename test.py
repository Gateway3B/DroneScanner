import numpy as np
import cv2

img = cv2.imread("opencv-logo.png")
cv2.namedWindow("Image",cv2.WINDOW_NORMAL)
cv2.imshow("Image", img)
cv2.waitKey(0)
cv2.imwrite("output.jpg",img)

#makes an array of 150 rows, 200 columns, 1 channel of unsigned 8bit integer and fills it with zeros
black = np.zeros([150,200,1],'uint8')
#prints all channel values at 0,0 in black
print(black[0,0,:])

white = np.ones([150,200,3],'uint16')
#multiples the matrix of ones by the max size of 2^16-1 making it white
white *= (2**16-1)

#makes a deep copy of white
color = white.copy()

#Moves the window to the top left corner
cv2.moveWindow("Image",0,0)

#prints the info on the mat
print(color.shape)

height,width,channels = color.shape

b,g,r = cv2.split(color)

#shows rgb split and next to each other
rgb_split = np.empty([height,width*3,3],'uint8')
rgb_split[:,0:width] = cv2.merge([b,b,b])
rgb_split[:, width:width*2] = cv2.merge([g,g,g])
rgb_split[:, width*2:width*3] = cv2.merge([r,r,r])

hsv = cv2.cvtColor(color, cv2.COLOR_BGR2HSV)
h,s,v = cv2.split(hsv)
hsv_split = np.concatenate((h,s,v),axis=1)


#Manipulating groups of pixels
color = cv2.imread("butterfly.jpg",1)#1 indicates color mode rgb
gray = cv2.cvtColor(color, cv2.COLOR_RGB2GRAY)#Turns rgb to gray
b = color[:,:,0]#gets all blue
g = color[:,:,1]#gets all green
r = color[:,:,2]#gets all red
rgba = cv2.merge((b,g,r,g))#green now controls transparancy, the more green the less transparent

#Gaussian Blur, Dilation, and Erosion
#Gaussian blur smooths an image by averageing pixels near each other.
#blur a bit on the x axis and a lot on the y axis
blur = cv2.GaussianBlur(image, 5,55)
#the kernel is a template for dilation and erosion
kernel = np.ones((5,5),'uint8')
#turns white pixels into black pixels, eating away at the foreground
dilate = cv2,dilate(image, kernel, iterations=1)
#turns the black pixes into white pixels, eating away at the background
erode = cv2.erode(image,kernel,iterations=1)


#scale and rotate
#scale down by one half in x and y
img_half = cv2.resize(img, (0,0), fy=0.5, fx=0.f)
#resize to 600 by 600 pixels
img_stretch = cv.resize(img, (600,600))
#resize to 600 by 600 without interpolation; no summing of pixels, just choose the nearest, causes pixelation
img_stretch_near = cv2.resize(img, (600,600), interpolation=cv2.INTER_NEAREST)

#creates a rotation matrix for -30 degrees around the point 0, 0 in the top left corner
M = cv2.getRotationMatrix2D((0,0), -30, 1)
#applies the rotation matrix to img. 
rotate = cv2.warpAffine(img, M, (img.shape[1], img.shape[0]))


#VIDEO FEEDS


cv2.destroyAllWindows()

