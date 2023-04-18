package com.group_1.account.utilities;

/**
 * com.group_1.account.utilities
 * Created by NhatLinh - 19127652
 * Date 4/18/2023 - 1:58 PM
 * Description: ...
 */
public class MemoryUtilities {

    private static final int unit = 1024;
    public static long gbToKb(long gb)
    {
        return gb * unit * unit;
    }
}
