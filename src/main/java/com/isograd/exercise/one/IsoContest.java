/*******
 * Read input from System.in
 * Use: System.out.println to ouput your result to STDOUT.
 * Use: System.err.println to ouput debugging information to STDERR.
 * ***/
package com.isograd.exercise.one;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IsoContest {
    public static void main( String[] argv ) throws Exception {
        String  line;
        Scanner sc = new Scanner(System.in);
        int personCount = sc.nextInt();
        Map<String, Integer> colorsCount = new HashMap<>(personCount);
        sc.nextLine();
        while(sc.hasNextLine()) {
            line = sc.nextLine();
            Integer count = colorsCount.get(line);
            if (count == null) {
                count = 0;
            }
            count++;
            colorsCount.put(line, count);
        }
        System.out.println(colorsCount.entrySet().stream().sorted((e1,e2) -> Integer.compare(e2.getValue(), e1.getValue())).limit(2).map(Map.Entry::getKey).collect(Collectors.joining(" ")));

        /* Vous pouvez aussi effectuer votre traitement une fois que vous avez lu toutes les données.*/
    }
}