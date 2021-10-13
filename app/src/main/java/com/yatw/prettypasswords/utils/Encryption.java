package com.yatw.prettypasswords.utils;


import org.libsodium.jni.NaCl;
import org.libsodium.jni.Sodium;
import org.libsodium.jni.SodiumConstants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Encryption {

    class EncryptionError extends Exception{

        public EncryptionError(Exception e) {
            super(e);
        }
    }

    public Encryption(){
        NaCl.sodium(); // required to load the native C library
    }

    //Generate asymmetric key pair using libsodium
    public byte[][] generateAKey() {

        long pklen = Sodium.crypto_box_publickeybytes();
        long sklen = Sodium.crypto_box_secretkeybytes();
        byte[] pk =new byte[(int)pklen];
        byte[] sk =new byte[(int)sklen];

        Sodium.crypto_box_keypair(pk,sk);

        return new byte[][]{pk,sk};
    }


    //Generate symmetric key using libsodium
    public byte[] generateSKey() {

        int size = Sodium.crypto_secretbox_keybytes();
        byte[] buf = new byte[size];
        Sodium.randombytes_buf(buf, size);
        return buf;

    }

    // to generate ESK, symmetric encryption, use the password as key to encrypt the SK
    // ESK consist of the nonce (24 byte) + encrypted sk (48 byte), total is 72 byte
    public byte[] generateESKey(byte[] sk, String password) throws EncryptionError {

        return sKeyEncrypt(sk, SHA256(password.getBytes()));

    }


    /**
     Same function to generate both BESAK and EESAK

     BESAK: begin vertex encrypted sysmmetric access key (use your own pk sk)
     EESAK: end vertex encrypted sysmmetric access key (use your sk, shared person's pk)

     - Parameter recvPublicKey: public key of recipient
     - Parameter sendSecretKey: secrect key of sender
     */
    public byte[] generateXESAK(byte[] receiverPK, byte[] senderSK){

        byte[] symmetricKey = generateSKey();
        byte[] halfKey = Arrays.copyOfRange(symmetricKey, 0, 16);

        return aKeyEncrypt(halfKey, receiverPK, senderSK);
    }


    /**
     xesak can be besak or eesak
     asymmertic decrypt xesak to get besak
     */
    public byte[] generateSAK(byte[] xesak, byte[] senderPublicKey, byte[] receiverPrivateKey) throws EncryptionError {

        byte[] halfKey =  aKeyDecrypt(xesak, senderPublicKey, receiverPrivateKey);

        return SHA256(halfKey); // after sha256 would be 32 bytes long, just right for libsodium symmetric key length
    }




    ///Decrypt esk using password to obtain sk
    public byte[] decryptESKtoSK(byte[] esk, String password) throws EncryptionError {

        return sKeyDecrypt(esk, SHA256(password.getBytes()));
    }


    ///Generate public key from secrect key
    public byte[] generatePk(byte[] privateKey){

        long pklen = Sodium.crypto_box_publickeybytes();
        byte[] computedPublicKey = new byte[(int)pklen];

        Sodium.crypto_scalarmult_base(computedPublicKey, privateKey);

        return computedPublicKey;

    }

    ///Check if the asymmetric keypair is valid
    public boolean aKeyCheck(byte[] publicKey, byte[] privateKey) {

        long pklen = Sodium.crypto_box_publickeybytes();
        byte[] computedPublicKey = new byte[(int)pklen];

        Sodium.crypto_scalarmult_base(computedPublicKey, privateKey);

        return Arrays.equals(computedPublicKey, publicKey);

    }



    public byte[] aKeyEncrypt(byte[] messageBytes, byte[] receiverPublicKey, byte[] senderPrivateKey) {

        byte[] nonce = generateNonce();

        byte[] cipherText = new byte[Sodium.crypto_box_macbytes() + messageBytes.length];

        Sodium.crypto_box_easy(

                cipherText,
                messageBytes,
                messageBytes.length,
                nonce,
                receiverPublicKey,
                senderPrivateKey
        );


        byte[] fullMessage = new byte[SodiumConstants.NONCE_BYTES + cipherText.length];
        System.arraycopy(nonce, 0, fullMessage, 0, nonce.length);
        System.arraycopy(cipherText, 0, fullMessage, nonce.length, cipherText.length);

        return fullMessage;

    }



    public byte[] aKeyDecrypt(byte[] eData, byte[] senderPublicKey, byte[] receiverPrivateKey) {


        byte[] nonce = new byte[SodiumConstants.NONCE_BYTES];
        byte[] cipherText = new byte[eData.length - nonce.length];
        System.arraycopy(eData, 0, nonce, 0, nonce.length);
        System.arraycopy(eData, nonce.length, cipherText, 0, cipherText.length);


        byte[] decryptedMessageBytes = new byte[(int) (cipherText.length - Sodium.crypto_box_macbytes())];


        Sodium.crypto_box_open_easy(

                decryptedMessageBytes,
                cipherText,
                cipherText.length,
                nonce,
                senderPublicKey,
                receiverPrivateKey

        );

        return decryptedMessageBytes;

    }


    public String aKeyDecryptStr(byte[] eData, byte[] senderPublicKey, byte[] receiverPrivateKey) throws EncryptionError {

        byte[] nonce = new byte[SodiumConstants.NONCE_BYTES];
        byte[] cipherText = new byte[eData.length - nonce.length];
        System.arraycopy(eData, 0, nonce, 0, nonce.length);
        System.arraycopy(eData, nonce.length, cipherText, 0, cipherText.length);

        try {

            byte[] decrypted = new byte[(int) (cipherText.length - Sodium.crypto_box_macbytes())];


            Sodium.crypto_box_open_easy(

                    decrypted,
                    cipherText,
                    cipherText.length,
                    nonce,
                    senderPublicKey,
                    receiverPrivateKey

            );

            return new String(decrypted, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            throw new EncryptionError(e);
        }

    }




    public byte[] sKeyEncrypt(byte[] messageBytes, byte[] key) {

        byte[] nonce = generateNonce();
        byte[] cipherText = new byte[Sodium.crypto_box_macbytes() + messageBytes.length];

        Sodium.crypto_secretbox_easy(

                cipherText,
                messageBytes,
                messageBytes.length,
                nonce,
                key
        );

        byte[] encryptedData = new byte[nonce.length + cipherText.length];
        System.arraycopy(nonce, 0, encryptedData, 0, nonce.length);
        System.arraycopy(cipherText, 0, encryptedData, nonce.length, cipherText.length);
        return encryptedData;
    }


    public byte[] sKeyDecrypt(byte[] eData, byte[] key) {

        byte[] nonce = new byte[SodiumConstants.NONCE_BYTES];
        byte[] cipherText = new byte[eData.length - nonce.length];
        System.arraycopy(eData, 0, nonce, 0, nonce.length);
        System.arraycopy(eData, nonce.length, cipherText, 0, cipherText.length);

        byte[] decrypted = new byte[(cipherText.length - Sodium.crypto_box_macbytes())];

        Sodium.crypto_secretbox_open_easy(decrypted,cipherText,cipherText.length,nonce,key);

        return decrypted;
    }



    // helper functions below ----------------------------------------------------------------------



    // used to turn password to SHA to encrypt sk
    public byte[] SHA256(byte[] password) throws EncryptionError {


        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionError(e);
        }

        byte[] pwSHA = md.digest(password);

        return pwSHA;

    }


    public byte[] generateNonce() {

        int size = Sodium.crypto_box_noncebytes();

        byte[] buf = new byte[size];
        Sodium.randombytes_buf(buf, size);
        return buf;
    }




}
