package com.tw.pkg.deb.helper;

import com.tw.pkg.deb.repo.DebianPackage;

public class DBHelper {
    public static String join(String... args) {
        String line = "";
        for (String current : args) {
            if (!line.isEmpty())
                line += ", ";
            line += getValue(current);
        }
        return line;
    }

    public static String getValue(String input) {
        if (input != null) {
            return "'" + input + "'";
        } else {
            return "NULL";
        }
    }

    public static boolean equals(DebianPackage thisObj, DebianPackage thatObj) {
        if (thisObj.getConflicts() == null) {
            if (thatObj.getConflicts() != null)
                return false;
        } else if (!thisObj.getConflicts().equals(thatObj.getConflicts()))
            return false;

        if (thisObj.getFilename() == null) {
            if (thatObj.getFilename() != null)
                return false;
        } else if (!thisObj.getFilename().equals(thatObj.getFilename()))
            return false;

        if (thisObj.getInstalledSize() == null) {
            if (thatObj.getInstalledSize() != null)
                return false;
        } else if (!thisObj.getInstalledSize().equals(thatObj.getInstalledSize()))
            return false;

        if (thisObj.getMaintainer() == null) {
            if (thatObj.getMaintainer() != null)
                return false;
        } else if (!thisObj.getMaintainer().equals(thatObj.getMaintainer()))
            return false;

        if (thisObj.getMd5sum() == null) {
            if (thatObj.getMd5sum() != null)
                return false;
        } else if (!thisObj.getMd5sum().equals(thatObj.getMd5sum()))
            return false;

        if (thisObj.getName() == null) {
            if (thatObj.getName() != null)
                return false;
        } else if (!thisObj.getName().equals(thatObj.getName()))
            return false;

        if (thisObj.getPriority() == null) {
            if (thatObj.getPriority() != null)
                return false;
        } else if (!thisObj.getPriority().equals(thatObj.getPriority()))
            return false;

        if (thisObj.getReplaces() == null) {
            if (thatObj.getReplaces() != null)
                return false;
        } else if (!thisObj.getReplaces().equals(thatObj.getReplaces()))
            return false;

        if (thisObj.getSection() == null) {
            if (thatObj.getSection() != null)
                return false;
        } else if (!thisObj.getSection().equals(thatObj.getSection()))
            return false;

        if (thisObj.getSha1() == null) {
            if (thatObj.getSha1() != null)
                return false;
        } else if (!thisObj.getSha1().equals(thatObj.getSha1()))
            return false;

        if (thisObj.getSha256() == null) {
            if (thatObj.getSha256() != null)
                return false;
        } else if (!thisObj.getSha256().equals(thatObj.getSha256()))
            return false;

        if (thisObj.getSize() == null) {
            if (thatObj.getSize() != null)
                return false;
        } else if (!thisObj.getSize().equals(thatObj.getSize()))
            return false;

        if (thisObj.getVersion() == null) {
            if (thatObj.getVersion() != null)
                return false;
        } else if (!thisObj.getVersion().equals(thatObj.getVersion()))
            return false;
        return true;
    }
}
