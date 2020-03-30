package com.Hello.controller;

import com.Hello.dto.FileDTO;
import com.Hello.provider.UcloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {


    @Autowired
    private UcloudProvider ucloudProvider;


    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            ucloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("IO error!!!!!");
        }
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl("/images/aimyon.jpg");
        return fileDTO;
    }

}
