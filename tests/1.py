import math
w1 = -0.14
w2 = 0.137
w3 = -0.13

def ff(x,y,z):
    return math.sin(w1*x+w2*y+w3*z)





print(( (ff(1,2,1)-1)**2 + (ff(2,3,1)-1)**2 + (ff(3,1,1)-0)**2 + (ff(4,3,1)-0)**2)/4)
