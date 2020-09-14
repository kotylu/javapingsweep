from flask import Flask, request
from network import Network


app = Flask(__name__, instance_relative_config=True)
net = Network()

@app.route('/ping')
def ping():
    address = request.args.get("address")
    return str(net.validate_ping(address))