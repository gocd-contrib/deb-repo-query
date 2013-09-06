package com.tw.pkg.deb.repo;

import com.tw.pkg.deb.db.PackageDAO;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class DebianRepoQuery {
    private DebianRepository debianRepository;
    private PackageDAO packageDAO;

    public DebianRepoQuery(String packagesZipURL) throws Exception {
        String rootFolder = System.getProperty("java.io.tmpdir") + packagesZipURL.hashCode();
        System.out.println("root dir: " + rootFolder);
        String databaseFilePath = rootFolder + File.separator + "cache.db";

        this.debianRepository = new DebianRepository(packagesZipURL, rootFolder);
        this.packageDAO = new PackageDAO(databaseFilePath);
    }

    PackageDAO getPackageDAO() {
        return packageDAO;
    }

    public void updateCacheWithUpstreamData() throws Exception {
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
                for (int i = 0; i < allPackages.size(); i++) {
                    DebianPackage currentPackage = allPackages.get(i);
                    packageDAO.insert(currentPackage);

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
        List<DebianPackage> packages = packageDAO.getPackagesBy_Name_Version_Architecture(packageName, versionSpec, architecture);
        if (!packages.isEmpty()) {
            for (DebianPackage current : packages) {
                current.parseVersion();
            }

            Collections.sort(packages);
            Collections.reverse(packages);
        }
        return packages;
    }
}
