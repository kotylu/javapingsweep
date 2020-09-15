import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;


public class NetworkManager {
    private String networkAddress;
    private int prefix;
    private ArrayList<String> lives;
    private ICmd cmdPing;

    public NetworkManager(int[] host, int prefix) {
        this.lives = new ArrayList<String>();
        this.prefix = prefix;
        String binary = String.join("", getBinaryOktet(host[0]),
                                                getBinaryOktet(host[1]),
                                                getBinaryOktet(host[2]),
                                                getBinaryOktet(host[3]));
        this.networkAddress = StringUtils.rightPad(binary.substring(0, prefix), 32, "0");
    }

    public ArrayList<String> getLiveDev() {
        if (this.lives.isEmpty())
            this.loadLiveDevices(this.cmdPing);
        return this.lives;
    }

    public ArrayList<String> loadLiveDevices(ICmd cmdPing) {
        ArrayList<String> possibilities = new ArrayList<String>();
        this.cmdPing = cmdPing;
        this.genBinaryOptions(32-this.prefix, new int[32-this.prefix], 0, possibilities);

        //TODO status bar
        for (int i = 0; i < possibilities.size(); i++) {
            String address = String.format("%s%s", this.networkAddress.substring(0, this.prefix), possibilities.get(i));
            //wtf shouldnt be 1 considered as true already???????????????????
            if (cmdPing.ping(address) == 1) {
                this.lives.add(address);
            }
        }
        return this.lives;
    }

    private ArrayList<String> genBinaryOptions(int count, int[] array, int v, ArrayList<String> out) {
        if (out == null)
            out = new ArrayList<String>();
        if (v == count)
        {
            out.add(StringUtils.join(ArrayUtils.toObject(array), ""));
        }

        array[v] = 0;
        this.genBinaryOptions(count, array, v + 1, out);
        array[v] = 1;
        this.genBinaryOptions(count, array, v + 1, out);

        return out;
    }

    private String getBinaryOktet(int value) {
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
