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
import java.text.SimpleDateFormat;

public class SorularPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;
    private JTable tblSorular;
    private JTextField txtSoruID;
    private JComboBox<SinavComboBoxItem> cmbSinav; // Sınavları listelemek için
    private JTextArea txtSoruMetni;
    private JTextField txtDogruCevap;
    private JButton btnEkle;
    private JButton btnGuncelle;
    private JButton btnSil;
    private JButton btnTemizle;

    // Sınavları JComboBox'ta göstermek için yardımcı sınıf
    private static class SinavComboBoxItem {
        private int id;
        private String aciklama; // Sınavı tanımlayıcı bir bilgi (Örn: Ders Adı - Tarih)

        public SinavComboBoxItem(int id, String dersAdi, Date tarih) {
            this.id = id;
            // Tarih null olabilir, kontrol et
            String tarihStr = (tarih != null) ? new SimpleDateFormat("yyyy-MM-dd").format(tarih) : "Tarih Yok";
            this.aciklama = dersAdi + " (Sınav Tarihi: " + tarihStr + ")";
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return aciklama + " (Sınav ID: " + id + ")";
        }
    }


    public SorularPanel() {
        initComponents();
        loadSinavlarToComboBox();
        loadSorular();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Form paneli
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JPanel pnlSoruID = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSoruID = new JLabel("Soru ID:");
        txtSoruID = new JTextField(10);
        txtSoruID.setEditable(false);
        pnlSoruID.add(lblSoruID);
        pnlSoruID.add(txtSoruID);

        JPanel pnlSinav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSinav = new JLabel("Sınav:");
        cmbSinav = new JComboBox<>();
        cmbSinav.setPreferredSize(new Dimension(400, 22)); // Combobox boyutunu ayarla
        pnlSinav.add(lblSinav);
        pnlSinav.add(cmbSinav);

        JPanel pnlSoruMetni = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSoruMetni = new JLabel("Soru Metni:");
        txtSoruMetni = new JTextArea(4, 35); // 4 satır, 35 sütun
        JScrollPane soruMetniScrollPane = new JScrollPane(txtSoruMetni);
        pnlSoruMetni.add(lblSoruMetni);
        pnlSoruMetni.add(soruMetniScrollPane);

        JPanel pnlDogruCevap = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblDogruCevap = new JLabel("Doğru Cevap:");
        txtDogruCevap = new JTextField(30);
        pnlDogruCevap.add(lblDogruCevap);
        pnlDogruCevap.add(txtDogruCevap);

        formPanel.add(pnlSoruID);
        formPanel.add(pnlSinav);
        formPanel.add(pnlSoruMetni);
        formPanel.add(pnlDogruCevap);

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
        tableModel = new DefaultTableModel(new String[]{"SoruID", "Sınav (Ders - Tarih)", "Soru Metni", "Doğru Cevap"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblSorular = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblSorular);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        btnEkle.addActionListener(e -> ekleSoru());
        btnGuncelle.addActionListener(e -> guncelleSoru());
        btnSil.addActionListener(e -> silSoru());
        btnTemizle.addActionListener(e -> temizleForm());

        tblSorular.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblSorular.getSelectedRow();
                if (selectedRow != -1) {
                    txtSoruID.setText(tableModel.getValueAt(selectedRow, 0).toString());

                    // SınavID'yi alıp ComboBox'ta seç
                    // Tablodaki "Sınav (Ders - Tarih)" sütunundan SinavID'yi parse etmemiz gerekiyor.
                    // Bu biraz karmaşık olabilir, SinavComboBoxItem'ın toString() formatına göre parse etmeliyiz.
                    // Şimdilik, SinavID'yi doğrudan tablodan alamıyoruz, bu yüzden ComboBox'ı manuel seçtirmeyeceğiz.
                    // Daha iyi bir çözüm, tabloya gizli bir SinavID sütunu eklemek veya ComboBox'taki item'ı bulmak için daha sağlam bir yol izlemektir.
                    // Geçici olarak, ComboBox seçimi mouseClicked'da güncellenmeyecek.
                    // VEYA: loadSorular'da SinavID'yi de çekip, cmbSinav'da o ID'ye sahip item'ı bulabiliriz.

                    Object sinavData = tableModel.getValueAt(selectedRow, 1); // "Ders Adı (Sınav Tarihi: YYYY-AA-GG) (Sınav ID: X)"
                     if (sinavData != null) {
                        String sinavString = sinavData.toString();
                        try {
                            // (Sınav ID: X) kısmından ID'yi al
                            int sinavID = Integer.parseInt(sinavString.substring(sinavString.lastIndexOf("(Sınav ID: ") + 11, sinavString.lastIndexOf(")")));
                            for (int i = 0; i < cmbSinav.getItemCount(); i++) {
                                SinavComboBoxItem item = cmbSinav.getItemAt(i);
                                if (item.getId() == sinavID) {
                                    cmbSinav.setSelectedIndex(i);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            System.err.println("Sınav ID parse error from table (SorularPanel): " + ex.getMessage() + " for string: " + sinavString);
                            cmbSinav.setSelectedIndex(-1);
                        }
                    } else {
                        cmbSinav.setSelectedIndex(-1);
                    }


                    txtSoruMetni.setText(tableModel.getValueAt(selectedRow, 2) != null ? tableModel.getValueAt(selectedRow, 2).toString() : "");
                    txtDogruCevap.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : "");
                }
            }
        });
    }

    private void loadSinavlarToComboBox() {
        cmbSinav.removeAllItems();
        // Sınavları, ilişkili ders adıyla birlikte alıyoruz
        String query = "SELECT S.SinavID, D.DersAdi, S.Tarih " +
                       "FROM Sinavlar S " +
                       "INNER JOIN Dersler D ON S.DersID = D.DersID " +
                       "ORDER BY D.DersAdi, S.Tarih DESC";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cmbSinav.addItem(new SinavComboBoxItem(rs.getInt("SinavID"), rs.getString("DersAdi"), rs.getDate("Tarih")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Sınavlar (Sorular için) combobox'a yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSorular() {
        tableModel.setRowCount(0);
        // Soruları, ilişkili sınavın ders adı ve tarihiyle birlikte alıyoruz
        String query = "SELECT SR.SoruID, SR.SinavID, D.DersAdi, SN.Tarih AS SinavTarihi, SR.SoruMetni, SR.DogruCevap " +
                       "FROM Sorular SR " +
                       "INNER JOIN Sinavlar SN ON SR.SinavID = SN.SinavID " +
                       "INNER JOIN Dersler D ON SN.DersID = D.DersID " +
                       "ORDER BY SR.SinavID, SR.SoruID";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("SoruID"));
                // Sınav bilgisini daha açıklayıcı hale getir
                Date sinavTarihiSql = rs.getDate("SinavTarihi");
                String sinavTarihiStr = (sinavTarihiSql != null) ? new SimpleDateFormat("yyyy-MM-dd").format(sinavTarihiSql) : "Tarih Yok";
                row.add(rs.getString("DersAdi") + " (Sınav Tarihi: " + sinavTarihiStr + ") (Sınav ID: " + rs.getInt("SinavID") + ")");
                row.add(rs.getString("SoruMetni"));
                row.add(rs.getString("DogruCevap"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Sorular yüklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void ekleSoru() {
        SinavComboBoxItem secilenSinav = (SinavComboBoxItem) cmbSinav.getSelectedItem();
        String soruMetni = txtSoruMetni.getText().trim();
        String dogruCevap = txtDogruCevap.getText().trim();

        if (secilenSinav == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir sınav seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (soruMetni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Soru metni boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (dogruCevap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Doğru cevap boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO Sorular (SinavID, SoruMetni, DogruCevap) VALUES (?, ?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenSinav.getId());
            pstmt.setString(2, soruMetni);
            pstmt.setString(3, dogruCevap);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Soru başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadSorular();
                temizleForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Soru eklenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void guncelleSoru() {
        String soruIDStr = txtSoruID.getText().trim();
        if (soruIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir soru seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SinavComboBoxItem secilenSinav = (SinavComboBoxItem) cmbSinav.getSelectedItem();
        String soruMetni = txtSoruMetni.getText().trim();
        String dogruCevap = txtDogruCevap.getText().trim();

        if (secilenSinav == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir sınav seçin.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (soruMetni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Soru metni boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (dogruCevap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Doğru cevap boş bırakılamaz.", "Giriş Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int soruID = Integer.parseInt(soruIDStr);
        String query = "UPDATE Sorular SET SinavID = ?, SoruMetni = ?, DogruCevap = ? WHERE SoruID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenSinav.getId());
            pstmt.setString(2, soruMetni);
            pstmt.setString(3, dogruCevap);
            pstmt.setInt(4, soruID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Soru başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadSorular();
                temizleForm();
            } else {
                JOptionPane.showMessageDialog(this, "Soru bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Soru güncellenirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void silSoru() {
        String soruIDStr = txtSoruID.getText().trim();
        if (soruIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir soru seçin.", "Seçim Hatası", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int soruID = Integer.parseInt(soruIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Bu soruyu silmek istediğinizden emin misiniz?\nBu soruyla ilişkili cevaplar varsa onlar da silinecektir (veya silme engellenecektir)!",
                "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            // Sorunun Cevaplar tablosunda kullanılıp kullanılmadığını kontrol et
            if (isSoruReferencedInCevaplar(soruID)) {
                 JOptionPane.showMessageDialog(this,
                        "Bu soru 'Cevaplar' tablosunda kullanıldığı için silinemez.\nLütfen önce bu soruya ait tüm cevapları silin.",
                        "Silme Engellendi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "DELETE FROM Sorular WHERE SoruID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, soruID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Soru başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadSorular();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Soru bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Soru silinirken hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private boolean isSoruReferencedInCevaplar(int soruID) {
        String query = "SELECT COUNT(*) FROM Cevaplar WHERE SoruID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, soruID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Soru Cevaplar tablosunda referans alınmış
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hata durumunda, güvenli tarafta kalıp referans var gibi davran
            return true;
        }
        return false; // Soru referans alınmamış
    }

    private void temizleForm() {
        txtSoruID.setText("");
        cmbSinav.setSelectedIndex(-1);
        txtSoruMetni.setText("");
        txtDogruCevap.setText("");
        tblSorular.clearSelection();
    }
}
