package com.ai.resume.service.impl;

import com.ai.resume.config.properties.AlgorithmPathProperties;
import com.ai.resume.entity.ResumePO;
import com.ai.resume.exception.CommonException;
import com.ai.resume.mapper.ResumeMapper;
import com.ai.resume.service.ResumeService;
import com.ai.resume.utils.MinioUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 简历表 服务实现类
 * </p>
 *
 * @author ws
 * @since 2026-04-18
 */
@Slf4j
@Service
public class ResumeServiceImpl extends ServiceImpl<ResumeMapper, ResumePO> implements ResumeService {

    private static final List<String> FILE_TYPE = Arrays.asList("pdf", "docx", "doc");

    private static final String SLASH = "/";

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    @Qualifier("algoExecutor")
    private ThreadPoolTaskExecutor algoExecutor;

    @Autowired
    private AlgorithmPathProperties pathProperties;

    @Override
    public Boolean upload(MultipartFile file) {
        long start = System.currentTimeMillis();
        if (file == null) {
            throw new CommonException("请上传简历文件");
        }
        String filename = file.getOriginalFilename();
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);

        if (!FILE_TYPE.contains(fileType)) {
            throw new CommonException("不支持的文件类型！");
        }
        long size = file.getSize();

        long end1 = System.currentTimeMillis();

        // minio文件路径
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String objectName = datePath + SLASH + UUID.randomUUID() + ".pdf";
        long end2 = System.currentTimeMillis();
        // 上传到minio
        String fileUrl = minioUtil.uploadFile(file, objectName);
        long end3 = System.currentTimeMillis();
        // 保存
        ResumePO po = new ResumePO();
        po.setUserId(1L);
        po.setFileName(filename);
        po.setFileSize(size);
        po.setFileType(fileType);
        po.setFileUrl(fileUrl);
        po.setParseStatus(1);
        this.save(po);
        long end4 = System.currentTimeMillis();
        String mainPath = pathProperties.getMainPath();
        // 调用算法
        algoExecutor.submit(() -> {
            StringBuilder builder = new StringBuilder();
            builder.append(mainPath).append(" ")
                    .append("--resume_id").append(" ").append(po.getId()).append(" ")
                    .append("--user_id").append(" ").append(po.getUserId()).append(" ")
                    .append("--resume_loc").append(" ")
                    .append("\"").append(po.getFileUrl()).append("\"");
            System.out.println(builder.toString());
        });
        long end5 = System.currentTimeMillis();

        System.out.println("end1: " + (end1 - start));
        System.out.println("end2: " + (end2 - start));
        System.out.println("end3: " + (end3 - start));
        System.out.println("end4: " + (end4 - start));
        System.out.println("end5: " + (end5 - start));
        return Boolean.TRUE;
    }

    private void execPython(String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", command);
            pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            pb.redirectError(ProcessBuilder.Redirect.DISCARD);
            pb.start();
        } catch (Exception e) {
            log.error("执行python算法命令报错，错误原因：{}", e.getMessage());
        }
    }
}
