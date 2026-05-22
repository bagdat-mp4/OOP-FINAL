package views;

import java.util.Scanner;


public abstract class BaseView {

    public static String currentLanguage = "EN";
    protected Scanner scanner = new Scanner(System.in);

    public BaseView() {
    }

    public int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String readString() {
        return scanner.nextLine().trim();
    }

    public void clearScreen() {
        System.out.println("\n".repeat(3));
    }

    public void changeLanguageMenu() {
        System.out.println("1. English\n2. Русский\n3. Қазақша");
        System.out.print("Choose: ");
        int c = readInt();
        switch (c) {
            case 1:
                currentLanguage = "EN";
                System.out.println("Language: English");
                break;
            case 2:
                currentLanguage = "RU";
                System.out.println("Язык: Русский");
                break;
            case 3:
                currentLanguage = "KZ";
                System.out.println("Тіл: Қазақша");
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    public abstract void displayMenu();

}
