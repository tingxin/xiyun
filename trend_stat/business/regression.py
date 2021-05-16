from datetime import datetime
from sklearn.preprocessing import PolynomialFeatures
from datetime import datetime
from sklearn.linear_model import LinearRegression
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.linear_model import Ridge
from sklearn.linear_model import Lasso
from sklearn import neighbors
from sklearn import ensemble
from numpy import shape
from sklearn import metrics
from sklearn import svm
import matplotlib.pyplot as plt
import sklearn.pipeline as pl
import sklearn.linear_model as lm
import sklearn.preprocessing as sp
import matplotlib.pyplot as mp
import numpy as np
import sklearn.metrics as sm

train_size = 0.8

def try_different_method(models, x_train, y_train, x_test, y_test):
    plt.figure()
    plt.plot(np.arange(len(x_test)), y_test, "go-", label="True value")
    for method in models:
        v = models[method]
        color = v['color']
        model = v['model']
        model.fit(x_train, y_train)
        score = model.score(x_test, y_test)
        result = model.predict(x_test)
        plt.plot(np.arange(len(result)), result, "ro-", color=color, label=method)

        print(f"method:{method}---score:{score}")

    plt.legend(loc="best")
    plt.show()


def parse_data(dateset_file, dt_begin_str, dt_end_str):
    begin_date = datetime(2019, 1, 1)
    x_data = list()
    y_data = list()

    dt_begin = datetime.strptime(dt_begin_str, "%Y-%m-%d")
    dt_end = datetime.strptime(dt_end_str, "%Y-%m-%d")
    with open(dateset_file) as f:
        for line in f:
            items = line.split(",")
            if items[0] == 'date':
                continue
            dt = datetime.strptime(items[0], "%Y-%m-%d")
            if dt_begin <= dt <= dt_end:
                day_index = (dt - begin_date).days
                v = int(items[1])
                if v < 1000:
                    # 过滤噪音数据
                    continue

                x_data.append([day_index])
                y_data.append(int(items[1]))
    # y_data= np.log(y_data)
    return x_data, y_data


def predict(dateset_file, dt_begin_str, dt_end_str):
    x_data, y_data = parse_data(dateset_file, dt_begin_str, dt_end_str)
    train_len = int(len(x_data) * train_size)
    x_train, x_test = x_data[0: train_len], x_data[train_len:]
    y_train, y_test = y_data[0: train_len], y_data[train_len:]

    # 多项式
    poly = pl.make_pipeline(sp.PolynomialFeatures(3), lm.LinearRegression())
    ridge_reg = Ridge(alpha=0.1, solver="cholesky")
    tree_reg = DecisionTreeRegressor()
    forest_reg = RandomForestRegressor(n_estimators=20)
    model_k_neighbor = neighbors.KNeighborsRegressor()
    model_svm = svm.SVR()
    # 岭回归，线性回归的正规化版

    models = dict()
    models["polynomial"] = {"model": poly, "color": 'red'}
    models["Ridge"] =  {"model": ridge_reg, "color": 'blue'}
    models["Decision Tree"] =  {"model": tree_reg, "color": 'green'}
    models["Random Forest"] =  {"model": forest_reg, "color": 'orange'}
    models["k neighbor"] = {"model": model_k_neighbor, "color": 'purple'}
    models["svm"] = {"model": model_svm, "color": 'gray'}

    try_different_method(models, x_train, y_train, x_test, y_test)
