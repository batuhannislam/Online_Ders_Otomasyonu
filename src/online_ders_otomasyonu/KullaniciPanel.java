package online_ders_otomasyonu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class KullaniciPanel extends javax.swing.JPanel {

    private DefaultTableModel tableModel;

    public KullaniciPanel() {
        initComponents();
        loadKullanicilar();
        loadRoller(); // Rolleri combobox'a yükle
    }

    private void loadKullanicilar() {
        String[] columnNames = {"KullaniciID", "KullaniciAdi", "Sifre", "RolID", "RolAdi"}; // RolAdi eklendi
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tablo hücreleri düzenlenemez
            }
        };
        tblKullanicilar.setModel(tableModel);

        String query = "SELECT K.KullaniciID, K.KullaniciAdi, K.Sifre, K.RolID, R.RolAdi " +
                       "FROM Kullanicilar K INNER JOIN Roller R ON K.RolID = R.RolID";

        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("KullaniciID"));
                row.add(rs.getString("KullaniciAdi"));
                row.add(rs.getString("Sifre")); // Genelde şifre gösterilmez ama örnek için ekliyorum
                row.add(rs.getInt("RolID"));
                row.add(rs.getString("RolAdi"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kullanıcıları yüklerken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadRoller() {
        cmbRol.removeAllItems(); // Önceki item'ları temizle
        String query = "SELECT RolID, RolAdi FROM Roller";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                // Combobox'a Rol objesini ekliyoruz, toString metodu RolAdi'nı gösterecek.
                cmbRol.addItem(new Rol(rs.getInt("RolID"), rs.getString("RolAdi")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Rolleri yüklerken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Rolleri combobox'ta göstermek için yardımcı sınıf
    private static class Rol {
        private int id;
        private String ad;

        public Rol(int id, String ad) {
            this.id = id;
            this.ad = ad;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return ad; // Combobox'ta bu görünecek
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblKullanicilar = new javax.swing.JTable();
        lblKullaniciAdi = new javax.swing.JLabel();
        txtKullaniciAdi = new javax.swing.JTextField();
        lblSifre = new javax.swing.JLabel();
        txtSifre = new javax.swing.JPasswordField();
        lblRol = new javax.swing.JLabel();
        cmbRol = new javax.swing.JComboBox<>();
        btnEkle = new javax.swing.JButton();
        btnGuncelle = new javax.swing.JButton();
        btnSil = new javax.swing.JButton();
        btnTemizle = new javax.swing.JButton();
        lblKullaniciID = new javax.swing.JLabel();
        txtKullaniciID = new javax.swing.JTextField();

        tblKullanicilar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"KullaniciID", "KullaniciAdi", "Sifre", "RolID"}
        ));
        tblKullanicilar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKullanicilarMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblKullanicilar);

        lblKullaniciAdi.setText("Kullanıcı Adı:");

        lblSifre.setText("Şifre:");

        lblRol.setText("Rol:");

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

        lblKullaniciID.setText("Kullanıcı ID:");

        txtKullaniciID.setEditable(false); // ID alanı düzenlenemez

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
                            .addComponent(lblKullaniciAdi)
                            .addComponent(lblSifre)
                            .addComponent(lblRol)
                            .addComponent(lblKullaniciID))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtKullaniciAdi)
                            .addComponent(txtSifre)
                            .addComponent(cmbRol, 0, 200, Short.MAX_VALUE)
                            .addComponent(txtKullaniciID))
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
                    .addComponent(lblKullaniciID)
                    .addComponent(txtKullaniciID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKullaniciAdi)
                    .addComponent(txtKullaniciAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEkle)
                    .addComponent(btnSil))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSifre)
                    .addComponent(txtSifre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuncelle)
                    .addComponent(btnTemizle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRol)
                    .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>

    private void btnEkleActionPerformed(java.awt.event.ActionEvent evt) {
        String kullaniciAdi = txtKullaniciAdi.getText().trim();
        String sifre = new String(txtSifre.getPassword()).trim();
        Rol secilenRol = (Rol) cmbRol.getSelectedItem();

        if (kullaniciAdi.isEmpty() || sifre.isEmpty() || secilenRol == null) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO Kullanicilar (KullaniciAdi, Sifre, RolID) VALUES (?, ?, ?)";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, kullaniciAdi);
            pstmt.setString(2, sifre); // Gerçek uygulamada şifre hash'lenmeli
            pstmt.setInt(3, secilenRol.getId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Kullanıcı başarıyla eklendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadKullanicilar(); // Tabloyu yenile
                temizleForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kullanıcı eklenirken hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void btnGuncelleActionPerformed(java.awt.event.ActionEvent evt) {
        String kullaniciIDStr = txtKullaniciID.getText().trim();
        if (kullaniciIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen güncellemek için bir kullanıcı seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int kullaniciID = Integer.parseInt(kullaniciIDStr);
        String kullaniciAdi = txtKullaniciAdi.getText().trim();
        String sifre = new String(txtSifre.getPassword()).trim();
        Rol secilenRol = (Rol) cmbRol.getSelectedItem();

        if (kullaniciAdi.isEmpty() || sifre.isEmpty() || secilenRol == null) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "UPDATE Kullanicilar SET KullaniciAdi = ?, Sifre = ?, RolID = ? WHERE KullaniciID = ?";
        try (Connection conn = AdminForm.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, kullaniciAdi);
            pstmt.setString(2, sifre);
            pstmt.setInt(3, secilenRol.getId());
            pstmt.setInt(4, kullaniciID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Kullanıcı başarıyla güncellendi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                loadKullanicilar();
                temizleForm();
            } else {
                JOptionPane.showMessageDialog(this, "Kullanıcı bulunamadı veya güncelleme yapılamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Kullanıcı güncellenirken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void btnSilActionPerformed(java.awt.event.ActionEvent evt) {
        String kullaniciIDStr = txtKullaniciID.getText().trim();
        if (kullaniciIDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen silmek için bir kullanıcı seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int kullaniciID = Integer.parseInt(kullaniciIDStr);
        int confirmation = JOptionPane.showConfirmDialog(this, "Bu kullanıcıyı silmek istediğinizden emin misiniz?", "Silme Onayı", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            // İlişkili tablolardaki verileri silme kontrolü (Öğretmenler, Öğrenciler)
            // Bu örnekte basitçe Kullanicilar'dan silme yapılıyor.
            // Gerçek bir uygulamada, foreign key constraint'leri nedeniyle bu işlem başarısız olabilir
            // veya ilişkili kayıtların da silinmesi gerekebilir.
            if (isKullaniciReferenced(kullaniciID)) {
                 JOptionPane.showMessageDialog(this, "Bu kullanıcı başka tablolarda (Öğretmenler, Öğrenciler) referans alındığı için silinemez.\nÖnce ilişkili kayıtları silin veya güncelleyin.", "Silme Hatası", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            String query = "DELETE FROM Kullanicilar WHERE KullaniciID = ?";
            try (Connection conn = AdminForm.DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, kullaniciID);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Kullanıcı başarıyla silindi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    loadKullanicilar();
                    temizleForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Kullanıcı bulunamadı veya silinemedi.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Kullanıcı silinirken hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private boolean isKullaniciReferenced(int kullaniciID) {
        String checkOgretmenQuery = "SELECT COUNT(*) FROM Ogretmenler WHERE KullaniciID = ?";
        String checkOgrenciQuery = "SELECT COUNT(*) FROM Ogrenciler WHERE KullaniciID = ?";

        try (Connection conn = AdminForm.DBConnection.getConnection()) {
            try (PreparedStatement pstmtOgretmen = conn.prepareStatement(checkOgretmenQuery)) {
                pstmtOgretmen.setInt(1, kullaniciID);
                try (ResultSet rs = pstmtOgretmen.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true; // Öğretmenler tablosunda referans var
                    }
                }
            }
            try (PreparedStatement pstmtOgrenci = conn.prepareStatement(checkOgrenciQuery)) {
                pstmtOgrenci.setInt(1, kullaniciID);
                try (ResultSet rs = pstmtOgrenci.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true; // Öğrenciler tablosunda referans var
                    }
                }
            }
        } catch (SQLException e) {
            // Hata durumunda güvenli tarafta kalıp referans var gibi davran
            e.printStackTrace();
            return true; 
        }
        return false; // Referans yok
    }


    private void tblKullanicilarMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = tblKullanicilar.getSelectedRow();
        if (selectedRow != -1) {
            txtKullaniciID.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtKullaniciAdi.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtSifre.setText(tableModel.getValueAt(selectedRow, 2).toString()); // Şifreyi forma çekme (güvenlik açısından önerilmez)

            int rolID = (int) tableModel.getValueAt(selectedRow, 3);
            for (int i = 0; i < cmbRol.getItemCount(); i++) {
                Rol rol = (Rol) cmbRol.getItemAt(i);
                if (rol.getId() == rolID) {
                    cmbRol.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void btnTemizleActionPerformed(java.awt.event.ActionEvent evt) {
        temizleForm();
    }

    private void temizleForm() {
        txtKullaniciID.setText("");
        txtKullaniciAdi.setText("");
        txtSifre.setText("");
        cmbRol.setSelectedIndex(-1); // Combobox'ı temizle
        tblKullanicilar.clearSelection();
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnEkle;
    private javax.swing.JButton btnGuncelle;
    private javax.swing.JButton btnSil;
    private javax.swing.JButton btnTemizle;
    private javax.swing.JComboBox<Rol> cmbRol;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblKullaniciAdi;
    private javax.swing.JLabel lblKullaniciID;
    private javax.swing.JLabel lblRol;
    private javax.swing.JLabel lblSifre;
    private javax.swing.JTable tblKullanicilar;
    private javax.swing.JTextField txtKullaniciAdi;
    private javax.swing.JTextField txtKullaniciID;
    private javax.swing.JPasswordField txtSifre;
    // End of variables declaration
}