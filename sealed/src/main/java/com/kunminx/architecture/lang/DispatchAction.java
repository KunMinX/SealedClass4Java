package com.kunminx.architecture.lang;

/**
 * Create by KunMinX at 2022/8/30
 */
public interface DispatchAction<T> {
  void onAction(T t);
}
