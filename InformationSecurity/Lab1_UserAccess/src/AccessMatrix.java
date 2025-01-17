import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class AccessMatrix {
    public enum AccessLevel {
        NO_ACCESS,
        READ,
        WRITE,
        FULL_ACCESS
    }

    private AccessLevel[][] accessMatrix; // Матрица доступа
    private String[] users;                // Массив пользователей
    private String[] objects;              // Массив объектов
    private Map<String, Integer> userMap;  // Сопоставление имен пользователей и их индексов
    private Random random;
    private String currentUser;             // Хранит текущее имя пользователя

    public AccessMatrix(String[] users, String[] objects) {
        this.users = users;
        this.objects = objects;

        // Инициализируем матрицу доступа
        this.accessMatrix = new AccessLevel[users.length][objects.length];
        for (AccessLevel[] row : accessMatrix) {
            Arrays.fill(row, AccessLevel.NO_ACCESS); // Запрещаем доступ по умолчанию
        }

        // Инициализируем отображение имен пользователей к индексам
        userMap = new HashMap<>();
        for (int i = 0; i < users.length; i++) {
            userMap.put(users[i], i);
        }

        this.random = new Random();
        setRandomAccess();
    }

    private void setRandomAccess() {
        // Случайный выбор пользователя для выдачи полных прав
        int fullAccessUserIndex = random.nextInt(users.length);
        for (int i = 0; i < users.length; i++) {
            if (i == fullAccessUserIndex) {
                for (int j = 0; j < objects.length; j++) {
                    accessMatrix[i][j] = AccessLevel.FULL_ACCESS; // Выдаем полные права
                }
            } else {
                // Для остальных пользователей случайным образом устанавливаем доступ
                for (int j = 0; j < objects.length; j++) {
                    accessMatrix[i][j] = AccessLevel.values()[random.nextInt(3)]; // NO_ACCESS, READ, WRITE
                }
            }
        }
    }

    // Метод для вывода данных по выбранному пользователю
    public void printUserAccess(String userName) {
        Integer userIndex = userMap.get(userName);
        if (userIndex != null) {
            this.currentUser = userName; // Сохраняем текущего пользователя
            System.out.println("Доступ пользователя " + userName + ":");
            for (int j = 0; j < objects.length; j++) {
                System.out.println(objects[j] + ": " + accessMatrix[userIndex][j]);
            }
            // Ожидание указаний пользователя
            waitForUserInstructions();
        } else {
            System.out.println("Недопустимое имя пользователя.");
        }
    }

    private void waitForUserInstructions() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command;
            do {
                System.out.print("Введите команду (READ/WRITE/TRANSFER/SWITCH/EXIT): ");
                command = scanner.nextLine().trim().toUpperCase();
            } while(command.isEmpty());

            if (command.equals("EXIT")) {
                System.out.println("Выход из программы.");
                break;
            }

            // Команда для смены пользователя
            if (command.equals("SWITCH")) {
                System.out.print("Введите новое имя пользователя: ");
                String newUser = scanner.nextLine().trim();
                if (userMap.containsKey(newUser)) {
                    printUserAccess(newUser); // Переходим к новому пользователю
                } else {
                    System.out.println("Недоступный пользователь.");
                }
                continue; // Пропускаем остальные команды после смены
            }

            // Запрос выбора объекта перед действиями
            System.out.print("Выберите объект (введите индекс объекта от 0 до " + (objects.length - 1) + "): ");
            int objectIndex = Integer.parseInt(scanner.nextLine());
            AccessLevel access = accessMatrix[userMap.get(currentUser)][objectIndex];

            switch (command) {
                case "READ":
                    if (access == AccessLevel.READ || access == AccessLevel.FULL_ACCESS) {
                        System.out.println(currentUser + " прочитал объект: " + objects[objectIndex]);
                    } else {
                        System.out.println(currentUser + " не имеет доступа к объекту: " + objects[objectIndex]);
                    }
                    break;
                case "WRITE":
                    if (access == AccessLevel.WRITE || access == AccessLevel.FULL_ACCESS) {
                        System.out.println(currentUser + " изменил объект: " + objects[objectIndex]);
                    } else {
                        System.out.println(currentUser + " не имеет доступа к объекту: " + objects[objectIndex]);
                    }
                    break;
                case "TRANSFER":
                    if (access == AccessLevel.FULL_ACCESS) {
                        System.out.print("Введите имя пользователя для передачи прав: ");
                        String targetUser = scanner.nextLine().trim();
                        Integer targetUserIndex = userMap.get(targetUser);
                        System.out.print("Какие права передать на объект" + java.util.Arrays.asList(AccessLevel.values()) + ": ");
                        AccessLevel targetAccessLevel = AccessLevel.valueOf(scanner.next().toUpperCase());

                        if (targetUserIndex != null && targetUserIndex != userMap.get(currentUser)) {
                            accessMatrix[targetUserIndex][objectIndex] = targetAccessLevel;
                            System.out.println("Права " + targetAccessLevel + " на объект " + objects[objectIndex] + " переданы пользователю " + targetUser);
                        } else {
                            System.out.println("Недопустимое имя пользователя или попытка передать права самому себе.");
                        }
                    } else {
                        System.out.println(currentUser + " не имеет полных прав для передачи прав.");
                    }
                    break;
                default:
                    System.out.println("Неизвестная команда.");
                    break;
            }
        }
    }

    // Метод для вывода матрицы доступа в виде таблицы
    public void printAccessMatrix() {
        System.out.printf("%-10s", " "); // Заголовок для объектов
        for (String object : objects) {
            System.out.printf("%-15s", object); // Ширина столбца 15 символов
        }
        System.out.println();

        // Строки с пользователями
        for (int i = 0; i < users.length; i++) {
            System.out.printf("%-10s", users[i] + ":"); // Групповая метка для пользователя
            for (AccessLevel access : accessMatrix[i]) {
                System.out.printf("%-15s", access); // Ширина столбца 15 символов
            }
            System.out.println();
        }
    }

    // Метод для проверки корректности индексов пользователя и объекта
    private boolean isValidIndex(int userIndex, int objectIndex) {
        return userIndex >= 0 && userIndex < users.length && objectIndex >= 0 && objectIndex < objects.length;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] users = {"User1", "User2", "User3", "User4"};
        String[] objects = {"File1", "File2", "File3", "File4"};
        AccessMatrix accessMatrix = new AccessMatrix(users, objects);

        // Печать всей матрицы доступа в виде таблицы
        accessMatrix.printAccessMatrix();

        // Меню для выбора пользователя
        System.out.print("Введите имя пользователя для вывода данных доступа (User1, User2, User3, User4): ");
        String userChoice = scanner.nextLine();
        accessMatrix.printUserAccess(userChoice);

        // Закрытие сканнера
        scanner.close();
    }
}