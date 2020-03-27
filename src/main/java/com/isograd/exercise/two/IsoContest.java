/*******
 * Read input from System.in
 * Use: System.out.println to ouput your result to STDOUT.
 * Use: System.err.println to ouput debugging information to STDERR.
 * ***/
package com.isograd.exercise.two;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IsoContest {
    public static void main( String[] argv ) throws Exception {
        Scanner sc = new Scanner(System.in);
        String tiragesCount = sc.nextLine();
        System.err.println(tiragesCount);
        int lastTirage = -1;
        int currentTirage;
        int currentSerieCount = 0;
        int maxSerieCount = -1;
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line != null && !line.isEmpty()) {
                currentTirage = Integer.parseInt(line);
                if (currentTirage == lastTirage) {
                    currentSerieCount++;
                } else {
                    currentSerieCount = 1;
                }
                lastTirage = currentTirage;
                maxSerieCount = Math.max(maxSerieCount, currentSerieCount);
            }

            /* Lisez les données et effectuez votre traitement */
        }
        System.out.println(maxSerieCount);
        /* Vous pouvez aussi effectuer votre traitement une fois que vous avez lu toutes les données.*/
    }
}