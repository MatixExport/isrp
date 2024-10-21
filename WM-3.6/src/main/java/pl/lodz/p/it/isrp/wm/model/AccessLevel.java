package pl.lodz.p.it.isrp.wm.model;

public enum AccessLevel {

    ACCOUNT(AccessLevelKeys.ACCOUNT),
    NEWREGISTERED(AccessLevelKeys.NEWREGISTERED),
    OFFICE(AccessLevelKeys.OFFICE),
    WAREHOUSE(AccessLevelKeys.WAREHOUSE),
    ADMINISTRATION(AccessLevelKeys.ADMINISTRATION);

    private AccessLevel(final String key) {
        this.accessLevelKey = key;
    }
    private String accessLevelKey;
    private String accessLevelI18NValue;

    public String getAccessLevelKey() {
        return accessLevelKey;
    }

    public String getAccessLevelI18NValue() {
        return accessLevelI18NValue;
    }

    public void setAccessLevelI18NValue(String accessLevelI18NValue) {
        this.accessLevelI18NValue = accessLevelI18NValue;
    }

    public static class AccessLevelKeys {

        public static final String NEWREGISTERED = "access.level.newregistered";
        public static final String ACCOUNT = "access.level.account";
        public static final String ADMINISTRATION = "access.level.administration";
        public static final String OFFICE = "access.level.office";
        public static final String WAREHOUSE = "access.level.warehouse";
    }
}
