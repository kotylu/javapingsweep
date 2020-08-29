import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;


public class NetworkManager {
    private final String networkAddress;
    private final int prefix;
    private final ArrayList<String> possibilities;
    private ArrayList<String> lives;
    private ICmd cmdPing;

    public NetworkManager(int[] host, int prefix) {
        this.possibilities = new ArrayList<>();
        this.lives = new ArrayList<>();
        this.prefix = prefix;
        String binary = String.join("", toBinaryOktet(host[0]),
                                                toBinaryOktet(host[1]),
                                                toBinaryOktet(host[2]),
                                                toBinaryOktet(host[3]));
        this.networkAddress = StringUtils.rightPad(binary.substring(0, prefix), 32, "0");
    }

    public ArrayList<String> getLiveDev() {
        if (this.lives.isEmpty())
            this.loadLiveDevices(this.cmdPing);
        return this.lives;
    }

    public ArrayList<String> loadLiveDevices(ICmd cmdPing) {
        this.cmdPing = cmdPing;
        if (this.possibilities.isEmpty())
            this.genBinaryOptions(32-this.prefix, new int[32-this.prefix], 0);

        //TODO status bar
        //TODO make it run async without getting my mac into space
        int i = 1;
        for (String item : this.possibilities) {
            int info = i;
                    this.validate(cmdPing, String.format("%s%s", this.networkAddress.substring(0, this.prefix), item).toCharArray(), info);
            i++;
        }

        return this.lives;
    }

    private void validate(ICmd cmdPing, char[] value, int step) {
        //TODO debugger: be able to see all of them - System.out.println(StringUtils.join(ArrayUtils.toObject(value), ""));
        //System.out.println(this.getFromBinary(StringUtils.join(ArrayUtils.toObject(value), "")));

        System.out.printf("%g%s%n",(((double)step/this.possibilities.size())*100), "%");
        String valid = cmdPing.cmdPing(this.getFromBinary(StringUtils.join(ArrayUtils.toObject(value), "")));
        if (valid != null) {
            this.lives.add(valid);
        }

    }

    private void genBinaryOptions(int count, int[] array, int v) {
        if (v == count)
        {
            this.possibilities.add(StringUtils.join(ArrayUtils.toObject(array), ""));
            return;
        }
        array[v] = 0;
        this.genBinaryOptions(count, array, v + 1);
        array[v] = 1;
        this.genBinaryOptions(count, array, v + 1);

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
