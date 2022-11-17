package io.pig;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AppTest {

    @Test
    public void testCombine() {
        String[] args = {
                "D:\\code\\java\\CsvCombiner\\fixtures\\clothing.csv",
                "D:\\code\\java\\CsvCombiner\\fixtures\\accessories.csv",
                ">",
                "D:\\code\\java\\CsvCombiner\\output.csv"
        };
        InputStream in = System.in;
        // redirect system in
        String mockInput = "Y";
        try (InputStream mock = new ByteArrayInputStream(mockInput.getBytes(StandardCharsets.UTF_8))) {
            System.setIn(mock);
            App app = new App();
            app.app(args);
        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
        } finally {
            System.setIn(in);
        }
    }
}