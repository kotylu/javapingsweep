import subprocess


class Network:
    def __init__(self):
        pass

    def validate_ping(self, address):
        if address is None or address == "":
            return -1
        cmd = subprocess.Popen(["ping", address, "-n", "1", "-w", "1"], stdout=subprocess.PIPE)
        return int("timed out" not in cmd.communicate()[0].decode('utf-8'))

