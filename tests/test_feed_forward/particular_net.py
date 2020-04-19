import math
def f(x):
    print("Требуется вычислить синус от", x)
    return math.sin(x)

x = 8
y = 9
z = 10
q = 11


x = 1
y=2
z=17
q=19




A = f(3*x + 5*y +7*z + 2*q)
B = f(9*x+0.5*y+4*z +7*q)

print("__________1st layer____________")
print("Neuron 1: ", A)
print("Neuron 2: ", B)
print()



C = f(3*A +19*B)


print("__________2nd layer____________")
print("Neuron 1: ", C)




A1 = f(5*C)
A2 = f(3*C)
A3 = f(0.1*C)


print("__________3rd layer____________")
print("Neuron 1: ", A1)
print("Neuron 2: ", A2)
print("Neuron 3: ", A3)



L = f(1*A1+2*A2+3*A3)
print("__________3rd layer____________")
print("Last neuron:", L)