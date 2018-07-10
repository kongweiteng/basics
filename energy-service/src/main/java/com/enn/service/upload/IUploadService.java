package com.enn.service.upload;

import com.enn.vo.energy.business.upload.UploadResp;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@FeignClient(value = "ENNBOSS-PROVIDER-COMMON", configuration = IUploadService.MultipartSupportConfig.class)
public interface IUploadService {

    /**
     * 文件上传
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    UploadResp singleFileUpload(MultipartFile file);

    @Configuration
    class MultipartSupportConfig {
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upObj")
    UploadResp upObj(@RequestBody Map bt, @RequestParam("ext") String ext);
}
