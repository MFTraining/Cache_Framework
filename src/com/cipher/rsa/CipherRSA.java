package com.cipher.rsa;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.cipher.CipherAlgo;

import java.util.Base64;

public class CipherRSA implements CipherAlgo {
	private Cipher cipher;

	public CipherRSA() throws NoSuchAlgorithmException, NoSuchPaddingException {
		this.cipher = Cipher.getInstance("RSA");
	}

	// https://docs.oracle.com/javase/8/docs/api/java/security/spec/PKCS8EncodedKeySpec.html
	public PrivateKey getPrivate(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	// https://docs.oracle.com/javase/8/docs/api/java/security/spec/X509EncodedKeySpec.html
	public PublicKey getPublic(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

	public void encryptFile(byte[] input, File output, PrivateKey key)
		throws IOException, GeneralSecurityException {
		this.cipher.init(Cipher.ENCRYPT_MODE, key);
		writeToFile(output, this.cipher.doFinal(input));
	}

	public void decryptFile(byte[] input, File output, PublicKey key)
		throws IOException, GeneralSecurityException {
		this.cipher.init(Cipher.DECRYPT_MODE, key);
		writeToFile(output, this.cipher.doFinal(input));
	}

	private void writeToFile(File output, byte[] toWrite)
			throws IllegalBlockSizeException, BadPaddingException, IOException {
		FileOutputStream fos = new FileOutputStream(output);
		fos.write(toWrite);
		fos.flush();
		fos.close();
	}

	public String encrypt(String msg, PrivateKey key)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException {
		this.cipher.init(Cipher.ENCRYPT_MODE, key);
 		return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));

	}


	public String decrypt(String msg, PublicKey key)
			throws InvalidKeyException, UnsupportedEncodingException,
			IllegalBlockSizeException, BadPaddingException {
		this.cipher.init(Cipher.DECRYPT_MODE, key);
 		return new String(cipher.doFinal(Base64.getDecoder().decode(msg)), "UTF-8");
	}

	public byte[] getFileInBytes(File f) throws IOException {
		FileInputStream fis = new FileInputStream(f);
		byte[] fbytes = new byte[(int) f.length()];
		fis.read(fbytes);
		fis.close();
		return fbytes;
	}

	public static void main(String[] args) throws Exception {
		CipherRSA ac = new CipherRSA();
		PrivateKey privateKey = ac.getPrivate("privateKey");
		PublicKey publicKey = ac.getPublic("publicKey");

		String msg = "OurValue";
		String encrypted_msg = ac.encrypt(msg, privateKey);
		String decrypted_msg = ac.decrypt(encrypted_msg, publicKey);
		System.out.println("Original Message: " + msg +
			"\nEncrypted Message: " + encrypted_msg
			+ "\nDecrypted Message: " + decrypted_msg);

		if (new File("original.txt").exists()) {
			ac.encryptFile(ac.getFileInBytes(new File("original.txt")),
				new File("encrypted.txt"),privateKey);

			ac.decryptFile(ac.getFileInBytes(new File("encrypted.txt")),
				new File("decrypted.txt"), publicKey);
		} else {
			System.out.println("First Create a file original.txt under folder with content");
		}
	}

	@Override
	public String encrypt(String strToEncrypt, String secret_key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String decrypt(String strToDecrypt, String secret_key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object keyGenerate(String myKey) {
		// TODO Auto-generated method stub
		return null;
	}
}