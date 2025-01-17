import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ComputerConnect {
    //getMicrosoftProductId - метод для получения идентификатора продукта для активации Windows из реестра
    public static String getMicrosoftProductId() {
        try {
            //Runtime - позволяет взаимодействовать со средой выполнения Java и дает доступ к системной информации
            //exec - запускает команду Windows Command Line
            //reg query - команда для запроса данных из реестра Windows
            //"HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows NT\CurrentVersion" - полный путь к определенному ключу в реестре, который содержит информацию о текущей версии Windows
            // /v ProductId - параметр, указывающий, что мы хотим получить значение конкретного параметра (в данном случае — ProductId) из указанного ключа реестра.
            //ProductId - уникальный идентификатор продукта, который используется для активации Windows
            Process process = Runtime.getRuntime().exec("reg query \"HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\" /v ProductId");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ProductId")) {
                   return line.split("\\s+")[line.split("\\s+").length - 1]; //Информация из реестра выводится выводится в виде: ProductId    REG_SZ    00330-80000-00000-AA274.
                }                                                                       //забираем только ключ - последний элемент массива строк, разбитых по пробелам, используя регулярное выражение)
            }
        } catch (Exception e) {
            System.out.println("Ошибка при доступе к реестру: " + e.getMessage()); //Вывод системной ошибки, если метод не сработал
        }
        return null;
    }

    //getCurrentClockSpeed - метод для получения тактовой частоты процессора
    public static String getCurrentClockSpeed() {
        try {
            //wmic cpu get CurrentClockSpeed - команда для получения данных о частоте процессора
            Process process = Runtime.getRuntime().exec("wmic cpu get CurrentClockSpeed");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            reader.readLine(); //Пропускаем первую строку, так как сперва выводится заголовок информации: CurrentClockSpeed
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    return line.trim(); //Получаем тактовую частоту процессора
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при получении частоты процессора: " + e.getMessage()); //Вывод системной ошибки, если метод не сработал
        }

        return null;
    }

    //generateCurrentKey - метод для создания уникального ключа состоящего из идентификатора продукта для активации Windos и базовой частоты процессора
    public static String generateCurrentKey() {
        String registryInfo = getMicrosoftProductId();
        String cpuFrequency = getCurrentClockSpeed();
        if (registryInfo != null && cpuFrequency != null) {
            return registryInfo + "-" + cpuFrequency; // Возвращаем уникальный ключ, состоящий из ProductId и ClockSpeed, записанных через тире
        }
        return null;
    }

    //Метод для проверки ключа текущего компьютера
    public static void main(String[] args) {
        String uniqueKey = "00324-80000-00000-AВ274-2592";  //Ключ с которым будем сравнивать
        String currentKey = generateCurrentKey(); //Текущий ключ этого компьютера

        System.out.println("Ключ этого компьютера: " + currentKey);

        if (uniqueKey.equals(currentKey)) {
            System.out.println("Ключ подходит для запуска на этом компьютере"); //Вывод информационного сообщения пользователю, если ключи совпадают
        }
        else{
            System.out.println("Ключ не подходит для запуска на этом компьютере"); //Вывод информационного сообщения пользователю, если ключи не совпадают
        }
    }
}