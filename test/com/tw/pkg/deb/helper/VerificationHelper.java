package com.tw.pkg.deb.helper;

import org.junit.Assert;

import com.tw.pkg.deb.repo.DebianPackage;

public class VerificationHelper {
    public static DebianPackage getDebianPackage(int i) {
        DebianPackage debPkg = new DebianPackage();
        debPkg.setName("name_" + i);
        debPkg.setPriority("priority_" + i);
        debPkg.setSection("section_" + i);
        debPkg.setInstalledSize("installedSize_" + i);
        debPkg.setMaintainer("maintainer_" + i);
        debPkg.setArchitecture("architecture_" + i);
        debPkg.setVersion("version_" + i);
        debPkg.setReplaces("replaces_" + i);
        debPkg.setConflicts("conflicts_" + i);
        debPkg.setFilename("filename_" + i);
        debPkg.setSize("size_" + i);
        debPkg.setMd5sum("md5sum_" + i);
        debPkg.setSha1("sha1_" + i);
        debPkg.setSha256("sha256_" + i);
        return debPkg;
    }

    public static void assertCorrectnessOfDebianPackage(DebianPackage debPkg, int i) {
        Assert.assertEquals("name_" + i, debPkg.getName());
        assertCorrectnessOfNonPrimaryData(debPkg, i);
    }

    public static void assertCorrectnessOfNonPrimaryData(DebianPackage debPkg, int i) {
        Assert.assertEquals("priority_" + i, debPkg.getPriority());
        Assert.assertEquals("section_" + i, debPkg.getSection());
        Assert.assertEquals("installedSize_" + i, debPkg.getInstalledSize());
        Assert.assertEquals("maintainer_" + i, debPkg.getMaintainer());
        Assert.assertEquals("architecture_" + i, debPkg.getArchitecture());
        Assert.assertEquals("version_" + i, debPkg.getVersion());
        Assert.assertEquals("replaces_" + i, debPkg.getReplaces());
        Assert.assertEquals("conflicts_" + i, debPkg.getConflicts());
        Assert.assertEquals("filename_" + i, debPkg.getFilename());
        Assert.assertEquals("size_" + i, debPkg.getSize());
        Assert.assertEquals("md5sum_" + i, debPkg.getMd5sum());
        Assert.assertEquals("sha1_" + i, debPkg.getSha1());
        Assert.assertEquals("sha256_" + i, debPkg.getSha256());
    }
}
