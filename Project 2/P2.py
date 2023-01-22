

import P2Q1
import P2Q2
import P2Q3
import P2Q4
import P2Q5
import csv
import random
import numpy as np

#get data
data = open('CSDS-391/irisdata.csv')
data = csv.reader(data)
sepalL = []
sepalW = []
petalL = []
petalW = []
species = []
weights = np.array([.5, 2.2, 6]).T # [w1 w2 bias]
stepSize = 0.01

for i in data:
    if data.line_num > 1:
        sepalL.append(float(i[0]))
        sepalW.append(float(i[1]))
        petalL.append(float(i[2]))
        petalW.append(float(i[3]))
        species.append(i[4])


#comment out commands to prevent unwanted graphs
#Q1
#1a
P2Q1.makeClusters(petalL, petalW, False)

#1b
P2Q1.distortion(petalL, petalW)

#1c
P2Q1.plotClusters(petalL, petalW, 2)
P2Q1.plotClusters(petalL, petalW, 3)

#1d
P2Q1.makeClusters(petalL, petalW, True)


#Q2
#2c
P2Q2.plotData(petalL, petalW, weights) 

#2d 
P2Q2.plot3D(weights) 

#2e
P2Q2.testClassifier(petalL, petalW, species, weights)



#Q3
#3a/b
print(P2Q3.meanSquareError(petalL[50:150], petalW[50:150], weights, species[50:150]))

badWeights = np.array([.2, 3, 7.5]).T
P2Q3.plotData(petalL, petalW, weights, badWeights)
print(P2Q3.meanSquareError(petalL, petalW, badWeights, species))

#3e
P2Q2.plotData(petalL, petalW, weights)
gradient = P2Q3.gradient(petalL, petalW, weights, species)
weights = weights - stepSize*gradient
print(weights)
P2Q2.plotData(petalL, petalW, weights)

# Part 4:
#4b
newWeights = P2Q4.learn(weights, stepSize, petalL, petalW, species)

#4c
newWeights = np.array([random.uniform(.1, 1.5),random.uniform(1.5, 2.5),random.uniform(4, 8)])
print(P2Q3.meanSquareError(petalL, petalW, newWeights, species))
newWeights = P2Q4.learn(newWeights, stepSize, petalL, petalW, species)


# Part 5: 

#5a 
P2Q5.diffBoundaries(sepalL, sepalW, petalL, petalW, choice = False)

#5b
P2Q5.testStepSize(sepalL, sepalW, petalL, petalW, 10)

