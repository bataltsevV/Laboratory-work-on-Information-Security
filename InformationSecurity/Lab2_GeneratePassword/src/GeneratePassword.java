public class GeneratePassword {
    //Задаем длинну пароля L =7
    private static int lenght = 7;
    //Ограничиваем мощность алфавита = 26, будем использовать только заглавные латинские буквы
    private static char startSymbol = 'A';
    private static char endSymbol = 'Z';

    public static String generatePassword(int length) {
        StringBuilder  password = new StringBuilder(length);
        int            startCodeSymbol = (int) startSymbol;
        int            endCodeSymbol = (int) endSymbol;

        for (int i = 0; i < length; i++) {
            // Генерируем случайное число в диапазоне от кода соотвесвующего символу 'A' до кода = 'Z'
            // Приводим сгенерированное число в символ
            char randomChar = (char) (startCodeSymbol + (int)(Math.random() * (endCodeSymbol - startCodeSymbol + 1)));
            // Добавляем символ в пароль
            password.append(randomChar);
        }

        return password.toString();
    }

    public static void main(String[] args) {
        System.out.println("Пароль: " + generatePassword(lenght));
    }
}