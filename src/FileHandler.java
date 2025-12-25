import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileHandler {
    public static void main(String[] args) {
        // переменные для работы с файлами:
        List<String> inputFiles = new ArrayList<>();
        String outputPath = "";                     // путь выходных файлов
        String prefix = "";                         // префикс имен выходных файлов
        boolean append = false;                     // флаг для перезаписи файлов или добавления данных в существующий файл
        List<String> data = new ArrayList<>();      // список данных прочитанных из файлов
        // списки куда помещаются обработанные данные:
        List<Integer> foundIntegers = new ArrayList<>();
        List<Float> foundFloats = new ArrayList<>();
        List<String> foundStrings = new ArrayList<>();
        // Переменные для сбора статистики:
        boolean shortStatistics = false;                         // флаг для отображения краткой статистики
        boolean fullStatistics = false;                          // флаг для отображения полной статистики
        int countInt = 0, countFloat = 0, countString = 0;                  // счетчики количества записанных в файл данных
        int maxString = Integer.MIN_VALUE, minString = Integer.MAX_VALUE;   // максимальная и минимальная длина строки
        float sumInt = 0, averageInt, minInt, maxInt, sumFloat = 0, averageFloat, minFloat, maxFloat;       //  сумма, среднее, минимальное и максимальное значения для целых и вещественных чисел

        // обрабатывает аргументы командной строки
        if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-a":                                    // включает режим добавления данных в существующие файлы
                        append = true;
                        break;
                    case "-o":                                    // передает путь к выходным файлам
                        if (i + 1 < args.length) {
                            outputPath = args[i + 1] + "/";
                            File outputDir = new File(outputPath);
                            outputDir.mkdirs();
                        }
                        break;
                    case "-p":                                    // добавляет префикс имен выходных файлов
                        if (!args[i + 1].endsWith(".txt")) {      // проверяет, что после -p не указано название файла
                            prefix = args[i + 1];
                        } else {System.out.println("Неверный префикс");}
                        break;
                    case "-s":                                    // выводит краткую статистику по записанным в файл данным
                        shortStatistics = true;
                        break;
                    case "-f":                                    // выводит полную статистику по записанным в файл данным
                        shortStatistics = true;
                        fullStatistics = true;
                        break;
                }
                if (args[i].endsWith(".txt")) inputFiles.add(args[i]);  // Проверяет, есть ли в аргументах имена файлов с расширением .txt
            }
        }
        // читает файл построчно
        try {
            for (String fileName : inputFiles) {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));  //  создаем объект с передачей пути к файлу с данными и заключаем его в BufferedReader, чтобы он обрабатывал нужный поток данных
                String line;
                while ((line = reader.readLine()) != null) {
                    data.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // фильтрует прочитанные данные и записывает их в соответствующие списки
        for (String dataLine : data) {
            try {
                int valueInt = Integer.parseInt(dataLine);
                foundIntegers.add(valueInt);
            } catch (NumberFormatException e1) {
                try {
                    float valueFloat = Float.parseFloat(dataLine);
                    foundFloats.add(valueFloat);
                } catch (NumberFormatException e2){
                    foundStrings.add(dataLine);
                }
            }
        }

        // записывает целые числа в файл и собирает статистику
        if (!foundIntegers.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + prefix + "integers.txt", append))) {
                for (Integer integerNum : foundIntegers) {
                    writer.write(String.valueOf(integerNum));
                    writer.newLine();
                    countInt++;
                    sumInt += integerNum;

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // записывает вещественные числа в файл и собирает статистику
        if (!foundFloats.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + prefix + "floats.txt", append))) {
                for (Float floatNum : foundFloats) {
                    writer.write(String.valueOf(floatNum));
                    writer.newLine();
                    countFloat++;
                    sumFloat += floatNum;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // записывает строки в файл и собирает статистику
        if (!foundStrings.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + prefix + "strings.txt", append))) {
                for (String string : foundStrings) {
                    writer.write(String.valueOf(string));
                    writer.newLine();
                    countString++;

                    // Находит максимальную и минимальную длину строки
                    int length = string.length();
                    if (length < minString){
                        minString = length;
                    }
                    if (length > maxString) {
                        maxString = length;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        // Выводит статистику
        if (shortStatistics) {
            System.out.println("Количество элементов записанных в " +prefix + "integers.txt" + ": " + countInt);
            System.out.println("Количество элементов записанных в " +prefix + "floats.txt" + ": " + countFloat);
            System.out.println("Количество элементов записанных в " +prefix + "strings.txt" + ": " + countString);
        }
        if (fullStatistics){
            if (!foundIntegers.isEmpty()) {
                averageInt = sumInt / countInt;
                minInt = Collections.min(foundIntegers);
                maxInt = Collections.max(foundIntegers);

                System.out.println();
                System.out.println("Полная статистика для integers:");
                System.out.println("Минимальное значение: " + minInt);
                System.out.println("Максимальное значение: " + maxInt);
                System.out.println("Сумма: " + sumInt);
                System.out.println("Среднее значение: " + averageInt);
            }
            if (!foundFloats.isEmpty()) {
                averageFloat = sumFloat / countFloat;
                minFloat = Collections.min(foundFloats);
                maxFloat = Collections.max(foundFloats);

                System.out.println();
                System.out.println("Полная статистика для floats:");
                System.out.println("Минимальное значение: " + minFloat);
                System.out.println("Максимальное значение: " + maxFloat);
                System.out.println("Сумма: " + sumFloat);
                System.out.println("Среднее значение: " + averageFloat);
            }
            if (!foundStrings.isEmpty()){
                System.out.println();
                System.out.println("Полная статистика для strings:");
                System.out.println("Длина минимальной строки: " + minString);
                System.out.println("Длина максимальной строки: " + maxString);
            }
        }
    }
}