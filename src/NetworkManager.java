import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


public class NetworkManager {
    private final String networkAddress;
    private final int prefix;
    private final ArrayList<String> possibilities;

    public NetworkManager(int[] localhost, int prefix) {
        this.possibilities = new ArrayList<>();
        this.prefix = prefix;
        String binary = String.join("", toBinaryOktet(localhost[0]),
                                                toBinaryOktet(localhost[1]),
                                                toBinaryOktet(localhost[2]),
                                                toBinaryOktet(localhost[3]));
        this.networkAddress = StringUtils.rightPad(binary.substring(0, prefix), 32, "0");
    }

    public ArrayList<String> pingSweep(IPing cmdPing) {
        ArrayList<String> result = new ArrayList<>();
        CompletableFuture<Void> cf = new CompletableFuture<>();
        this.genBinaryOptions(32-this.prefix, new int[32-this.prefix], 0);

        //TODO status bar
        //TODO make it run async without getting my mac into space
        int i = 1;
        for (String item : this.possibilities) {
            int info = i;
//                cf = CompletableFuture.runAsync(() -> {
                    this.validate(cmdPing, result, String.format("%s%s", this.networkAddress.substring(0, this.prefix), item).toCharArray(), info);
//                });
            i++;
        }

//        while (!CompletableFuture.allOf(cf).isDone()) {
//        }
            return result;
    }

    private void validate(IPing cmdPing, ArrayList<String> data, char[] value, int step) {
        //TODO debugger: be able to see all of them - System.out.println(StringUtils.join(ArrayUtils.toObject(value), ""));
//        System.out.println(this.getFromBinary(StringUtils.join(ArrayUtils.toObject(value), "")));

        System.out.printf("%g%s%n",(((double)step/this.possibilities.size())*100), "%");
        String valid = cmdPing.cmdPing(this.getFromBinary(StringUtils.join(ArrayUtils.toObject(value), "")));
        if (valid != null) {
            data.add(valid);
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
