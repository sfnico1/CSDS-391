

import math
import numpy as np
import matplotlib.pyplot as plt
import random


#getting distances from each point
def getDist(data, clusters, num):
    allDist = np.array([]).reshape(150,0)
    for i in range(num):
        dist = np.sum((data-clusters[i])**2,axis=1)
        allDist = np.c_[allDist,dist]
    return allDist

# updating the clusters using the min distances
def updateClusters(petalL, petalW, min, clusters, num):
    temp = [0]
    for i in range(num):
        temp.append(np.array([]).reshape(2,0))
    for i in range(150):
        temp[min[i]]=np.c_[temp[min[i]],[petalL[i],petalW[i]]]
    for i in range(num):
        clusters[i]=np.mean(temp[i+1].T,axis=0)
    return clusters

# making the clusters
def makeClusters(petalL, petalW, lines = True):
    data = np.concatenate(([petalL], [petalW])).T
    clusters = []
    for i in range(3):
        choice = math.floor(random.uniform(0+i*50, (i+1)*50))
        clusters.append([petalL[choice],petalW[choice]])
        
    for i in range(50):
        allDist = getDist(data, clusters, 3)
        clusters = updateClusters(petalL, petalW, np.argmin(allDist,axis=1)+1, clusters, 3)

    fig = plt.figure()
    data = fig.add_subplot(1, 1, 1)
    plt.title("Iris Data with Clustering")
    plt.ylabel("Petal Width (cm)")
    plt.xlabel("Petal Length (cm)")
    data.scatter(petalL[0:49], petalW[0:49], color='red', label='Setosa')
    data.scatter(petalL[50:100], petalW[50:100], color='green', label='Versicolor')
    data.scatter(petalL[101:150], petalW[101:150], color='blue', label='Virginica')
    for i in range(3):
        data.scatter(clusters[i][0], clusters[i][1], marker='x', s=100, color='black')
    if lines == True:
        for i in range(2):
            mid = [(clusters[i][0] + clusters[i+1][0])/2, (clusters[i][1] + clusters[i+1][1])/2]
            slope = (clusters[i][0] - clusters[i+1][0])/(clusters[i+1][1] - clusters[i][1])
            plt.plot(np.linspace(1,8,10), slope*np.linspace(1,8,10) + mid[1] - slope*mid[0], 'k')
        plt.ylim(0, 3)
    plt.legend()
    plt.show()

# finding the distortion of the clusters 
def distortion(petalL, petalW):
    data = np.concatenate(([petalL], [petalW])).T
    y = []  
    for i in range(1,15):
        total = 0
        for j in range(0, 100):
            centroids = []
            for k in range(i):
                centroids.append(data[random.randint(0,149)])  
            allDist = getDist(data, centroids, i)
            min = np.argmin(allDist,axis=1)
            for k in range(150):
                total =  total + allDist[k][min[k]]
        y.append(total/100)

    fig = plt.figure()
    data = fig.add_subplot(1, 1, 1)
    plt.title("Distortion over Clusters")
    plt.xlabel("Clusters")
    plt.ylabel("Distortion")
    data.scatter(range(1,15), y, marker='x', s=100, color='black')
    plt.show()

# plotting the change in clusters over updates 
def plotClusters(petalL, petalW, num):
    fig = plt.figure()
    data = np.concatenate(([petalL], [petalW])).T
    data2 = fig.add_subplot(1, 1, 1)
    data2.scatter(petalL[0:49], petalW[0:49], color='red', label='Setosa')
    data2.scatter(petalL[50:100], petalW[50:100], color='green', label='Versicolor')
    data2.scatter(petalL[101:150], petalW[101:150], color='blue', label='Virginica')
    centroids = []
    for i in range(num):
        centroids.append(data[random.randint(0,149)])  
        data2.scatter(centroids[i][0], centroids[i][1], s=200, alpha=0.75, c='purple', label='Initial')
    
    for i in range(5):
        allDist = getDist(data, centroids, num)
        centroids = updateClusters(petalL, petalW, np.argmin(allDist,axis=1)+1, centroids, num)
        if i == 2: 
            for j in range(num):
                data2.scatter(centroids[j][0], centroids[j][1], s=200, alpha=0.75, c='pink', label='Intermediate')
        elif i == 4:
            for j in range(num):
                data2.scatter(centroids[j][0], centroids[j][1], s=200, alpha=0.75, c='yellow', label='Converged')
    
    plt.title("Clusters over time, k = 3")
    plt.legend()
    plt.ylabel("Petal Width (cm)")
    plt.xlabel("Petal Length (cm)")
    plt.ylim(0, 3.5)
    plt.xlim(0, 8)
    plt.show()
