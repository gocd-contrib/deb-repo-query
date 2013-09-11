package com.tw.pkg.deb.db;

import com.tw.pkg.deb.helper.DBHelper;
import com.tw.pkg.deb.repo.DebianPackage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackageDAO {
    private String databaseFilePath;

    public PackageDAO(String databaseFilePath) {
        super();
        this.databaseFilePath = databaseFilePath;
    }

    protected Connection getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + databaseFilePath);
    }

    protected void executeUpdateQuery(String sql) throws Exception {
        Connection connection = getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }

    public void createTableIfNotExists() throws Exception {
        executeUpdateQuery("CREATE TABLE IF NOT EXISTS package (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, priority STRING, section STRING, "
                + "installedSize STRING, maintainer STRING, architecture STRING, version STRING, replaces STRING, "
                + "conflicts STRING, filename STRING, size STRING, md5sum STRING, sha1 STRING, sha256 STRING)");
    }

    public void insert(DebianPackage debPkg) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT INTO package (name, priority, section, installedSize, maintainer, architecture, " +
                    "version, replaces, conflicts, filename, size, md5sum, sha1, sha256) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, debPkg.getName());
            statement.setString(2, debPkg.getPriority());
            statement.setString(3, debPkg.getSection());
            statement.setString(4, debPkg.getInstalledSize());
            statement.setString(5, debPkg.getMaintainer());
            statement.setString(6, debPkg.getArchitecture());
            statement.setString(7, debPkg.getVersion());
            statement.setString(8, debPkg.getReplaces());
            statement.setString(9, debPkg.getConflicts());
            statement.setString(10, debPkg.getFilename());
            statement.setString(11, debPkg.getSize());
            statement.setString(12, debPkg.getMd5sum());
            statement.setString(13, debPkg.getSha1());
            statement.setString(14, debPkg.getSha256());
            statement.executeUpdate();
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }

    public void update(DebianPackage debPkg) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("UPDATE package SET name = ?, priority = ?, section = ?, installedSize = ?, maintainer = ?, " +
                    "architecture = ?, version = ?, replaces = ?, conflicts = ?, filename = ?, size = ?, md5sum = ?, sha1 = ?, sha256 = ? WHERE id = ?");
            statement.setString(1, debPkg.getName());
            statement.setString(2, debPkg.getPriority());
            statement.setString(3, debPkg.getSection());
            statement.setString(4, debPkg.getInstalledSize());
            statement.setString(5, debPkg.getMaintainer());
            statement.setString(6, debPkg.getArchitecture());
            statement.setString(7, debPkg.getVersion());
            statement.setString(8, debPkg.getReplaces());
            statement.setString(9, debPkg.getConflicts());
            statement.setString(10, debPkg.getFilename());
            statement.setString(11, debPkg.getSize());
            statement.setString(12, debPkg.getMd5sum());
            statement.setString(13, debPkg.getSha1());
            statement.setString(14, debPkg.getSha256());
            statement.setLong(15, debPkg.getId());
            statement.executeUpdate();
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }

    public void updateIfRequired(List<DebianPackage> latestDebianPackages) throws Exception {
        Map<String, DebianPackage> packagesInDBMap = getPackagesInDBMap();
        int packageCount = latestDebianPackages.size();
        for (int i = 0; i < packageCount; i++) {
            DebianPackage currentPackage = latestDebianPackages.get(i);
            DebianPackage packageInDB = packagesInDBMap.get(currentPackage.getName());

            if (packageInDB != null) {
                if (!DBHelper.equals(packageInDB, currentPackage)) {
                    currentPackage.setId(packageInDB.getId());
                    update(currentPackage);
                }
            } else {
                insert(currentPackage);
            }
        }
    }

    private Map<String, DebianPackage> getPackagesInDBMap() throws Exception {
        List<DebianPackage> allPackagesInDB = getAllPackages();
        Map<String, DebianPackage> packagesInDBMap = new HashMap<String, DebianPackage>();
        for (DebianPackage currentPackage : allPackagesInDB) {
            packagesInDBMap.put(currentPackage.getName(), currentPackage);
        }
        return packagesInDBMap;
    }

    public List<DebianPackage> getAllPackages() throws Exception {
        return getPackagesForQuery("SELECT * FROM package ORDER BY id");
    }

    public List<DebianPackage> getPackagesBy_Name_Version_Architecture(String packageName, String versionSpec, String architecture) throws Exception {
        String sql = "SELECT * FROM package WHERE name = " + DBHelper.getValue(packageName);
        if (versionSpec != null && !versionSpec.trim().isEmpty()) {
            sql += " AND version LIKE '" + versionSpec + "'";
        }
        if (architecture != null && !architecture.trim().isEmpty()) {
            sql += " AND architecture LIKE '" + architecture + "'";
        }
        return getPackagesForQuery(sql);
    }

    public List<DebianPackage> getPackagesForQuery(String sql) throws Exception {
        List<DebianPackage> debianPackages = new ArrayList<DebianPackage>();
        Connection connection = getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                DebianPackage debPkg = getDebianPackageFromResultSet(rs);
                debianPackages.add(debPkg);
            }
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
        return debianPackages;
    }

    private DebianPackage getDebianPackageFromResultSet(ResultSet rs) throws Exception {
        DebianPackage debPkg = new DebianPackage();
        debPkg.setId(rs.getInt("id"));
        debPkg.setName(rs.getString("name"));
        debPkg.setPriority(rs.getString("priority"));
        debPkg.setSection(rs.getString("section"));
        debPkg.setInstalledSize(rs.getString("installedSize"));
        debPkg.setMaintainer(rs.getString("maintainer"));
        debPkg.setArchitecture(rs.getString("architecture"));
        debPkg.setVersion(rs.getString("version"));
        debPkg.setReplaces(rs.getString("replaces"));
        debPkg.setConflicts(rs.getString("conflicts"));
        debPkg.setFilename(rs.getString("filename"));
        debPkg.setSize(rs.getString("size"));
        debPkg.setMd5sum(rs.getString("md5sum"));
        debPkg.setSha1(rs.getString("sha1"));
        debPkg.setSha256(rs.getString("sha256"));
        return debPkg;
    }

    public int getPackageCount() throws Exception {
        Connection connection = getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM package");
            while (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
        return 0;
    }

    public void deleteTableIfExists() throws Exception {
        executeUpdateQuery("DROP TABLE IF EXISTS package");
    }
}
