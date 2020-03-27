package com.isograd.exercise.three;

import com.isograd.exercise.three.IsoContest;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * LECTRA
 * IsoContestTest class
 *
 * @author l.tosser
 */
public class IsoContestTest implements WithAssertions {

    private static final String FOLDER = "three";
    private static final String COMPLETE_FOLDER = "src/test/resources/" + FOLDER;

    @ParameterizedTest
    @MethodSource("provideFileNames")
    void output_should_correspond_to_input(String input, String output) throws Exception {
        Assertions.assertFalse(input.isEmpty());
        String data = "the text you want to send";
        InputStream old = System.in;
        try (InputStream testInput = new FileInputStream(COMPLETE_FOLDER + "/" + input)) {
            System.setIn(testInput);

            PrintStream oldOutputStream = System.out;
            String outputConsole = "target/" + output + ".txt";
            try (PrintStream printOut = new PrintStream(new FileOutputStream(outputConsole))) {
                System.setOut(printOut);
                IsoContest.main(new String[]{});
                String expectedResult = new String(Files.readAllBytes(Paths.get(COMPLETE_FOLDER + "/" + output)), StandardCharsets.UTF_8);
                List<String> expectedList = Arrays.asList(expectedResult.split(" "));
                String actualResult = new String(Files.readAllBytes(Paths.get(outputConsole)), StandardCharsets.UTF_8);
                List<String> actuaList = Arrays.asList(actualResult.trim().split(" "));
                assertThat(actuaList).containsExactlyInAnyOrderElementsOf(expectedList);
            } finally {
                System.setOut(oldOutputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.setIn(old);
        }
    }


    private static Stream<Arguments> provideFileNames() {
        File f = new File(COMPLETE_FOLDER);

        // Populates the array with names of files and directories
        String[] files = f.list((dir, name) -> name.startsWith("input"));
        return Arrays.stream(files)
                .map(file -> {
                    String fileIndex = file.substring("input".length(), file.lastIndexOf("."));
                    return Arguments.of(file, "output" + fileIndex + ".txt");
                });
    }

}
