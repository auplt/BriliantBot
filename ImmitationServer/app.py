from flask import Flask
from flask import request
from flask import jsonify
import json

#
with open('test.json') as json_file:
    json_data = json.load(json_file)
    # json_data=json.dumps(json_data).encode()
    # print(json_data)

# with open('test.json', 'w') as f:
#     j = json.dump(json_data, f, indent=2, ensure_ascii=False)
#     j= json(j)
app = Flask(__name__)
# app.config['JSON_AS_ASCII'] = False

@app.route('/api/rows', methods=['GET'])
def rows_show():
    row_id = int(request.args.get('id'))
    data = [x for x in json_data if x["id"] == row_id]
    # urllib.parse.unquote(
    # f = json.dumps(data, ensure_ascii=False)
    # print(f)
    # print(type(jsonify(data)))
    print(type(data))
    f = open('xyz.txt', 'w')  # открытие в режиме записи
    for item in data:
        f.write("%s\n" % item)
    f.close()
    import json

    with open("123.json", "w", encoding="utf-8") as file:
        json.dump(data, file)
    return jsonify(data)
        # .decode()

# @app.route('/api/rows', methods=['GET'])
# def rows_index():
#     return jsonify(json_data)

# linux (ubuntu + packages)
# php-postgresql
# php-pgsql
# moodle
# nginx
# php pm
# Postgres

# php-pgsql
# import urllib.request
# import urllib.parse
# url = 'http://127.0.0.1:5000/api/rows'
# values = {
# 'info':'Новая строка'
# }
# data = urllib.parse.urlencode(values).encode()
# req = urllib.request.Request(url, data)
# page_string = urllib.request.urlopen(req).read()
# print(page_string)