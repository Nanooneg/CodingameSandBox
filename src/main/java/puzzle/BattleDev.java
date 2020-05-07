package puzzle;

import java.util.*;

public class BattleDev {
    /*public static void main( String[] argv ) throws Exception {
        String  line;
        StringBuilder solution = new StringBuilder();
        List<String> answers = new ArrayList<>();
        Map<String, Integer> counts = new HashMap<>();

        Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()) {
            line = sc.nextLine();
            answers.add(line);
        }

        int people = Integer.parseInt(answers.get(0));
        answers.remove(0);

        System.err.println("list answer : " + answers);
        Set<String> colors = new HashSet<>(answers);
        System.err.println("set colors : " + colors);

        for (String answer : answers){

            if (counts.containsKey(answer)){

                int newCount = counts.get(answer) + 1;
                counts.replace(answer,newCount);

            }else {

                counts.put(answer,1);

            }

        }

        String tempColor = "";
        int tempCount = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()){

            if (entry.getValue() > tempCount){
                tempColor = entry.getKey();
                tempCount = entry.getValue();
            }

        }

        counts.remove(tempColor);
        solution.append(tempColor).append(" ");

        tempColor = "";
        tempCount = 0;
        for (Map.Entry<String, Integer> entry : counts.entrySet()){

            if (entry.getValue() > tempCount){
                tempColor = entry.getKey();
                tempCount = entry.getValue();            }

        }

        solution.append(tempColor);
        System.out.println(solution.toString());

    }*/

    /*public static void main(String[] args) {

        String  line;
        Scanner sc = new Scanner(System.in);
        int draws = sc.nextInt();
        StringBuilder numbers = new StringBuilder();
        while(sc.hasNextLine()) {
            line = sc.nextLine();
            numbers.append(line);
        }

        System.err.println(numbers);

        char[] numbersTable = numbers.toString().toCharArray();
        char tempChar = numbersTable[0];
        int tempCount = 1;
        int biggestCount = 0;
        for (int i = 1; i < numbersTable.length; i++ ){

            if (numbersTable[i] == tempChar){
                tempCount ++;
            }else {
                if (tempCount > biggestCount)
                    biggestCount = tempCount;
                tempCount = 1;
                tempChar = numbersTable[i];
            }

        }

        *//* Sorry c deg je sais pas comment gérer une chaine avec que le meme chiffre... \o/ *//*
        if (biggestCount == 0)
            biggestCount = tempCount;

        System.out.println(biggestCount);
    }*/

}