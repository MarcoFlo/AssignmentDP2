package it.polito.dp2.BIB.sol3.db;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BookshelfDB {
    private static Map<BigInteger, BookshelfEntity> map = new ConcurrentHashMap<>();
    private static AtomicLong last = new AtomicLong(0);

    public static Map<BigInteger, BookshelfEntity> getMap() {
        return map;
    }

    public static long getNext() {
        return last.getAndIncrement();
    }
}
