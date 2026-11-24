
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileManager {

    // Perioada de verificare: 30 de zile
    private static final long THIRTY_DAYS =
            30L * 24 * 60 * 60 * 1000;

    // Identificarea fisierelor mai vechi de 30 de zile
    public static List<File> findOldFiles(File folder) {

        List<File> oldFiles = new ArrayList<>();

        // Verificarea validitatii folderului
        if (folder == null || !folder.isDirectory() || !folder.canRead()) {
            return oldFiles;
        }

        // Obtinem fisierele din folder
        File[] files = folder.listFiles();

        if (files == null) {
            return oldFiles;
        }

        // Obtinem timpul curent
        long currentTime = System.currentTimeMillis();

        // Parcurgem fisierele din folder
        for (File file : files) {

            // Ignoram directoarele
            if (!file.isFile()) {
                continue;
            }

            // Obtinem data ultimei modificari
            long lastModified = file.lastModified();

            if (lastModified <= 0) {
                continue;
            }

            // Verificam daca fisierul este mai vechi de 30 de zile
            if (currentTime - lastModified > THIRTY_DAYS) {
                oldFiles.add(file);
            }
        }

        // Sortarea dupa extensie si apoi dupa nume
        oldFiles.sort(
                Comparator.comparing(
                        FileManager::getExtension,
                        String.CASE_INSENSITIVE_ORDER
                ).thenComparing(
                        File::getName,
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return oldFiles;
    }

    // Determinarea extensiei fisierului
    public static String getExtension(File file) {

        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }

        return "fara extensie";
    }

    // Calcularea dimensiunii fisierului in MB
    public static double getSizeMB(File file) {

        return file.length() / 1_000_000.0;
    }

    // Stergerea fisierului
    public static boolean deleteFile(File file) {

        return file != null && file.isFile() && file.delete();
    }
}
