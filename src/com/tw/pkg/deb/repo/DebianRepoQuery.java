package com.tw.pkg.deb.repo;

import com.tw.pkg.deb.db.PackageDAO;

import java.io.File;
import java.util.List;

public class DebianRepoQuery {
    private String packagesZipURL;
    private String rootFolder;
    private String databaseFilePath;
    private PackageDAO packageDAO;

    public DebianRepoQuery(String packagesZipURL) {
        this.packagesZipURL = packagesZipURL;
        this.rootFolder = System.getProperty("java.io.tmpdir") + packagesZipURL.hashCode();
        System.out.println("root dir: " + rootFolder);
        this.databaseFilePath = rootFolder + File.separator + "cache.db";
        this.packageDAO = new PackageDAO(databaseFilePath);
    }

    public void updateCacheWithUpstreamData() throws Exception {
        DebianRepository debianRepository = new DebianRepository(packagesZipURL, rootFolder);

        if (!debianRepository.isRepositoryValid()) {
            System.out.println("invalid repository!");
            System.exit(0);
        }

        if (debianRepository.hasChanges()) {
            System.out.println("repository has changes. updating cache...");

            List<DebianPackage> allPackages = debianRepository.getAllPackages();
            int packageCount = allPackages.size();
            System.out.println("repository packages count: " + packageCount);

            packageDAO.createTableIfNotExists();
            if (packageDAO.getPackageCount() > 0) {
                System.out.println("cache found. updating cache with new data...");
                packageDAO.updateIfRequired(allPackages);
            } else {
                System.out.println("no cache found. populating all data...");
                int i = 0;
                for (DebianPackage currentPackage : allPackages) {
                    packageDAO.insert(currentPackage);
                    i++;
                    if (i % 1000 == 0) {
                        System.out.println("processed: " + i + " of " + packageCount);
                    }
                }
                System.out.println("finished processing all packages...");
            }
        } else {
            System.out.println("repository has not changed since last run...");
        }
    }

    public List<DebianPackage> getDebianPackagesFor(String packageName, String versionSpec, String architecture) throws Exception {
        return packageDAO.getPackagesBy_Name_Version_Architecture(packageName, versionSpec, architecture);
    }
}
