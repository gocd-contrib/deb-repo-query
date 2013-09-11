package com.tw.pkg.deb.repo;

import com.tw.pkg.deb.db.PackageDAO;
import com.tw.pkg.deb.helper.IOHelper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DebianRepoQuery {
    private String rootDirectory;
    private String lockFilePath;
    private DebianRepository debianRepository;
    private PackageDAO packageDAO;

    public DebianRepoQuery(String packagesZipURL) throws Exception {
        String tempDir = System.getProperty("java.io.tmpdir");
        String debRepoQueryDirPath = null;
        if (tempDir.endsWith("/"))
            debRepoQueryDirPath = tempDir + "deb-repo-query";
        else
            debRepoQueryDirPath = tempDir + File.separator + "deb-repo-query";
        this.rootDirectory = debRepoQueryDirPath + File.separator + packagesZipURL.hashCode();
        this.lockFilePath = this.rootDirectory + File.separator + "filelock";
        String databaseFilePath = this.rootDirectory + File.separator + "cache.db";

        new File(this.rootDirectory).mkdirs();
        File lockFile = new File(this.lockFilePath);
        if (!lockFile.exists()) {
            FileUtils.touch(lockFile);
        }

        this.debianRepository = new DebianRepository(packagesZipURL, this.rootDirectory);
        this.packageDAO = new PackageDAO(databaseFilePath);
    }

    PackageDAO getPackageDAO() {
        return packageDAO;
    }

    public void updateCacheIfRequired() throws Exception {
        if (!debianRepository.isRepositoryValid()) {
            new RuntimeException("invalid repository!");
        }

        FileLock lock = null;
        try {
            lock = IOHelper.getLockOnFile(lockFilePath);

            if (debianRepository.hasChanges()) {
                updateCache();
            } else {
            }
        } finally {
            IOHelper.releaseFileLock(lock);
        }
    }

    private void updateCache() throws Exception {
        List<DebianPackage> allPackages = debianRepository.getAllPackages();
        int packageCount = allPackages.size();

        packageDAO.createTableIfNotExists();
        if (packageDAO.getPackageCount() > 0) {
            packageDAO.updateIfRequired(allPackages);
        } else {
            for (int i = 0; i < allPackages.size(); i++) {
                DebianPackage currentPackage = allPackages.get(i);
                packageDAO.insert(currentPackage);
            }
        }
    }

    public List<DebianPackage> getDebianPackagesFor(String packageName, String versionSpec, String architecture) throws Exception {
        List<DebianPackage> packages = null;
        FileLock lock = null;
        try {
            lock = IOHelper.getLockOnFile(lockFilePath);

            packages = packageDAO.getPackagesBy_Name_Version_Architecture(packageName, versionSpec, architecture);
        } finally {
            IOHelper.releaseFileLock(lock);
        }

        if (packages != null && packages.size() > 1) {
            checkForInconsistentRevisions(packages);
            sortPackagesOnVersion(packages);
        }
        return packages;
    }

    private void checkForInconsistentRevisions(List<DebianPackage> packages) {
        DebianPackage referencePackage = packages.get(0);
        String referencePackageName = referencePackage.getName();
        String referencePackageArchitecture = referencePackage.getArchitecture();
        List<String> packagesFound = new ArrayList<String>();
        packagesFound.add(referencePackage.getName() + " - " + referencePackage.getArchitecture());

        for (int i = 1; i < packages.size(); i++) {
            DebianPackage currentPackage = packages.get(i);
            if (!currentPackage.getName().equals(referencePackageName) || !currentPackage.getArchitecture().equals(referencePackageArchitecture)) {
                packagesFound.add(currentPackage.getName() + " - " + currentPackage.getArchitecture());
            }
        }
        if (packagesFound.size() > 1) {
            throw new RuntimeException("multiple package found: " + join(packagesFound));
        }
    }

    private String join(List<String> packagesFound) {
        String line = "";
        for (String current : packagesFound) {
            if (!line.trim().isEmpty()) {
                line += ", ";
            }
            line += current;
        }
        return line;
    }

    private void sortPackagesOnVersion(List<DebianPackage> packages) {
        for (DebianPackage current : packages) {
            current.parseVersion();
        }
        Collections.sort(packages);
        Collections.reverse(packages);
    }
}
