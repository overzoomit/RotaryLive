package it.stasbranger.rotarylive.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.stasbranger.rotarylive.dao.AttachRepository;
import it.stasbranger.rotarylive.domain.Attach;
import it.stasbranger.rotarylive.service.utility.ImageService;
import it.stasbranger.rotarylive.service.utility.UtilityService;

@PropertySource("classpath:setting.properties")
@Service("AttachService")
public class AttachServiceImpl implements AttachService {

	@Autowired UtilityService utilityService;
	@Autowired ImageService imageService;
	@Autowired AttachRepository attachRepository;

	@Value("${setting.base.path.storage}")
	private String basePathStorage;

	@Value("${setting.path.storage.image}")
	private String pathStorageImage;
	
	public void create(Attach attach){
		attachRepository.save(attach);
	}

	public Attach update(Attach attach){
		return attachRepository.save(attach);
	}
	
	public void delete(Attach attach){
		attachRepository.delete(attach);
	}
	
	public void delete(String id){
		attachRepository.delete(id);
	}
	
	public Attach findOne(String id){
		return attachRepository.findOne(id);
	}
	
	public List<Attach> findAll(){
		return attachRepository.findAll();
	}
	
	public Page<Attach> findAll(Pageable pageable){
		return attachRepository.findAll(pageable);
	}
	
	public void deleteAttach(Attach attach) throws IOException {
		String pathFile = attach.getPathFile();

		File file = new File(pathFile);
		if(file!=null) file.delete();

		String ext = FilenameUtils.getExtension(pathFile);
		String pathThumb = pathFile.replace("."+ext, "_thumbnail") + "." + ext;
		File thumbnail = new File(pathThumb);
		if(thumbnail!=null) thumbnail.delete();

		attachRepository.delete(attach);
	}

	public Attach createAttach(MultipartFile file, String type) throws IOException {
		
		String contentType = file.getContentType();
		byte[] bfile = file.getBytes();
		
		Attach attach = new Attach();
		attach.setTipo(type);
		attach.setContentType(contentType);

		InputStream imputStream = new ByteArrayInputStream(bfile);
		String pathtype;
		if(contentType.startsWith("image/")) pathtype = "images";
		else pathtype = "files";

		File theDir = new File(basePathStorage + pathtype);
		if (!theDir.exists()) theDir.mkdir();

		// genero il timestamp
		Calendar cal = Calendar.getInstance();
		String nameFile = ""+cal.getTimeInMillis();
		String path = "";
		byte[] file2write;
		if(type.startsWith("IMAGE")){
			// ridimensiono immagine
			String ext = contentType.replace("image/", "").equals("jpeg")? "jpg":contentType.replace("image/", "");
			file2write = imageService.prepareToScale(imputStream, ext, 806, "0");
			path = basePathStorage+pathtype+"/" + nameFile + "." + ext;

			// set thumbnail
			thumbnail(file, nameFile);
		}else if(type.startsWith("CROP")){	
			// non ridimensiono immagine
			file2write = bfile;
			String ext = contentType.replace("image/", "").equals("jpeg")? "jpg":contentType.replace("image/", "");
			path = basePathStorage+pathtype+"/" + nameFile + "." + ext;

			// set thumbnail
			thumbnail(file, nameFile);
		}else{
			file2write = bfile;
			String ext = imageService.getExtension(contentType);
			path = basePathStorage+pathtype+"/" + nameFile + "." + ext;
		}

		FileOutputStream fos = new FileOutputStream(path);
		fos.write(file2write);
		fos.close();

		attach.setPathFile(path);
		attach.setOriginalFilename(file.getOriginalFilename());
		attach = attachRepository.save(attach);
		String code = utilityService.encodeID(attach.getId());
		attach.setUriCode(code);

		return attachRepository.save(attach);
	}

	private String thumbnail(MultipartFile file, String nameFile) throws IOException {
		InputStream imputStream = new ByteArrayInputStream(file.getBytes());
		String ext = file.getContentType().replace("image/", "").equals("jpeg")? "jpg":file.getContentType().replace("image/", "");

		
		byte[] thumbnail = imageService.getThumbnail(imputStream, file.getContentType(), "0");
		String path = pathStorageImage  + nameFile + "_thumbnail." + ext;
		FileOutputStream fosThumb = new FileOutputStream(path);
		fosThumb.write(thumbnail);
		fosThumb.close();

		return path;
	}
}
