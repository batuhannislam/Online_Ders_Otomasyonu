package online_ders_otomasyonu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DerslerPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private JTable tblDersler;
    private JTextField txtDersID;
    private JTextField txtDersAdi;
    private JComboBox<OgretmenComboBoxItem> cmbOgretmen; // Öğretmenleri listelemek için
    private JButton btnEkle;
    private JButton btnGuncelle;
    private JButton btnSil;
    private JButton btnTemizle;

    // Öğretmenleri JComboBox'ta göstermek için yardımcı sınıf
    private static class OgretmenComboBoxItem {
        private int id;
        private String adSoyad;

        public OgretmenComboBoxItem(int id, String ad, String soyad) {
            this.id = id;
            this.adSoyad = ad + " " + soyad;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return adSoyad + " (ID: " + id + ")"; // ComboBox'ta görünecek metin
        }
    }

    public DerslerPanel() {
        initComponents();
        loadOgretmenlerToComboBox();
        loadDersler();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        JPanel pnlDersID = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pnlDersAdi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel pnlOgretmen = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel lblDersID = new JLabel("Ders ID:");
        txtDersID = new JTextField(10);
        txtDersID.setEditable(false);
        pnlDersID.add(lblDersID);
        pnlDersID.add(txtDersID);

        JLabel lblDersAdi = new JLabel("Ders Adı:");
        txtDersAdi = new JTextField(25);
        pnlDersAdi.add(lblDersAdi);
        pnlDersAdi.add(txtDersAdi);

        JLabel lblOgretmen = new JLabel("Öğretmen:");
        cmbOgretmen = new JComboBox<>();
        cmbOgretmen.setPreferredSize(new java.awt.Dimension(250, 22)); // Combobox boyutunu ayarla
        pnlOgretmen.add(lblOgretmen);
        pnlOgretmen.add(cmbOgretmen);

        formPanel.add(pnlDersID);
        formPanel.add(pnlDersAdi);
        formPanel.add(pnlOgretmen);

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
        tableModel = new DefaultTableModel(new String[]{"DersID", "Ders Adı", "Öğretmen ID", "Öğretmen Adı Soyadı"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDersler = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblDersler);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnEkle.addActionListener(e -> ekleDers());
        btnGuncelle.addActionListener(e -> guncelleDers());
        btnSil.addActionListener(e -> silDers());
        btnTemizle.addActionListener(e -> temizleForm());

        tblDersler.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblDersler.getSelectedRow();
                if (selectedRow != -1) {
                    txtDersID.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtDersAdi.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    int ogretmenID = (int) tableModel.getValueAt(selectedRow, 2);
                    // ComboBox'ta ilgili öğretmeni seç
                    for (int i = 0; i < cmbOgretmen.getItemCount(); i++) {
                        OgretmenComboBoxItem item = cmbOgretmen.getItemAt(i);
                        if (item.getId() == ogretmenID) {
                            cmbOgretmen.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void loadOgretmenlerToComboBox() {
        cmbOgretmen.removeAllItems();
        String query = "SELECT OgretmenID, Ad, Soyad FROM Ogretmenler ORDER BY Ad, Soyad";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbOgretmen.addItem(new OgretmenComboBoxItem(rs.getInt("OgretmenID"), rs.getString("Ad"), rs.getString("Soyad")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğretmenler yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDersler() {
        tableModel.setRowCount(0);
        String query = "SELECT D.DersID, D.DersAdi, D.OgretmenID, O.Ad AS OgretmenAdi, O.Soyad AS OgretmenSoyadi " +
                       "FROM Dersler D " +
                       "INNER JOIN Ogretmenler O ON D.OgretmenID = O.OgretmenID " +
                       "ORDER BY D.DersAdi";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("DersID"));
                row.add(rs.getString("DersAdi"));
                row.add(rs.getInt("OgretmenID"));
                row.add(rs.getString("OgretmenAdi") + " " + rs.getString("OgretmenSoyadi"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Dersler yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void ekleDers() {
        String dersAdi = txtDersAdi.getText().trim();
        OgretmenComboBoxItem secilenOgretmen = (OgretmenComboBoxItem) cmbOgretmen.getSelectedItem();

        if (dersAdi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ders adı boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (secilenOgretmen == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir öğretmen seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO Dersler (DersAdi, OgretmenID) VALUES (?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dersAdi);
            pstmt.setInt(2, secilenOgretmen.getId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Ders başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadDersler();
                temizleForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ders eklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void guncelleDers() {
        String dersIDStr = txtDersID.getText().trim();
        if (dersIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir ders seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dersAdi = txtDersAdi.getText().trim();
        OgretmenComboBoxItem secilenOgretmen = (OgretmenComboBoxItem) cmbOgretmen.getSelectedItem();

        if (dersAdi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ders adı boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (secilenOgretmen == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir öğretmen seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int dersID = Integer.parseInt(dersIDStr);
        String query = "UPDATE Dersler SET DersAdi = ?, OgretmenID = ? WHERE DersID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dersAdi);
            pstmt.setInt(2, secilenOgretmen.getId());
            pstmt.setInt(3, dersID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Ders başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadDersler();
                temizleForm();
            } else {
                JOptionPane.showMessageDialog(this, "Ders bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ders güncellenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void silDers() {
        String dersIDStr = txtDersID.getText().trim();
        if (dersIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir ders seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int dersID = Integer.parseInt(dersIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bu dersi silmek istediğinizden emin misiniz?\nBu dersle ilişkili materyaller, sınavlar ve öğrenci kayıtları varsa sorun oluşabilir!",
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            // İlişkili kayıtları kontrol et (Materyaller, Sinavlar, OgrenciDersler)
            if (isDersReferenced(dersID)) {
                 JOptionPane.showMessageDialog(this,
                        "Bu ders başka tablolarda (Materyaller, Sınavlar, Öğrenci Dersleri) kullanıldığı için silinemez.\nLütfen önce bu dersle ilişkili tüm kayıtları silin.",
                        "Silme Engellendi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "DELETE FROM Dersler WHERE DersID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, dersID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Ders başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadDersler();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Ders bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Ders silinirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private boolean isDersReferenced(int dersID) {
        String[] tablesToCheck = {"Materyaller", "Sinavlar", "OgrenciDersler"};
        try (Connection conn = AdminForm.DBConnection.getConnection()) {
            for (String tableName : tablesToCheck) {
                String query = "SELECT COUNT(*) FROM " + tableName + " WHERE DersID = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, dersID);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            return true; // Ders bu tabloda referans alınmış
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hata durumunda, güvenli tarafta kalıp referans var gibi davran
            return true;
        }
        return false; // Ders hiçbir tabloda referans alınmamış
    }


    private void temizleForm() {
        txtDersID.setText("");
        txtDersAdi.setText("");
        cmbOgretmen.setSelectedIndex(-1); // Combobox'ı temizle (eğer içinde item varsa ilkini seçebilir)
        if (cmbOgretmen.getItemCount() > 0) {
            // cmbOgretmen.setSelectedIndex(0); // Veya hiçbir şey seçili olmasın
        }
        tblDersler.clearSelection();
    }
}
