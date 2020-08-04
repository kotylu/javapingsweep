public class Main {
    public static void main(String[] args) {
        System.out.println("starting...");

        Localhost localhost = new Localhost();
        System.out.println(localhost.getHostname());
        System.out.println(localhost.getIpAddressAsString());
        System.out.println(localhost.getPrefix());

        System.out.println("ending...");
        // for closing shit and stuff
        System.out.println("ended");
    }
}
