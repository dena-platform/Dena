package com.dena.platform.core.feature.app.domain;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DenaApplication {

    public final static String CREATOR_ID_FIELD = "creator_id";

    public final static String APP_NAME_FIELD = "application_name";

    public final static String APP_ID_FIELD = "application_id";

    public final static String SECRET_KEY_FIELD = "secret_key";

    private String creatorId;

    private String applicationName;

    private String appId;

    private String secretKey;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId.trim();
    }

    public static final class DenaAPPBuilder {
        private DenaApplication denaApplication;

        private DenaAPPBuilder() {
            denaApplication = new DenaApplication();
        }

        public static DenaAPPBuilder aDenaAPP() {
            return new DenaAPPBuilder();
        }

        public DenaAPPBuilder withApplicationName(String appName) {
            denaApplication.setApplicationName(appName);
            return this;
        }

        public DenaAPPBuilder withApplicationId(String appId) {
            denaApplication.setAppId(appId);
            return this;
        }

        public DenaAPPBuilder withSecretKey(String secretKey) {
            denaApplication.setSecretKey(secretKey);
            return this;
        }

        public DenaApplication build() {
            return denaApplication;
        }
    }
}
