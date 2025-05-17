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

public class OgrenciDerslerPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private JTable tblOgrenciDersler;
    private JTextField txtOgrenciDersID;
    private JComboBox<CevaplarPanel.OgrenciComboBoxItem> cmbOgrenci; // CevaplarPanel'deki OgrenciComboBoxItem'ı kullanabiliriz
    private JComboBox<MateryallerPanel.DersComboBoxItem> cmbDers;   // MateryallerPanel'deki DersComboBoxItem'ı kullanabiliriz
    private JButton btnEkle;
    private JButton btnGuncelle; // Genellikle bu tür bir eşleşme tablosunda güncelleme yerine silip yeniden ekleme yapılır.
                                 // Ancak örnek olması açısından ekleyelim.
    private JButton btnSil;
    private JButton btnTemizle;

    public OgrenciDerslerPanel() {
        initComponents();
        loadOgrencilerToComboBox();
        loadDerslerToComboBox();
        loadOgrenciDersler();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel pnlOgrenciDersID = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblOgrenciDersID = new JLabel("Kayıt ID:");
        txtOgrenciDersID = new JTextField(10);
        txtOgrenciDersID.setEditable(false);
        pnlOgrenciDersID.add(lblOgrenciDersID);
        pnlOgrenciDersID.add(txtOgrenciDersID);

        JPanel pnlOgrenci = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblOgrenci = new JLabel("Öğrenci:");
        cmbOgrenci = new JComboBox<>();
        cmbOgrenci.setPreferredSize(new Dimension(300, 22));
        pnlOgrenci.add(lblOgrenci);
        pnlOgrenci.add(cmbOgrenci);

        JPanel pnlDers = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblDers = new JLabel("Ders:");
        cmbDers = new JComboBox<>();
        cmbDers.setPreferredSize(new Dimension(300, 22));
        pnlDers.add(lblDers);
        pnlDers.add(cmbDers);

        formPanel.add(pnlOgrenciDersID);
        formPanel.add(pnlOgrenci);
        formPanel.add(pnlDers);

        // Buton paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnEkle = new JButton("Derse Kaydet");
        // btnGuncelle = new JButton("Kaydı Güncelle"); // Bu genellikle yapılmaz
        btnSil = new JButton("Kaydı Sil");
        btnTemizle = new JButton("Formu Temizle");

        buttonPanel.add(btnEkle);
        // buttonPanel.add(btnGuncelle);
        buttonPanel.add(btnSil);
        buttonPanel.add(btnTemizle);

        // Tablo
        tableModel = new DefaultTableModel(new String[]{"Kayıt ID", "Öğrenci Adı Soyadı (ID)", "Ders Adı (ID)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblOgrenciDersler = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblOgrenciDersler);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnEkle.addActionListener(e -> ekleOgrenciDers());
        // btnGuncelle.addActionListener(e -> guncelleOgrenciDers());
        btnSil.addActionListener(e -> silOgrenciDers());
        btnTemizle.addActionListener(e -> temizleForm());

        tblOgrenciDersler.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblOgrenciDersler.getSelectedRow();
                if (selectedRow != -1) {
                    txtOgrenciDersID.setText(tableModel.getValueAt(selectedRow, 0).toString());

                    // OgrenciID'yi alıp ComboBox'ta seç
                    Object ogrenciData = tableModel.getValueAt(selectedRow, 1);
                    if (ogrenciData != null) {
                        String ogrenciString = ogrenciData.toString();
                        try {
                            int ogrenciID = Integer.parseInt(ogrenciString.substring(ogrenciString.lastIndexOf("(ID: ") + 5, ogrenciString.lastIndexOf(")")));
                            for (int i = 0; i < cmbOgrenci.getItemCount(); i++) {
                                CevaplarPanel.OgrenciComboBoxItem item = (CevaplarPanel.OgrenciComboBoxItem) cmbOgrenci.getItemAt(i);
                                if (item.getId() == ogrenciID) {
                                    cmbOgrenci.setSelectedIndex(i);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            System.err.println("Öğrenci ID parse error from table (OgrenciDerslerPanel): " + ex.getMessage());
                            cmbOgrenci.setSelectedIndex(-1);
                        }
                    } else {
                         cmbOgrenci.setSelectedIndex(-1);
                    }

                    // DersID'yi alıp ComboBox'ta seç
                    Object dersData = tableModel.getValueAt(selectedRow, 2);
                    if (dersData != null) {
                        String dersString = dersData.toString();
                         try {
                            int dersID = Integer.parseInt(dersString.substring(dersString.lastIndexOf("(ID: ") + 5, dersString.lastIndexOf(")")));
                            for (int i = 0; i < cmbDers.getItemCount(); i++) {
                                MateryallerPanel.DersComboBoxItem item = (MateryallerPanel.DersComboBoxItem) cmbDers.getItemAt(i);
                                if (item.getId() == dersID) {
                                    cmbDers.setSelectedIndex(i);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            System.err.println("Ders ID parse error from table (OgrenciDerslerPanel): " + ex.getMessage());
                            cmbDers.setSelectedIndex(-1);
                        }
                    } else {
                        cmbDers.setSelectedIndex(-1);
                    }
                }
            }
        });
    }

    private void loadOgrencilerToComboBox() {
        cmbOgrenci.removeAllItems();
        String query = "SELECT OgrenciID, Ad, Soyad FROM Ogrenciler ORDER BY Ad, Soyad";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbOgrenci.addItem(new CevaplarPanel.OgrenciComboBoxItem(rs.getInt("OgrenciID"), rs.getString("Ad"), rs.getString("Soyad")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğrenciler (ÖğrenciDersler için) combobox'a yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDerslerToComboBox() {
        cmbDers.removeAllItems();
        String query = "SELECT DersID, DersAdi FROM Dersler ORDER BY DersAdi";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbDers.addItem(new MateryallerPanel.DersComboBoxItem(rs.getInt("DersID"), rs.getString("DersAdi")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Dersler (ÖğrenciDersler için) combobox'a yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOgrenciDersler() {
        tableModel.setRowCount(0);
        String query = "SELECT OD.OgrenciDersID, OD.OgrenciID, O.Ad AS OgrenciAdi, O.Soyad AS OgrenciSoyadi, OD.DersID, D.DersAdi " +
                       "FROM OgrenciDersler OD " +
                       "INNER JOIN Ogrenciler O ON OD.OgrenciID = O.OgrenciID " +
                       "INNER JOIN Dersler D ON OD.DersID = D.DersID " +
                       "ORDER BY O.Ad, O.Soyad, D.DersAdi";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("OgrenciDersID"));
                row.add(rs.getString("OgrenciAdi") + " " + rs.getString("OgrenciSoyadi") + " (ID: " + rs.getInt("OgrenciID") + ")");
                row.add(rs.getString("DersAdi") + " (ID: " + rs.getInt("DersID") + ")");
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğrenci-Ders kayıtları yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void ekleOgrenciDers() {
        CevaplarPanel.OgrenciComboBoxItem secilenOgrenci = (CevaplarPanel.OgrenciComboBoxItem) cmbOgrenci.getSelectedItem();
        MateryallerPanel.DersComboBoxItem secilenDers = (MateryallerPanel.DersComboBoxItem) cmbDers.getSelectedItem();

        if (secilenOgrenci == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir öğrenci seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (secilenDers == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Öğrencinin bu derse zaten kayıtlı olup olmadığını kontrol et
        if (isKayitExists(secilenOgrenci.getId(), secilenDers.getId())) {
            JOptionPane.showMessageDialog(this, "Bu öğrenci bu derse zaten kayıtlı.", "Kayıt Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO OgrenciDersler (OgrenciID, DersID) VALUES (?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenOgrenci.getId());
            pstmt.setInt(2, secilenDers.getId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Öğrenci derse başarıyla kaydedildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadOgrenciDersler();
                temizleForm();
            }
        } catch (SQLException e) {
            // SQLState "23000" ve Error Code 2627 (veya benzeri) unique constraint ihlali anlamına gelir.
            // Bu genellikle (OgrenciID, DersID) çifti için bir UNIQUE kısıtlaması varsa oluşur.
            if (e.getSQLState().equals("23000")) {
                 JOptionPane.showMessageDialog(this, "Bu öğrenci bu derse zaten kayıtlı olabilir (Veritabanı Kısıtlaması).", "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Öğrenci derse kaydedilirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }

    // OgrenciDersler tablosunda genellikle güncelleme işlemi direkt yapılmaz.
    // Bir kaydı değiştirmek yerine, mevcut kayıt silinip yeni bir kayıt eklenir.
    // Bu yüzden guncelleOgrenciDers() metodu yorum satırı olarak bırakıldı.
    /*
    private void guncelleOgrenciDers() {
        String ogrenciDersIDStr = txtOgrenciDersID.getText().trim();
        if (ogrenciDersIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir kayıt seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CevaplarPanel.OgrenciComboBoxItem secilenOgrenci = (CevaplarPanel.OgrenciComboBoxItem) cmbOgrenci.getSelectedItem();
        MateryallerPanel.DersComboBoxItem secilenDers = (MateryallerPanel.DersComboBoxItem) cmbDers.getSelectedItem();

        if (secilenOgrenci == null || secilenDers == null) {
            JOptionPane.showMessageDialog(this, "Lütfen öğrenci ve ders seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ogrenciDersID = Integer.parseInt(ogrenciDersIDStr);

        // Güncelleme sırasında (OgrenciID, DersID) çiftinin başka bir OgrenciDersID'ye ait olup olmadığını kontrol etmek
        if (isKayitExistsForOtherEntry(secilenOgrenci.getId(), secilenDers.getId(), ogrenciDersID)) {
            JOptionPane.showMessageDialog(this, "Bu öğrenci-ders eşleşmesi zaten başka bir kayıtta mevcut.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String query = "UPDATE OgrenciDersler SET OgrenciID = ?, DersID = ? WHERE OgrenciDersID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenOgrenci.getId());
            pstmt.setInt(2, secilenDers.getId());
            pstmt.setInt(3, ogrenciDersID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Kayıt başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadOgrenciDersler();
                temizleForm();
            } else {
                JOptionPane.showMessageDialog(this, "Kayıt bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) {
                 JOptionPane.showMessageDialog(this, "Bu öğrenci bu derse zaten kayıtlı olabilir (Veritabanı Kısıtlaması).", "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Kayıt güncellenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    */

    private void silOgrenciDers() {
        String ogrenciDersIDStr = txtOgrenciDersID.getText().trim();
        if (ogrenciDersIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir kayıt seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ogrenciDersID = Integer.parseInt(ogrenciDersIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bu öğrenci-ders kaydını silmek istediğinizden emin misiniz?",
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            String query = "DELETE FROM OgrenciDersler WHERE OgrenciDersID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, ogrenciDersID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Kayıt başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadOgrenciDersler();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Kayıt bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Kayıt silinirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Bir öğrencinin belirli bir derse zaten kayıtlı olup olmadığını kontrol eder
    private boolean isKayitExists(int ogrenciID, int dersID) {
        String query = "SELECT COUNT(*) FROM OgrenciDersler WHERE OgrenciID = ? AND DersID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ogrenciID);
            pstmt.setInt(2, dersID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Hata durumunda var gibi davran
        }
        return false;
    }
    
    // Güncelleme sırasında, mevcut kayıt DIŞINDA aynı öğrenci-ders çiftinin olup olmadığını kontrol eder
    private boolean isKayitExistsForOtherEntry(int ogrenciID, int dersID, int currentOgrenciDersID) {
        String query = "SELECT COUNT(*) FROM OgrenciDersler WHERE OgrenciID = ? AND DersID = ? AND OgrenciDersID != ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ogrenciID);
            pstmt.setInt(2, dersID);
            pstmt.setInt(3, currentOgrenciDersID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Hata durumunda var gibi davran
        }
        return false;
    }


    private void temizleForm() {
        txtOgrenciDersID.setText("");
        cmbOgrenci.setSelectedIndex(-1);
        cmbDers.setSelectedIndex(-1);
        tblOgrenciDersler.clearSelection();
    }
}
