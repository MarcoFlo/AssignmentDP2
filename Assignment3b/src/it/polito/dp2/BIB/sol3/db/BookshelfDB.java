package it.polito.dp2.BIB.sol3.db;

import it.polito.dp2.BIB.sol3.service.jaxb.Bookshelf;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BookshelfDB {
    private static Map<BigInteger, Bookshelf> map = new ConcurrentHashMap<>();
    private static long last = 0;

    public static Map<BigInteger, Bookshelf> getMap() {
        return map;
    }

//    public static void setMap(Map<Long, Bookshelf> map) {
//        BookshelfDB.map = map;
//    }

    public static synchronized long getNext() {
        return ++last;
    }
}
