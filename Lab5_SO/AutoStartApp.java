
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

    private static void createWindow(String time) {

        JFrame frame = new JFrame("AutoStartApp");
        frame.setSize(500, 400);
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

        JButton filesButton = new JButton(
                "GESTIONAREA FISIERELOR"
        );
        filesButton.setFont(new Font("Arial", Font.BOLD, 13));
        filesButton.setBackground(new Color(40, 105, 165));
        filesButton.setForeground(Color.WHITE);
        filesButton.setFocusPainted(false);
        filesButton.setBorderPainted(false);
        filesButton.setOpaque(true);
        filesButton.setPreferredSize(new Dimension(250, 42));

        filesButton.addActionListener(e -> {
            try {
                Path projectDir = Path.of(
                        AutoStartApp.class
                                .getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .toURI()
                );

                String javaExe = Path.of(
                        System.getProperty("java.home"),
                        "bin",
                        "java.exe"
                ).toString();

                String command = "\"" + javaExe
                        + "\" -cp \"" + projectDir
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

        JButton closeButton = new JButton("INCHIDE APLICATIA");
        closeButton.setFont(new Font("Arial", Font.BOLD, 13));
        closeButton.setBackground(new Color(100, 110, 120));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setOpaque(true);
        closeButton.setPreferredSize(new Dimension(250, 38));
        closeButton.addActionListener(e -> frame.dispose());

        JPanel footer = new JPanel();
        footer.setBackground(mainPanel.getBackground());
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        filesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(filesButton);
        footer.add(Box.createVerticalStrut(10));
        footer.add(closeButton);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
