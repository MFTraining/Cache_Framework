package com.cipher.generatekeys;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.cipher.CipherAlgo;

public class GenerateKeys implements CipherAlgo {

	private KeyPairGenerator keyGen;
	private KeyPair pair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		this.keyGen.initialize(keylength);
	}

	public void createKeys() {
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public void writeToFile(String path, byte[] key) throws IOException {
		File f = new File(path);
		//f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}

	public static void main(String[] args) {
		GenerateKeys gk;
		try {
			gk = new GenerateKeys(1024);
			gk.createKeys();
			gk.writeToFile("publicKey", gk.getPublicKey().getEncoded());
			gk.writeToFile("privateKey", gk.getPrivateKey().getEncoded());
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
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