
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AutoStartApp {

    public static void main(String[] args) {

        try {
            Path desktop = Path.of(
                    System.getProperty("user.home"),
                    "OneDrive",
                    "Desktop"
            );

            Path logFile = desktop.resolve("AutoStart_log.txt");

            String time = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            );

            Files.writeString(
                    logFile,
                    LocalDateTime.now()
                            + " - Aplicatia AutoStartApp a pornit."
                            + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            SwingUtilities.invokeLater(() -> createWindow(time));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Determina folderul in care se afla aplicatia
    private static Path getProjectDirectory() throws Exception {
        return Path.of(
                AutoStartApp.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
        ).toAbsolutePath();
    }

    // Adauga aplicatia in pornirea automata din registrul Windows
    private static void configureAutostart(JFrame frame) {

        try {
            Path projectDir = getProjectDirectory();

            String javawExe = Path.of(
                    System.getProperty("java.home"),
                    "bin",
                    "javaw.exe"
            ).toString();

            String startupCommand = "\""
                    + javawExe
                    + "\" -cp \""
                    + projectDir
                    + "\" AutoStartApp";

            Process process = new ProcessBuilder(
                    "reg.exe",
                    "add",
                    "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run",
                    "/v",
                    "AutoStartApp",
                    "/t",
                    "REG_SZ",
                    "/d",
                    startupCommand,
                    "/f"
            ).redirectErrorStream(true).start();

            String output = new String(
                    process.getInputStream().readAllBytes()
            );

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Aplicatia a fost adaugata cu succes "
                                + "in pornirea automata Windows!\n\n"
                                + "Registrul Windows a fost actualizat.",
                        "Configurare reusita",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "Nu s-a putut configura pornirea automata.\n"
                                + output,
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Eroare la configurarea pornirii automate:\n"
                            + ex.getMessage(),
                    "Eroare",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Reporneste calculatorul dupa confirmarea utilizatorului
    private static void restartComputer(JFrame frame) {

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Esti sigur ca doresti sa repornesti calculatorul?\n\n"
                        + "Repornirea va incepe in 30 de secunde.",
                "Confirmare repornire",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                new ProcessBuilder(
                        "shutdown.exe",
                        "/r",
                        "/t",
                        "30",
                        "/c",
                        "Repornire pentru lucrarea de laborator"
                ).start();

                JOptionPane.showMessageDialog(
                        frame,
                        "Calculatorul va fi repornit in 30 de secunde.\n"
                                + "Salveaza fisierele deschise!",
                        "Repornire programata",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Nu s-a putut reporni calculatorul:\n"
                                + ex.getMessage(),
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private static void createWindow(String time) {

        JFrame frame = new JFrame("AutoStartApp");
        frame.setSize(500, 510);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(238, 244, 250));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setLayout(new BorderLayout(10, 15));

        JLabel title = new JLabel("AutoStartApp");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(new Color(30, 60, 95));

        JLabel subtitle = new JLabel(
                "Sistem de pornire automata"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        JPanel header = new JPanel();
        header.setBackground(mainPanel.getBackground());
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(subtitle);

        JLabel welcome = new JLabel("Bine ai revenit!");
        welcome.setFont(new Font("Arial", Font.BOLD, 23));
        welcome.setForeground(new Color(35, 110, 75));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel status = new JLabel(
                "Aplicatia a pornit cu succes!"
        );
        status.setFont(new Font("Arial", Font.PLAIN, 15));
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel date = new JLabel("Data si ora: " + time);
        date.setFont(new Font("Arial", Font.PLAIN, 14));
        date.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel center = new JPanel();
        center.setBackground(mainPanel.getBackground());
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalStrut(15));
        center.add(welcome);
        center.add(Box.createVerticalStrut(10));
        center.add(status);
        center.add(Box.createVerticalStrut(10));
        center.add(date);

        // Buton pentru gestionarea fisierelor
        JButton filesButton = new JButton(
                "GESTIONAREA FISIERELOR"
        );
        styleButton(
                filesButton,
                new Color(40, 105, 165),
                250,
                42
        );

        filesButton.addActionListener(e -> {
            try {
                Path projectDir = getProjectDirectory();

                String javaExe = Path.of(
                        System.getProperty("java.home"),
                        "bin",
                        "java.exe"
                ).toString();

                String command = "\""
                        + javaExe
                        + "\" -cp \""
                        + projectDir
                        + "\" Main";

                new ProcessBuilder(
                        "cmd.exe",
                        "/c",
                        "start",
                        "Gestionarea fisierelor",
                        "cmd.exe",
                        "/k",
                        command
                ).start();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Nu s-a putut porni aplicatia: "
                                + ex.getMessage(),
                        "Eroare",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Buton pentru configurarea pornirii automate
        JButton autostartButton = new JButton(
                "CONFIGUREAZA PORNIREA AUTOMATA"
        );
        styleButton(
                autostartButton,
                new Color(35, 135, 95),
                250,
                42
        );

        autostartButton.addActionListener(
                e -> configureAutostart(frame)
        );

        // Buton pentru repornirea calculatorului
        JButton restartButton = new JButton(
                "REPORNESTE CALCULATORUL"
        );
        styleButton(
                restartButton,
                new Color(190, 115, 40),
                250,
                42
        );

        restartButton.addActionListener(
                e -> restartComputer(frame)
        );

        // Buton pentru inchiderea aplicatiei
        JButton closeButton = new JButton(
                "INCHIDE APLICATIA"
        );
        styleButton(
                closeButton,
                new Color(100, 110, 120),
                250,
                38
        );

        closeButton.addActionListener(e -> frame.dispose());

        JPanel footer = new JPanel();
        footer.setBackground(mainPanel.getBackground());
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));

        filesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        autostartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(filesButton);
        footer.add(Box.createVerticalStrut(8));
        footer.add(autostartButton);
        footer.add(Box.createVerticalStrut(8));
        footer.add(restartButton);
        footer.add(Box.createVerticalStrut(8));
        footer.add(closeButton);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Stabileste aspectul butoanelor
    private static void styleButton(
            JButton button,
            Color color,
            int width,
            int height
    ) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(width, height));
        button.setMaximumSize(new Dimension(width, height));
    }
}
