package com.tw.pkg.deb.helper;

import com.tw.pkg.deb.repo.DebianPackage;

public class DBHelper {
    public static String getValue(String input) {
        if (input != null) {
            return "'" + input + "'";
        } else {
            return "NULL";
        }
    }

    public static boolean equals(DebianPackage thisObj, DebianPackage thatObj) {
        if (thisObj.getArchitecture() != null ? !thisObj.getArchitecture().equals(thatObj.getArchitecture()) : thatObj.getArchitecture() != null)
            return false;
        if (thisObj.getConflicts() != null ? !thisObj.getConflicts().equals(thatObj.getConflicts()) : thatObj.getConflicts() != null)
            return false;
        if (thisObj.getFilename() != null ? !thisObj.getFilename().equals(thatObj.getFilename()) : thatObj.getFilename() != null)
            return false;
        if (thisObj.getInstalledSize() != null ? !thisObj.getInstalledSize().equals(thatObj.getInstalledSize()) : thatObj.getInstalledSize() != null)
            return false;
        if (thisObj.getMaintainer() != null ? !thisObj.getMaintainer().equals(thatObj.getMaintainer()) : thatObj.getMaintainer() != null)
            return false;
        if (thisObj.getMd5sum() != null ? !thisObj.getMd5sum().equals(thatObj.getMd5sum()) : thatObj.getMd5sum() != null)
            return false;
        if (thisObj.getName() != null ? !thisObj.getName().equals(thatObj.getName()) : thatObj.getName() != null)
            return false;
        if (thisObj.getPriority() != null ? !thisObj.getPriority().equals(thatObj.getPriority()) : thatObj.getPriority() != null)
            return false;
        if (thisObj.getReplaces() != null ? !thisObj.getReplaces().equals(thatObj.getReplaces()) : thatObj.getReplaces() != null)
            return false;
        if (thisObj.getSection() != null ? !thisObj.getSection().equals(thatObj.getSection()) : thatObj.getSection() != null)
            return false;
        if (thisObj.getSha1() != null ? !thisObj.getSha1().equals(thatObj.getSha1()) : thatObj.getSha1() != null)
            return false;
        if (thisObj.getSha256() != null ? !thisObj.getSha256().equals(thatObj.getSha256()) : thatObj.getSha256() != null)
            return false;
        if (thisObj.getSize() != null ? !thisObj.getSize().equals(thatObj.getSize()) : thatObj.getSize() != null)
            return false;
        if (thisObj.getVersion() != null ? !thisObj.getVersion().equals(thatObj.getVersion()) : thatObj.getVersion() != null)
            return false;

        return true;
    }
}
