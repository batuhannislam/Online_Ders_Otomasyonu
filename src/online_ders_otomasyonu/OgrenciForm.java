
package online_ders_otomasyonu;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class OgrenciForm extends javax.swing.JFrame {
private int ogrenciID;



    public OgrenciForm(int ogrenciID) {
       initComponents(); 
       this.ogrenciID = ogrenciID;
       dersleriYukle();             
       ogrenciBilgileriniYukle(); 
    }
        public static class DBConnection {
            private static final String URL = "jdbc:sqlserver://localhost;databaseName=Online_Ders_Otomasyonu;encrypt=true;trustServerCertificate=true;";
            private static final String USER = "bi";
            private static final String PASSWORD = "123456Aa";

            public static Connection getConnection() throws SQLException {
                return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSinavListele = new javax.swing.JButton();
        btnMateryalListele = new javax.swing.JButton();
        lblOgrenci = new javax.swing.JLabel();
        btnCikis = new javax.swing.JButton();
        comboBoxDersler = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        OgrenciList = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnSinavListele.setText("Sınavları listele");
        btnSinavListele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSinavListeleActionPerformed(evt);
            }
        });

        btnMateryalListele.setText("Materyallari listele");
        btnMateryalListele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateryalListeleActionPerformed(evt);
            }
        });

        lblOgrenci.setText("OgrenciAd/Soyad");

        btnCikis.setText("Çıkış ");

        comboBoxDersler.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        jScrollPane1.setViewportView(OgrenciList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCikis)
                .addGap(16, 16, 16))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOgrenci)
                            .addComponent(comboBoxDersler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnMateryalListele, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSinavListele, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(67, 67, 67))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOgrenci)
                    .addComponent(btnSinavListele))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMateryalListele)
                    .addComponent(comboBoxDersler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCikis)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void dersleriYukle() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT D.DersAdi FROM Dersler D " +
                         "JOIN OgrenciDersler OD ON D.DersID = OD.DersID " +
                         "WHERE OD.OgrenciID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, ogrenciID);
            ResultSet rs = pst.executeQuery();

            comboBoxDersler.removeAllItems(); 
            while (rs.next()) {
                comboBoxDersler.addItem(rs.getString("DersAdi"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OgrenciForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void btnMateryalListeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateryalListeleActionPerformed
    String secilenDers = (String) comboBoxDersler.getSelectedItem();

    System.out.println("--- Materyal Listele Butonuna Basıldı ---");
    System.out.println("ComboBox'tan Seçilen Ders: '" + secilenDers + "'");
    System.out.println("Kullanılan OgrenciID: " + ogrenciID);

    if (secilenDers == null || secilenDers.trim().isEmpty() || secilenDers.equals(" ")) {
        JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.");
        System.out.println("Hata: Ders seçilmedi veya geçersiz.");
        return;
    }

    DefaultListModel<String> listModel = new DefaultListModel<>();

    try (Connection conn = DBConnection.getConnection()) {
        // SQL SORGUSUNU DÜZELT: MateryalAdi -> Baslik
        String sql = "SELECT M.Baslik FROM Materyaller M " +
                     "JOIN Dersler D ON M.DersID = D.DersID " +
                     "JOIN OgrenciDersler OD ON OD.DersID = D.DersID " +
                     "WHERE D.DersAdi = ? AND OD.OgrenciID = ?";

        System.out.println("Çalıştırılacak SQL Sorgusu: " + sql);
        System.out.println("SQL Parametre 1 (DersAdi): '" + secilenDers + "'");
        System.out.println("SQL Parametre 2 (OgrenciID): " + ogrenciID);

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, secilenDers);
        pst.setInt(2, ogrenciID);
        ResultSet rs = pst.executeQuery();

        boolean veriBulundu = false;
        while (rs.next()) {
            veriBulundu = true;
            // RESULTSET'TEN VERİ ÇEKMEYİ DÜZELT: MateryalAdi -> Baslik
            String materyalBasligi = rs.getString("Baslik");
            System.out.println("Veritabanından Okunan Materyal Başlığı: '" + materyalBasligi + "'");
            listModel.addElement(materyalBasligi);
        }

        if (!veriBulundu) {
            System.out.println("SQL sorgusu sonuç döndürmedi (Seçilen ders ve öğrenci için materyal bulunamadı).");
        }

        OgrenciList.setModel(listModel);
        System.out.println("OgrenciList Modeli Güncellendi. Modeldeki Eleman Sayısı: " + listModel.getSize());

        if (listModel.isEmpty() && !secilenDers.trim().isEmpty() && !secilenDers.equals(" ")) {
             JOptionPane.showMessageDialog(this, "'" + secilenDers + "' dersi için kayıtlı materyal bulunamadı.");
        }

    } catch (SQLException ex) {
        Logger.getLogger(OgrenciForm.class.getName()).log(Level.SEVERE, "Materyal listeleme sırasında SQL hatası oluştu.", ex);
        // Hata mesajını kullanıcıya gösterirken daha spesifik olabiliriz.
        if (ex.getMessage().contains("Invalid column name")) {
            JOptionPane.showMessageDialog(this, "Veritabanı yapılandırma hatası: Sütun adı yanlış. Lütfen sistem yöneticisine başvurun.\nDetay: " + ex.getMessage());
        } else {
            JOptionPane.showMessageDialog(this, "Materyaller listelenirken bir veritabanı hatası oluştu: " + ex.getMessage());
        }
        ex.printStackTrace();
    } catch (Exception e) {
        Logger.getLogger(OgrenciForm.class.getName()).log(Level.SEVERE, "Materyal listeleme sırasında genel bir hata oluştu.", e);
        JOptionPane.showMessageDialog(this, "Materyaller listelenirken beklenmedik bir hata oluştu: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnMateryalListeleActionPerformed

    private void btnSinavListeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSinavListeleActionPerformed
    String secilenDers = (String) comboBoxDersler.getSelectedItem();

    // --- LOGLAMA BAŞLANGICI ---
    System.out.println("--- Sınav Listele Butonuna Basıldı ---");
    System.out.println("ComboBox'tan Seçilen Ders: '" + secilenDers + "'");
    System.out.println("Kullanılan OgrenciID: " + ogrenciID);
    // --- LOGLAMA BİTİŞİ ---

    if (secilenDers == null || secilenDers.trim().isEmpty() || secilenDers.equals(" ")) {
        JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.");
        System.out.println("Sınav Listeleme Hatası: Ders seçilmedi veya geçersiz."); // LOG
        return;
    }

    DefaultListModel<String> listModel = new DefaultListModel<>();

    try (Connection conn = DBConnection.getConnection()) {
        // SQL sorgusu güncellendi: 'LIMIT 1' yerine SQL Server uyumlu 'TOP 1' kullanıldı
        String sql = "SELECT S.SinavID, D.DersAdi, S.Tarih, " +
                     "(SELECT TOP 1 SQ.SoruMetni FROM Sorular SQ WHERE SQ.SinavID = S.SinavID AND SQ.DogruCevap = 'ANSWER_KEY') AS CevapAnahtariDosyaYolu " +
                     "FROM Sinavlar S " +
                     "JOIN Dersler D ON S.DersID = D.DersID " +
                     "JOIN OgrenciDersler OD ON OD.DersID = D.DersID " + // Öğrenci-Ders ilişkisi için
                     "WHERE D.DersAdi = ? AND OD.OgrenciID = ?";

        // --- LOGLAMA BAŞlangıcı ---
        System.out.println("Çalıştırılacak SQL Sorgusu (Sınavlar ve Cevap Anahtarı): " + sql);
        System.out.println("SQL Parametre 1 (DersAdi): '" + secilenDers + "'");
        System.out.println("SQL Parametre 2 (OgrenciID): " + ogrenciID);
        // --- LOGLAMA BİTİŞİ ---

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, secilenDers);
        pst.setInt(2, ogrenciID);
        ResultSet rs = pst.executeQuery();

        boolean veriBulundu = false;
        SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Veritabanındaki string formatı
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Listede göstermek istediğiniz format

        while (rs.next()) {
            veriBulundu = true;
            String dersAdi = rs.getString("DersAdi");
            String sinavTarihString = rs.getString("Tarih"); // Tarihi String olarak oku
            String cevapAnahtariYolu = rs.getString("CevapAnahtariDosyaYolu"); // Cevap Anahtarı Dosya Yolu

            String formattedDateForDisplay = "Tarih Belirtilmedi"; // Varsayılan değer

            if (sinavTarihString != null && !sinavTarihString.trim().isEmpty()) {
                try {
                    java.util.Date sinavTarihi = databaseDateFormat.parse(sinavTarihString);
                    formattedDateForDisplay = displayDateFormat.format(sinavTarihi);
                } catch (ParseException e) {
                    System.err.println("Tarih stringi parse edilemedi: " + sinavTarihString + " Hata: " + e.getMessage());
                    formattedDateForDisplay = "Geçersiz Tarih Formatı";
                }
            }

            // Ders adı, formatlanmış tarihi ve Cevap Anahtarı Dosya Yolunu birleştirme
            StringBuilder sinavBilgisi = new StringBuilder();
            sinavBilgisi.append(dersAdi);
            sinavBilgisi.append(" (").append(formattedDateForDisplay).append(")");

            if (cevapAnahtariYolu != null && !cevapAnahtariYolu.trim().isEmpty()) {
                sinavBilgisi.append(" - Cevap Anahtarı: ").append(cevapAnahtariYolu);
            } else {
                sinavBilgisi.append(" - Cevap Anahtarı: Yüklenmedi");
            }


            // --- LOGLAMA BAŞLANGICI ---
            System.out.println("Veritabanından Okunan Sınav Bilgisi (Listeye Eklenecek): '" + sinavBilgisi.toString() + "'");
            // --- LOGLAMA BİTİŞİ ---

            listModel.addElement(sinavBilgisi.toString());
        }

        if (!veriBulundu) {
            System.out.println("SQL sorgusu (Sınavlar) sonuç döndürmedi (Seçilen ders ve öğrenci için sınav bulunamadı).");
        }

        OgrenciList.setModel(listModel);
        System.out.println("OgrenciList Modeli Güncellendi (Sınavlar). Modeldeki Eleman Sayısı: " + listModel.getSize());

        if (listModel.isEmpty() && !secilenDers.trim().isEmpty() && !secilenDers.equals(" ")) {
            JOptionPane.showMessageDialog(this, "'" + secilenDers + "' dersi için kayıtlı sınav bulunamadı.");
        }

    } catch (SQLException ex) {
        Logger.getLogger(OgrenciForm.class.getName()).log(Level.SEVERE, "Sınav listeleme sırasında SQL hatası oluştu.", ex);
        // Hata mesajını kullanıcıya gösterirken daha anlaşılır bir mesaj verebiliriz.
        String errorMessage = "Sınavlar listelenirken bir veritabanı hatası oluştu.";
        if (ex.getMessage() != null && ex.getMessage().contains("Incorrect syntax near 'LIMIT'")) {
             errorMessage += "\nHata Detayı: Sorguda LIMIT anahtar kelimesi kullanılmış, ancak veritabanınız SQL Server olduğu için TOP kullanılmalıdır.";
        } else {
             errorMessage += "\nTeknik detaylar için konsol loglarını kontrol edin. Hata: " + ex.getMessage();
        }
        JOptionPane.showMessageDialog(this, errorMessage, "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    } catch (Exception e) {
        Logger.getLogger(OgrenciForm.class.getName()).log(Level.SEVERE, "Sınav listeleme sırasında genel bir hata oluştu.", e);
        JOptionPane.showMessageDialog(this, "Sınavlar listelenirken beklenmedik bir hata oluştu: " + e.getMessage(), "Genel Hata", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSinavListeleActionPerformed
 private void ogrenciBilgileriniYukle() {
     try {
        Connection conn = OgrenciForm.DBConnection.getConnection(); // Doğru sınıf adı
        String sql = "SELECT Ad, Soyad FROM Ogrenciler WHERE OgrenciID = ?";
        PreparedStatement pst = conn.prepareStatement(sql);  
        pst.setInt(1, ogrenciID);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            lblOgrenci.setText(rs.getString("Ad") + " " + rs.getString("Soyad"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Öğrenci bilgileri yüklenemedi: " + e.getMessage());
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> OgrenciList;
    private javax.swing.JButton btnCikis;
    private javax.swing.JButton btnMateryalListele;
    private javax.swing.JButton btnSinavListele;
    private javax.swing.JComboBox<String> comboBoxDersler;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblOgrenci;
    // End of variables declaration//GEN-END:variables
}
