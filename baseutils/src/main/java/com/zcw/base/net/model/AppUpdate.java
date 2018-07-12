package com.zcw.base.net.model;

/**
 * Created by 朱城委 on 2017/12/27.<br><br>
 *
 * 应用更新信息对象
 */
public class AppUpdate {
    /** 请求结果 */
    private String result;

    /** 客户端平台（Android、IOS） */
    private String platform;

    /** 版本修改时间 */
    private String modifiedAt;

    /** （未知此字段用途） */
    private String bundle;

    /** 版本说明 */
    private String releaseNote;

    /** 版本构建号 */
    private int build;

    /** 版本号 */
    private String version;

    /** 应用包名 */
    private String identifier;

    /** 是否强制更新 */
    private boolean forceUpdateEnabled;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isForceUpdateEnabled() {
        return forceUpdateEnabled;
    }

    public void setForceUpdateEnabled(boolean forceUpdateEnabled) {
        this.forceUpdateEnabled = forceUpdateEnabled;
    }
}
