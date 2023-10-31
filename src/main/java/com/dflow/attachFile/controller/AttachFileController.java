package com.dflow.attachFile.controller;

import com.dflow.attachFile.service.AttachFileService;
import com.dflow.entity.AttachFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/files")
public class AttachFileController {

    private final AttachFileService attachFileService;

    @Value("${resource.handlerPath}")
    private String resourceHandler;


    //이미지 출력
    @GetMapping("/images/{fileId}")
    @ResponseBody
    public Resource selImage(@PathVariable("fileId") Long id) throws IOException {
        return attachFileService.selFile(id);
    }

    //파일 다운로드
    @GetMapping("/download/{fileId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long id) throws IOException {
        return attachFileService.downloadBoardFile(id);
    }

    //에디터 이미지 업로드
    @PostMapping("/editor/upload")
    public ModelAndView editorUpload(@RequestParam Map<String, Object> map, MultipartHttpServletRequest request) throws Exception{
        ModelAndView mav = new ModelAndView("jsonView");

        // ckeditor 에서 파일을 보낼 때 upload : [파일] 형식으로 해서 넘어오기 때문에 upload라는 키의 밸류를 받아서 uploadFile에 저장
        MultipartFile uploadFile = request.getFile("upload");
        /*Long imgNo = attachFileService.insFile(uploadFile).getFileAttachNo();
        Resource imgUrlResource  = attachFileService.selFile(imgNo);
        String imageUrl = "/file/post/image/" + imgUrlResource.getFilename();*/

        String fileName = attachFileService.insFile(uploadFile).getFileSaveName();
        String imageUrl = resourceHandler + fileName;

        mav.addObject("uploaded",true);
        mav.addObject("url",imageUrl);

        return mav;
    }
}
