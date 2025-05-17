package online_ders_otomasyonu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.text.SimpleDateFormat; // Gerekirse diye eklendi, SoruComboBoxItem içinde kullanılabilir.

public class CevaplarPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private JTable tblCevaplar;
    private JTextField txtCevapID;
    private JComboBox<SoruComboBoxItem> cmbSoru;
    private JComboBox<OgrenciComboBoxItem> cmbOgrenci;
    private JTextField txtVerilenCevap;
    private JButton btnEkle;
    private JButton btnGuncelle;
    private JButton btnSil;
    private JButton btnTemizle;

    // Soruları JComboBox'ta göstermek için yardımcı sınıf
    private static class SoruComboBoxItem {
        private int id;
        private String soruMetniKisa; // Soru metninin kısa bir kısmı

        public SoruComboBoxItem(int id, String soruMetni) {
            this.id = id;
            // Soru metni çok uzunsa kısalt
            this.soruMetniKisa = (soruMetni.length() > 50) ? soruMetni.substring(0, 47) + "..." : soruMetni;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "ID: " + id + " - " + soruMetniKisa;
        }
    }

    // Öğrencileri JComboBox'ta göstermek için yardımcı sınıf
    public static class OgrenciComboBoxItem {
        private int id;
        private String adSoyad;

        public OgrenciComboBoxItem(int id, String ad, String soyad) {
            this.id = id;
            this.adSoyad = ad + " " + soyad;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return adSoyad + " (ID: " + id + ")";
        }
    }

    public CevaplarPanel() {
        initComponents();
        loadSorularToComboBox();
        loadOgrencilerToComboBox();
        loadCevaplar();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel pnlCevapID = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblCevapID = new JLabel("Cevap ID:");
        txtCevapID = new JTextField(10);
        txtCevapID.setEditable(false);
        pnlCevapID.add(lblCevapID);
        pnlCevapID.add(txtCevapID);

        JPanel pnlSoru = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSoru = new JLabel("Soru:");
        cmbSoru = new JComboBox<>();
        cmbSoru.setPreferredSize(new Dimension(350, 22));
        pnlSoru.add(lblSoru);
        pnlSoru.add(cmbSoru);

        JPanel pnlOgrenci = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblOgrenci = new JLabel("Öğrenci:");
        cmbOgrenci = new JComboBox<>();
        cmbOgrenci.setPreferredSize(new Dimension(350, 22));
        pnlOgrenci.add(lblOgrenci);
        pnlOgrenci.add(cmbOgrenci);

        JPanel pnlVerilenCevap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblVerilenCevap = new JLabel("Verilen Cevap:");
        txtVerilenCevap = new JTextField(30);
        pnlVerilenCevap.add(lblVerilenCevap);
        pnlVerilenCevap.add(txtVerilenCevap);

        formPanel.add(pnlCevapID);
        formPanel.add(pnlSoru);
        formPanel.add(pnlOgrenci);
        formPanel.add(pnlVerilenCevap);

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
        tableModel = new DefaultTableModel(new String[]{"CevapID", "Soru (ID)", "Öğrenci (Ad Soyad)", "Verilen Cevap"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCevaplar = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblCevaplar);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnEkle.addActionListener(e -> ekleCevap());
        btnGuncelle.addActionListener(e -> guncelleCevap());
        btnSil.addActionListener(e -> silCevap());
        btnTemizle.addActionListener(e -> temizleForm());

        tblCevaplar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblCevaplar.getSelectedRow();
                if (selectedRow != -1) {
                    txtCevapID.setText(tableModel.getValueAt(selectedRow, 0).toString());

                    // SoruID'yi alıp ComboBox'ta seç
                    Object soruData = tableModel.getValueAt(selectedRow, 1); // "ID: X - Soru Metni Kısaltılmış"
                    if (soruData != null) {
                        String soruString = soruData.toString();
                        try {
                            int soruID = Integer.parseInt(soruString.substring(soruString.indexOf("ID: ") + 4, soruString.indexOf(" - ")));
                            for (int i = 0; i < cmbSoru.getItemCount(); i++) {
                                SoruComboBoxItem item = cmbSoru.getItemAt(i);
                                if (item.getId() == soruID) {
                                    cmbSoru.setSelectedIndex(i);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            System.err.println("Soru ID parse error from table (CevaplarPanel): " + ex.getMessage() + " for string: " + soruString);
                            cmbSoru.setSelectedIndex(-1);
                        }
                    } else {
                        cmbSoru.setSelectedIndex(-1);
                    }


                    // OgrenciID'yi alıp ComboBox'ta seç
                    Object ogrenciData = tableModel.getValueAt(selectedRow, 2); // "Ad Soyad (ID: X)"
                     if (ogrenciData != null) {
                        String ogrenciString = ogrenciData.toString();
                        try {
                            int ogrenciID = Integer.parseInt(ogrenciString.substring(ogrenciString.lastIndexOf("(ID: ") + 5, ogrenciString.lastIndexOf(")")));
                            for (int i = 0; i < cmbOgrenci.getItemCount(); i++) {
                                OgrenciComboBoxItem item = cmbOgrenci.getItemAt(i);
                                if (item.getId() == ogrenciID) {
                                    cmbOgrenci.setSelectedIndex(i);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                             System.err.println("Öğrenci ID parse error from table (CevaplarPanel): " + ex.getMessage() + " for string: " + ogrenciString);
                            cmbOgrenci.setSelectedIndex(-1);
                        }
                    } else {
                        cmbOgrenci.setSelectedIndex(-1);
                    }

                    txtVerilenCevap.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");
                }
            }
        });
    }

    private void loadSorularToComboBox() {
        cmbSoru.removeAllItems();
        String query = "SELECT SoruID, SoruMetni FROM Sorular ORDER BY SoruID"; // SoruMetni çok uzun olabilir, dikkat.
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbSoru.addItem(new SoruComboBoxItem(rs.getInt("SoruID"), rs.getString("SoruMetni")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Sorular (Cevaplar için) combobox'a yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOgrencilerToComboBox() {
        cmbOgrenci.removeAllItems();
        String query = "SELECT OgrenciID, Ad, Soyad FROM Ogrenciler ORDER BY Ad, Soyad";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbOgrenci.addItem(new OgrenciComboBoxItem(rs.getInt("OgrenciID"), rs.getString("Ad"), rs.getString("Soyad")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğrenciler (Cevaplar için) combobox'a yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCevaplar() {
        tableModel.setRowCount(0);
        String query = "SELECT C.CevapID, C.SoruID, SR.SoruMetni, C.OgrenciID, O.Ad AS OgrenciAdi, O.Soyad AS OgrenciSoyadi, C.VerilenCevap " +
                       "FROM Cevaplar C " +
                       "INNER JOIN Sorular SR ON C.SoruID = SR.SoruID " +
                       "INNER JOIN Ogrenciler O ON C.OgrenciID = O.OgrenciID " +
                       "ORDER BY C.CevapID";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("CevapID"));
                // Soru bilgisini ComboBox'taki gibi göster
                String soruMetniKisa = (rs.getString("SoruMetni").length() > 30) ? rs.getString("SoruMetni").substring(0, 27) + "..." : rs.getString("SoruMetni");
                row.add("ID: " + rs.getInt("SoruID") + " - " + soruMetniKisa);
                row.add(rs.getString("OgrenciAdi") + " " + rs.getString("OgrenciSoyadi") + " (ID: " + rs.getInt("OgrenciID") + ")");
                row.add(rs.getString("VerilenCevap"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Cevaplar yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void ekleCevap() {
        SoruComboBoxItem secilenSoru = (SoruComboBoxItem) cmbSoru.getSelectedItem();
        OgrenciComboBoxItem secilenOgrenci = (OgrenciComboBoxItem) cmbOgrenci.getSelectedItem();
        String verilenCevap = txtVerilenCevap.getText().trim();

        if (secilenSoru == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir soru seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (secilenOgrenci == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir öğrenci seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (verilenCevap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Verilen cevap boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Bir öğrencinin aynı soruya birden fazla cevap vermesini engellemek için kontrol (isteğe bağlı)
        // Bu, veritabanında (SoruID, OgrenciID) üzerinde bir UNIQUE kısıtlaması ile de sağlanabilir.
        // if (isCevapExists(secilenSoru.getId(), secilenOgrenci.getId(), 0)) {
        // JOptionPane.showMessageDialog(this, "Bu öğrenci bu soruya zaten cevap vermiş.", "Hata", JOptionPane.ERROR_MESSAGE);
        // return;
        // }


        String query = "INSERT INTO Cevaplar (SoruID, OgrenciID, VerilenCevap) VALUES (?, ?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenSoru.getId());
            pstmt.setInt(2, secilenOgrenci.getId());
            pstmt.setString(3, verilenCevap);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Cevap başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadCevaplar();
                temizleForm();
            }
        } catch (SQLException e) {
            // SQLState "23000" ve Error Code 2627 (veya benzeri) unique constraint ihlali anlamına gelir.
            if (e.getSQLState().equals("23000")) {
                 JOptionPane.showMessageDialog(this, "Bu öğrenci bu soruya zaten cevap vermiş olabilir (Veritabanı Kısıtlaması).", "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cevap eklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }

    private void guncelleCevap() {
        String cevapIDStr = txtCevapID.getText().trim();
        if (cevapIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir cevap seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SoruComboBoxItem secilenSoru = (SoruComboBoxItem) cmbSoru.getSelectedItem();
        OgrenciComboBoxItem secilenOgrenci = (OgrenciComboBoxItem) cmbOgrenci.getSelectedItem();
        String verilenCevap = txtVerilenCevap.getText().trim();

        if (secilenSoru == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir soru seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (secilenOgrenci == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir öğrenci seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (verilenCevap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Verilen cevap boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int cevapID = Integer.parseInt(cevapIDStr);
        // Güncelleme sırasında (SoruID, OgrenciID) çiftinin başka bir CevapID'ye ait olup olmadığını kontrol etmek
        // if (isCevapExists(secilenSoru.getId(), secilenOgrenci.getId(), cevapID)) {
        // JOptionPane.showMessageDialog(this, "Bu öğrenci bu soruya zaten başka bir cevapla kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
        // return;
        // }

        String query = "UPDATE Cevaplar SET SoruID = ?, OgrenciID = ?, VerilenCevap = ? WHERE CevapID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenSoru.getId());
            pstmt.setInt(2, secilenOgrenci.getId());
            pstmt.setString(3, verilenCevap);
            pstmt.setInt(4, cevapID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Cevap başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadCevaplar();
                temizleForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cevap bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
             if (e.getSQLState().equals("23000")) {
                 JOptionPane.showMessageDialog(this, "Bu öğrenci bu soruya zaten cevap vermiş olabilir (Veritabanı Kısıtlaması).", "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cevap güncellenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }

    private void silCevap() {
        String cevapIDStr = txtCevapID.getText().trim();
        if (cevapIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir cevap seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cevapID = Integer.parseInt(cevapIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bu cevabı silmek istediğinizden emin misiniz?",
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            // Cevaplar tablosunun başka bir tabloya referans verdiği bir durum yok gibi görünüyor (child değil).
            String query = "DELETE FROM Cevaplar WHERE CevapID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, cevapID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Cevap başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadCevaplar();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Cevap bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Cevap silinirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    // Bir öğrencinin belirli bir soruya zaten cevap verip vermediğini kontrol eden metot (isteğe bağlı)
    // currentCevapID, güncelleme sırasında mevcut cevabın kendisini kontrol etmemek için kullanılır.
    private boolean isCevapExists(int soruID, int ogrenciID, int currentCevapID) {
        String query = "SELECT COUNT(*) FROM Cevaplar WHERE SoruID = ? AND OgrenciID = ? AND CevapID != ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, soruID);
            pstmt.setInt(2, ogrenciID);
            pstmt.setInt(3, currentCevapID > 0 ? currentCevapID : -1); // Yeni kayıtta CevapID kontrol edilmesin
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Bu SoruID ve OgrenciID çifti zaten var
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Hata durumunda var gibi davranmak daha güvenli olabilir
        }
        return false;
    }


    private void temizleForm() {
        txtCevapID.setText("");
        cmbSoru.setSelectedIndex(-1);
        cmbOgrenci.setSelectedIndex(-1);
        txtVerilenCevap.setText("");
        tblCevaplar.clearSelection();
    }
}
