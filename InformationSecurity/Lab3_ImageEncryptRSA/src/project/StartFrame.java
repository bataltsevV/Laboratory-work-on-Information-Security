package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

//класс старового окна с кнопками
public class StartFrame extends JFrame {

    private JButton btnDecrypt;
    private JButton btnEncrypt;
    private JLabel  actionDescript;

    public StartFrame() {
        super("Шифрование данных");
        super.setBounds(200, 100, 300, 230);

        //Инициализация объектов формы
        actionDescript = new JLabel("Выберите действие:");
        btnEncrypt     = new JButton();
        btnDecrypt     = new JButton();

        actionDescript.setFont(new Font("Tahoma", 1, 28));
        actionDescript.setHorizontalAlignment(SwingConstants.CENTER);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btnEncrypt.setFont(new Font("Tahoma", 0, 18));
        btnEncrypt.setText("Шифрование изображения");
        btnEncrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnEncryptActionPerformed(evt);
            }
        });

        btnDecrypt.setFont(new Font("Tahoma", 0, 18));
        btnDecrypt.setText("Дешифрование изображения");
        btnDecrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDecryptActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());

        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(106, Short.MAX_VALUE)
                                .addComponent(btnEncrypt)
                                .addGap(65, 65, 65)
                                .addComponent(btnDecrypt)
                                .addGap(113, 113, 113))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(138, 138, 138)
                                .addComponent(actionDescript, GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(actionDescript, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEncrypt)
                                        .addComponent(btnDecrypt))
                                .addContainerGap(116, Short.MAX_VALUE))
        );

        pack();
    }

    private void btnEncryptActionPerformed(ActionEvent evt) {
        new ImageEncryptionFrame().setVisible(true);
        this.dispose();
    }

    private void btnDecryptActionPerformed(ActionEvent evt) {
        new ImageDecryptionFrame().setVisible(true);
        this.dispose();
    }

    public static void main(String args[]) throws NoSuchAlgorithmException, IOException {
        new StartFrame().setVisible(true);
    }
}