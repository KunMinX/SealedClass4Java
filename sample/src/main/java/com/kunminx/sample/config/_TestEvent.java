package com.kunminx.sample.config;

import com.kunminx.sealed.annotation.Param;
import com.kunminx.sealed.annotation.SealedClass;
/**
 * Create by KunMinX at 2022/8/29
 */
@SealedClass
public interface _TestEvent {
  void resultTest1(@Param String a, int b);
  void resultTest2(@Param String a, @Param int b, int c, String d);
}
