import json
import re

file_path = "/Users/fugui/Work/project/output/noise.csv"
file_path2 = "/Users/fugui/Work/project/output/stat.json"
file_path3 = "/Users/fugui/Work/project/output/stat3.json"
stat = dict()
stat_2 = dict()
line_count = 0
if __name__ == '__main__':
    print("begin")
    with open(file_path) as f:
        for line in f:
            line_count += 1
            items = re.split(",|\n", line)
            for item in items:
                array = item.split('=')
                key = array[0].strip()
                if key == "tpep_pickup_datetime":
                    if array[1] == "":
                        if key not in stat:
                            stat[key] = 1
                        else:
                            stat[key] += 1

                elif key == "tpep_dropoff_datetime":
                    if array[1] == "":
                        if key not in stat:
                            stat[key] = 1
                        else:
                            stat[key] += 1
                elif key == "passenger_count":
                    if key == "" or int(array[1]) == 0:
                        if key not in stat:
                            stat[key] = 1
                        else:
                            stat[key] += 1
                elif key == "trip_distance":
                    if array[1] == "" or float(array[1]) == 0:
                        if key not in stat:
                            stat[key] = 1
                        else:
                            stat[key] += 1
                elif key == "speed":
                    if array[1] == "" or float(array[1]) == 0:
                        if key not in stat:
                            stat[key] = 1
                        else:
                            stat[key] += 1
                elif key == "PULocationID":
                    if array[1] != "":
                        if array[1] not in stat_2:
                            stat_2[array[1]] = 1
                        else:
                            stat_2[array[1]] += 1

                elif key == "DOLocationID":
                    if array[1] != "":
                        if array[1] not in stat_2:
                            stat_2[array[1]] = 1
                        else:
                            stat_2[array[1]] += 1

    stat["total"] = line_count
    with open(file_path2, mode='w') as f2:
        json.dump(stat, f2)

    with open(file_path3, mode='w') as f3:
        json.dump(stat_2, f3)