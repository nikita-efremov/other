import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.util.Date;

public class Encoder {

    private static String CIPHER_NAME = "DES";
    private static String CIPHER_CONFIGURATION = "DES/ECB/PKCS5Padding";

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.out.println("Not enought args were entered, so only key will be generated");
            System.out.println("Your key is [" + generateKey() + "]");
            System.out.println("\nUsage of encoder: \nEncoder [mode] [input file] [output file] [key in string representation]\n" +
                    "Where [mode] is: 0 - for encoding, 1 - for decoding");
            return;
        }

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] keyInByteRepresentation = decoder.decodeBuffer(args[3]);
        Key key = new SecretKeySpec(keyInByteRepresentation,0,keyInByteRepresentation.length, CIPHER_NAME);

        FileInputStream input = new FileInputStream(args[1]);
        FileOutputStream output = new FileOutputStream(args[2]);

        switch (args[0]) {
            case "0": {
                System.out.println("Encoding mode was chosen. Starting to encode...");
                long startTime = new Date().getTime();
                encrypt(key, input, output);
                System.out.println("Encoding finished. Encoding time is " + (new Date().getTime() - startTime) + " ms");
                break;
            }
            case "1": {
                System.out.println("Decoding mode was chosen. Starting to decode...");
                long startTime = new Date().getTime();
                decrypt(key, input, output);
                System.out.println("Decoding finished. Decoding time is " + (new Date().getTime() - startTime) + " ms");
                break;
            }
            default: throw new IllegalArgumentException("Wrong mode type were chosen: " + args[0]);
        }
    }

    public static void encrypt(Key key, InputStream is, OutputStream os) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_CONFIGURATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        CipherInputStream cis = new CipherInputStream(is, cipher);
        doCopy(cis, os);
    }

    public static void decrypt(Key key, InputStream is, OutputStream os) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_CONFIGURATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        CipherOutputStream cos = new CipherOutputStream(os, cipher);
        doCopy(is, cos);
    }

    public static void doCopy(InputStream is, OutputStream os)
            throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

    private static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CIPHER_NAME);
        keyGenerator.init(56);
        Key keyToEncrypt = keyGenerator.generateKey();
        return new BASE64Encoder().encode(keyToEncrypt.getEncoded());
    }
}
