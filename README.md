# EMNIST Character Recognizer
This project is an Android app that allows users to draw doodles on the screen and classify them into [Extended MNIST characters.](https://www.nist.gov/itl/products-and-services/emnist-dataset)

The app is built using the **Java** programming language and **Android Studio** development environment. The classification is done using custom made **Tensorflow** model trained on the **EMNIST** dataset 

![TFlite banner](tflite.png?raw=true "TFLite Banner")


## Description

### TensorFlow Model
Python notebook `mnist_mnist_CNN.ipynb` helps you build the required TensorFlow model.

1. EMNIST dataset consists of **47 classes** and each example is a **handwritten character**.
    - Example images are resized into 28x28 shape and then their pixel values are normalized.
    - We also define the labels for each example between 0 and 46.
  
2. The model is a **Convolutional Neural Network** built using **Keras** API.
    - Model consists of three **Convolutional** layers followed by a **MaxPooling layer** each.
    - These layers are then connected to a **Feed Forward** network with **Softmax** activation for output layer.
    - Model is compiled using **Categorical Crossentropy** loss along with *AdaDelta* optimizer.
    - Model is trained using ImageDataGenerators that have 20% validation split and persforms augmentation to the images.
    - After training, the model is stored as a **Protobuf** file that contains model architecture and its weights.


### Android App
1. `MainActivity.java`: This file initializes the custom canvas (DrawView) and performs inference on user input doodles.
2. `TensorFlowClassifier.java`: This files loads our TensorFlow model and contains implementation of classifying the characters. 
3. `views folder`: This folder contains files to implement Draview to draw on the app screen and convert it into images that can be used as input to our model. 
