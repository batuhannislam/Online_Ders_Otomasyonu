package online_ders_otomasyonu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension; // Boyutlandırma için eklendi

public class RollerPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private JTable tblRoller;
    private JTextField txtRolID;
    private JTextField txtRolAdi;
    private JButton btnEkle;
    private JButton btnGuncelle;
    private JButton btnSil;
    private JButton btnTemizle;

    public RollerPanel() {
        initComponents();
        loadRoller();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // Ana panel için BorderLayout

        // Form paneli (giriş alanları ve etiketler için)
        JPanel formInnerPanel = new JPanel(); // İç içe panel yapısı daha iyi kontrol için
        formInnerPanel.setLayout(new BoxLayout(formInnerPanel, BoxLayout.Y_AXIS)); // Dikey sıralama

        JPanel pnlRolID = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblRolID = new JLabel("Rol ID:");
        lblRolID.setPreferredSize(new Dimension(80, 22)); // Etiket boyutunu ayarla
        txtRolID = new JTextField(10);
        txtRolID.setEditable(false); // ID alanı düzenlenemez
        pnlRolID.add(lblRolID);
        pnlRolID.add(txtRolID);

        JPanel pnlRolAdi = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblRolAdi = new JLabel("Rol Adı:");
        lblRolAdi.setPreferredSize(new Dimension(80, 22)); // Etiket boyutunu ayarla
        txtRolAdi = new JTextField(20);
        pnlRolAdi.add(lblRolAdi);
        pnlRolAdi.add(txtRolAdi);

        formInnerPanel.add(pnlRolID);
        formInnerPanel.add(pnlRolAdi);

        // formInnerPanel'i sarmalayan bir dış panel (kenar boşlukları için)
        JPanel formOuterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formOuterPanel.add(formInnerPanel);


        // Buton paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnEkle = new JButton("Ekle");
        btnGuncelle = new JButton("Güncelle");
        btnSil = new JButton("Sil");
        btnTemizle = new JButton("Temizle");

        buttonPanel.add(btnEkle);
        buttonPanel.add(btnGuncelle);
        buttonPanel.add(btnSil);
        buttonPanel.add(btnTemizle);

        // Tablo
        tableModel = new DefaultTableModel(new String[]{"RolID", "Rol Adı"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tablo hücreleri düzenlenemez
            }
        };
        tblRoller = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblRoller);

        // Üst panel (form ve butonları içerecek)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formOuterPanel, BorderLayout.CENTER); // formOuterPanel'i merkeze al
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnEkle.addActionListener(e -> ekleRol());
        btnGuncelle.addActionListener(e -> guncelleRol());
        btnSil.addActionListener(e -> silRol());
        btnTemizle.addActionListener(e -> temizleForm());

        tblRoller.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblRoller.getSelectedRow();
                if (selectedRow != -1) {
                    txtRolID.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtRolAdi.setText(tableModel.getValueAt(selectedRow, 1).toString());
                }
            }
        });
    }

    private void loadRoller() {
        // Modeli temizle
        tableModel.setRowCount(0);

        String query = "SELECT RolID, RolAdi FROM Roller ORDER BY RolID";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("RolID"));
                row.add(rs.getString("RolAdi"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Roller yüklenirken hata oluştu: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void ekleRol() {
        String rolAdi = txtRolAdi.getText().trim();
        if (rolAdi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rol adı boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO Roller (RolAdi) VALUES (?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, rolAdi);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Rol başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadRoller(); // Tabloyu yenile
                temizleForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Rol eklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void guncelleRol() {
        String rolIDStr = txtRolID.getText().trim();
        if (rolIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir rol seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String rolAdi = txtRolAdi.getText().trim();
        if (rolAdi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rol adı boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rolID = Integer.parseInt(rolIDStr);
        String query = "UPDATE Roller SET RolAdi = ? WHERE RolID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, rolAdi);
            pstmt.setInt(2, rolID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Rol başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadRoller();
                temizleForm();
            } else {
                JOptionPane.showMessageDialog(this, "Rol bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Rol güncellenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void silRol() {
        String rolIDStr = txtRolID.getText().trim();
        if (rolIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir rol seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rolID = Integer.parseInt(rolIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bu rolü silmek istediğinizden emin misiniz?\nBu role sahip kullanıcılar varsa sorun oluşabilir!",
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            // Rolün Kullanicilar tablosunda kullanılıp kullanılmadığını kontrol et
            if (isRolReferenced(rolID)) {
                JOptionPane.showMessageDialog(this,
                        "Bu rol 'Kullanicilar' tablosunda kullanıldığı için silinemez.\nLütfen önce bu role sahip kullanıcıların rollerini değiştirin.",
                        "Silme Engellendi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "DELETE FROM Roller WHERE RolID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, rolID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Rol başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadRoller();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Rol bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Rol silinirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private boolean isRolReferenced(int rolID) {
        String query = "SELECT COUNT(*) FROM Kullanicilar WHERE RolID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rolID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Rol kullanılıyor
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hata durumunda, güvenli tarafta kalıp referans var gibi davran
            return true;
        }
        return false; // Rol kullanılmıyor
    }

    private void temizleForm() {
        txtRolID.setText("");
        txtRolAdi.setText("");
        tblRoller.clearSelection();
    }
}

