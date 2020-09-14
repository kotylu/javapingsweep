from network import Network


net = Network()
def valid_ping():
    assert net.validate_ping("127.0.0.1") == 1

def noaddress_ping():
    assert net.validate_ping("") == -1

def invalid_ping():
    assert net.validate_ping("123.123.123.123") == 0


valid_ping()
invalid_ping()
noaddress_ping()

