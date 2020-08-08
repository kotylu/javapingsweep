class UnknownInterfaceException extends RuntimeException {
    public UnknownInterfaceException() {
        super("Interface wasn't recognised");
    }
}