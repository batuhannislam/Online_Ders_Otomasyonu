package online_ders_otomasyonu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class OgrenciPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;

    public OgrenciPanel() {
        initComponents();
        loadOgrenciler();
        loadKullanicilarToComboBox(); // Öğrenci rolündeki kullanıcıları combobox'a yükle
    }

    private void loadOgrenciler() {
        String[] columnNames = {"OgrenciID", "KullaniciID", "Ad", "Soyad", "Email", "KullaniciAdi"};
        tableModel = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblOgrenciler.setModel(tableModel);

        String query = "SELECT Ogr.OgrenciID, Ogr.KullaniciID, Ogr.Ad, Ogr.Soyad, Ogr.Email, K.KullaniciAdi " +
                       "FROM Ogrenciler Ogr " +
                       "INNER JOIN Kullanicilar K ON Ogr.KullaniciID = K.KullaniciID";

        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("OgrenciID"));
                row.add(rs.getInt("KullaniciID"));
                row.add(rs.getString("Ad"));
                row.add(rs.getString("Soyad"));
                row.add(rs.getString("Email"));
                row.add(rs.getString("KullaniciAdi"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğrencileri yüklerken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadKullanicilarToComboBox() {
        cmbKullanici.removeAllItems();
        // Sadece öğrenci rolündeki kullanıcıları listelemek daha mantıklı.
        // RolID=3 (varsayımsal öğrenci rolü)
        String query = "SELECT KullaniciID, KullaniciAdi FROM Kullanicilar WHERE RolID = (SELECT RolID FROM Roller WHERE RolAdi = 'Öğrenci')";
        // Alternatif: Tüm kullanıcıları listelemek için
        // String query = "SELECT KullaniciID, KullaniciAdi FROM Kullanicilar";

        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             boolean hasItems = false;
            while (rs.next()) {
                cmbKullanici.addItem(new OgretmenPanel.KullaniciComboBoxItem(rs.getInt("KullaniciID"), rs.getString("KullaniciAdi"))); // Önceki paneldeki sınıfı kullanabiliriz
                hasItems = true;
            }
            if (!hasItems) {
                // cmbKullanici.addItem(new OgretmenPanel.KullaniciComboBoxItem(0, "Uygun Kullanıcı Yok"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kullanıcıları (Öğrenci) combobox'a yüklerken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblOgrenciler = new javax.swing.JTable();
        lblAd = new javax.swing.JLabel();
        txtAd = new javax.swing.JTextField();
        lblSoyad = new javax.swing.JLabel();
        txtSoyad = new javax.swing.JTextField();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        lblKullanici = new javax.swing.JLabel();
        cmbKullanici = new javax.swing.JComboBox<>();
        btnEkle = new javax.swing.JButton();
        btnGuncelle = new javax.swing.JButton();
        btnSil = new javax.swing.JButton();
        btnTemizle = new javax.swing.JButton();
        lblOgrenciID = new javax.swing.JLabel();
        txtOgrenciID = new javax.swing.JTextField();

        tblOgrenciler.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"OgrenciID", "KullaniciID", "Ad", "Soyad", "Email"}
        ));
        tblOgrenciler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOgrencilerMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblOgrenciler);

        lblAd.setText("Ad:");
        lblSoyad.setText("Soyad:");
        lblEmail.setText("Email:");
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

        lblOgrenciID.setText("Öğrenci ID:");
        txtOgrenciID.setEditable(false);

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
                            .addComponent(lblEmail)
                            .addComponent(lblKullanici)
                            .addComponent(lblOgrenciID))
                        .addGap(30, 30, 30) // Adjusted spacing
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAd)
                            .addComponent(txtSoyad)
                            .addComponent(txtEmail)
                            .addComponent(cmbKullanici, 0, 200, Short.MAX_VALUE)
                            .addComponent(txtOgrenciID))
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
                    .addComponent(lblOgrenciID)
                    .addComponent(txtOgrenciID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>

    private void btnEkleActionPerformed(java.awt.event.ActionEvent evt) {
        OgretmenPanel.KullaniciComboBoxItem secilenKullanici = (OgretmenPanel.KullaniciComboBoxItem) cmbKullanici.getSelectedItem();
        String ad = txtAd.getText().trim();
        String soyad = txtSoyad.getText().trim();
        String email = txtEmail.getText().trim();

        if (secilenKullanici == null || ad.isEmpty() || soyad.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir e-posta adresi girin.", "Geçersiz Email", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (isKullaniciAlreadyOgrenci(secilenKullanici.getId(), 0)) {
            JOptionPane.showMessageDialog(this, "Seçilen kullanıcı zaten bir öğrenci olarak kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO Ogrenciler (KullaniciID, Ad, Soyad, Email) VALUES (?, ?, ?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenKullanici.getId());
            pstmt.setString(2, ad);
            pstmt.setString(3, soyad);
            pstmt.setString(4, email);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Öğrenci başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadOgrenciler();
                temizleForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğrenci eklenirken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            if (e.getSQLState().equals("23000")) {
                 JOptionPane.showMessageDialog(this, "Bu KullanıcıID zaten bir öğrenciye atanmış veya başka bir benzersizlik kısıtlaması ihlal edildi.", "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }
    
    private boolean isKullaniciAlreadyOgrenci(int kullaniciID, int currentOgrenciID) {
        String query = "SELECT COUNT(*) FROM Ogrenciler WHERE KullaniciID = ? AND OgrenciID != ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, kullaniciID);
            pstmt.setInt(2, currentOgrenciID > 0 ? currentOgrenciID : -1);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    private void btnGuncelleActionPerformed(java.awt.event.ActionEvent evt) {
        String ogrenciIDStr = txtOgrenciID.getText().trim();
        if (ogrenciIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir öğrenci seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ogrenciID = Integer.parseInt(ogrenciIDStr);
        OgretmenPanel.KullaniciComboBoxItem secilenKullanici = (OgretmenPanel.KullaniciComboBoxItem) cmbKullanici.getSelectedItem();
        String ad = txtAd.getText().trim();
        String soyad = txtSoyad.getText().trim();
        String email = txtEmail.getText().trim();

        if (secilenKullanici == null || ad.isEmpty() || soyad.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli bir e-posta adresi girin.", "Geçersiz Email", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (isKullaniciAlreadyOgrenci(secilenKullanici.getId(), ogrenciID)) {
            JOptionPane.showMessageDialog(this, "Seçilen kullanıcı zaten başka bir öğrenci olarak kayıtlı.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "UPDATE Ogrenciler SET KullaniciID = ?, Ad = ?, Soyad = ?, Email = ? WHERE OgrenciID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, secilenKullanici.getId());
            pstmt.setString(2, ad);
            pstmt.setString(3, soyad);
            pstmt.setString(4, email);
            pstmt.setInt(5, ogrenciID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Öğrenci başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadOgrenciler();
                temizleForm();
            } else {
                 JOptionPane.showMessageDialog(this, "Öğrenci bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Öğrenci güncellenirken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
             if (e.getSQLState().equals("23000")) {
                 JOptionPane.showMessageDialog(this, "Bu KullanıcıID zaten bir öğrenciye atanmış veya başka bir benzersizlik kısıtlaması ihlal edildi.", "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
            }
            e.printStackTrace();
        }
    }

    private void btnSilActionPerformed(java.awt.event.ActionEvent evt) {
        String ogrenciIDStr = txtOgrenciID.getText().trim();
        if (ogrenciIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir öğrenci seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ogrenciID = Integer.parseInt(ogrenciIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this, "Bu öğrenciyi silmek istediğinizden emin misiniz?\n(İlişkili kayıtları -örn: ÖğrenciDersler- varsa bu işlem sorun yaratabilir!)", "Silme Onayı", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            // Öğrencinin kayıtlı olduğu dersler var mı kontrol et (OgrenciDersler tablosu)
            if (isOgrenciReferencedInOgrenciDersler(ogrenciID)) {
                JOptionPane.showMessageDialog(this, "Bu öğrenci 'OgrenciDersler' tablosunda kayıtlı derslere sahip olduğu için silinemez.\nLütfen önce bu öğrencinin ders kayıtlarını silin.", "Silme Hatası", JOptionPane.ERROR_MESSAGE);
                return;
            }
             // Öğrencinin cevapları var mı kontrol et (Cevaplar tablosu)
            if (isOgrenciReferencedInCevaplar(ogrenciID)) {
                JOptionPane.showMessageDialog(this, "Bu öğrenci 'Cevaplar' tablosunda kayıtlı cevaplara sahip olduğu için silinemez.\nLütfen önce bu öğrencinin cevaplarını silin.", "Silme Hatası", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String query = "DELETE FROM Ogrenciler WHERE OgrenciID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, ogrenciID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Öğrenci başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadOgrenciler();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Öğrenci bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                 JOptionPane.showMessageDialog(this, "Öğrenci silinirken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                 e.printStackTrace();
            }
        }
    }
    
    private boolean isOgrenciReferencedInOgrenciDersler(int ogrenciID) {
        String query = "SELECT COUNT(*) FROM OgrenciDersler WHERE OgrenciID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ogrenciID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
        return false;
    }
    
    private boolean isOgrenciReferencedInCevaplar(int ogrenciID) {
        String query = "SELECT COUNT(*) FROM Cevaplar WHERE OgrenciID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ogrenciID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; 
        }
        return false;
    }

    private void tblOgrencilerMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = tblOgrenciler.getSelectedRow();
        if (selectedRow != -1) {
            txtOgrenciID.setText(tableModel.getValueAt(selectedRow, 0).toString());
            int kullaniciID = (int) tableModel.getValueAt(selectedRow, 1);
            txtAd.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtSoyad.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 4).toString());

            for (int i = 0; i < cmbKullanici.getItemCount(); i++) {
                OgretmenPanel.KullaniciComboBoxItem item = (OgretmenPanel.KullaniciComboBoxItem) cmbKullanici.getItemAt(i);
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
        txtOgrenciID.setText("");
        txtAd.setText("");
        txtSoyad.setText("");
        txtEmail.setText("");
        cmbKullanici.setSelectedIndex(-1);
        tblOgrenciler.clearSelection();
        loadKullanicilarToComboBox();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    // Variables declaration - do not modify
    private javax.swing.JButton btnEkle;
    private javax.swing.JButton btnGuncelle;
    private javax.swing.JButton btnSil;
    private javax.swing.JButton btnTemizle;
    private javax.swing.JComboBox<OgretmenPanel.KullaniciComboBoxItem> cmbKullanici; // Reusing the item class
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAd;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblKullanici;
    private javax.swing.JLabel lblOgrenciID;
    private javax.swing.JLabel lblSoyad;
    private javax.swing.JTable tblOgrenciler;
    private javax.swing.JTextField txtAd;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtOgrenciID;
    private javax.swing.JTextField txtSoyad;
    // End of variables declaration
}