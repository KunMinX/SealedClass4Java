package com.kunminx.sample.config;

/**
 * Create by KunMinX at 2022/8/30
 */
public class TestEvent_ {

  public final String id;

  public TestEvent_(String id) {
    this.id = id;
  }

  public static class ResultTest1 extends TestEvent_ {
    public final static String ID = "com.kunminx.sample.config.ResultTest1";
    public final String paramA;
    public final int resultA;
    public ResultTest1(String paramA, int resultA) {
      super(TestEvent.ResultTest1.class.getName());
      this.paramA = paramA;
      this.resultA = resultA;
    }
    public TestEvent.ResultTest1 copy(int resultA) {
      return new TestEvent.ResultTest1(this.paramA, resultA);
    }
  }

  public static class ResultTest2 extends TestEvent_ {
    public final static String ID = "com.kunminx.sample.config.ResultTest2";
    public final String paramA;
    public final int resultA;
    public ResultTest2(String paramA, int resultA) {
      super(ResultTest2.class.getName());
      this.paramA = paramA;
      this.resultA = resultA;
    }
    public ResultTest2 copy(int resultA) {
      return new ResultTest2(this.paramA, resultA);
    }
  }

  public static TestEvent_ ResultTest1(String paramA) {
    return new ResultTest1(paramA, 0);
  }

  public static TestEvent_ ResultTest2(String paramA) {
    return new ResultTest2(paramA, 0);
  }
}
