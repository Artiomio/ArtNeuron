import math
def f(x):
    #print("Calculating sin at ", x)
    return math.sin(x)
    
def feedforward(x,y,z):
    A = f(7*x + 5*y +11*z)
    B = f(3*x+4*y+19*z)

    #print("__________1st layer____________")
    #print("Neuron 1: ", A)
    #print("Neuron 2: ", B)
    #print()

    C = f(100*A +10*B)
    D=f(0.1*A+0.3*B)
    E=f(5*A +7*B)




    #print("__________2nd layer____________")
    #print("Neuron 1: ", C)
    #print("Neuron 2: ", D)
    #print("Neuron 3: ", E)



    F = f(3*C+5*D+7*E)

    #print("__________3rd layer____________")
    #print("Neuron 1: ", F)

    return F

r1=feedforward(1,2,3)
d1=-0.5
print("r1=",r1)

r2=feedforward(7,10,0.3)
d2=1
print("r2=",r2)

r3=feedforward(11,13,19)
d3=-1
print("r3=",r3)



print("Assuming the desired output for (1,2,3) is not exactly r1, but ", d1)

mean_square_error = (((r1-d1)**2)+ ((r2-d2)**2) + ((r3-d3)**2))/3
print("Mean square error ", mean_square_error)



