from datetime import datetime
from sklearn.preprocessing import PolynomialFeatures
from datetime import datetime
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from numpy import shape
from sklearn import metrics
import matplotlib.pyplot as plt
import sklearn.pipeline as pl
import sklearn.linear_model as lm
import sklearn.preprocessing as sp
import matplotlib.pyplot as mp
import numpy as np
import sklearn.metrics as sm


def predict(dateset_file, dt_begin_str, dt_end_str):
    begin_date = datetime(2019, 1, 1)
    X = list()
    y = list()

    dt_begin = datetime.strptime(dt_begin_str, "%Y-%m-%d")
    dt_end = datetime.strptime(dt_end_str, "%Y-%m-%d")
    with open(dateset_file) as f:
        for line in f:
            items = line.split(",")
            print(items)
            dt = datetime.strptime(items[0], "%Y/%m/%d")
            if dt_begin <= dt <= dt_end:
                day_index = (dt - begin_date).days
                v = int(items[1])
                if v < 1000:
                    # 过滤噪音数据
                    continue

                X.append([day_index])
                y.append(int(items[1]))
    print("a")
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)
    model = pl.make_pipeline(sp.PolynomialFeatures(7), lm.LinearRegression())
    # 训练模型
    model.fit(X_train, y_train)
    # 求预测值y
    y_predict = model.predict(X_test)

    model2 = LinearRegression()
    model2.fit(X_train, y_train)
    y_predict2 = model2.predict(X_test)
    print("MSE: {0}".format(metrics.mean_squared_error(y_test, y_predict)))

    plt.scatter(X_test, y_predict, color='y', marker='o', label='polynomial regression')
    plt.scatter(X_test, y_predict2, color='r', marker='o', label='linear regression')
    plt.scatter(X_test, y_test, color='g', marker='+', label='Original')
    plt.show()
