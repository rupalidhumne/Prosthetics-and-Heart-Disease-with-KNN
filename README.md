# Prosthetics-and-Heart-Disease-with-KNN
Code used in my Ultrasound-based Prosthetic Forearm and Heart Disease Severity Classifier Research

Heart Disease Severity Classifier:
-Used the "Cleveland data.xlsx" data to train my classification program
- Declared each patient as an "Instance" (Instance.java)
-Enabled user to manually enter a new "Patient" via "NearestNeighbor.java", which utilized a "Nearest Neighbor object" to obtain the data as well as a manually determined "k" nearest number of neighbors for classification (sorted via CoordSort.java)
-Used "Generic.java" as a means of transferring this kNN model from one solely for the Heart Disease Data Set to one that can be used for all possible data sets (i.e. "fakedata.txt")

Prosthetic Classification Part 1 (Classification Metrics when classifying index and middle fingers)
-Established each motion to be used in training as a type "Instancem.java"
-Established each test motion as type "Instancemtest.java"
-Each finger movement was identified based on a 3D matrix (converted from Matlab) of 640x480 numerical textfile matrices (refer to Motion1_1.txt for an example) , as well as its motion (represented as numerical values)
- Ran the Five Fold Cross Validation Process to establish Classification metrics via three different forms of computing similarity (correlations)
 1) Comparing all data points from each motion to classify the test motion
 2) Aggregating Data into a "Block Mean" system my averaging motion deltas to establish new activity patterns with only 10% of the original data
 3) Randomly hashing 10% of the points from the test and training instances to correlate
-Ran correlations across a test instance and each trainng instance in parallel (using threads) on servers to minimize overall classification time

Prosthetic Classification Part 2 (Classification Metrics when classifying iterations of 11 complex motions on the Raspberry Pi)
-Used the same format of converting ultrasound motion data into "Instancem" and "Instancemtest" types for the new set of complex motions
-Decreased the overall memory usage (processing requirements) experimenting with the amout of data necessary for the classification algorithm to effectively learn and accurately classify the more complex motions.
Methods of correlation
1) Block-mean correlation: Similar to above
2) Region-based correlation: 
