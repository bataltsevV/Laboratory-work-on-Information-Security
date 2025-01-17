package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//класс с алгоритмами шифрования, используя Java Cipher
public class ImageEncryption {

    public static PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        PublicKey publicKey;
        File publicKeyFile = new File("RSA/publicKey.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        publicKey = keyFactory.generatePublic(publicKeySpec);

        return publicKey;
    }
    public static PrivateKey getPrivateKey()
            throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        File privateKeyFile = new File("RSA/privateKey.key");
        byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
        KeyFactory     keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey     privateKey;
        privateKey =   keyFactory.generatePrivate(privateKeySpec);

        return privateKey;
    }

    public void encrypt(String srcPath, String destPath)
            throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); //создание экземпляра шифра на основании алгоритма RSA
        File         rawFile       = new File(srcPath);
        File         encryptedFile = new File(destPath);
        InputStream  inStream      = null;
        OutputStream outStream     = null;

        cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKey()); //инициализация шифра в режиме шифрования с помощью открытого ключа

        inStream  = new FileInputStream(rawFile);
        outStream = new FileOutputStream(encryptedFile);
        byte[]  buffer = new byte[1024];
        int     len;

        while ((len = inStream.read(buffer)) > 0) {
            outStream.write(cipher.update(buffer,0, len));
            outStream.flush();
        }
        outStream.write(cipher.doFinal()); //шифрование файла и его сохранение в папку
        inStream.close();
        outStream.close();
    }

    public void decrypt(String srcPath, String destPath)
            throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        //для дешифрования используем уже ранее сгенерированный закрытый ключ
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//создание экземпляра шифра на основании алгоритма RSA
        File encryptedFile = new File(srcPath);
        File decryptedFile = new File(destPath);
        InputStream  inStream  = new FileInputStream(encryptedFile);
        OutputStream outStream = new FileOutputStream(decryptedFile);

        cipher.init(Cipher.DECRYPT_MODE, this.getPrivateKey()); //инициализация шифра в режиме дешифрования с помощью ключа

        byte[]  buffer = new byte[1024];
        int     len;

        while   ((len = inStream.read(buffer)) > 0) {
            outStream.write(cipher.update(buffer,0, len));
            outStream.flush();
        }
        outStream.write(cipher.doFinal()); //дешифрование файла и его сохранение в папку
        inStream.close();
        outStream.close();
    }
}
