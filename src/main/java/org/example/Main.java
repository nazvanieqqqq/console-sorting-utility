package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> options = new HashMap<>();
        List<String> filePaths = new ArrayList<>();
        parseArgs(args, options, filePaths);

        String outputPath = options.getOrDefault("-o", ".");
        String prefix = options.getOrDefault("-p", "");
        boolean appendMode = options.containsKey("-a");
        boolean shortStats = options.containsKey("-s");
        boolean fullStats = options.containsKey("-f");

        if (!shortStats && !fullStats) {
            System.out.println("Укажите один из параметров: -s (краткая статистика) или -f (полная статистика)");
            return;
        }

        File integerFile = null, floatFile = null, stringFile = null;
        BufferedWriter intWriter = null, floatWriter = null, stringWriter = null;
        TypeOfStats floatTypeOfStats = TypeOfStats.FLOAT;
        TypeOfStats doubleTypeOfStats = TypeOfStats.DOUBLE;
        TypeOfStats strTypeOfStats = TypeOfStats.STRING;
        Statistics intStats = new Statistics(doubleTypeOfStats);
        Statistics floatStats = new Statistics(floatTypeOfStats);
        Statistics stringStats = new Statistics(strTypeOfStats);

        try {
            for (String filePath: filePaths){
                List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
                for (String line: lines){
                    line = line.trim();
                    System.out.println("Обрабатываем строку: " + line);

                    if (line.isEmpty()) continue;

                    if(isInteger(line)){
                        if (integerFile == null) {
                            integerFile = new File(outputPath, prefix + "integers.txt");
                            intWriter = new BufferedWriter(new FileWriter(integerFile, appendMode));
                        }
                        intWriter.write(line + "\n");
                        intStats.update(doubleTypeOfStats, line);
                    }else if(isFloat(line)){
                        if (floatFile == null) {
                            floatFile = new File(outputPath, prefix + "floats.txt");
                            floatWriter = new BufferedWriter(new FileWriter(floatFile, appendMode));
                        }
                        floatWriter.write(line + "\n");
                        floatStats.update(floatTypeOfStats, line);
                    }else{
                        if (stringFile == null) {
                            stringFile = new File(outputPath, prefix + "strings.txt");
                            stringWriter = new BufferedWriter(new FileWriter(stringFile, appendMode));
                        }
                        stringWriter.write(line + "\n");
                        stringStats.update(strTypeOfStats, line);
                    }
                }
            }
        }catch(IOException e){
            System.err.println("Ошибка при чтении или записи файлов: " + e.getMessage());
        }finally{
            try {
                if (intWriter != null) intWriter.close();
                if (floatWriter != null) floatWriter.close();
                if (stringWriter != null) stringWriter.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии файлов: " + e.getMessage());
            }
        }


        if (integerFile != null) {
            System.out.println("Статистика для целых чисел:");
            System.out.println(intStats.getStat(doubleTypeOfStats, shortStats));
        }
        if (floatFile != null) {
            System.out.println("Статистика для вещественных чисел:");
            System.out.println(floatStats.getStat(floatTypeOfStats, shortStats));
        }
        if (stringFile != null) {
            System.out.println("Статистика для строк:");
            System.out.println(stringStats.getStat(strTypeOfStats, shortStats));
        }

    }

    private static void parseArgs(String[] args, Map<String, String> options, List<String> filePaths){
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    options.put(args[i], args[i + 1]);
                    i++;
                } else {
                    options.put(args[i], "");
                }
            } else {
                filePaths.add(args[i]);
            }
        }
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return str.contains(".");
        } catch (NumberFormatException e) {
            return false;
        }
    }
}