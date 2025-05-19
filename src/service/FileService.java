package service;

import java.io.*;
import java.util.Scanner;

public class FileService {
    private static final String FILES_DIR = "files/";

    // Create a new file
    public static void createFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file name: ");
        String fileName = scanner.nextLine();

        File file = new File(FILES_DIR + fileName);

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                System.out.print("Enter content: ");
                String content = scanner.nextLine();
                FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();
                System.out.println("Content written to file.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating file.");
        }
    }

    // View all files in the directory
    public static void viewFiles() {
        File directory = new File(FILES_DIR);
        String[] files = directory.list();

        if (files != null && files.length > 0) {
            System.out.println("\nStored Files:");
            for (String file : files) {
                System.out.println("- " + file);
            }
        } else {
            System.out.println("No files found.");
        }
    }

    // Delete a file
    public static void deleteFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file name to delete: ");
        String fileName = scanner.nextLine();

        File file = new File(FILES_DIR + fileName);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Deleted file: " + fileName);
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }
}
