from business.linear_regression import predict
from setting import dateset_file
if __name__ == '__main__':
    predict(dateset_file, dt_begin_str="2019-01-01", dt_end_str="2020-03-12")