package it.polito.dp2.BIB.sol3.db;

import it.polito.dp2.BIB.sol3.service.jaxb.Journal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JournalsDB {
    private static Map<String, Journal> map = new ConcurrentHashMap<>();
    public static Map<String, Journal> getMap() {
        return map;
    }
}
