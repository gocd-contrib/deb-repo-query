package com.tw.pkg.deb.repo;

import com.tw.pkg.deb.helper.VerificationHelper;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DebianRepositoryTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldCheckDebainReporitoryValidityCorrectly() throws Exception {
        DebianRepository debianRepository1 = new DebianRepository("http://archive.ubuntu.com/ubuntu/dists/xenial/main/binary-amd64/Packages.gz", "/tmp/getPackagesForQuery");
        assertEquals(true, debianRepository1.isRepositoryValid());

        DebianRepository debianRepository2 = new DebianRepository("http://archive.ubuntu.com/ubuntu/dists/INVALID/main/binary-amd64/Packages.gz", "/tmp/getPackagesForQuery");
        assertEquals(false, debianRepository2.isRepositoryValid());
    }

    @Test
    public void shouldTellIfRepositoryHasChangesCorrectly() throws Exception {
        DebianRepository debianRepository = new DebianRepository("http://archive.ubuntu.com/ubuntu/dists/xenial/main/binary-amd64/Packages.gz", "/tmp/getPackagesForQuery");
        debianRepository.clearDownloadFolder();
        FileUtils.deleteQuietly(new File(debianRepository.getLastKnowDateStoreFilePath()));
        debianRepository.setKnownDate(0L);
        assertEquals(true, debianRepository.hasChanges());
        assertEquals(false, debianRepository.hasChanges());
    }

    @Ignore
    @Test
    public void shouldFetchPackageDataCorrectly() throws Exception {
        DebianRepository debianRepository = new DebianRepository("http://archive.ubuntu.com/ubuntu/dists/xenial/main/binary-amd64/Packages.gz", "/tmp/getPackagesForQuery");
        List<DebianPackage> allPackages = debianRepository.getAllPackages();
        assertEquals(5800, allPackages.size());
    }

    @Test
    public void shouldFetchPackageDataForFileBasedRepositories() throws Exception {
        String packagesZipFile = "file://" + new File(getClass().getResource("/test-data/samplerepo/Packages.gz").toURI()).getAbsolutePath();
        System.err.println(temporaryFolder);
        String absolutePath = temporaryFolder.getRoot().getAbsolutePath();
        List<DebianPackage> packages = new DebianRepository(packagesZipFile, absolutePath).getAllPackages();

        assertEquals(2, packages.size());
        assertEquals("name_1", packages.get(0).getName());
        assertEquals("name_2", packages.get(1).getName());
    }

    @Test
    public void shouldReadPackageDataCorrectly() throws Exception {
        DebianRepository debianRepository = new DebianRepository(null, "/tmp/getPackagesForQuery");
        List<DebianPackage> debianPackages = new ArrayList<DebianPackage>();
        debianRepository.readData(debianPackages, new BufferedReader(new FileReader(new File(getClass().getResource("/test-data/data1.txt").toURI()))));
        VerificationHelper.assertCorrectnessOfDebianPackage(debianPackages.get(0), 1);
        VerificationHelper.assertCorrectnessOfDebianPackage(debianPackages.get(1), 2);
    }

    @Test
    public void shouldReadPackageDataWithMultilineDescriptionCorrectly() throws Exception {
        DebianRepository debianRepository = new DebianRepository(null, "/tmp/getPackagesForQuery");
        List<DebianPackage> debianPackages = new ArrayList<DebianPackage>();
        debianRepository.readData(debianPackages, new BufferedReader(new FileReader(new File(getClass().getResource("/test-data/data2.txt").toURI()))));

        assertEquals("name_1", debianPackages.get(0).getName());

        assertEquals("name_2", debianPackages.get(1).getName());
        assertEquals("version_2", debianPackages.get(1).getVersion());
    }
}
