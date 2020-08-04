import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.regex.Pattern;


public class Localhost {
    private InetAddress localhost;
    private NetworkInterface nInterface;
    public Localhost () {
        try {
            this.localhost = InetAddress.getLocalHost();
            this.nInterface = NetworkInterface.getByInetAddress(localhost);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public String getHostname() {
        return localhost.getHostName();
    }

    public int[] getIpAddress() {
        Pattern pattern = Pattern.compile("([0-9]{1,3}\\.)[0-9]");
        if (pattern.matcher(localhost.getHostName()).find())
            throw new UnknownInterfaceException();
        String[] split = localhost.getHostAddress().split("\\.");
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = Integer.parseInt(split[i]);
        }
        return result;
    }

    public String getIpAddressAsString() {
        return StringUtils.join(ArrayUtils.toObject(this.getIpAddress()), ".");
    }

    public int getPrefix() {
        Pattern pattern = Pattern.compile("([0-9]{1,3}\\.)[0-9]");
        String address = StringUtils.join(ArrayUtils.toObject(this.getIpAddress()), ".");
        final int[] prefix = {0};
        this.nInterface.getInterfaceAddresses()
            .stream()
            .filter(x -> pattern.matcher(x.getAddress().toString()).find() && StringUtils.strip(x.getAddress().toString(), "/").equals(address))
            .forEach(item -> {
                prefix[0] = item.getNetworkPrefixLength();
            });
        return prefix[0];
    }
}

class UnknownInterfaceException extends RuntimeException {
    public UnknownInterfaceException() {
        super("Interface wasn't recognised");
    }
}
