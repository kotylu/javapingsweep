import java.util.ArrayList;
import java.util.Scanner;

public class CLI {
    public CLI() {

    }

    public static <T> void printList(ArrayList<T> list) {
        //TODO: some fancy format
        for (T item : list) {
            System.out.printf("%s - %s%n", list.indexOf(item), item);
        }
    }

    public static void printHelp() {

    }

    public static int getInt() {
        int index = -1;
        try {
            index = Integer.parseInt(new Scanner(System.in).nextLine());
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return index;
    }
}
