package com.dena.platform.common.utils;

import java.util.UUID;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */

public abstract class UUIDGenerator {
    // todo use better algorithm for generating random uuid
    public static UUID createNewUUID() {
        return UUID.randomUUID();
    }

}
