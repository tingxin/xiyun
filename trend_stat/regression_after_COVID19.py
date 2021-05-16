from business.regression import predict
from setting import dateset_file
if __name__ == '__main__':
    predict(dateset_file, dt_begin_str="2020-04-01", dt_end_str="2020-12-31")