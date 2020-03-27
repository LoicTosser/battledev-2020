/*******
 * Read input from System.in
 * Use: System.out.println to ouput your result to STDOUT.
 * Use: System.err.println to ouput debugging information to STDERR.
 * ***/
package com.isograd.exercise.five;

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
        int cableCount = sc.nextInt();
        int requestCount = sc.nextInt();
        System.err.println("Cable count: " + cableCount + "; request count: " + requestCount);

        PriorityQueue<Integer> freeCables = IntStream.range(1, cableCount+1).boxed().collect(Collectors.toCollection(PriorityQueue::new));

        PriorityQueue<Request> requests = IntStream.range(1, requestCount+1)
                .mapToObj(i -> new Request(sc.nextInt(), sc.nextInt(), i))
                .collect(Collectors.toCollection(() ->
                        new PriorityQueue<>(Comparator.comparingInt(
                                Request::getFrom
                        ).thenComparingInt(Request::getTo))));

        List<Request> seenRequests = new ArrayList<>();

        while(!requests.isEmpty()) {
            Request currentRequest = requests.poll();
            if(freeCables.isEmpty()) {
                seenRequests.stream().filter(seenRequest -> seenRequest.finishBefore(currentRequest) && !seenRequest.isCableRemoved())
                        .forEach(seenRequest -> {
                            freeCables.add(seenRequest.getAssignedCable());
                            seenRequest.setCableAsRemoved();
                        });
            }
            if(freeCables.isEmpty()) {
                System.out.println("pas possible");
                return;
            } else {
                currentRequest.setAssignedCable(freeCables.poll());
                seenRequests.add(currentRequest);
            }
        }
        System.out.println(seenRequests.stream()
                .map(aRequest -> aRequest.getAssignedCable().toString())
                .collect(Collectors.joining(" ")));
    }

    static class Request {
        private final int from, to, index;

        private boolean cableRemoved = false;

        private Integer assignedCable;

        public Request(int from, int to, int index) {
            this.from = from;
            this.to = to;
            this.index = index;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getIndex() {
            return index;
        }

        public void setCableAsRemoved() {
            this.cableRemoved = true;
        }

        public boolean isCableRemoved() {
            return cableRemoved;
        }

        public boolean finishBefore(Request anotherRequest) {
            return to <= anotherRequest.getFrom();
        }

        public Integer getAssignedCable() {
            return assignedCable;
        }

        public void setAssignedCable(Integer assignedCable) {
            this.assignedCable = assignedCable;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "from=" + from +
                    ", to=" + to +
                    ", index=" + index +
                    '}';
        }
    }
}