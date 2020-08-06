import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.regex.Pattern;


public class Host {
    private InetAddress host;
    private NetworkInterface nInterface;
    public Host(byte[] address) {
        try {
            this.host = InetAddress.getByAddress(address);
            this.nInterface = NetworkInterface.getByInetAddress(this.host);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static Host getLocalhost() {
        byte[] addr = new byte[0];
        try {
            addr = InetAddress.getLocalHost().getAddress();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return new Host(addr);
    }
    public String getHostname() {
        return this.host.getHostName();
    }

    public int[] getIpAddress() {
        Pattern pattern = Pattern.compile("([0-9]{1,3}\\.)[0-9]");
        if (pattern.matcher(this.host.getHostName()).find())
            throw new UnknownInterfaceException();
        String[] split = this.host.getHostAddress().split("\\.");
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
