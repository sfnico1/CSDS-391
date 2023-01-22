

import numpy as np
import matplotlib.pyplot as plt
import matplotlib as mpl
from sklearn import svm, datasets
from sklearn.inspection import DecisionBoundaryDisplay
import tensorflow as tf
from tensorflow import keras

# finding different boundaries
def diffBoundaries(sepalL, sepalW, petalL, petalW, choice = False):
    iris = datasets.load_iris()
    data = np.concatenate(([petalL], [petalW])).T
    if choice == True:
        data = np.concatenate(([sepalL], [sepalW])).T

    models = [svm.SVC(kernel="linear"), svm.SVC(kernel="rbf", gamma=1), svm.SVC(kernel="poly", degree=5, gamma=3)]
    titles = ["Linear","RBF (gamma = 1)","Polynomial (5th Degree, gamma = 3)"]

    for i in range(3):
        DecisionBoundaryDisplay.from_estimator(models[i].fit(data, iris.target),data,cmap=mpl.cm.jet,response_method="predict")
        if choice == False:
            plt.scatter(petalL[0:49], petalW[0:49], color='red', label='Setosa')
            plt.scatter(petalL[50:100], petalW[50:100], color='green', label='Versicolor')
            plt.scatter(petalL[101:150], petalW[101:150], color='blue', label='Virginica')
            plt.ylabel("Petal Width (cm)")
            plt.xlabel("Petal Length (cm)")
        else: 
            plt.scatter(sepalL[0:49], sepalW[0:49], color='red', label='Setosa')
            plt.scatter(sepalL[50:100], sepalW[50:100], color='green', label='Versicolor')
            plt.scatter(sepalL[101:150], sepalW[101:150], color='blue', label='Virginica')
            plt.ylabel("Sepal Width (cm)")
            plt.xlabel("Sepal Length (cm)")
        plt.legend()
        plt.title(titles[i])
    plt.show()
    
# testing different stepsizes
def testStepSize(sepalL, sepalW, petalL, petalW, stepSize):
    fullData = np.concatenate(([sepalL], [sepalW], [petalL], [petalW])).T
    Y = []
    temp = np.array([1, 0, 0])
    for i in range(150):
        Y.append(temp)
        if i == 50:
            temp = np.array([0, 1, 0])
        if i == 100:
            temp = np.array([0, 0, 1])
    Y = np.asarray(Y)
    
    model = keras.models.Sequential()
    model.add(keras.Input((fullData[0].size)))
    model.add(keras.layers.Dense(3, activation='softmax'))

    model.compile(optimizer=keras.optimizers.Adam(learning_rate = stepSize), loss=keras.losses.categorical_crossentropy, metrics=['accuracy'])
    history = model.fit(fullData, Y, epochs = 250)
    #print(history.history)





