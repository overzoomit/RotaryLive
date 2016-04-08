package it.stasbranger.rotarylive.controller;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import it.stasbranger.rotarylive.dao.AttachRepository;
import it.stasbranger.rotarylive.domain.Attach;
import it.stasbranger.rotarylive.service.utility.UtilityService;

@RestController
@RequestMapping("/api/attach")
@ExposesResourceFor(Attach.class)
public class AttachController {

	@Autowired private AttachRepository attachRepository;
	@Autowired UtilityService utilityService;
	
	@InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
	
	@RequestMapping(value = { "/file/{code}", "/image/{code}", "/thumbnail/{code}" }, method = RequestMethod.GET)
    public HttpEntity<Resources<Attach>> showFile(@PathVariable("code") ObjectId code, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String uri = request.getRequestURI();
        try {
            Attach attach = attachRepository.findOne(code);
            String pathFile = attach.getPathFile();
            if(uri.contains("/thumbnail/")) {
            	pathFile = attach.getPathFile();
            	String ext = FilenameUtils.getExtension(pathFile);
            	pathFile = pathFile.replace("."+ext, "_thumbnail") + "." + ext;
            }
            Path path = Paths.get(pathFile);
            byte[] image = null;
            try {
                image = Files.readAllBytes(path);
            } catch (NoSuchFileException nsfe) {
                nsfe.printStackTrace();
            }
            if (image != null) {
                try {
                    String originalFilename = attach.getOriginalFilename() != null ? attach.getOriginalFilename() : path.getFileName().toString();
                    response.setContentType(attach.getContentType());
                    response.setHeader("Content-Disposition", "filename=\"" + originalFilename + "\"");
                    OutputStream out = response.getOutputStream();
                    IOUtils.copy(new ByteArrayInputStream(image), out);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new ResponseEntity<Resources<Attach>>(HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<Resources<Attach>>(HttpStatus.NOT_FOUND);
        }
    }
}
