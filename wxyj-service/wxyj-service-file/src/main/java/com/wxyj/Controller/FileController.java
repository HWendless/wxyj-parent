package com.wxyj.Controller;

import com.wxyj.file.FastDFSFile;
import com.wxyj.util.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin
@RequestMapping( value = "/upload")
public class FileController {
    /***
     * 文件上传
     * @return
     */
    @PostMapping
    public Result upload(@RequestParam("file")MultipartFile file) throws Exception {
        //封装一个FastDFSFile
        FastDFSFile fastDFSFile = new FastDFSFile(
                file.getOriginalFilename(), //文件名字
                file.getBytes(),            //文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename()));//文件扩展名

        //文件上传
        String [] uploads = FastDFSUtil.upload(fastDFSFile);
        //组装文件上传地址 192.168.60.204:8080/+
        return new Result(true, StatusCode.OK,"192.168.60.204:8080/"+uploads[0]+"/"+uploads[1]);
    }
}
