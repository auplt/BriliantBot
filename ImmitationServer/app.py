from flask import Flask
from flask import request
from flask import jsonify
import json

app = Flask(__name__)


# HTTP request example: http://127.0.0.1:5000/api/avg?stud=65913

@app.route('/api/avg', methods=['GET'])
def avg_show():
    stud = int(request.args.get('stud'))
    file_name = "data/" + str(stud) + ".json"
    with open(file_name, encoding="utf8") as json_file:
        json_d = json.load(json_file)
    avg = json_d['avg_points']
    resp = {"avg_points": avg}
    return jsonify(resp)


if __name__ == "__main__":
    app.run(debug=True, port=5000)
