package com.codemages.Moviee.test.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
  private static final Map<String, AtomicLong> COUNTERS = new ConcurrentHashMap<>();

  public static long nextId(String entityType) {
    return COUNTERS.computeIfAbsent( entityType, k -> new AtomicLong( 1 ) ).getAndIncrement();
  }

  public static void reset() {
    COUNTERS.clear();
  }
}
