package $kvo.separat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Использование: java SplitTextFile <путь_к_файлу> <количество_строк>");
            return;
        }

        String inputFilePath = args[0];
        int linesPerFile = Integer.parseInt(args[1]);

        splitFile(inputFilePath, linesPerFile);
        System.out.println("Файл успешно разбит.");
    }

    public static void splitFile(String inputFilePath, int linesPerFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            int lineCount = 0;
            int fileCount = 0;
            StringBuilder fileContent = new StringBuilder();
            //Перенос в основную папку шаблона
            Path testFilePath = Paths.get(inputFilePath);
            Path parent = testFilePath.getParent();
            //Перенос в основную папку шаблона
            while ((line = reader.readLine()) != null) {
                lineCount++;
                fileContent.append(line).append(System.lineSeparator());

                // Если достигнуто максимальное количество строк, записываем файл
                if (lineCount == linesPerFile) {
                    String outputFileName = createOutputFileName(fileCount, lineCount);
                    outputFileName = parent + "\\"+ outputFileName;
                    writeToFile(outputFileName, fileContent.toString());
                    fileCount++;
                    lineCount = 0; // Сброс счетчика строк
                    fileContent.setLength(0); // Сброс содержимого файла
                }
            }

            // Если остались строки после последнего заполнения файла
            if (lineCount > 0) {
                String outputFileName = createOutputFileName(fileCount, lineCount);
                writeToFile(outputFileName, fileContent.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String createOutputFileName(int fileCount, int lastLineCount) {
        int startLine = fileCount * lastLineCount + 1;
        int endLine = startLine + lastLineCount - 1;
        return startLine + " - " + endLine + ".txt";
    }

    private static void writeToFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
