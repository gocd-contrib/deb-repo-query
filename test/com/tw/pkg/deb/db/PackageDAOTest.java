package com.tw.pkg.deb.db;

import com.tw.pkg.deb.helper.VerificationHelper;
import com.tw.pkg.deb.repo.DebianPackage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PackageDAOTest {
    PackageDAO packageDAO;

    @Before
    public void setup() throws Exception {
        packageDAO = new PackageDAO("sample.db");
        packageDAO.createTableIfNotExists();
    }

    @After
    public void teardown() throws Exception {
        packageDAO.deleteTableIfExists();
    }

    @Test
    public void testCRUDonPackageTable() throws Exception {
        DebianPackage debPkg1 = VerificationHelper.getDebianPackage(1);
        packageDAO.insert(debPkg1);

        DebianPackage debPkg2 = VerificationHelper.getDebianPackage(2);
        packageDAO.insert(debPkg2);

        List<DebianPackage> allDebPkgs = packageDAO.getAllPackages();
        Assert.assertEquals(2, allDebPkgs.size());
        VerificationHelper.assertCorrectnessOfDebianPackage(allDebPkgs.get(0), 1);
        VerificationHelper.assertCorrectnessOfDebianPackage(allDebPkgs.get(1), 2);

        Assert.assertEquals(2, packageDAO.getPackageCount());

        List<DebianPackage> debPkgs = new ArrayList<DebianPackage>();
        debPkgs.add(debPkg1);
        DebianPackage debPkg3 = VerificationHelper.getDebianPackage(3);
        debPkg3.setName("name_" + 2);
        debPkgs.add(debPkg3);
        DebianPackage debPkg4 = VerificationHelper.getDebianPackage(4);
        debPkgs.add(debPkg4);
        packageDAO.updateIfRequired(debPkgs);

        allDebPkgs = packageDAO.getAllPackages();
        Assert.assertEquals(3, allDebPkgs.size());
        VerificationHelper.assertCorrectnessOfDebianPackage(allDebPkgs.get(0), 1);
        Assert.assertEquals("name_" + 2, allDebPkgs.get(1).getName());
        VerificationHelper.assertCorrectnessOfNonPrimaryData(allDebPkgs.get(1), 3);
        VerificationHelper.assertCorrectnessOfDebianPackage(allDebPkgs.get(2), 4);
    }

    @Test
    public void shouldGetPackagesByNameAndVersionCorrectly() throws Exception {
        DebianPackage debPkg1 = VerificationHelper.getDebianPackage(1);
        packageDAO.insert(debPkg1);

        DebianPackage debPkg2 = VerificationHelper.getDebianPackage(2);
        packageDAO.insert(debPkg2);

        List<DebianPackage> debPkgsByName = packageDAO.getPackagesBy_Name_Version_Architecture("name_1", null, null);
        Assert.assertEquals(1, debPkgsByName.size());
        VerificationHelper.assertCorrectnessOfDebianPackage(debPkgsByName.get(0), 1);

        debPkgsByName = packageDAO.getPackagesBy_Name_Version_Architecture("invalid_name", null, null);
        Assert.assertEquals(0, debPkgsByName.size());

        DebianPackage debPkg1_2 = VerificationHelper.getDebianPackage(1);
        debPkg1_2.setVersion("version_" + 2);
        debPkg1_2.setArchitecture("architecture_" + 2);
        packageDAO.insert(debPkg1_2);

        DebianPackage debPkg1_3 = VerificationHelper.getDebianPackage(1);
        debPkg1_3.setVersion("version_" + 3);
        debPkg1_3.setArchitecture("architecture_" + 3);
        packageDAO.insert(debPkg1_3);

        List<DebianPackage> debPkgsForSpec = packageDAO.getPackagesBy_Name_Version_Architecture("name_1", "version%", null);
        Assert.assertEquals(3, debPkgsForSpec.size());
        Assert.assertEquals("name_1", debPkgsForSpec.get(0).getName());
        Assert.assertEquals("version_1", debPkgsForSpec.get(0).getVersion());
        Assert.assertEquals("name_1", debPkgsForSpec.get(1).getName());
        Assert.assertEquals("version_2", debPkgsForSpec.get(1).getVersion());
        Assert.assertEquals("name_1", debPkgsForSpec.get(2).getName());
        Assert.assertEquals("version_3", debPkgsForSpec.get(2).getVersion());

        debPkgsForSpec = packageDAO.getPackagesBy_Name_Version_Architecture("name_1", "version%", "architecture_1");
        Assert.assertEquals(1, debPkgsForSpec.size());
        Assert.assertEquals("name_1", debPkgsForSpec.get(0).getName());
        Assert.assertEquals("version_1", debPkgsForSpec.get(0).getVersion());
        Assert.assertEquals("architecture_1", debPkgsForSpec.get(0).getArchitecture());
    }
}
