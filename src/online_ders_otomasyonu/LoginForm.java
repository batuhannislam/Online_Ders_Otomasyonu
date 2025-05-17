
package online_ders_otomasyonu;

import java.sql.*;
import javax.swing.*;

public class LoginForm extends javax.swing.JFrame {

    private int ogrenciID;

    public LoginForm() {
        initComponents();
        lblDurum.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider();
        txtKullaniciAdi = new javax.swing.JTextField();
        txtSifre = new javax.swing.JPasswordField();
        btnGirisYap = new javax.swing.JButton();
        lblSifremiUnuttum = new javax.swing.JLabel();
        lblDurum = new javax.swing.JLabel();
        lblKullaniciAdi = new javax.swing.JLabel();
        lblSifre = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnGirisYap.setText("Giriş yap");
        btnGirisYap.setToolTipText("");
        btnGirisYap.setName(""); // NOI18N
        btnGirisYap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGirisYapActionPerformed(evt);
            }
        });

        lblSifremiUnuttum.setForeground(new java.awt.Color(51, 153, 255));
        lblSifremiUnuttum.setText("Şifremi unuttum");
        lblSifremiUnuttum.setToolTipText("Şifrenizi sıfırlamak için tıklayın");
        lblSifremiUnuttum.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblSifremiUnuttum.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSifremiUnuttumMouseClicked(evt);
            }
        });

        lblDurum.setBackground(new java.awt.Color(204, 0, 51));
        lblDurum.setForeground(new java.awt.Color(255, 0, 0));
        lblDurum.setEnabled(false);

        lblKullaniciAdi.setText("Kullanıcı adı");

        lblSifre.setText("Şifre");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDurum, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblSifre, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblKullaniciAdi)
                        .addComponent(txtSifre)
                        .addComponent(txtKullaniciAdi)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblSifremiUnuttum)
                            .addGap(18, 18, 18)
                            .addComponent(btnGirisYap))))
                .addContainerGap(112, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(lblKullaniciAdi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKullaniciAdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSifre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSifre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGirisYap)
                    .addComponent(lblSifremiUnuttum))
                .addGap(29, 29, 29)
                .addComponent(lblDurum, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        btnGirisYap.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblSifremiUnuttumMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSifremiUnuttumMouseClicked
       new SifreGuncellemeForm().setVisible(true);

    }//GEN-LAST:event_lblSifremiUnuttumMouseClicked

    private void btnGirisYapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGirisYapActionPerformed

        String kullaniciAdi = txtKullaniciAdi.getText();
    String sifre = String.valueOf(txtSifre.getPassword());

    try (Connection conn = DriverManager.getConnection(
            "jdbc:sqlserver://localhost;databaseName=Online_Ders_Otomasyonu;encrypt=true;trustServerCertificate=true;",
            "bi", "123456Aa")) {

        String sql = "SELECT KullaniciID, RolID FROM Kullanicilar WHERE kullaniciAdi = ? AND sifre = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, kullaniciAdi);
        ps.setString(2, sifre);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int kullaniciID = rs.getInt("KullaniciID");
            int rolID = rs.getInt("RolID");

            Session.setKullaniciID(kullaniciID); // 👈 Oturuma yaz
            Session.setRolID(rolID);

            switch (rolID) {
                case 1:
                    new AdminForm().setVisible(true);
                    break;
                case 2:
                    // KullaniciID → OgretmenID
                    int ogretmenID = 0;
                    String ogretmenSQL = "SELECT OgretmenID FROM Ogretmenler WHERE KullaniciID = ?";
                    PreparedStatement pst = conn.prepareStatement(ogretmenSQL);
                    pst.setInt(1, kullaniciID);
                    ResultSet ogretmenRS = pst.executeQuery();
                    if (ogretmenRS.next()) {
                        ogretmenID = ogretmenRS.getInt("OgretmenID");
                    }

                    Session.setKullaniciID(kullaniciID); // istersen bunu da tut
                    Session.setRolID(rolID);

                    new OgretmenForm(ogretmenID).setVisible(true);
                    break;
                case 3:
                    
                   int OgrenciID = 0;
                    String ogrenciSQL = "SELECT OgrenciID FROM Ogrenciler WHERE KullaniciID = ?";
                    PreparedStatement pst1 = conn.prepareStatement(ogrenciSQL);
                    pst1.setInt(1, kullaniciID);
                    ResultSet ogrenciRS = pst1.executeQuery();
                    if (ogrenciRS.next()) {
                        OgrenciID = ogrenciRS.getInt("OgrenciID"); 
                    }

                    Session.setKullaniciID(kullaniciID);
                    Session.setRolID(rolID);

                    new OgrenciForm(OgrenciID).setVisible(true);
                    break;
            }
            this.dispose();
        }else {
            lblDurum.setText("Kullanıcı adı veya şifre hatalı!");
            lblDurum.setVisible(true);
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Veritabanı bağlantı hatası: " + ex.getMessage());
    }
    }//GEN-LAST:event_btnGirisYapActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
  
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGirisYap;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JLabel lblDurum;
    private javax.swing.JLabel lblKullaniciAdi;
    private javax.swing.JLabel lblSifre;
    private javax.swing.JLabel lblSifremiUnuttum;
    private javax.swing.JTextField txtKullaniciAdi;
    private javax.swing.JPasswordField txtSifre;
    // End of variables declaration//GEN-END:variables
}
