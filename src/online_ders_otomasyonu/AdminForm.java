package online_ders_otomasyonu;

import java.awt.BorderLayout; // Ensure this is imported
import java.awt.GridLayout; // For button panel
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AdminForm extends javax.swing.JFrame {

    // DBConnection inner class
    public static class DBConnection {
        private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Online_Ders_Otomasyonu;encrypt=true;trustServerCertificate=true;";
        private static final String USER = "bi"; // Your DB username
        private static final String PASSWORD = "123456Aa"; // Your DB password

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }

    // Constructor
    public AdminForm() {
        initComponents();
        setLocationRelativeTo(null); // Center the frame on screen
        setTitle("Admin Paneli - Online Ders Otomasyonu"); // Set a title for the frame
        // Optionally set a default panel
        btnKullanicilarActionPerformed(null); // Show KullanicilarPanel by default
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        // Panel for buttons
        pnlButtons = new javax.swing.JPanel();
        btnKullanicilar = new javax.swing.JButton();
        btnOgretmenler = new javax.swing.JButton();
        btnOgrenciler = new javax.swing.JButton();
        btnRoller = new javax.swing.JButton();
        btnDersler = new javax.swing.JButton();
        btnMateryaller = new javax.swing.JButton();
        btnSinavlar = new javax.swing.JButton();
        btnSorular = new javax.swing.JButton();
        btnCevaplar = new javax.swing.JButton();
        btnOgrenciDersler = new javax.swing.JButton();

        // Main content panel
        pnlContent = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 700)); // Set a preferred size for the window

        // Configure button panel
        pnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 5)); // FlowLayout for buttons

        btnKullanicilar.setText("Kullanıcılar");
        btnKullanicilar.addActionListener(evt -> btnKullanicilarActionPerformed(evt));
        pnlButtons.add(btnKullanicilar);

        btnOgretmenler.setText("Öğretmenler");
        btnOgretmenler.addActionListener(evt -> btnOgretmenlerActionPerformed(evt));
        pnlButtons.add(btnOgretmenler);

        btnOgrenciler.setText("Öğrenciler");
        btnOgrenciler.addActionListener(evt -> btnOgrencilerActionPerformed(evt));
        pnlButtons.add(btnOgrenciler);

        btnRoller.setText("Roller");
        btnRoller.addActionListener(evt -> btnRollerActionPerformed(evt));
        pnlButtons.add(btnRoller);

        btnDersler.setText("Dersler");
        btnDersler.addActionListener(evt -> btnDerslerActionPerformed(evt));
        pnlButtons.add(btnDersler);

        btnMateryaller.setText("Materyaller");
        btnMateryaller.addActionListener(evt -> btnMateryallerActionPerformed(evt));
        pnlButtons.add(btnMateryaller);

        btnSinavlar.setText("Sınavlar");
        btnSinavlar.addActionListener(evt -> btnSinavlarActionPerformed(evt));
        pnlButtons.add(btnSinavlar);

        btnSorular.setText("Sorular");
        btnSorular.addActionListener(evt -> btnSorularActionPerformed(evt));
        pnlButtons.add(btnSorular);

        btnCevaplar.setText("Cevaplar");
        btnCevaplar.addActionListener(evt -> btnCevaplarActionPerformed(evt));
        pnlButtons.add(btnCevaplar);

        btnOgrenciDersler.setText("Öğrenci Dersleri");
        btnOgrenciDersler.addActionListener(evt -> btnOgrenciDerslerActionPerformed(evt));
        pnlButtons.add(btnOgrenciDersler);


        // Configure pnlContent
        pnlContent.setBackground(new java.awt.Color(220, 220, 240)); // Light lavender background
        pnlContent.setLayout(new java.awt.BorderLayout());

        // Main layout for the JFrame's contentPane
        getContentPane().setLayout(new java.awt.BorderLayout(0, 5)); // Main BorderLayout with a small vertical gap
        getContentPane().add(pnlButtons, java.awt.BorderLayout.NORTH); // Buttons at the top
        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER); // Content panel in the center

        pack();
    }// </editor-fold>

    // Action Performed methods for each button
    private void btnKullanicilarActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new KullaniciPanel());
    }

    private void btnOgretmenlerActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new OgretmenPanel());
    }

    private void btnOgrencilerActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new OgrenciPanel());
    }

    private void btnRollerActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new RollerPanel());
    }

    private void btnDerslerActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new DerslerPanel());
    }

    private void btnMateryallerActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new MateryallerPanel());
    }

    private void btnSinavlarActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new SinavlarPanel());
    }

    private void btnSorularActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new SorularPanel());
    }

    private void btnCevaplarActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new CevaplarPanel());
    }

    private void btnOgrenciDerslerActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(new OgrenciDerslerPanel());
    }

    // Method to switch panels in pnlContent
    private void showPanel(JPanel panel) {
        pnlContent.removeAll();
        pnlContent.add(panel, java.awt.BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    // Main method
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new AdminForm().setVisible(true);
        });
    }

    // Variables declaration -
    private javax.swing.JPanel pnlButtons; // Panel to hold all navigation buttons
    private javax.swing.JButton btnKullanicilar;
    private javax.swing.JButton btnOgretmenler;
    private javax.swing.JButton btnOgrenciler;
    private javax.swing.JButton btnRoller;
    private javax.swing.JButton btnDersler;
    private javax.swing.JButton btnMateryaller;
    private javax.swing.JButton btnSinavlar;
    private javax.swing.JButton btnSorular;
    private javax.swing.JButton btnCevaplar;
    private javax.swing.JButton btnOgrenciDersler;
    private javax.swing.JPanel pnlContent; // Panel to display the content of selected button
    // End of variables declaration
}
