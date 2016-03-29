package it.stasbranger.rotarylive.service.utility;

import java.io.IOException;
import java.io.InputStream;

public interface ImageService {

	public byte[] getThumbnail(InputStream inputStream, String contentType, String rotation) throws IOException;
	
	/**
	 * 
	 * @param inputStream
	 * @param contentType
	 * @param targetSize
	 * @param rotation è right o left per far girare la foto di 90 gradi o di 270 
	 * se viene passata qualsiasi altra stringa o null non effettua nessuna rotazione
	 * @return il byte array della foto
	 * @throws IOException
	 */
	public byte[] prepareToScale(InputStream inputStream, String contentType, Integer targetSize, String rotation) throws IOException;

	/**
	 * Dato il content type in input estrapola la vera estensione da dare al file.
	 * @param contentType 
	 * @author flavio
	 */
	public String getExtension(String contentType);
}
