import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CatVirus {
    // Рисунок котика в ASCII
    private static final String CAT_ART = """
         /\\_/\\  
        ( o.o ) 
         > ^ <
        """;

    public static void main(String[] args) {
        infectFiles();
    }

    //Добавляет ASCII-арт котика в конец каждого текстового файла в текущей директории.
    public static void infectFiles() {
        // Сканируем файлы в текущей директории
        File directory = new File("D:\\progy\\Forjava\\Projects\\1RANHiGS\\InformationSecurity\\Lab5_VirusAndAntivirus\\Files");
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                // Проверяем, что файл имеет расширение .txt
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    try {
                        // Открываем файл в режиме добавления
                        FileWriter writer = new FileWriter(file, true);
                        writer.write("\n" + CAT_ART); // Добавляем рисунок котика
                        writer.close();
                        System.out.println("Файл " + file.getName() + " успешно 'инфицирован'.");
                    } catch (IOException e) {
                        System.err.println("Ошибка при обработке файла " + file.getName() + ": " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("Не удалось получить список файлов в директории.");
        }
    }
}
