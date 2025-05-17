
package online_ders_otomasyonu;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;

public class SinavForm extends javax.swing.JFrame {

    private int ogretmenID;
    private JTable tblSinavlar;
    private JScrollPane scrollSinav;
    

public SinavForm(int ogretmenID) {
    this.ogretmenID = ogretmenID;
    initComponents();        // Design-generated GUI
    dersleriYukle();         // ComboBox'ı doldur
    tabloyuElleEkle();       // JTable'ı kendimiz ekle
    sinavlariYukle();        // Verileri tabloya doldur
}

private void tabloyuElleEkle() {
    // Kolon isimleri
    // Yeni kolon eklendi
    String[] kolonlar = {"Sınav ID", "Ders Adı", "Tarih", "Cevap Anahtarı Dosya Yolu"};
    DefaultTableModel model = new DefaultTableModel(kolonlar, 0) {
        // Make cells non-editable except potentially for the file path if you allow inline editing
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 3; // Allow editing only the "Cevap Anahtarı Dosya Yolu" column
            // return false; // Make all cells non-editable
        }
    };
    tblSinavlar = new JTable(model);

    // ScrollPane'e yerleştir
    scrollSinav = new JScrollPane(tblSinavlar);
    // Boyutları ve konumu ayarlayın (Form tasarımınıza göre ayarlayın)
    scrollSinav.setBounds(30, 120, 750, 250); // Genişliği artırdık

    // Layout yoksa elle konumla
    // getContentPane().setLayout(null); // Bu zaten constructor'da yapılıyor, burada tekrar yapmayın.
    getContentPane().add(scrollSinav);
}

private void sinavlariYukle() {
    try (Connection conn = DBConnection.getConnection()) {
        String sql = "SELECT S.SinavID, D.DersAdi, S.Tarih " +
                     "FROM Sinavlar S " +
                     "JOIN Dersler D ON S.DersID = D.DersID " +
                     "WHERE D.OgretmenID = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, ogretmenID);
        ResultSet rs = pst.executeQuery();

        DefaultTableModel model = (DefaultTableModel) tblSinavlar.getModel();
        model.setRowCount(0); // tabloyu temizle

        while (rs.next()) {
            int id = rs.getInt("SinavID");
            String ders = rs.getString("DersAdi");
            String tarih = rs.getString("Tarih");
            model.addRow(new Object[]{id, ders, tarih});
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Sınavlar yüklenemedi: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")

    private void btnSinavActionPerformed(java.awt.event.ActionEvent evt) {
        kaydetSinav();
    }

    private void dersleriYukle() {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT DersAdi FROM Dersler WHERE OgretmenID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, ogretmenID);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                dersComboBox.addItem(rs.getString("DersAdi"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Dersler yüklenemedi: " + e.getMessage());
        }
    }

    private void kaydetSinav() {
        String secilenDersAdi = (String) dersComboBox.getSelectedItem();
        String tarihStr = textTarih.getText().trim();

        if (secilenDersAdi == null || tarihStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun.");
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateFormat.parse(tarihStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Tarih formatı hatalı. yyyy-MM-dd biçiminde olmalı.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement dersIdStatement = conn.prepareStatement("SELECT DersID FROM Dersler WHERE DersAdi = ? AND OgretmenID = ?");
            dersIdStatement.setString(1, secilenDersAdi);
            dersIdStatement.setInt(2, ogretmenID);
            ResultSet dersIdResult = dersIdStatement.executeQuery();

            if (dersIdResult.next()) {
                int dersID = dersIdResult.getInt("DersID");

                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO Sinavlar (DersID, Tarih) VALUES (?, ?)");
                insertStmt.setInt(1, dersID);
                insertStmt.setString(2, tarihStr);

                int affectedRows = insertStmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Sınav başarıyla eklendi.");
                    textTarih.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Sınav eklenemedi.");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Seçilen ders bulunamadı.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + e.getMessage());
        }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dersComboBox = new javax.swing.JComboBox<>();
        textTarih = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnsinav = new javax.swing.JButton();
        btnCvp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Ders seçin:");

        jLabel2.setText("Tarih girin:");

        btnsinav.setText("Sınav Ekle");
        btnsinav.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsinavActionPerformed(evt);
            }
        });

        btnCvp.setText("Cevap anahtarını yayınla");
        btnCvp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCvpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textTarih, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dersComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnsinav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCvp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(382, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(dersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnsinav))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textTarih, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnCvp))
                .addContainerGap(260, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnsinavActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsinavActionPerformed
        kaydetSinav();
        sinavlariYukle(); 
    }//GEN-LAST:event_btnsinavActionPerformed

    private void btnCvpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCvpActionPerformed
        int selectedRow = tblSinavlar.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Lütfen önce tablodan bir sınav seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Get the SinavID from the selected row
    DefaultTableModel model = (DefaultTableModel) tblSinavlar.getModel();
    int sinavID = (int) model.getValueAt(selectedRow, 0); // Assuming SinavID is in the first column

    // Use JFileChooser to select the answer key file
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Cevap Anahtarı Dosyasını Seçin");
    int userSelection = fileChooser.showOpenDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        String filePath = selectedFile.getAbsolutePath(); // Get the absolute path

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Check if an answer key entry already exists for this SinavID
            String checkSoruSql = "SELECT COUNT(*) FROM Sorular WHERE SinavID = ? AND DogruCevap = 'ANSWER_KEY'";
            PreparedStatement checkSoruPst = conn.prepareStatement(checkSoruSql);
            checkSoruPst.setInt(1, sinavID);
            ResultSet rs = checkSoruPst.executeQuery();
            rs.next();
            int existingEntries = rs.getInt(1);
            rs.close();
            checkSoruPst.close();

            if (existingEntries > 0) {
                // If an entry exists, update it
                String updateSoruSql = "UPDATE Sorular SET SoruMetni = ? WHERE SinavID = ? AND DogruCevap = 'ANSWER_KEY'";
                PreparedStatement updateSoruPst = conn.prepareStatement(updateSoruSql);
                updateSoruPst.setString(1, filePath);
                updateSoruPst.setInt(2, sinavID);
                updateSoruPst.executeUpdate();
                updateSoruPst.close();
            } else {
                // If no entry exists, insert a new one
                String insertSoruSql = "INSERT INTO Sorular (SinavID, SoruMetni, DogruCevap) VALUES (?, ?, 'ANSWER_KEY')";
                PreparedStatement insertSoruPst = conn.prepareStatement(insertSoruSql);
                insertSoruPst.setInt(1, sinavID);
                insertSoruPst.setString(2, filePath);
                insertSoruPst.executeUpdate();
                insertSoruPst.close();
            }

            conn.commit(); // Commit the transaction

            // Update the JTable model to show the file path in the selected row
            model.setValueAt(filePath, selectedRow, 3); // Update the 4th column (index 3)

            JOptionPane.showMessageDialog(this, "Cevap anahtarı başarıyla kaydedildi.", "Başarılı", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanı hatası: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    } else {
        // User cancelled the file selection
        JOptionPane.showMessageDialog(this, "Dosya seçimi iptal edildi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
    }
    }//GEN-LAST:event_btnCvpActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCvp;
    private javax.swing.JButton btnsinav;
    private javax.swing.JComboBox<String> dersComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField textTarih;
    // End of variables declaration//GEN-END:variables
}

