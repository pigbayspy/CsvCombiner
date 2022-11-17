package io.pig;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CsvCombiner {
    private final Set<String> validUserInput = Set.of("Y", "N", "y", "n");

    /**
     * combine csv files
     *
     * @param filenames input file name in absolute path
     * @param output    output file name
     */
    public void combine(List<String> filenames, String output) {
        // Print to console the files the user wants to combine
        System.out.print("-Files to combine:");
        for (String filename : filenames) {
            System.out.print(" |" + filename + "| ");
        }
        System.out.println("-Output file will be: " + output);

        try (Scanner in = new Scanner(System.in)) {
            System.out.print("Are these the correct files (y/n): ");
            String userCheck = in.nextLine();
            while (!isValidUserConfirm(userCheck)) {
                System.out.println("*please confirm with 'y' or 'n'*");
                System.out.print("Are these the correct files (y/n): ");
                userCheck = in.nextLine();
            }
            // if the files are not correct program stops
            if ("n".equalsIgnoreCase(userCheck)) {
                System.out.println();
                System.out.println("Please try running the program again...");
                System.out.println("For help type -help");
                return;
            }
        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
        }
        doCombine(filenames, output);
    }

    private void doCombine(List<String> input, String output) {
        File outputFile = new File(output);
        int allCombinedRow = 0;
        for (String file : input) {
            File inputFile = getFile(file);
            if (inputFile != null) {
                int count = combine(inputFile, outputFile);
                allCombinedRow += count;
                System.out.println("All " + allCombinedRow + " rows from " + input + " have been combined with: " + output);
            }
        }
    }

    private int combine(File input, File output) {
        try (Reader reader = new FileReader(input);
             BufferedReader csvInputReader = new BufferedReader(reader)) {
            // get first row "AKA" the header for the and add the new column
            String header = csvInputReader.readLine() + ",filename";
            int counter = 0;
            // if this is the first time opening output file -> create it
            if (!output.exists()) {
                if (output.createNewFile()) {
                    System.out.println("create output file");
                }
                try (FileWriter csvOutputWriter = new FileWriter(output)) {
                    csvOutputWriter.append(header);
                    csvOutputWriter.append("\n");

                    String row = null;
                    while ((row = csvInputReader.readLine()) != null) {
                        csvOutputWriter.append(row);
                        csvOutputWriter.append(",").append(input.getName());
                        csvOutputWriter.append("\n");
                    }
                    csvOutputWriter.flush();

                } catch (Exception e) {
                    System.err.println("error: " + e.getMessage());
                }
            } else {
                try (FileWriter csvOutputWriter = new FileWriter(output, true)) {
                    // skip the header since we already have it
                    csvInputReader.readLine();
                    String row = null;
                    while ((row = csvInputReader.readLine()) != null) {
                        csvOutputWriter.append(row);
                        csvOutputWriter.append(",").append(input.getName());
                        csvOutputWriter.append("\n");
                        counter++;
                    }
                    csvOutputWriter.flush();
                }
                System.out.println("All " + (counter) + " rows from " + input.getName() + " have been combined with: " + output.getName());
            }
            return counter;
        } catch (IOException e) {
            System.err.println("error: " + e.getMessage());
            return 0;
        }
    }

    private File getFile(String path) {
        try {
            File file = new File(path);
            return file.exists() ? file : null;
        } catch (Exception e) {
            System.err.println("error: " + e.getMessage());
            return null;
        }
    }

    private boolean isValidUserConfirm(String input) {
        return validUserInput.contains(input);
    }
}
