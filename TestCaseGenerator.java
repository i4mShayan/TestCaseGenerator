import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TestCaseGenerator {
    private static final String TEST_CASE_FOLDER = "test_cases";
    private static final String INPUT_FOLDER = TEST_CASE_FOLDER + "/in";
    private static final String OUTPUT_FOLDER = TEST_CASE_FOLDER + "/out";
    private static final String ZIP_FILE = "problem.zip";

    enum TimeComplexity {
        N("n", (int) 1e8),
        N_LOG_N("nlogn", (int) 1e5),
        N_SQUARED("n^2", (int) 1e4),
        EXPONENTIAL("2^n", 20),
        LOG_N("logn", (int) 1e9),
        SQRT_N("sqrt(n)", (int) 1e9);

        final String complexityOrder;
        final int numInputs;

        TimeComplexity(String complexityOrder, int numInputs) {
            this.complexityOrder = complexityOrder;
            this.numInputs = numInputs;
        }
    }

    public static void main(String[] args) {
        // ----------------- INPUT SIZE -----------------
        TimeComplexity order = TimeComplexity.N_LOG_N;
        int numInputs = order.numInputs / 2;

        // ----------------- INPUT CONDITION -----------------
        generateTestCases(
            numInputs, //numInputs
            1, //testCaseStart
            10, //testCaseEnd
            -1000, //numRangeMin
            1000, //numRangeMax
            0, //minNumOccurrences
            100 //maxNumOccurrences
        );
    }

    public static void generateTestCases(int numInputs, int testCaseStart, int testCaseEnd, int numRangeMin, int numRangeMax, int minNumOccurrences, int maxNumOccurrences) {
        for (int i = testCaseStart; i <= testCaseEnd; i++) {
            int[] arr = getRandomList(numInputs, numRangeMin, numRangeMax, minNumOccurrences, maxNumOccurrences);

            String inputFileName = INPUT_FOLDER + "/input" + i + ".txt";
            String outputFileName = OUTPUT_FOLDER + "/output" + i + ".txt";

            generateInputFile(inputFileName, numInputs, arr);
            generateOutputFile(outputFileName, arr);
        }
        zipFolders();
    }

    private static int getRandomNumber(int max, int min) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    private static int[] getRandomList(int len, int max, int min, int minOccurrences, int maxOccurrences) {
        int[] randomList = new int[len];
        Map<Integer, Integer> numCountMap = new HashMap<>();

        for (int i = 0; i < len; i++) {
            int randomNumber = getRandomNumber(max, min);

            while ((numCountMap.containsKey(randomNumber) && numCountMap.get(randomNumber) < minOccurrences) || (numCountMap.containsKey(randomNumber) && numCountMap.get(randomNumber) >= maxOccurrences)) {
                randomNumber = getRandomNumber(max, min);
            }

            randomList[i] = randomNumber;
            numCountMap.put(randomNumber, numCountMap.getOrDefault(randomNumber, 0) + 1);
        }

        return randomList;
    }

    private static void generateInputFile(String fileName, int n, int[] arr) {
        try {
            Path inputPath = Paths.get(fileName);
            Files.createDirectories(inputPath.getParent());
            FileWriter writer = new FileWriter(fileName);
            // ----------------- INPUT FORMAT -----------------
            writer.write(n + "\n");
            for (int i = 0; i < n; i++) {
                writer.write(arr[i] + " ");
            }
            // ----------------- INPUT FORMAT -----------------
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateOutputFile(String fileName, int[] arr) {
        try {
            Path outputPath = Paths.get(fileName);
            Files.createDirectories(outputPath.getParent());
            FileWriter writer = new FileWriter(fileName);
            // ----------------- OUTPUT FORMAT -----------------
            int ans = Solution.questionSolution(arr);
            writer.write(String.valueOf(ans));
            // ----------------- OUTPUT FORMAT -----------------
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFolders() {
        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(Paths.get(ZIP_FILE)))) {
            addFolderToZip(INPUT_FOLDER, zipOut);
            addFolderToZip(OUTPUT_FOLDER, zipOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFolderToZip(String folderPath, ZipOutputStream zipOut) throws IOException {
        Path path = Paths.get(folderPath);
        Path rootPath = Paths.get(TEST_CASE_FOLDER);
        Files.walk(path)
        .filter(Files::isRegularFile)
        .forEach(file -> {
            try {
                Path relativePath = rootPath.relativize(file);
                String zipEntryName = relativePath.toString();
                zipOut.putNextEntry(new ZipEntry(zipEntryName));
                Files.copy(file, zipOut);
                zipOut.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}


class Solution {
    static public int questionSolution(int[] nums) {
        // ----------------- SOLUTION CODE -----------------
        int ans = 0;
        return ans;
    }
}