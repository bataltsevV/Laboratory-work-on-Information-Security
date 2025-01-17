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

//форма с выбором файла для шифрования, по кнопке "Дешифровать" запускается алгоритм дешифрования
//зашифрованный файл сохраняется в ту же папку с новым расширением .jpg
public class ImageDecryptionFrame extends JFrame {

    private File       f = null;
    private JButton    btnChooseFile;
    private JButton    btnDecrypt;
    private JTextField txtFile;

    public ImageDecryptionFrame(){
        super("Дешифрование изображения");

        btnChooseFile = new JButton();
        txtFile = new JTextField();
        btnDecrypt = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btnChooseFile.setFont(new Font("Tahoma", 1, 14)); // NOI18N
        btnChooseFile.setText("Выберите файл:");
        btnChooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnChooseFileActionPerformed(evt);
            }
        });

        btnDecrypt.setText("Дешифровать");
        btnDecrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    try {
                        btnDecryptActionPerformed(evt);
                    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        throw new RuntimeException(e);
                    }
                } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
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
                                                .addComponent(btnDecrypt)))
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
                                .addComponent(btnDecrypt)
                                .addContainerGap(92, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnChooseFileActionPerformed(ActionEvent evt) {
        JFileChooser fileСhooser = new JFileChooser();
        FileFilter   filter      = new FileNameExtensionFilter("Encrypted File","encryptedFile");
        fileСhooser.setFileFilter(filter);
        if (fileСhooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            f = fileСhooser.getSelectedFile();
            txtFile.setText(f.getPath());
        }
    }

    private void btnDecryptActionPerformed(ActionEvent evt)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        project.ImageEncryption ob = new project.ImageEncryption();
        ob.decrypt(f.getPath(), f.getPath() + ".jpg");
        this.dispose();
    }

    public static void main(String args[]) {
        new ImageDecryptionFrame().setVisible(true);
    }
}