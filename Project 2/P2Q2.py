

import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl

# plotting the data with or without the decision line
def plotData(petalL, petalW, weights,line = True):
    fig = plt.figure()
    data = fig.add_subplot(1, 1, 1)
    plt.title("Iris Data for Class 2 and 3")
    plt.ylabel("Petal Width (cm)")
    plt.xlabel("Petal Length (cm)")
    plt.xlim(2, 8)
    plt.ylim(0.75, 2.75)
    data.scatter(petalL[50:100], petalW[50:100], color='green', label='Versicolor')
    data.scatter(petalL[101:150], petalW[101:150], color='blue', label='Virginica')
    if line:
        x1 = np.linspace(2, 8, 100)
        x2 = []
        for i in x1:
            x2.append(-(weights[0]/weights[1])*i+(weights[2]/weights[1]))
        plt.plot(x1, x2, color='black')
    plt.legend()
    plt.show()


# sigmoid function
def getSigmoid(weights, x1, x2):
    return 1 / (1 + np.exp(-(weights[0]*x1 + weights[1]*x2) + weights[2]))


#3D plots of the weights
def plot3D(weights):
    x1 = np.linspace(0, 7.5, 100) 
    x2 = np.linspace(0, 2.5, 100) 
    x1, x2 = np.meshgrid(x1, x2)
    fig, data = plt.subplots(subplot_kw={"projection": "3d"})
    sigmoid = getSigmoid(weights, x1, x2)
    data.plot_surface(x1, x2,  sigmoid, cmap=mpl.cm.cividis)
    data.set_xlim(0, 7.5)
    data.set_xlabel("Petal Length (cm)")
    data.set_ylim(0, 2.5)
    data.set_ylabel("Petal Width (cm)")
    data.set_zlim(0, 1.0)
    data.set_zlabel("Sigmoid Value")
    plt.show()


# test the given data points and plotting them
def testClassifier(petalL, petalW, species, weights):
    versicolorPL = []
    versicolorPW = []
    virginicaPL = []
    virginicaPW = []
    index = np.array([50,60,70,80,85,100,105,119,129,134])
    for i in range(10):
        estimatedClass = "virginica"
        sigmoid = getSigmoid(weights, petalL[index[i]], petalW[index[i]])
        if sigmoid < 0.5:
            estimatedClass = "versicolor"
        print("Real Class:", species[index[i]], ", Estimated Class:", estimatedClass, "(",round(sigmoid, 3), ")")
        if species[index[i]] == 'versicolor':
            versicolorPL.append(petalL[index[i]])
            versicolorPW.append(petalW[index[i]])
        else:
            virginicaPL.append(petalL[index[i]])
            virginicaPW.append(petalW[index[i]])
    x1 = np.linspace(2, 8, 100)
    x2 = []
    for i in x1:
        x2.append(-(weights[0]/weights[1])*i+(weights[2]/weights[1]))
        
    fig = plt.figure()
    data = fig.add_subplot(1, 1, 1)
    plt.title("10 Data Points")
    plt.ylabel("Petal Width (cm)")
    plt.xlabel("Petal Length (cm)")
    plt.xlim(2, 8)
    plt.ylim(0.75, 2.75)
    data.scatter(versicolorPL, versicolorPW, color='green', label='Versicolor')
    data.scatter(virginicaPL, virginicaPW, color='blue', label='Virginica')
    plt.plot(x1, x2, color='black')
    plt.legend()
    plt.show()
