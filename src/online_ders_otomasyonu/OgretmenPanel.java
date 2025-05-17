package online_ders_otomasyonu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class OgretmenPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;

    public OgretmenPanel() {
        initComponents();
        loadOgretmenler();
        loadKullanicilarToComboBox(); // Kullanıcıları combobox'a yükle
    }

    private void loadOgretmenler() {
        String[] columnNames = {"OgretmenID", "KullaniciID", "Ad", "Soyad", "Brans", "KullaniciAdi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblOgretmenler.setModel(tableModel);

        // JOIN ile KullaniciAdi'nı da alıyoruz
        String query = "SELECT O.OgretmenID, O.KullaniciID, O.Ad, O.Soyad, O.Brans, K.KullaniciAdi " +
                       "FROM Ogretmenler O " +
                       "INNER JOIN Kullanicilar K ON O.KullaniciID = K.KullaniciID";

        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("OgretmenID"));
                row.add(rs.getInt("KullaniciID"));
                row.add(rs.getString("Ad"));
                row.add(rs.getString("Soyad"));
                row.add(rs.getString("Brans"));
                row.add(rs.getString("KullaniciAdi")); // Kullanıcı adını ekle
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğretmenleri yüklerken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadKullanicilarToComboBox() {
        cmbKullanici.removeAllItems();
        // Sadece öğretmen rolündeki kullanıcıları veya uygun olanları listelemek daha mantıklı olabilir.
        // Bu örnekte RolID=2 olanları (varsayımsal olarak öğretmen rolü) alıyoruz.
        // Eğer tüm kullanıcıları listelemek isterseniz WHERE koşulunu kaldırın.
        String query = "SELECT KullaniciID, KullaniciAdi FROM Kullanicilar WHERE RolID = (SELECT RolID FROM Roller WHERE RolAdi = 'Öğretmen')";
        // Alternatif: Tüm kullanıcıları listelemek için
        // String query = "SELECT KullaniciID, KullaniciAdi FROM Kullanicilar";

        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            boolean hasItems = false;
            while (rs.next()) {
                cmbKullanici.addItem(new KullaniciComboBoxItem(rs.getInt("KullaniciID"), rs.getString("KullaniciAdi")));
                hasItems = true;
            }
            if (!hasItems) {
                 // Eğer öğretmen rolünde kullanıcı yoksa veya hiç kullanıcı yoksa bilgi ver
                 // Veya genel bir "Kullanıcı Seçin" item'ı eklenebilir.
                 // cmbKullanici.addItem(new KullaniciComboBoxItem(0, "Uygun Kullanıcı Yok"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kullanıcıları (Öğretmen) combobox'a yüklerken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ComboBox için yardımcı sınıf
    public static class KullaniciComboBoxItem {
        private int id;
        private String ad;

        public KullaniciComboBoxItem(int id, String ad) {
            this.id = id;
            this.ad = ad;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return ad + " (ID: " + id + ")"; // Combobox'ta görünecek metin
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblOgretmenler = new javax.swing.JTable();
        lblAd = new javax.swing.JLabel();
        txtAd = new javax.swing.JTextField();
        lblSoyad = new javax.swing.JLabel();
        txtSoyad = new javax.swing.JTextField();
        lblBrans = new javax.swing.JLabel();
        txtBrans = new javax.swing.JTextField();
        lblKullanici = new javax.swing.JLabel();
        cmbKullanici = new javax.swing.JComboBox<>();
        btnEkle = new javax.swing.JButton();
        btnGuncelle = new javax.swing.JButton();
        btnSil = new javax.swing.JButton();
        btnTemizle = new javax.swing.JButton();
        lblOgretmenID = new javax.swing.JLabel();
        txtOgretmenID = new javax.swing.JTextField();

        tblOgretmenler.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"OgretmenID", "KullaniciID", "Ad", "Soyad", "Brans"}
        ));
        tblOgretmenler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOgretmenlerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblOgretmenler);

        lblAd.setText("Ad:");
        lblSoyad.setText("Soyad:");
        lblBrans.setText("Branş:");
        lblKullanici.setText("Kullanıcı:");

        btnEkle.setText("Ekle");
        btnEkle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEkleActionPerformed(evt);
            }
        });

        btnGuncelle.setText("Güncelle");
        btnGuncelle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuncelleActionPerformed(evt);
            }
        });

        btnSil.setText("Sil");
        btnSil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSilActionPerformed(evt);
            }
        });

        btnTemizle.setText("Temizle");
        btnTemizle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTemizleActionPerformed(evt);
            }
        });

        lblOgretmenID.setText("Öğretmen ID:");
        txtOgretmenID.setEditable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAd)
                            .addComponent(lblSoyad)
                            .addComponent(lblBrans)
                            .addComponent(lblKullanici)
                            .addComponent(lblOgretmenID))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAd)
                            .addComponent(txtSoyad)
                            .addComponent(txtBrans)
                            .addComponent(cmbKullanici, 0, 200, Short.MAX_VALUE)
                            .addComponent(txtOgretmenID))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEkle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGuncelle, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnSil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTemizle, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOgretmenID)
                    .addComponent(txtOgretmenID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKullanici)
                    .addComponent(cmbKullanici, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEkle)
                    .addComponent(btnSil))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAd)
                    .addComponent(txtAd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuncelle)
                    .addComponent(btnTemizle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoyad)
                    .addComponent(txtSoyad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBrans)
                    .addComponent(txtBrans, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE) // Düzeltildi: 235 -> 199
                .addContainerGap())
        );
    }// </editor-fold>

    private void btnEkleActionPerformed(java.awt.event.ActionEvent evt) {
        KullaniciComboBoxItem secilenKullanici = (KullaniciComboBoxItem) cmbKullanici.getSelectedItem();
        String ad = txtAd.getText().trim();
        String soyad = txtSoyad.getText().trim();
        String brans = txtBrans.getText().trim();

        if (secilenKullanici == null || ad.isEmpty() || soyad.isEmpty() || brans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Aynı KullaniciID ile başka bir öğretmen var mı kontrolü
        if (isKullaniciAlreadyOgretmen(secilenKullanici.getId(), 0)) { // 0, yeni kayıt olduğu için OgretmenID'yi dışarıda bırakır
            JOptionPane.showMessageDialog(this, "Seçilen kullanıcı zaten bir öğretmen olarak kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO Ogretmenler (KullaniciID, Ad, Soyad, Brans) VALUES (?, ?, ?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenKullanici.getId());
            pstmt.setString(2, ad);
            pstmt.setString(3, soyad);
            pstmt.setString(4, brans);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Öğretmen başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadOgretmenler();
                temizleForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğretmen eklenirken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
             // SQLState "23000" ve Error Code 2627 (veya benzeri) unique constraint ihlali anlamına gelir.
            if (e.getSQLState().equals("23000")) {
                 JOptionPane.showMessageDialog(this, "Bu KullanıcıID zaten bir öğretmene atanmış veya başka bir benzersizlik kısıtlaması ihlal edildi.", "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    
    private boolean isKullaniciAlreadyOgretmen(int kullaniciID, int currentOgretmenID) {
        // currentOgretmenID, güncelleme sırasında mevcut öğretmenin kendisini kontrol etmemek için kullanılır.
        // Yeni ekleme sırasında currentOgretmenID = 0 veya < 0 olabilir.
        String query = "SELECT COUNT(*) FROM Ogretmenler WHERE KullaniciID = ? AND OgretmenID != ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, kullaniciID);
            pstmt.setInt(2, currentOgretmenID > 0 ? currentOgretmenID : -1); // Yeni kayıtta OgretmenID kontrol edilmesin
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Bu KullaniciID başka bir öğretmene atanmış
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Hata durumunda var gibi davranmak daha güvenli olabilir
            return true;
        }
        return false;
    }


    private void btnGuncelleActionPerformed(java.awt.event.ActionEvent evt) {
        String ogretmenIDStr = txtOgretmenID.getText().trim();
         if (ogretmenIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir öğretmen seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ogretmenID = Integer.parseInt(ogretmenIDStr);
        KullaniciComboBoxItem secilenKullanici = (KullaniciComboBoxItem) cmbKullanici.getSelectedItem();
        String ad = txtAd.getText().trim();
        String soyad = txtSoyad.getText().trim();
        String brans = txtBrans.getText().trim();

        if (secilenKullanici == null || ad.isEmpty() || soyad.isEmpty() || brans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (isKullaniciAlreadyOgretmen(secilenKullanici.getId(), ogretmenID)) {
            JOptionPane.showMessageDialog(this, "Seçilen kullanıcı zaten başka bir öğretmen olarak kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "UPDATE Ogretmenler SET KullaniciID = ?, Ad = ?, Soyad = ?, Brans = ? WHERE OgretmenID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenKullanici.getId());
            pstmt.setString(2, ad);
            pstmt.setString(3, soyad);
            pstmt.setString(4, brans);
            pstmt.setInt(5, ogretmenID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Öğretmen başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadOgretmenler();
                temizleForm();
            } else {
                 JOptionPane.showMessageDialog(this, "Öğretmen bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğretmen güncellenirken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
             if (e.getSQLState().equals("23000")) {
                 JOptionPane.showMessageDialog(this, "Bu KullanıcıID zaten bir öğretmene atanmış veya başka bir benzersizlik kısıtlaması ihlal edildi.", "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }

    private void btnSilActionPerformed(java.awt.event.ActionEvent evt) {
        String ogretmenIDStr = txtOgretmenID.getText().trim();
        if (ogretmenIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir öğretmen seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ogretmenID = Integer.parseInt(ogretmenIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this, "Bu öğretmeni silmek istediğinizden emin misiniz?\n(İlişkili dersler varsa bu işlem sorun yaratabilir!)", "Silme Onayı", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            // Öğretmenin dersleri var mı kontrol et
            if (isOgretmenReferencedInDersler(ogretmenID)) {
                JOptionPane.showMessageDialog(this, "Bu öğretmen 'Dersler' tablosunda kayıtlı derslere sahip olduğu için silinemez.\nLütfen önce bu öğretmene ait dersleri silin veya başka bir öğretmene atayın.", "Silme Hatası", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String query = "DELETE FROM Ogretmenler WHERE OgretmenID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, ogretmenID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Öğretmen başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadOgretmenler();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Öğretmen bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                 JOptionPane.showMessageDialog(this, "Öğretmen silinirken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                 e.printStackTrace();
            }
        }
    }
    
    private boolean isOgretmenReferencedInDersler(int ogretmenID) {
        String query = "SELECT COUNT(*) FROM Dersler WHERE OgretmenID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ogretmenID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Dersler tablosunda referans var
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Hata durumunda referans var gibi davran
        }
        return false;
    }


    private void tblOgretmenlerMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = tblOgretmenler.getSelectedRow();
        if (selectedRow != -1) {
            txtOgretmenID.setText(tableModel.getValueAt(selectedRow, 0).toString());
            int kullaniciID = (int) tableModel.getValueAt(selectedRow, 1);
            txtAd.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtSoyad.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtBrans.setText(tableModel.getValueAt(selectedRow, 4).toString());

            for (int i = 0; i < cmbKullanici.getItemCount(); i++) {
                KullaniciComboBoxItem item = (KullaniciComboBoxItem) cmbKullanici.getItemAt(i);
                if (item.getId() == kullaniciID) {
                    cmbKullanici.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void btnTemizleActionPerformed(java.awt.event.ActionEvent evt) {
        temizleForm();
    }
    private void temizleForm(){
        txtOgretmenID.setText("");
        txtAd.setText("");
        txtSoyad.setText("");
        txtBrans.setText("");
        cmbKullanici.setSelectedIndex(-1);
        tblOgretmenler.clearSelection();
        loadKullanicilarToComboBox(); // Kullanıcıları tekrar yükle (yeni kullanıcı eklenmiş olabilir)
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnEkle;
    private javax.swing.JButton btnGuncelle;
    private javax.swing.JButton btnSil;
    private javax.swing.JButton btnTemizle;
    private javax.swing.JComboBox<KullaniciComboBoxItem> cmbKullanici;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAd;
    private javax.swing.JLabel lblBrans;
    private javax.swing.JLabel lblKullanici;
    private javax.swing.JLabel lblOgretmenID;
    private javax.swing.JLabel lblSoyad;
    private javax.swing.JTable tblOgretmenler;
    private javax.swing.JTextField txtAd;
    private javax.swing.JTextField txtBrans;
    private javax.swing.JTextField txtOgretmenID;
    private javax.swing.JTextField txtSoyad;
    // End of variables declaration
}