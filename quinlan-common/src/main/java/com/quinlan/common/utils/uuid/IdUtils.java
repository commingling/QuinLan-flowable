package com.quinlan.common.utils.uuid;


/**
 * ID生成器工具类
 */
public class IdUtils {

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }




}
