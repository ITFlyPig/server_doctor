package com.wyl.doctor.upload.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.wyl.doctor.bean.Codes;
import com.wyl.doctor.bean.Response;
import com.wyl.doctor.utils.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/uplaod")
public class UploadController {
    //配置文件的配置的
    @Value("${spring.servlet.multipart.location}")
    private String fileTempPath;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response save(@RequestParam("file")MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Resp.create().code(Codes.SUCCESS).message("文件内容为空").done();
        }
        String fileName = file.getOriginalFilename();
        String rawFileName = StrUtil.subBefore(fileName, ".", true);
        String fileType = StrUtil.subAfter(fileName, ".", true);
        String localFilePath = StrUtil.appendIfMissing(fileTempPath, "/") + rawFileName + "-" + DateUtil.current(false) + "." + fileType;
        try {
            file.transferTo(new File(localFilePath));
        } catch (IOException e) {
            return Resp.create().code(Codes.SUCCESS).message("文件上传失败").done();
        }
        return Resp.create().code(Codes.SUCCESS).message("文件上传成功").done();
    }

}
