package com.kunminx.sample.config;

import com.kunminx.sealed.annotation.Param;
import com.kunminx.sealed.annotation.SealedClass;
/**
 * Create by KunMinX at 2022/8/29
 */
@SealedClass
public interface _ComplexEvent {
  void resultTest1(@Param String a, String b);
  void resultTest2(@Param String a, String b);
  void resultTest3(@Param String a);
  void resultTest4(@Param String a);
}
