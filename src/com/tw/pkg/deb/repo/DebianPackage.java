package com.tw.pkg.deb.repo;

public class DebianPackage {
    private long id;
    private String name;
    private String version;
    private String architecture;
    private String filename;
    private String maintainer;
    private String size;
    private String installedSize;
    private String md5sum;
    private String sha1;
    private String sha256;
    private String replaces;
    private String conflicts;
    private String section;
    private String priority;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getArchitecture() {
        return architecture;
    }

    public String getFilename() {
        return filename;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public String getSize() {
        return size;
    }

    public String getInstalledSize() {
        return installedSize;
    }

    public String getMd5sum() {
        return md5sum;
    }

    public String getSha1() {
        return sha1;
    }

    public String getSha256() {
        return sha256;
    }

    public String getReplaces() {
        return replaces;
    }

    public String getConflicts() {
        return conflicts;
    }

    public String getSection() {
        return section;
    }

    public String getPriority() {
        return priority;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setMaintainer(String maintainer) {
        this.maintainer = maintainer;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setInstalledSize(String installedSize) {
        this.installedSize = installedSize;
    }

    public void setMd5sum(String md5sum) {
        this.md5sum = md5sum;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public void setReplaces(String replaces) {
        this.replaces = replaces;
    }

    public void setConflicts(String conflicts) {
        this.conflicts = conflicts;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
