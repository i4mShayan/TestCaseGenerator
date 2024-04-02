import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

public class TestCaseGenerator {
    private static final String TEST_CASE_FOLDER = "test_cases";
    private static final String INPUT_FOLDER = TEST_CASE_FOLDER + "/in";
    private static final String OUTPUT_FOLDER = TEST_CASE_FOLDER + "/out";
    private static final String ZIP_FILE = "problem.zip";

    public static void main(String[] args) {
        // ----------------- INPUT SIZE -----------------
        TimeComplexity order = TimeComplexity.N_LOG_N;
        int numInputs = order.numInputs / 2;

        // ----------------- INPUT CONDITION -----------------
        generateTestCases(
            numInputs, //numInputs
            1, //testCaseStart
            1, //testCaseEnd
            -100000, //numRangeMin
            100000, //numRangeMax
            2, //minNumOccurrences
            20 //maxNumOccurrences
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
        Map<Integer, Boolean> seenNumbers = new HashMap<>();

        int index = 0;
        while (index < len) {
            int potentialNumber = getRandomNumber(max, min);
            if (!seenNumbers.containsKey(potentialNumber)) {
                seenNumbers.put(potentialNumber, true);

                int count = getRandomNumber(maxOccurrences, minOccurrences);
                for (int i = 0; i < Math.min(count, len - index + 1); i++) {
                    randomList[index++] = potentialNumber;
                }
            }
        }

        shuffleList(randomList);
        return randomList;
    }

    private static void shuffleList(int[] randomList) {
        Random rand = new Random();
        int len = randomList.length;

        for (int i = len - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = randomList[i];
            randomList[i] = randomList[j];
            randomList[j] = temp;
        }
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