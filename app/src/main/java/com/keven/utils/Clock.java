package com.keven.utils;

public interface Clock {
  long millis();

  //  Clock REAL = System::currentTimeMillis
  Clock REAL = new Clock() {
    @Override
    public long millis() {
      return System.currentTimeMillis();
    }
  };

  class Creator {
    public static Clock newClock() {
      return new Clock() {
        @Override
        public long millis() {
          return System.currentTimeMillis();
        }
      };
    }
  }
}
