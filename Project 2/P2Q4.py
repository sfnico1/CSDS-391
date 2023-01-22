

import P2Q2
import P2Q3
import matplotlib.pyplot as plt
import math

# learning by using the gradient function multiple times
def learn(weights, stepSize, petalL, petalW, species):
    P2Q2.plotData(petalL, petalW, weights)
    mse = []
    w = []
    counter = 1
    converged = True
    while converged:
        gradient = P2Q3.gradient(petalL, petalW, weights, species)
        weights = weights - stepSize*gradient
        mse.append(P2Q3.meanSquareError(petalL[50:150], petalW[50:150], weights, species[50:150]))
        w.append(weights)
        if mse[counter-1] < .05:  
            converged = False
        counter = counter + 1 
    print("It took", counter-1, "iterations.")
    P2Q2.plotData(petalL, petalW, w[math.floor(counter/2)])
    P2Q2.plotData(petalL, petalW, weights)
    plt.title("Mean Square Error over Tteration")
    plt.ylabel("MSE")
    plt.xlabel("iterations")
    plt.plot(range(1,counter),mse)
    plt.show()
    return weights

