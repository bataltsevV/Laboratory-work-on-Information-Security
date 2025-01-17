package project;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageKeyGenerator {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public ImageKeyGenerator() throws NoSuchAlgorithmException{
        KeyPairGenerator keyGen=KeyPairGenerator.getInstance("RSA"); //KeyPairGenerator - ?
        keyGen.initialize(4096); //Ограничение на размер ключа
        KeyPair pair=keyGen.generateKeyPair();
        this.privateKey=pair.getPrivate();
        this.publicKey=pair.getPublic();
    }

    //Метод для записи в файл
    public void writeToFile(String path,byte[] key) throws IOException {
        File f=new File(path);
        f.getParentFile().mkdirs();
        FileOutputStream fos=new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    //Геттеры для получения сгенерированных ключей
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    public PublicKey getPublicKey() {
        return publicKey;
    }

    //Метод вызывает генерацию 2 ключей (открытый и закрытый) и сохраняет их в папку RSA
    public void createKeyFiles() throws NoSuchAlgorithmException, IOException {
        ImageKeyGenerator keyPairGenerator = new ImageKeyGenerator();
        keyPairGenerator.writeToFile("RSA/publicKey.key", keyPairGenerator.getPublicKey().getEncoded());
        keyPairGenerator.writeToFile("RSA/privateKey.key", keyPairGenerator.getPrivateKey().getEncoded());

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        ImageKeyGenerator keyPairGenerator = new ImageKeyGenerator();
        keyPairGenerator.createKeyFiles();
    }
}
