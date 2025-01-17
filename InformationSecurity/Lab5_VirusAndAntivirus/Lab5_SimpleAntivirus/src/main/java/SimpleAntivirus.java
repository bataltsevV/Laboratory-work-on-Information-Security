import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SimpleAntivirus {
    private static final String SIGNATURE_DB = "signatures.json";
    private static final String QUARANTINE_DIR = "quarantine";
    private Map<String, String> signatures;

    public SimpleAntivirus() {
        this.signatures = loadSignatures();
    }

    //Загружает сигнатуры из файла.

    private Map<String, String> loadSignatures() {
        try {
            File dbFile = new File(SIGNATURE_DB);
            if (dbFile.exists()) {
                Reader reader = new FileReader(dbFile);
                return new Gson().fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке сигнатур: " + e.getMessage());
        }
        return new HashMap<>();
    }

    //Сохраняет сигнатуры в файл.

    private void saveSignatures() {
        try (Writer writer = new FileWriter(SIGNATURE_DB)) {
            new Gson().toJson(signatures, writer);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении сигнатур: " + e.getMessage());
        }
    }

    //Вычисляет MD5-хэш файла.
    private String extractSignature(String filePath) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    md.update(buffer, 0, bytesRead);
                }
            }
            byte[] hash = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            System.err.println("Ошибка при вычислении MD5 для файла " + filePath + ": " + e.getMessage());
            return null;
        }
    }

    //Генерирует уникальное имя вируса.

    private String generateUniqueName(String baseName) {
        int index = 1;
        String uniqueName = baseName;
        while (signatures.containsKey(uniqueName)) {
            uniqueName = baseName + "_" + index;
            index++;
        }
        return uniqueName;
    }

    //Добавляет сигнатуру файла в базу данных.
    public void addSignature(String filePath, String virusName) {
        String signature = extractSignature(filePath);
        if (signature == null) {
            return;
        }

        if (virusName == null || virusName.isEmpty()) {
            virusName = new File(filePath).getName();
            if (signatures.containsKey(virusName) && !signatures.get(virusName).equals(signature)) {
                virusName = generateUniqueName(virusName);
            }
        }

        signatures.put(virusName, signature);
        saveSignatures();
        System.out.println("Сигнатура для вируса '" + virusName + "' добавлена в базу: " + signature);
    }

    //Сканирует директорию на наличие вирусов.
    public void scanDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Указанный путь не является директорией.");
            return;
        }

        File quarantineDir = new File(QUARANTINE_DIR);
        if (!quarantineDir.exists()) {
            quarantineDir.mkdir();
        }

        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                String fileSignature = extractSignature(file.getAbsolutePath());
                if (fileSignature == null) {
                    continue;
                }

                for (Map.Entry<String, String> entry : signatures.entrySet()) {
                    if (fileSignature.equals(entry.getValue())) {
                        System.out.println("Обнаружен вирус '" + entry.getKey() + "' в файле " + file.getAbsolutePath());
                        moveToQuarantine(file);
                        break;
                    }
                }
            }
        }
    }

    //Перемещает заражённый файл в карантин.
    private void moveToQuarantine(File file) {
        try {
            File quarantineDir = new File(QUARANTINE_DIR);
            if (!quarantineDir.exists()) {
                quarantineDir.mkdir();
            }
            Path quarantinePath = Paths.get(QUARANTINE_DIR, file.getName());
            Files.move(file.toPath(), quarantinePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Файл " + file.getAbsolutePath() + " перемещён в карантин (" + quarantinePath + ").");
        } catch (IOException e) {
            System.err.println("Ошибка при перемещении файла " + file.getAbsolutePath() + " в карантин: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SimpleAntivirus antivirus = new SimpleAntivirus();
        antivirus.scanDirectory("D:\\progy\\Forjava\\Projects\\1RANHiGS\\InformationSecurity\\Lab5_VirusAndAntivirus\\Files");
        //antivirus.addSignature("D:\\progy\\Forjava\\Projects\\1RANHiGS\\InformationSecurity\\Lab5_VirusAndAntivirus\\Files\\Lab5_SimpleVirus.jar", "CatVirus");
    }
}
