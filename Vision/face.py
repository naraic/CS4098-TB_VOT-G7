import cv2 

face_cascade = cv2.CascadeClassifier('/home/naraic/.local/lib/python3.6/site-packages/cv2/data/haarcascade_frontalface_default.xml')

'''
f_img = cv2.imread('2019-02-14-230929.jpg')

gray_f_img= cv2.cvtColor(f_img, cv2.COLOR_BGR2GRAY)

faces = face_cascade.detectMultiScale(gray_f_img, 1.1, 5)
for (x,y,w,h) in faces:
    cv2.rectangle(f_img,(x,y),(x+w,y+h),(255,0,0),2)
    roi_gray = gray_f_img[y:y+h, x:x+w]
    roi_color = f_img[y:y+h, x:x+w]

cv2.imshow('f_img', f_img)
cv2.waitKey(0)
cv2.destroyAllWindows()

f_img = cv2.imread('2019-02-14-230941.jpg')

gray_f_img= cv2.cvtColor(f_img, cv2.COLOR_BGR2GRAY)

faces = face_cascade.detectMultiScale(gray_f_img, 1.1, 5)
for (x,y,w,h) in faces:
    cv2.rectangle(f_img,(x,y),(x+w,y+h),(255,0,0),2)
    roi_gray = gray_f_img[y:y+h, x:x+w]
    roi_color = f_img[y:y+h, x:x+w]

cv2.imshow('f_img', f_img)
cv2.waitKey(0)
cv2.destroyAllWindows()
'''


cap = cv2.VideoCapture('2019-02-14-222520.webm')
cap = cv2.VideoCapture('2019-02-15-002326.webm')

fourcc = cv2.VideoWriter_fourcc(*'XVID')
out = cv2.VideoWriter('facevid.avi',fourcc, 10.0, (1280,720))

while(cap.isOpened()):
  ret, frame = cap.read()
  if ret == True:
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray, 1.1, 5)
    if len(faces) is not 0:
      out.write(frame)
  else:
    break

cap.release()
out.release()
cv2.destroyAllWindows()

