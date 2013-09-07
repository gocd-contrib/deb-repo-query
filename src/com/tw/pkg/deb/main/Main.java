package com.tw.pkg.deb.main;

import com.tw.pkg.deb.repo.DebianPackage;
import com.tw.pkg.deb.repo.DebianRepoQuery;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("usage: java -jar deb-repo-query-with-dependencies.jar <uri> [<version-spec>] [<architecture>]");
            System.out.println("sample: java -jar deb-repo-query-with-dependencies.jar http://in.archive.ubuntu.com/ubuntu/dists/hardy/main/binary-amd64 gcc [4.4%] [amd64]");
            System.exit(0);
        }

        String packagesZipURL = null;
        if (args[0].endsWith("/")) {
            packagesZipURL = args[0] + "Packages.gz";
        } else {
            packagesZipURL = args[0] + "/Packages.gz";
        }
        String packageName = args[1];
        String versionSpec = null;
        if (args.length > 2) {
            versionSpec = args[2];
        }
        String architecture = null;
        if (args.length > 3) {
            architecture = args[3];
        }

        DebianRepoQuery debianRepoQuery = new DebianRepoQuery(packagesZipURL);
        debianRepoQuery.updateCacheIfRequired();
        List<DebianPackage> debianPackagesForSpec = debianRepoQuery.getDebianPackagesFor(packageName, versionSpec, architecture);
        for (DebianPackage currentPackage : debianPackagesForSpec) {
            System.out.println(currentPackage.getName() + " - " + currentPackage.getVersion());
        }
    }
}
