package online_ders_otomasyonu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.sql.DriverManager;


public class OgretmenForm extends javax.swing.JFrame {
private int ogretmenID;

    public class DBConnection {
        private static final String URL = "jdbc:sqlserver://localhost;databaseName=Online_Ders_Otomasyonu;encrypt=true;trustServerCertificate=true;";
        private static final String USER = "bi";
        private static final String PASSWORD = "123456Aa";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
    public OgretmenForm(int ogretmenID) {
        initComponents();
        this.ogretmenID = ogretmenID;
        ogretmenBilgileriniYukle();
        dersleriYukle();
        materyalleriyukle();
    }
    
    public void materyalleriyukle(){
          String selectedDers = (String) cmbDersler.getSelectedItem();
        if (selectedDers == null) {
            JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();

            String dersSql = "SELECT DersID FROM Dersler WHERE DersAdi = ? AND OgretmenID = ?;";
            PreparedStatement dersPst = conn.prepareStatement(dersSql);
            dersPst.setString(1, selectedDers);
            dersPst.setInt(2, ogretmenID);
            ResultSet dersRs = dersPst.executeQuery();
            int dersID = -1;
            if (dersRs.next()) {
                dersID = dersRs.getInt("DersID");
            }

            String materyalSql = "SELECT Baslik FROM Materyaller WHERE DersID = ?";
            PreparedStatement matPst = conn.prepareStatement(materyalSql);
            matPst.setInt(1, dersID);
            ResultSet rs = matPst.executeQuery();

            DefaultListModel<String> model = new DefaultListModel<>();
            while (rs.next()) {
                model.addElement(rs.getString("Baslik"));
            }

            listMateryaller.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Materyaller alınamadı: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblAdSoyad = new javax.swing.JLabel();
        lblBrans = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        cmbDersler = new javax.swing.JComboBox<>();
        btnMateryalYukle = new javax.swing.JButton();
        btnMateryalListele = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listMateryaller = new javax.swing.JList<>();
        btnCikis = new java.awt.Button();
        btnSınavForm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblAdSoyad.setText("Ad Soyad:..................................................");

        lblBrans.setText("Branş:........................................................");

        jLabel1.setText("Dersleriniz:");

        cmbDersler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDerslerActionPerformed(evt);
            }
        });

