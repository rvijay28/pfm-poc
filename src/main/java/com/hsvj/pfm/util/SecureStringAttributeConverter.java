package com.hsvj.pfm.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecureStringAttributeConverter implements AttributeConverter<String, String> {
    private static final String AES = "AES";
    private static final byte[] encryptionKey = "mYq3t6w9z$C&F)J@".getBytes(StandardCharsets.UTF_8);
    private final Key key;
    private final Cipher cipher;
    public SecureStringAttributeConverter() throws NoSuchPaddingException, NoSuchAlgorithmException {
        key = new SecretKeySpec(encryptionKey, AES);
        cipher = Cipher.getInstance(AES);
    }
    @Override
    public String convertToDatabaseColumn(String value) {
        if (null == value) return null;
        try{
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        }catch (InvalidKeyException| IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbValue) {
        if (null == dbValue) return null;
        try{
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(Base64.getDecoder().decode(dbValue));
        }catch(InvalidKeyException e){
            throw new IllegalArgumentException(e);
        }
    }
}
