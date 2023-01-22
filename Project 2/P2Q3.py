

import numpy as np
import matplotlib.pyplot as plt
import math


# mse function
def meanSquareError(petalL, petalW, weights, species):
    sum = 0
    for i in range(len(petalL)):
        sigmoid = 1 / (1 + np.exp(-(weights[0]*petalL[i] + weights[1]*petalW[i]) + weights[2]))
        real = 1
        if species[i] == "versicolor":
            real = 0
        sum = sum + (real - sigmoid)**2
    return sum/len(petalL)

# gradient function
def gradient(petalL, petalW, weights, species):
    gradient = np.zeros(3)
    for i in range(100):
        index = i + 50
        y = 1.0
        if species[index] == "versicolor":
            y = 0.0
        sigmoid = 1 / (1 + math.exp(-(weights[0]*petalL[index] + weights[1]*petalW[index]) + weights[2]))
        gradient[0] = gradient[0] + 2*(sigmoid - y)*sigmoid*(1 - sigmoid)*petalL[index]
        gradient[1] = gradient[1] + 2*(sigmoid - y)*sigmoid*(1 - sigmoid)*petalW[index]
        gradient[2] = gradient[2] - 2*(sigmoid - y)*sigmoid*(1 - sigmoid)
    return gradient

# plotting two decision lines 
def plotData(petalL, petalW, weights, badWeights):
    x1 = np.linspace(2, 8, 100)
    x2 = []
    x3 = []
    for i in x1:
        x2.append(-(weights[0]/weights[1])*i+(weights[2]/weights[1]))
        x3.append(-(badWeights[0]/badWeights[1])*i+(badWeights[2]/badWeights[1]))
        
    fig = plt.figure()
    data = fig.add_subplot(1, 1, 1)
    data.scatter(petalL[50:100], petalW[50:100], color='green', label='Versicolor')
    data.scatter(petalL[101:150], petalW[101:150], color='blue', label='Virginica')
    plt.plot(x1, x2, color='black')
    plt.plot(x1, x3, color='red')
    plt.title("Iris Data")
    plt.ylabel("Petal Width (cm)")
    plt.xlabel("Petal Length (cm)")
    plt.legend()
    plt.show()
