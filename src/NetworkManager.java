import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;


public class NetworkManager {
    private final String networkAddress;
    private final int prefix;

    public NetworkManager(int[] localhost, int prefix) {
        this.prefix = prefix;
        String binary = String.join("", toBinaryOktet(localhost[0]),
                                                toBinaryOktet(localhost[1]),
                                                toBinaryOktet(localhost[2]),
                                                toBinaryOktet(localhost[3]));
        this.networkAddress = StringUtils.rightPad(binary.substring(0, prefix), 32, "0");
    }

    public ArrayList<String> pingSweep(IPing cmdPing) {
        ArrayList<String> result = new ArrayList<>();
        char[] base = this.networkAddress.toCharArray();

        //TODO algorithm: calculate all ips
        this.validate(cmdPing, result, base);

        return result;
    }

    private void validate(IPing cmdPing, ArrayList<String> data, char[] value) {
        System.out.println(StringUtils.join(ArrayUtils.toObject(value), ""));
        System.out.println(this.getFromBinary(StringUtils.join(ArrayUtils.toObject(value), "")));

        String valid = cmdPing.cmdPing(this.getFromBinary(StringUtils.join(ArrayUtils.toObject(value), "")));
        System.out.println(valid);
        if (valid != null) {
            data.add(valid);
        }

    }

    private String toBinaryOktet(int value) {
        return StringUtils.leftPad(Integer.toBinaryString(value), 8, "0");
    }

    private String getFromBinary(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int decimal = Integer.parseInt(value.substring(i*8,i*8+8),2);
            result.append(decimal).append(".");
        }
        return result.deleteCharAt(result.length()-1).toString();
    }
}
