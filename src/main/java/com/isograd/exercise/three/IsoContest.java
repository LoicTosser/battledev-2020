/*******
 * Read input from System.in
 * Use: System.out.println to ouput your result to STDOUT.
 * Use: System.err.println to ouput debugging information to STDERR.
 * ***/
package com.isograd.exercise.three;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IsoContest {

    private static final LocalTime FIRST_TIME_OF_DAY = LocalTime.parse("08:00");
    private static final LocalTime LAST_TIME_OF_DAY = LocalTime.parse("17:59");
    private static final Supplier<PriorityQueue<TimeSlot>> PRIORITY_QUEUE_SUPPLIER = () -> new PriorityQueue<TimeSlot>(
            Comparator.comparingInt(TimeSlot::getDay)
                    .thenComparing(TimeSlot::getFrom)
                    .thenComparing(TimeSlot::getTo));

    public static void main(String[] argv) throws Exception {
        String line;
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
        sc.nextLine();
        Map<Integer, PriorityQueue<TimeSlot>> freeSlotsByDay = new HashMap<>();
        while (sc.hasNextLine()) {
            line = sc.nextLine();

            TimeSlot timeSlot = TimeSlot.from(line);

            int day = timeSlot.getDay();

            PriorityQueue<TimeSlot> freeSlots = freeSlotsByDay.get(day);

            Stream<TimeSlot> freeSlotStream = freeSlots == null ? Stream.of(new TimeSlot(day, FIRST_TIME_OF_DAY, LAST_TIME_OF_DAY)) : freeSlots.stream();

            freeSlots = freeSlotStream.flatMap(timeSlot1 -> timeSlot1.exclude(timeSlot)).filter(TimeSlot::isAvailableForFreeSlot).collect(Collectors.toCollection(PRIORITY_QUEUE_SUPPLIER));

            freeSlotsByDay.put(day, freeSlots);
        }

        freeSlotsByDay.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .findFirst()
                .map(Map.Entry::getValue)
                .map(PriorityQueue::peek)
                .map(TimeSlot::reduceToOneHour)
                .ifPresent(System.out::println);
    }

    public static class TimeSlot {

        private final int day;

        private final LocalTime from;

        private final LocalTime to;

        private final long duration;

        public static TimeSlot from(String timeSlotAsString) {
            String[] dayTimeValues = timeSlotAsString.split(" ");
            String[] timeInterval = dayTimeValues[1].split("-");
            return new TimeSlot(Integer.parseInt(dayTimeValues[0]), LocalTime.parse(timeInterval[0]), LocalTime.parse(timeInterval[1]));
        }

        public TimeSlot(int day, LocalTime from, LocalTime to) {
            this.day = day;
            this.from = from;
            this.to = to;
            duration = Duration.between(from, to).toMinutes();
        }

        public boolean intersect(TimeSlot anotherTimeSlot) {
            if (anotherTimeSlot == null || anotherTimeSlot.getDay() != day) {
                return false;
            }
            return (from.isBefore(anotherTimeSlot.getFrom()) && to.isAfter(anotherTimeSlot.getFrom()))
                    || (from.isBefore(anotherTimeSlot.getTo()) && to.isAfter(anotherTimeSlot.getTo()))
                    || from.equals(anotherTimeSlot.getFrom()) || to.equals(anotherTimeSlot.getTo());
        }

        public boolean overlap(TimeSlot anotherTimeSlot) {
            if (anotherTimeSlot == null || anotherTimeSlot.getDay() != day) {
                return false;
            }
            return from.isBefore(anotherTimeSlot.getFrom()) && to.isAfter(anotherTimeSlot.getTo());
        }

        public boolean isAvailableForFreeSlot() {
            return duration >= 59;
        }

        public Stream<TimeSlot> exclude(TimeSlot anotherTimeSlot) {
            Stream<TimeSlot> exclusions = Stream.empty();
            if (anotherTimeSlot.overlap(this)) {
                return exclusions;
            }
            if (!this.intersect(anotherTimeSlot)) {
                exclusions = addTo(exclusions, this);
            } else {
                if (anotherTimeSlot.getFrom().isAfter(from)) {
                    TimeSlot before = new TimeSlot(day, from, anotherTimeSlot.getFrom().minusMinutes(1L));
                    exclusions = addTo(exclusions, before);
                }
                if (anotherTimeSlot.getTo().isBefore(to)) {
                    TimeSlot after = new TimeSlot(day, anotherTimeSlot.getTo().plusMinutes(1), to);
                    exclusions = addTo(exclusions, after);
                }
            }
            return exclusions;
        }

        private Stream<TimeSlot> addTo(Stream<TimeSlot> originalStream, TimeSlot element) {
            return Stream.concat(originalStream, Stream.of(element));
        }

        public TimeSlot reduceToOneHour() {
            if (duration <= 60) {
                return this;
            }
            return new TimeSlot(day, from, from.plusMinutes(59));
        }

        @Override
        public String toString() {
            return day + " " + from + "-" + to;
        }

        public int getDay() {
            return day;
        }

        public LocalTime getFrom() {
            return from;
        }

        public LocalTime getTo() {
            return to;
        }
    }


}