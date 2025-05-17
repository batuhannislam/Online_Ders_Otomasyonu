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
import java.text.ParseException;
import java.text.SimpleDateFormat;
// Kullandığımız DersComboBoxItem'ı import etmeye gerek yok çünkü aynı pakette.
// Eğer farklı bir pakette olsaydı: import online_ders_otomasyonu.MateryallerPanel.DersComboBoxItem;


public class SinavlarPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private JTable tblSinavlar;
    private JTextField txtSinavID;
    private JComboBox<MateryallerPanel.DersComboBoxItem> cmbDers; // Dersleri listelemek için (MateryallerPanel'deki sınıfı kullanabiliriz)
    private JTextField txtTarih; // Tarih için JTextField, format: YYYY-MM-DD
    private JButton btnEkle;
    private JButton btnGuncelle;
    private JButton btnSil;
    private JButton btnTemizle;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public SinavlarPanel() {
        initComponents();
        loadDerslerToComboBox();
        loadSinavlar();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel pnlSinavID = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSinavID = new JLabel("Sınav ID:");
        txtSinavID = new JTextField(10);
        txtSinavID.setEditable(false);
        pnlSinavID.add(lblSinavID);
        pnlSinavID.add(txtSinavID);

        JPanel pnlDers = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblDers = new JLabel("Ders:");
        cmbDers = new JComboBox<>();
        cmbDers.setPreferredSize(new Dimension(300, 22));
        pnlDers.add(lblDers);
        pnlDers.add(cmbDers);

        JPanel pnlTarih = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTarih = new JLabel("Tarih (YYYY-AA-GG):");
        txtTarih = new JTextField(15);
        pnlTarih.add(lblTarih);
        pnlTarih.add(txtTarih);

        formPanel.add(pnlSinavID);
        formPanel.add(pnlDers);
        formPanel.add(pnlTarih);

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
        tableModel = new DefaultTableModel(new String[]{"SınavID", "Ders Adı (ID)", "Tarih"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSinavlar = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblSinavlar);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnEkle.addActionListener(e -> ekleSinav());
        btnGuncelle.addActionListener(e -> guncelleSinav());
        btnSil.addActionListener(e -> silSinav());
        btnTemizle.addActionListener(e -> temizleForm());

        tblSinavlar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblSinavlar.getSelectedRow();
                if (selectedRow != -1) {
                    txtSinavID.setText(tableModel.getValueAt(selectedRow, 0).toString());

                    Object dersData = tableModel.getValueAt(selectedRow, 1);
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
                            System.err.println("Ders ID parse error from table: " + ex.getMessage());
                            cmbDers.setSelectedIndex(-1);
                        }
                    } else {
                        cmbDers.setSelectedIndex(-1);
                    }
                    txtTarih.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
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
                // MateryallerPanel'deki DersComboBoxItem sınıfını kullanıyoruz
                cmbDers.addItem(new MateryallerPanel.DersComboBoxItem(rs.getInt("DersID"), rs.getString("DersAdi")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Dersler (Sınavlar için) combobox'a yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSinavlar() {
        tableModel.setRowCount(0);
        String query = "SELECT S.SinavID, S.DersID, D.DersAdi, S.Tarih " +
                       "FROM Sinavlar S " +
                       "INNER JOIN Dersler D ON S.DersID = D.DersID " +
                       "ORDER BY S.Tarih DESC, D.DersAdi";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("SinavID"));
                row.add(rs.getString("DersAdi") + " (ID: " + rs.getInt("DersID") + ")");
                // Tarihi Date objesinden String'e formatla
                Date tarihSql = rs.getDate("Tarih");
                row.add(tarihSql != null ? dateFormat.format(tarihSql) : "");
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Sınavlar yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            java.util.Date utilDate = dateFormat.parse(dateStr.trim());
            return new Date(utilDate.getTime()); // java.sql.Date'e çevir
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Geçersiz tarih formatı. Lütfen YYYY-AA-GG formatını kullanın.", "Tarih Hatası", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

    private void ekleSinav() {
        MateryallerPanel.DersComboBoxItem secilenDers = (MateryallerPanel.DersComboBoxItem) cmbDers.getSelectedItem();
        Date tarih = parseDate(txtTarih.getText());

        if (secilenDers == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tarih == null && !txtTarih.getText().trim().isEmpty()) { // Eğer parseDate null döndürdüyse ve alan boş değilse, format hatasıdır.
             // parseDate zaten mesajı gösterdi, burada ek bir şey yapmaya gerek yok.
            return;
        }
         if (tarih == null && txtTarih.getText().trim().isEmpty()){
             JOptionPane.showMessageDialog(this, "Tarih boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }


        String query = "INSERT INTO Sinavlar (DersID, Tarih) VALUES (?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenDers.getId());
            pstmt.setDate(2, tarih);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Sınav başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadSinavlar();
                temizleForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Sınav eklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void guncelleSinav() {
        String sinavIDStr = txtSinavID.getText().trim();
        if (sinavIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir sınav seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MateryallerPanel.DersComboBoxItem secilenDers = (MateryallerPanel.DersComboBoxItem) cmbDers.getSelectedItem();
        Date tarih = parseDate(txtTarih.getText());

        if (secilenDers == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tarih == null && !txtTarih.getText().trim().isEmpty()) {
            return;
        }
        if (tarih == null && txtTarih.getText().trim().isEmpty()){
             JOptionPane.showMessageDialog(this, "Tarih boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sinavID = Integer.parseInt(sinavIDStr);
        String query = "UPDATE Sinavlar SET DersID = ?, Tarih = ? WHERE SinavID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenDers.getId());
            pstmt.setDate(2, tarih);
            pstmt.setInt(3, sinavID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Sınav başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadSinavlar();
                temizleForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sınav bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Sınav güncellenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void silSinav() {
        String sinavIDStr = txtSinavID.getText().trim();
        if (sinavIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir sınav seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sinavID = Integer.parseInt(sinavIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bu sınavı silmek istediğinizden emin misiniz?\nBu sınavla ilişkili sorular varsa onlar da silinecektir (veya silme engellenecektir)!",
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            // Sınavın Sorular tablosunda kullanılıp kullanılmadığını kontrol et
            if (isSinavReferencedInSorular(sinavID)) {
                 JOptionPane.showMessageDialog(this,
                        "Bu sınav 'Sorular' tablosunda kullanıldığı için silinemez.\nLütfen önce bu sınava ait tüm soruları silin.",
                        "Silme Engellendi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "DELETE FROM Sinavlar WHERE SinavID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, sinavID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Sınav başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadSinavlar();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Sınav bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Sınav silinirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private boolean isSinavReferencedInSorular(int sinavID) {
        String query = "SELECT COUNT(*) FROM Sorular WHERE SinavID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, sinavID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Sınav Sorular tablosunda referans alınmış
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hata durumunda, güvenli tarafta kalıp referans var gibi davran
            return true;
        }
        return false; // Sınav referans alınmamış
    }


    private void temizleForm() {
        txtSinavID.setText("");
        cmbDers.setSelectedIndex(-1);
        txtTarih.setText("");
        tblSinavlar.clearSelection();
    }
}
