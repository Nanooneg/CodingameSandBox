import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Main {

    public static void main(String args[]) {


        Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        String[] entries = new String[count];
        StringBuilder answer = new StringBuilder();

        if (in.hasNextLine()) {
            in.nextLine();
        }

        for (int i = 0; i < count; i++) {
            String line = in.nextLine();
            entries[i] = line;
        }

        for (int i = 0; i < count; i++){

            if ( !entries[i].substring(0,1).equalsIgnoreCase("") ){
                if (i != 0)
                    answer.append("\n");
                answer.append(entries[i]);
            }

            if ( entries[i].substring(0,1).equalsIgnoreCase("") ) {
                
            }

        }

        Entity test = new Entity();

        // Write an answer using System.out.println()
        // To debug: System.err.println("Debug messages...");

        System.out.println(answer.toString());
    }
}