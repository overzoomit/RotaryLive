package it.stasbranger.rotarylive.service.utility;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.imgscalr.Scalr.Rotation;
import org.springframework.stereotype.Service;

@Service("ImageService")
public class ImageServiceImpl implements ImageService {

	public byte[] getThumbnail(InputStream inputStream, String contentType, String rotation) throws IOException {
		try{
			String ext = contentType.replace("image/", "").equals("jpeg")? "jpg":contentType.replace("image/", "");

			BufferedImage bufferedImage = readImage(inputStream);	
			BufferedImage thumbImg = Scalr.resize(bufferedImage, Method.QUALITY,Mode.AUTOMATIC, 
					100,
					100, Scalr.OP_ANTIALIAS);
			//convert bufferedImage to outpurstream 
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(thumbImg,ext,baos);
			baos.flush();

			return baos.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

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
	public byte[] prepareToScale(InputStream inputStream, String contentType, Integer targetSize, String rotation) throws IOException{
		try {	
			BufferedImage bufferedImage;
			bufferedImage = ImageIO.read(inputStream);

			if(bufferedImage.getHeight() > targetSize || bufferedImage.getWidth() > targetSize){
				bufferedImage = Scalr.resize(bufferedImage, targetSize);

			}
			if(rotation != null && rotation.equals("left")){
				bufferedImage = Scalr.rotate(bufferedImage, Rotation.CW_270);
			}else if(rotation != null && rotation.equals("right")){
				bufferedImage = Scalr.rotate(bufferedImage, Rotation.CW_90);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, contentType, baos);
			baos.flush();

			return baos.toByteArray();

		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
	}

	private BufferedImage readImage(InputStream picture) throws IOException {
		try{
			BufferedImage bufferedImage = ImageIO.read(picture);
			return bufferedImage;
		} catch(Exception e){
			//Find a suitable ImageReader
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("JPEG");
			while (readers.hasNext()) {
				System.out.println("reader: " + readers.next());
			}
			ImageReader reader = null;
			while(readers.hasNext()) {
				reader = (ImageReader)readers.next();
				if(reader.canReadRaster()) {
					break;
				}
			}

			//Stream the image file (the original CMYK image)
			ImageInputStream input = ImageIO.createImageInputStream(picture); 
			reader.setInput(input); 

			//Read the image raster
			Raster raster = reader.readRaster(0, null); 

			//Create a new RGB image
			BufferedImage bi = new BufferedImage(raster.getWidth(), raster.getHeight(), 
					BufferedImage.TYPE_INT_RGB); 

			//Fill the new image with the old raster
			bi.getRaster().setRect(raster);
			return bi;
		}
	}	

	/**
	 * Dato il content type in input estrapola la vera estensione da dare al file.
	 * @param contentType 
	 * @author flavio
	 */
	public String getExtension(String contentType) {
		try{
			MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
			MimeType mime = allTypes.forName(contentType);
			return mime.getExtension();
		}catch(Exception e){
			return "";
		}
	}
}
