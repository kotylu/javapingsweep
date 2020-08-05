public class Main {
    public static void main(String[] args) {
        System.out.println("starting...");

        Localhost localhost = new Localhost();
        System.out.println(localhost.getHostname());
        System.out.println(Integer.toBinaryString(localhost.getIpAddress()[0]));
        System.out.println(localhost.getPrefix());

        System.out.println("---------------------");

        CmdRunner runner = new CmdRunner();
        System.out.println(runner.cmdPing(localhost.getIpAddressAsString()));

        System.out.println("---------------------");

        NetworkManager nm = new NetworkManager(localhost.getIpAddress(), localhost.getPrefix());
        nm.pingSweep(runner);
        for (String item : nm.pingSweep(runner)) {
            System.out.println(item);
        }
        System.out.println("---------------------");

        System.out.println("ending...");
        // for closing shit and stuff
        System.out.println("ended");
    }
}
