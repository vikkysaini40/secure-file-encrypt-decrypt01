/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Scanner;

public class SecureFileTool {

    // -------------------- AES KEY GENERATION --------------------
    public static SecretKeySpec generateKey(String password) throws Exception {
        byte[] key = password.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        return new SecretKeySpec(Arrays.copyOf(key, 16), "AES");
    }

    // -------------------- ENCRYPT FILE --------------------
    public static void encryptFile(String inputFile, String password) {
        try {
            SecretKeySpec key = generateKey(password);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] fileData = Files.readAllBytes(new File(inputFile).toPath());
            byte[] encrypted = cipher.doFinal(fileData);

            FileOutputStream fos = new FileOutputStream(inputFile + ".enc");
            fos.write(encrypted);
            fos.close();

            System.out.println("✔ Encrypted Successfully → " + inputFile + ".enc");

        } catch (Exception e) {
            System.out.println("❌ Encryption Failed!");
        }
    }

    // -------------------- DECRYPT FILE --------------------
    public static void decryptFile(String inputFile, String password) {
        try {
            SecretKeySpec key = generateKey(password);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encData = Files.readAllBytes(new File(inputFile).toPath());
            byte[] decrypted = cipher.doFinal(encData);

            String outFile = inputFile.replace(".enc", "_decrypted");
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(decrypted);
            fos.close();

            System.out.println("✔ Decrypted Successfully → " + outFile);

        } catch (Exception e) {
            System.out.println("❌ Wrong password or invalid file!");
        }
    }

    // ===============================================================
    //                GUI MODE (SWING)
    // ===============================================================
    public static void launchGUI() {
        JFrame frame = new JFrame("Secure File Encryption & Decryption");
        frame.setSize(500, 350);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(40, 40, 60));

        JLabel title = new JLabel("Secure File Tool (GUI Mode)");
        title.setForeground(Color.WHITE);
        title.setBounds(150, 10, 300, 30);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(title);

        JTextField fileField = new JTextField();
        fileField.setBounds(40, 70, 300, 30);
        frame.add(fileField);

        JButton browseBtn = new JButton("Browse");
        browseBtn.setBounds(350, 70, 100, 30);
        frame.add(browseBtn);

        JLabel passLabel = new JLabel("Enter Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(40, 120, 150, 30);
        frame.add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(160, 120, 200, 30);
        frame.add(passwordField);

        JButton encryptBtn = new JButton("Encrypt");
        encryptBtn.setBounds(100, 200, 120, 40);
        frame.add(encryptBtn);

        JButton decryptBtn = new JButton("Decrypt");
        decryptBtn.setBounds(260, 200, 120, 40);
        frame.add(decryptBtn);

        // Browse File
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        // Encrypt Button
        encryptBtn.addActionListener(e -> {
            String path = fileField.getText();
            String pass = new String(passwordField.getPassword());

            if (path.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select file & enter password!");
                return;
            }

            encryptFile(path, pass);
            JOptionPane.showMessageDialog(frame, "File Encrypted Successfully!");
        });

        // Decrypt Button
        decryptBtn.addActionListener(e -> {
            String path = fileField.getText();
            String pass = new String(passwordField.getPassword());

            if (path.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select file & enter password!");
                return;
            }

            decryptFile(path, pass);
            JOptionPane.showMessageDialog(frame, "File Decrypted Successfully!");
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // ===============================================================
    //                CONSOLE MODE
    // ===============================================================
    public static void launchConsole() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Secure File Tool (Console Mode) ===");
        System.out.println("1. Encrypt File");
        System.out.println("2. Decrypt File");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter file path: ");
        String path = sc.nextLine();

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (choice == 1) encryptFile(path, password);
        else decryptFile(path, password);
    }

    // ===============================================================
    //                MAIN MENU
    // ===============================================================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Select Mode ===");
        System.out.println("1. GUI Mode");
        System.out.println("2. Console Mode");
        System.out.print("Choose: ");
        int mode = sc.nextInt();

        if (mode == 1) launchGUI();
        else launchConsole();
    }
}