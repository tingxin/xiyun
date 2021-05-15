import numpy as np
import pandas as pd
from datetime import datetime
from matplotlib import pyplot as plt
from statsmodels.tsa.stattools import adfuller
from statsmodels.tsa.seasonal import seasonal_decompose
from statsmodels.tsa.arima_model import ARIMA
from pandas.plotting import register_matplotlib_converters

register_matplotlib_converters()
window_size = 7


def get_stationarity(timeseries):
    # rolling statistics

    rolling_mean = timeseries.rolling(window=window_size).mean()
    rolling_std = timeseries.rolling(window=window_size).std()
    plt.plot(timeseries, color='blue', label='Original')
    plt.plot(rolling_mean, color='red', label='Rolling Mean')
    plt.plot(rolling_std, color='black', label='Rolling Std')
    plt.legend(loc='best')
    plt.title('Rolling Mean & Rolling Standard Deviation')
    plt.show()

    # Dickey–Fuller test:
    result = adfuller(timeseries['value'])
    print('ADF Statistic: {}'.format(result[0]))
    print('p-value: {}'.format(result[1]))
    print('Critical Values:')
    for key, value in result[4].items():
        print('\t{}: {}'.format(key, value))

def predict(dateset_file, dt_begin_str, dt_end_str):
    dt_begin = datetime.strptime(dt_begin_str, "%Y-%m-%d")
    dt_end = datetime.strptime(dt_end_str, "%Y-%m-%d")

    df = pd.read_csv(dateset_file, parse_dates=['date'], index_col=['date'])
    df = df[df["value"] > 1000]
    print(df.index)
    df = df[df.index < dt_end]
    df = df[df.index > dt_begin]

    df_log = np.log(df)
    # get_stationarity(df_log)
    df_log_shift = df_log - df_log.shift()
    df_log_shift.dropna(inplace=True)

    # decomposition = seasonal_decompose(df_log)
    model = ARIMA(df_log, order=(1, 1, 2))
    results = model.fit(disp=-1)
    plt.plot(df_log_shift)
    plt.plot(results.fittedvalues, color='red')
    plt.show()

    predict_data = model.predict(start="2021-01-01", end="2021-09-01", dynamic=False)


