package com.quinlan.web.controller.common;

import com.quinlan.common.config.QuinLanConfig;
import com.quinlan.common.constant.Constants;
import com.quinlan.common.core.domain.AjaxResult;
import com.quinlan.common.core.redis.RedisCache;
import com.quinlan.common.utils.sign.Base64;
import com.quinlan.common.utils.uuid.IdUtils;
import com.quinlan.system.service.ISysConfigService;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import com.quinlan.common.constant.CacheConstants;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import com.google.code.kaptcha.Producer;
import java.util.concurrent.TimeUnit;

@RestController
public class CaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ISysConfigService configService;

    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response){

        AjaxResult ajax = AjaxResult.success();
        //查询 是否 开启了, 验证码  获取验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();

        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled) {
            return ajax;
        }
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String capStr = null;
        String code = null;
        BufferedImage image = null;
        // 生成验证码
        String captchaType = QuinLanConfig.getCaptchaType();
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }
        redisCache.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));

        return ajax;
    }

}