        btnMateryalYukle.setText("Materyal yükle");
        btnMateryalYukle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateryalYukleActionPerformed(evt);
            }
        });

        btnMateryalListele.setText("Materyal listele");
        btnMateryalListele.setToolTipText("");
        btnMateryalListele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMateryalListeleActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(listMateryaller);

        btnCikis.setLabel("Çıkış");
        btnCikis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCikisActionPerformed(evt);
            }
        });

        btnSınavForm.setText("Sınav ekle");
        btnSınavForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSınavFormActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbDersler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblAdSoyad, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                                .addComponent(lblBrans, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnMateryalListele, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                            .addComponent(btnMateryalYukle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSınavForm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8)))
                .addGap(45, 124, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCikis, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAdSoyad)
                    .addComponent(btnSınavForm))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBrans)
                    .addComponent(btnMateryalYukle))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(cmbDersler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMateryalListele)))
                .addGap(53, 53, 53)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCikis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMateryalListeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateryalListeleActionPerformed
         String selectedDers = (String) cmbDersler.getSelectedItem();
    if (selectedDers == null) {
        JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.");
        return;
    }

    try {
        Connection conn = DBConnection.getConnection();

        String dersSql = "SELECT DersID FROM Dersler WHERE DersAdi = ? AND OgretmenID = ?;";
        PreparedStatement dersPst = conn.prepareStatement(dersSql);
        dersPst.setString(1, selectedDers);
        dersPst.setInt(2, ogretmenID);
        ResultSet dersRs = dersPst.executeQuery();
        int dersID = -1;
        if (dersRs.next()) {
            dersID = dersRs.getInt("DersID");
        }

        String materyalSql = "SELECT Baslik FROM Materyaller WHERE DersID = ?";
        PreparedStatement matPst = conn.prepareStatement(materyalSql);
        matPst.setInt(1, dersID);
        ResultSet rs = matPst.executeQuery();

        DefaultListModel<String> model = new DefaultListModel<>();
        while (rs.next()) {
            model.addElement(rs.getString("Baslik"));
        }

        listMateryaller.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Materyaller alınamadı: " + e.getMessage());
    }
    }//GEN-LAST:event_btnMateryalListeleActionPerformed

    private void btnMateryalYukleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMateryalYukleActionPerformed
         String selectedDers = (String) cmbDersler.getSelectedItem();
    if (selectedDers == null) {
        JOptionPane.showMessageDialog(this, "Lütfen bir ders seçin.");
        return;
    }
    
    String baslik = JOptionPane.showInputDialog("Materyal Başlığı:");
    if (baslik == null || baslik.trim().isEmpty()) return;

    String aciklama = JOptionPane.showInputDialog("Materyal Açıklaması:");
    if (aciklama == null || aciklama.trim().isEmpty()) return;

    JFileChooser chooser = new JFileChooser();
    int secim = chooser.showOpenDialog(this);
    if (secim == JFileChooser.APPROVE_OPTION) {
        String dosyaYolu = chooser.getSelectedFile().getAbsolutePath();

        try {
            Connection conn = DBConnection.getConnection();

            String dersSql = "SELECT DersID FROM Dersler WHERE DersAdi = ? AND OgretmenID =?;";
            PreparedStatement dersPst = conn.prepareStatement(dersSql);
            dersPst.setString(1, selectedDers);
            dersPst.setInt(2, ogretmenID);
            ResultSet dersRs = dersPst.executeQuery();
            int dersID = -1;
            if (dersRs.next()) {
                dersID = dersRs.getInt("DersID");
            }

            String insertSql = "INSERT INTO Materyaller (DersID, Baslik, Aciklama, DosyaYolu) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(insertSql);
            pst.setInt(1, dersID);
            pst.setString(2, baslik);
            pst.setString(3, aciklama);
            pst.setString(4, dosyaYolu);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Materyal başarıyla yüklendi.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_btnMateryalYukleActionPerformed

    private void btnCikisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCikisActionPerformed
        this.dispose(); // mevcut formu kapat
        new LoginForm().setVisible(true); // tekrar giriş ekranına dön
    }//GEN-LAST:event_btnCikisActionPerformed

    private void btnSınavFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSınavFormActionPerformed

            new SinavForm(ogretmenID).setVisible(true);  // Yeni formu aç
           setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Sadece bu form kapanır
    }//GEN-LAST:event_btnSınavFormActionPerformed

    private void cmbDerslerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDerslerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDerslerActionPerformed



    private void dersleriYukle() {
    try {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT DersAdi FROM Dersler WHERE OgretmenID = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, ogretmenID);
        ResultSet rs = pst.executeQuery();
        
        while (rs.next()) {
            cmbDersler.addItem(rs.getString("DersAdi"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Dersler yüklenemedi: " + e.getMessage());
    }
}
    private void ogretmenBilgileriniYukle() {
    try {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT Ad, Soyad, Brans FROM Ogretmenler WHERE OgretmenID = ?";
        PreparedStatement pst = conn.prepareStatement(sql);  
        pst.setInt(1, ogretmenID);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            lblAdSoyad.setText(rs.getString("Ad") + " " + rs.getString("Soyad"));
            lblBrans.setText(rs.getString("Brans"));
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Öğretmen bilgileri yüklenemedi: " + e.getMessage());
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button btnCikis;
    private javax.swing.JButton btnMateryalListele;
    private javax.swing.JButton btnMateryalYukle;
    private javax.swing.JButton btnSınavForm;
    private javax.swing.JComboBox<String> cmbDersler;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAdSoyad;
    private javax.swing.JLabel lblBrans;
    private javax.swing.JList<String> listMateryaller;
    // End of variables declaration//GEN-END:variables
}
