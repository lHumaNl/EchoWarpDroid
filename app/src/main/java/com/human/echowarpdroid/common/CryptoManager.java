package com.human.echowarpdroid.common;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import android.util.Base64;

public class CryptoManager {
    private final boolean isServer;
    private Boolean isEncrypt;
    private Boolean isHashControl;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private PublicKey peerPublicKey;
    private byte[] aesKey;
    private byte[] aesIv;
    private SecretKeySpec secretKeySpec;
    private IvParameterSpec ivParameterSpec;
    private Cipher rsaCipherDecrypt;
    private Cipher rsaCipherEncrypt;
    private Cipher aesCipherDecrypt;
    private Cipher aesCipherEncrypt;
    private MessageDigest digest;


    public CryptoManager(boolean isServer, Boolean isHashControl, Boolean isEncrypt) {
        this.isServer = isServer;
        this.isEncrypt = isEncrypt;
        this.isHashControl = isHashControl;

        generateRSAKeys();
        if (isServer) generateAESKeyAndIV();

        try {
            initCrypts();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public String getAesKeyInBase64() {
        if (!isServer)
            throw new IllegalArgumentException("Only server can get AES key");

        return Base64.encodeToString(aesKey, Base64.DEFAULT);
    }

    public String getAesIvInBase64() {
        if (!isServer)
            throw new IllegalArgumentException("Only server can get AES IV");

        return Base64.encodeToString(aesIv, Base64.DEFAULT);
    }

    public void loadAesKeyAndIv(String aesKeyBase64, String aesIvBase64) throws InvalidKeyException {
        if (isServer)
            throw new IllegalArgumentException("Only client can load AES key and IV");

        aesKey = Base64.decode(aesKeyBase64, Base64.DEFAULT);
        aesIv = Base64.decode(aesIvBase64, Base64.DEFAULT);

        secretKeySpec = new SecretKeySpec(aesKey, "AES");
        ivParameterSpec = new IvParameterSpec(aesIv);

        try {
            aesCipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            aesCipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new InvalidKeyException(e);
        }
    }

    public byte[] getSerializedPublicKey() {
        try {
            return publicKey.getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadPeerPublicKey(byte[] peerPublicKeyBytes) {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(peerPublicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            peerPublicKey = keyFactory.generatePublic(spec);

            rsaCipherEncrypt.init(Cipher.ENCRYPT_MODE, peerPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCrypts() throws InvalidKeyException {
        try {
            aesCipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipherEncrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");

            if (isServer) {
                aesCipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
                aesCipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            }

            rsaCipherDecrypt = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            rsaCipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey);

            rsaCipherEncrypt = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 NoSuchPaddingException | InvalidKeyException e) {
            throw new InvalidKeyException(e);
        }
    }

    public void loadEncryptionConfigForClient(boolean isEncrypt, boolean isHashControl) {
        if (isServer)
            throw new IllegalArgumentException("Load encryption config applied only for client");

        this.isEncrypt = isEncrypt;
        this.isHashControl = isHashControl;
    }

    public byte[] encryptAESAndSignData(byte[] data) throws Exception {
        if (data == null)
            throw new IllegalArgumentException("Data to encrypt and sign cannot be null");

        if (isHashControl) {
            data = appendHash(data);
        }

        if (isEncrypt) {
            try {
                data = encryptAES(data);
            } catch (Exception e) {
                throw new Exception("Failed to encrypt data: " + e.getMessage());
            }
        }

        return data;
    }

    public byte[] decryptAESAndVerifyData(byte[] data) throws Exception {
        if (data == null)
            throw new IllegalArgumentException("Data to decrypt and verify cannot be null");

        if (isEncrypt) {
            try {
                data = decryptAES(data);
            } catch (Exception e) {
                throw new Exception("Failed to decrypt data: " + e.getMessage());
            }
        }

        if (isHashControl) {
            try {
                data = verifyAndExtractData(data);
            } catch (Exception e) {
                throw new Exception("Data integrity check failed: " + e.getMessage());
            }
        }

        return data;
    }

    private void generateRSAKeys() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
            KeyPair pair = keyGen.generateKeyPair();

            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateAESKeyAndIV() {
        SecureRandom random = new SecureRandom();
        aesKey = new byte[32];
        aesIv = new byte[16];

        random.nextBytes(aesKey);
        random.nextBytes(aesIv);

        secretKeySpec = new SecretKeySpec(aesKey, "AES");
        ivParameterSpec = new IvParameterSpec(aesIv);
    }

    public byte[] encryptAES(byte[] data) throws Exception {
        if (data == null)
            throw new IllegalArgumentException("Data cannot be null");

        return aesCipherEncrypt.doFinal(data);
    }

    public byte[] decryptAES(byte[] data) throws Exception {
        if (data == null)
            throw new IllegalArgumentException("Data cannot be null");

        return aesCipherDecrypt.doFinal(data);
    }

    public byte[] encryptRSA(byte[] data) throws Exception {
        if (peerPublicKey == null)
            throw new IllegalStateException("Peer public key is not loaded");

        return rsaCipherEncrypt.doFinal(data);
    }

    public byte[] decryptRSA(byte[] data) throws Exception {
        return rsaCipherDecrypt.doFinal(data);
    }

    public byte[] calculateHash(byte[] data) {
        try {
            return digest.digest(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] appendHash(byte[] data) {
        byte[] hash = calculateHash(data);
        byte[] combined = new byte[hash.length + data.length];
        System.arraycopy(hash, 0, combined, 0, hash.length);
        System.arraycopy(data, 0, combined, hash.length, data.length);

        return combined;
    }

    public byte[] verifyAndExtractData(byte[] data) throws Exception {
        byte[] hash = Arrays.copyOfRange(data, 0, 32);
        byte[] actualData = Arrays.copyOfRange(data, 32, data.length);
        byte[] calculatedHash = calculateHash(actualData);

        if (!Arrays.equals(hash, calculatedHash))
            throw new Exception("Data integrity check failed");

        return actualData;
    }
}
