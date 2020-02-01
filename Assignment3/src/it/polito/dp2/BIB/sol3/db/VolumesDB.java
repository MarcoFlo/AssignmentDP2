package it.polito.dp2.BIB.sol3.db;

import it.polito.dp2.BIB.sol3.service.jaxb.Volume;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VolumesDB {
    private static Map<String, Volume> map = new ConcurrentHashMap<>();
    public static Map<String, Volume> getMap() {
        return map;
    }
}
