
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileRun {

    public static void run() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println("       GESTIONAREA FISIERELOR");
        System.out.println("======================================");

        // Selectarea folderului
        System.out.print("\nIntroduceti calea folderului: ");
        File folder = new File(scanner.nextLine().trim());

        // Verificarea folderului
        if (!folder.isDirectory() || !folder.canRead()) {
            System.out.println("Eroare: folder invalid sau inaccesibil!");
            scanner.close();
            return;
        }

        // Identificarea fisierelor mai vechi de 30 de zile
        List<File> files = FileManager.findOldFiles(folder);

        if (files.isEmpty()) {
            System.out.println("Nu exista fisiere mai vechi de 30 de zile.");
            scanner.close();
            return;
        }

        // Afisarea fisierelor identificate
        System.out.println("\nFISIERE IDENTIFICATE:");

        for (int i = 0; i < files.size(); i++) {

            File file = files.get(i);

            System.out.printf(
                    "%d. %s | Tip: %s | Dimensiune: %.2f MB%n",
                    i + 1,
                    file.getName(),
                    FileManager.getExtension(file),
                    FileManager.getSizeMB(file)
            );
        }

        // Selectarea fisierelor pentru stergere
        System.out.println("\nIntroduceti numarul fisierului dorit.");
        System.out.println("Pentru mai multe fisiere, separati numerele prin virgula.");
        System.out.println("Introduceti A pentru toate fisierele.");
        System.out.println("Introduceti 0 pentru anulare.");

        System.out.print("Selectia: ");
        String choice = scanner.nextLine().trim();

        List<File> selectedFiles = new ArrayList<>();

        if (choice.equals("0")) {

            System.out.println("Stergerea a fost anulata.");
            scanner.close();
            return;

        } else if (choice.equalsIgnoreCase("A")) {

            selectedFiles.addAll(files);

        } else {

            // Impartim selectia in numere separate
            String[] selections = choice.split(",");

            try {

                for (String selection : selections) {

                    int index = Integer.parseInt(selection.trim());

                    // Verificam daca numarul introdus este valid
                    if (index < 1 || index > files.size()) {
                        System.out.println("Eroare: selectie invalida!");
                        scanner.close();
                        return;
                    }

                    // Obtinem fisierul selectat
                    File selectedFile = files.get(index - 1);

                    // Evitam selectarea repetata a aceluiasi fisier
                    if (!selectedFiles.contains(selectedFile)) {
                        selectedFiles.add(selectedFile);
                    }
                }

            } catch (NumberFormatException e) {

                System.out.println("Eroare: introduceti numere valide!");
                scanner.close();
                return;
            }
        }

        // Confirmarea stergerii
        System.out.println("\nAti selectat " + selectedFiles.size() + " fisier(e).");
        System.out.println("Atentie: fisierele vor fi sterse definitiv.");
        System.out.print("Confirmati stergerea? (DA/NU): ");

        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equalsIgnoreCase("DA")) {

            System.out.println("Stergerea a fost anulata.");
            scanner.close();
            return;
        }

        // Stergerea fisierelor selectate
        System.out.println("\nREZULTATUL STERGERII:");

        for (File file : selectedFiles) {

            if (FileManager.deleteFile(file)) {
                System.out.println("Sters: " + file.getName());
            } else {
                System.out.println("Nu a putut fi sters: " + file.getName());
            }
        }

        // Verificarea fisierelor ramase dupa stergere
        System.out.println("\nFISIERE RAMASE DUPA STERGERE:");

        List<File> remainingFiles = FileManager.findOldFiles(folder);

        if (remainingFiles.isEmpty()) {

            System.out.println("Nu au ramas fisiere mai vechi de 30 de zile.");

        } else {

            for (File file : remainingFiles) {

                System.out.printf(
                        "%s | Tip: %s | Dimensiune: %.2f MB%n",
                        file.getName(),
                        FileManager.getExtension(file),
                        FileManager.getSizeMB(file)
                );
            }
        }

        scanner.close();
    }
}
