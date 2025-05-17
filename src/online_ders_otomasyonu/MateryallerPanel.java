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

public class MateryallerPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private JTable tblMateryaller;
    private JTextField txtMateryalID;
    public JComboBox<DersComboBoxItem> cmbDers; // Dersleri listelemek için
    private JTextField txtBaslik;
    private JTextArea txtAciklama; // Açıklama için JTextArea daha uygun olabilir
    private JTextField txtDosyaYolu;
    private JButton btnEkle;
    private JButton btnGuncelle;
    private JButton btnSil;
    private JButton btnTemizle;

    // Dersleri JComboBox'ta göstermek için yardımcı sınıf
    public static class DersComboBoxItem {
        private int id;
        private String ad;

        public DersComboBoxItem(int id, String ad) {
            this.id = id;
            this.ad = ad;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return ad + " (ID: " + id + ")"; // ComboBox'ta görünecek metin
        }
    }

    public MateryallerPanel() {
        initComponents();
        loadDerslerToComboBox();
        loadMateryaller();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS)); // Dikey sıralama için

        JPanel pnlMateryalID = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblMateryalID = new JLabel("Materyal ID:");
        txtMateryalID = new JTextField(10);
        txtMateryalID.setEditable(false);
        pnlMateryalID.add(lblMateryalID);
        pnlMateryalID.add(txtMateryalID);

        JPanel pnlDers = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblDers = new JLabel("Ders:");
        cmbDers = new JComboBox<>();
        cmbDers.setPreferredSize(new Dimension(300, 22));
        pnlDers.add(lblDers);
        pnlDers.add(cmbDers);

        JPanel pnlBaslik = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblBaslik = new JLabel("Başlık:");
        txtBaslik = new JTextField(30);
        pnlBaslik.add(lblBaslik);
        pnlBaslik.add(txtBaslik);

        JPanel pnlAciklama = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblAciklama = new JLabel("Açıklama:");
        txtAciklama = new JTextArea(3, 30); // 3 satır, 30 sütun
        JScrollPane aciklamaScrollPane = new JScrollPane(txtAciklama);
        pnlAciklama.add(lblAciklama);
        pnlAciklama.add(aciklamaScrollPane);

        JPanel pnlDosyaYolu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblDosyaYolu = new JLabel("Dosya Yolu:");
        txtDosyaYolu = new JTextField(30);
        pnlDosyaYolu.add(lblDosyaYolu);
        pnlDosyaYolu.add(txtDosyaYolu);

        formPanel.add(pnlMateryalID);
        formPanel.add(pnlDers);
        formPanel.add(pnlBaslik);
        formPanel.add(pnlAciklama);
        formPanel.add(pnlDosyaYolu);


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
        tableModel = new DefaultTableModel(new String[]{"MateryalID", "Ders Adı (ID)", "Başlık", "Açıklama", "Dosya Yolu"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMateryaller = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblMateryaller);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnEkle.addActionListener(e -> ekleMateryal());
        btnGuncelle.addActionListener(e -> guncelleMateryal());
        btnSil.addActionListener(e -> silMateryal());
        btnTemizle.addActionListener(e -> temizleForm());

        tblMateryaller.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblMateryaller.getSelectedRow();
                if (selectedRow != -1) {
                    txtMateryalID.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    // DersID'yi alıp ComboBox'ta seç
                    Object dersData = tableModel.getValueAt(selectedRow, 1); // "Ders Adı (ID: X)" formatında
                    if (dersData != null) {
                        String dersString = dersData.toString();
                        try {
                            // ID'yi string'den parse etmeye çalışalım
                            int dersID = Integer.parseInt(dersString.substring(dersString.lastIndexOf("(ID: ") + 5, dersString.lastIndexOf(")")));
                            for (int i = 0; i < cmbDers.getItemCount(); i++) {
                                DersComboBoxItem item = cmbDers.getItemAt(i);
                                if (item.getId() == dersID) {
                                    cmbDers.setSelectedIndex(i);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            System.err.println("Ders ID parse error: " + ex.getMessage());
                            cmbDers.setSelectedIndex(-1);
                        }
                    } else {
                         cmbDers.setSelectedIndex(-1);
                    }


                    txtBaslik.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
                    txtAciklama.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");
                    txtDosyaYolu.setText(tableModel.getValueAt(selectedRow, 4) != null ? tableModel.getValueAt(selectedRow, 4).toString() : "");
                }
            }
        });
    }

    private void loadDerslerToComboBox() {
        cmbDers.removeAllItems();
        String query = "SELECT DersID, DersAdi FROM Dersler ORDER BY DersAdi";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbDers.addItem(new DersComboBoxItem(rs.getInt("DersID"), rs.getString("DersAdi")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Dersler combobox'a yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMateryaller() {
        tableModel.setRowCount(0);
        String query = "SELECT M.MateryalID, M.DersID, D.DersAdi, M.Baslik, M.Aciklama, M.DosyaYolu " +
                       "FROM Materyaller M " +
                       "INNER JOIN Dersler D ON M.DersID = D.DersID " +
                       "ORDER BY M.Baslik";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("MateryalID"));
                row.add(rs.getString("DersAdi") + " (ID: " + rs.getInt("DersID") + ")"); // Tabloda ders adını ve ID'sini göster
                row.add(rs.getString("Baslik"));
                row.add(rs.getString("Aciklama"));
                row.add(rs.getString("DosyaYolu"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Materyaller yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void ekleMateryal() {
        DersComboBoxItem secilenDers = (DersComboBoxItem) cmbDers.getSelectedItem();
        String baslik = txtBaslik.getText().trim();
        String aciklama = txtAciklama.getText().trim();
        String dosyaYolu = txtDosyaYolu.getText().trim();

        if (secilenDers == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (baslik.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Başlık boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Dosya yolu zorunlu olmayabilir, duruma göre kontrol eklenebilir.

        String query = "INSERT INTO Materyaller (DersID, Baslik, Aciklama, DosyaYolu) VALUES (?, ?, ?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenDers.getId());
            pstmt.setString(2, baslik);
            pstmt.setString(3, aciklama);
            pstmt.setString(4, dosyaYolu);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Materyal başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadMateryaller();
                temizleForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Materyal eklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void guncelleMateryal() {
        String materyalIDStr = txtMateryalID.getText().trim();
        if (materyalIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir materyal seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DersComboBoxItem secilenDers = (DersComboBoxItem) cmbDers.getSelectedItem();
        String baslik = txtBaslik.getText().trim();
        String aciklama = txtAciklama.getText().trim();
        String dosyaYolu = txtDosyaYolu.getText().trim();

        if (secilenDers == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (baslik.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Başlık boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int materyalID = Integer.parseInt(materyalIDStr);
        String query = "UPDATE Materyaller SET DersID = ?, Baslik = ?, Aciklama = ?, DosyaYolu = ? WHERE MateryalID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenDers.getId());
            pstmt.setString(2, baslik);
            pstmt.setString(3, aciklama);
            pstmt.setString(4, dosyaYolu);
            pstmt.setInt(5, materyalID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Materyal başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadMateryaller();
                temizleForm();
            } else {
                JOptionPane.showMessageDialog(this, "Materyal bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Materyal güncellenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void silMateryal() {
        String materyalIDStr = txtMateryalID.getText().trim();
        if (materyalIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir materyal seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int materyalID = Integer.parseInt(materyalIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bu materyali silmek istediğinizden emin misiniz?",
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            // Materyallerin başka tablolarla doğrudan bir foreign key ilişkisi (child table) yok gibi görünüyor şemada.
            // Eğer olsaydı, isMateryalReferenced() gibi bir kontrol gerekirdi.

            String query = "DELETE FROM Materyaller WHERE MateryalID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, materyalID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Materyal başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadMateryaller();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Materyal bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Materyal silinirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void temizleForm() {
        txtMateryalID.setText("");
        cmbDers.setSelectedIndex(-1);
        txtBaslik.setText("");
        txtAciklama.setText("");
        txtDosyaYolu.setText("");
        tblMateryaller.clearSelection();
    }
}
