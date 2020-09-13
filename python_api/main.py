from flask import Flask, request
import subprocess

app = Flask(__name__, instance_relative_config=True)

@app.route('/ping')
def ping():
    address = request.args.get("address")
    if address is None:
        return False
    cmd = subprocess.Popen(["ping", address, "-n", "1", "-w", "1"], stdout=subprocess.PIPE)
    return str("timed out" not in cmd.communicate()[0].decode('utf-8'))

