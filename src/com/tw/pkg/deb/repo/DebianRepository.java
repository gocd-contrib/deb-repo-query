package com.tw.pkg.deb.repo;

import com.tw.pkg.deb.helper.IOHelper;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DebianRepository {
    private String packagesZipURL;
    private String lastKnowDateStoreFilePath;
    private long knownDate;
    private String rootDirectory;
    private String downloadDirectory;
    private String packagesFilePath;
    private String unzippedPackagesFilePath;

    public DebianRepository(String packagesZipURL, String rootDirectory) throws Exception {
        super();
        this.packagesZipURL = packagesZipURL;
        this.rootDirectory = rootDirectory;

        this.downloadDirectory = this.rootDirectory + File.separator + "download";
        this.packagesFilePath = this.downloadDirectory + File.separator + "Packages.gz";
        this.unzippedPackagesFilePath = this.downloadDirectory + File.separator + "Packages";
        this.lastKnowDateStoreFilePath = this.downloadDirectory + File.separator + "last-known-modification-time";

        if (new File(this.lastKnowDateStoreFilePath).exists()) {
            String dateStr = FileUtils.readFileToString(new File(this.lastKnowDateStoreFilePath));
            this.knownDate = Long.parseLong(dateStr);
        }

        new File(this.rootDirectory).mkdirs();
        new File(this.downloadDirectory).mkdirs();
    }

    void setKnownDate(long knownDate) {
        this.knownDate = knownDate;
    }

    public boolean isRepositoryValid() {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = (HttpURLConnection) new URL(packagesZipURL).openConnection();
            connection.setRequestMethod("HEAD");
            return (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            throw new RuntimeException("could not check if repository is valid", e);
        }
    }

    public boolean hasChanges() {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = (HttpURLConnection) new URL(packagesZipURL).openConnection();
            long newDate = connection.getLastModified();

            if (newDate != knownDate) {
                knownDate = newDate;
                FileUtils.writeStringToFile(new File(this.lastKnowDateStoreFilePath), new Long(knownDate).toString());
                return true;
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException("could not check if repository has changes", e);
        }
    }

    void clearDownloadFolder() {
        FileUtils.deleteQuietly(new File(packagesFilePath));
        FileUtils.deleteQuietly(new File(unzippedPackagesFilePath));
    }

    private void downloadAndUnzipPackagesZip() throws Exception {
        clearDownloadFolder();

        System.out.println("downloading Packages.gz...");
        IOHelper.fetchFile(packagesZipURL, packagesFilePath);

        System.out.println("unzipping...");
        IOHelper.gunzip(packagesFilePath, unzippedPackagesFilePath);
    }

    public List<DebianPackage> getAllPackages() throws Exception {
        downloadAndUnzipPackagesZip();

        List<DebianPackage> packages = new ArrayList<DebianPackage>();

        BufferedReader reader = new BufferedReader(new FileReader(unzippedPackagesFilePath));

        readData(packages, reader);

        reader.close();

        return packages;
    }

    void readData(List<DebianPackage> packages, BufferedReader reader) throws IOException {
        System.out.println("reading data...");
        String line;
        DebianPackage debPkg = new DebianPackage();

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                packages.add(debPkg);
                debPkg = new DebianPackage();
            } else if (Character.isWhitespace(line.charAt(0))) {
                // do nothing
            } else {
                String parts[] = line.split(": ");
                String data = parts[0];
                String value = parts[1];

                if (data.equalsIgnoreCase("Package")) {
                    debPkg.setName(value);
                } else if (data.equalsIgnoreCase("Priority")) {
                    debPkg.setPriority(value);
                } else if (data.equalsIgnoreCase("Section")) {
                    debPkg.setSection(value);
                } else if (data.equalsIgnoreCase("Installed-Size")) {
                    debPkg.setInstalledSize(value);
                } else if (data.equalsIgnoreCase("Maintainer")) {
                    debPkg.setMaintainer(value);
                } else if (data.equalsIgnoreCase("Architecture")) {
                    debPkg.setArchitecture(value);
                } else if (data.equalsIgnoreCase("Version")) {
                    debPkg.setVersion(value);
                } else if (data.equalsIgnoreCase("Replaces")) {
                    debPkg.setReplaces(value);
                } else if (data.equalsIgnoreCase("Conflicts")) {
                    debPkg.setConflicts(value);
                } else if (data.equalsIgnoreCase("Filename")) {
                    debPkg.setFilename(value);
                } else if (data.equalsIgnoreCase("Size")) {
                    debPkg.setSize(value);
                } else if (data.equalsIgnoreCase("MD5sum")) {
                    debPkg.setMd5sum(value);
                } else if (data.equalsIgnoreCase("SHA1")) {
                    debPkg.setSha1(value);
                } else if (data.equalsIgnoreCase("SHA256")) {
                    debPkg.setSha256(value);
                }
            }
        }

        if (debPkg.getName() != null && !debPkg.getName().trim().isEmpty()) {
            packages.add(debPkg);
        }
    }
}
