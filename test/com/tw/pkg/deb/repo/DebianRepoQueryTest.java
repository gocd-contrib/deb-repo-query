package com.tw.pkg.deb.repo;

import com.tw.pkg.deb.db.PackageDAO;
import com.tw.pkg.deb.helper.VerificationHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DebianRepoQueryTest {
    @Test
    public void shouldSortPackagesReturnedByDebianRepository() throws Exception {
        DebianRepoQuery debianRepoQuery = new DebianRepoQuery("http://test.com");

        PackageDAO packageDAO = debianRepoQuery.getPackageDAO();
        packageDAO.deleteTableIfExists();
        packageDAO.createTableIfNotExists();

        DebianPackage debPkg1 = VerificationHelper.getDebianPackage(1);
        debPkg1.setVersion("13.1.0-17152");
        packageDAO.insert(debPkg1);

        DebianPackage debPkg1_2 = VerificationHelper.getDebianPackage(1);
        debPkg1_2.setVersion("14.1.0-17152");
        packageDAO.insert(debPkg1_2);

        DebianPackage debPkg1_3 = VerificationHelper.getDebianPackage(1);
        debPkg1_3.setVersion("13.2.0-17152");
        packageDAO.insert(debPkg1_3);

        DebianPackage debPkg1_4 = VerificationHelper.getDebianPackage(1);
        debPkg1_4.setVersion("13.1.1-17152");
        packageDAO.insert(debPkg1_4);

        DebianPackage debPkg1_5 = VerificationHelper.getDebianPackage(1);
        debPkg1_5.setVersion("13.1.0-17153");
        packageDAO.insert(debPkg1_5);

        DebianPackage debPkg6 = VerificationHelper.getDebianPackage(1);
        debPkg6.setVersion("13.1.0-17152");
        debPkg6.setArchitecture("architecture_" + 2);
        packageDAO.insert(debPkg6);

        List<DebianPackage> debianPackages = debianRepoQuery.getDebianPackagesFor("name_1", null, null);
        Assert.assertEquals(6, debianPackages.size());
        Assert.assertEquals("14.1.0-17152", debianPackages.get(0).getVersion());
        Assert.assertEquals("13.2.0-17152", debianPackages.get(1).getVersion());
        Assert.assertEquals("13.1.1-17152", debianPackages.get(2).getVersion());
        Assert.assertEquals("13.1.0-17153", debianPackages.get(3).getVersion());
        Assert.assertEquals("13.1.0-17152", debianPackages.get(4).getVersion());
        Assert.assertEquals("13.1.0-17152", debianPackages.get(5).getVersion());

        packageDAO.deleteTableIfExists();
    }
}
