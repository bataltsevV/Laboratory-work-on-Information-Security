package project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

//форма с выбором файла для шифрования, по кнопке "Зашифровать" запускается алгоритм шифрования
//зашифрованный файл сохраняется в ту же папку с новым расширением .encryptedFile
public class ImageEncryptionFrame extends JFrame {

    private JButton     btnChooseFile;
    private JButton     btnEncrypt;
    private JTextField  txtFile;
    private File        f = null;

    public ImageEncryptionFrame() {
        super("Шифрование изображения");

        btnChooseFile = new JButton();
        txtFile       = new JTextField();
        btnEncrypt    = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btnChooseFile.setFont(new Font("Tahoma", 1, 14));
        btnChooseFile.setText("Выберите файл:");
        btnChooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnChooseFileActionPerformed(evt);
            }
        });

        btnEncrypt.setText("Зашифровать");
        btnEncrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    btnEncryptActionPerformed(evt);
                } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException |
                         NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(138, 138, 138))
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(80, 80, 80)
                                                .addComponent(btnChooseFile)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtFile, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(238, 238, 238)
                                                .addComponent(btnEncrypt)))
                                .addContainerGap(86, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGap(29, 29, 29)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtFile, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnChooseFile))
                                .addGap(30, 30, 30)
                                .addComponent(btnEncrypt)
                                .addContainerGap(92, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnChooseFileActionPerformed(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileFilter filter = new FileNameExtensionFilter("JPG File","jpg");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            f = fileChooser.getSelectedFile();
            txtFile.setText(f.getPath());
        }
    }

    private void btnEncryptActionPerformed(ActionEvent evt)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        ImageEncryption ob = new ImageEncryption();
        ob.encrypt(f.getPath(), f.getPath() + ".encryptedFile");
        this.dispose();
    }

    public static void main(String args[]) {
        new ImageEncryptionFrame().setVisible(true);
    }
}