# Binary Classification ML Library in Java

## Overview
This project involves creating a **simple machine learning library** in **Java** that supports **binary classification**. The library allows users to select from multiple classification algorithms, train models on a dataset, and evaluate their performance using various statistical measures. The implementation utilizes **object-oriented principles** and leverages key **Java features** including **interfaces, inheritance, polymorphism, generics, collections, exceptions, I/O streams, serialization, functional interfaces, and Java 8 streams**.

## Key Features

### 1. Multiple Classification Algorithms
- Implement at least three binary classification algorithms (e.g., **K-Nearest Neighbors (KNN)**, **Perceptron**, **Naïve Bayes**, **Logistic Regression**, **Decision Tree**).
- Each classifier follows a common interface to ensure consistency in **training** and **testing** operations.

### 2. Training and Testing
- **Data Loading**: Read data from a file (CSV) or a database.
- **Model Training**: Train each classifier using a provided training set.
- **Model Testing**: Test the trained models on a separate test set.
- **Model Persistence**: Serialize trained models for future reuse.

### 3. Performance Evaluation
- Implement various evaluation metrics, such as:
  - **True Positives (TP), False Positives (FP), True Negatives (TN), False Negatives (FN)**
  - **Precision**
  - **Recall**
  - **F1 Score**
- Provide a means to calculate and display these metrics for each classifier.

### 4. Configurable Hyperparameters
- Allow classifier selection and hyperparameter tuning via:
  - An **external configuration file**
  - User input at the **start of the application**

### 5. Real Dataset Testing
- Use the **Pima Indians Diabetes** dataset to showcase how the library performs on a real-world problem.
- Split the dataset into **training** and **testing** sets.
- **Compare** and **report** performance results to determine the best classifier for this dataset.

## Implementation Details

- **Object-Oriented Design**: Utilize **interfaces**, **inheritance**, and **polymorphism** to create a structured hierarchy for classifiers and evaluation measures. Implement a clear separation of concerns: data handling, model training, model evaluation, etc.
- **Generics and Collections**: Employ **Java generics** and **collections** to handle datasets flexibly, using lists, maps, or other appropriate structures for storing and processing data.
- **Exceptions and I/O**: Use **exception handling** to manage errors that may occur when reading files or interacting with a database. Incorporate **I/O streams** for reading data and **serialization** for saving models.
- **Functional Interfaces and Streams**: Leverage **Java 8 streams** for data processing (e.g., filtering, mapping). Define **functional interfaces** where only one abstract method is required (e.g., for evaluation measure calculation).

## Conclusion
By building this **Binary Classification ML Library** in Java, you will gain practical experience in **object-oriented design**, **data handling**, **model evaluation**, and **modern Java features**. The final product is a **lightweight yet powerful** tool for experimenting with different binary classification algorithms and analyzing their performance on real-world datasets.
