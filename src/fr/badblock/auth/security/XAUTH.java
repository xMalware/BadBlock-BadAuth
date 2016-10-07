package fr.badblock.auth.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class XAUTH {
	private static SecureRandom rnd = new SecureRandom();
	
	public static String createSalt(int length) throws NoSuchAlgorithmException {
        byte[] msg = new byte[40];
        rnd.nextBytes(msg);
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.reset();
        byte[] digest = sha1.digest(msg);
        return String.format("%0" + (digest.length << 1) + "x", new BigInteger(1, digest)).substring(0, length);
    }
	
	public String getHash(String password) throws NoSuchAlgorithmException {
        return getHash(password, createSalt(12));
    }
	
	public String getHash(String password, String salt) throws NoSuchAlgorithmException {
        String hash = getWhirlpool(salt + password).toLowerCase();
        int saltPos = (password.length() >= hash.length() ? hash.length() - 1 : password.length());
        return hash.substring(0, saltPos) + salt + hash.substring(saltPos);
    }

    public boolean comparePassword(String hash, String password) throws NoSuchAlgorithmException {
        int saltPos = (password.length() >= hash.length() ? hash.length() - 1 : password.length());
        String salt = hash.substring(saltPos, saltPos + 12);
        return hash.equals(getHash(password, salt));
    }

    public static String getWhirlpool(String message) {
        WHIRLPOOL w = new WHIRLPOOL();
        byte[] digest = new byte[WHIRLPOOL.DIGESTBYTES];
        w.NESSIEinit();
        w.NESSIEadd(message);
        w.NESSIEfinalize(digest);
        return WHIRLPOOL.display(digest);
    }

}
