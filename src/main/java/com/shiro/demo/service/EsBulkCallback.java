package com.shiro.demo.service;

import java.util.List;

@FunctionalInterface
public interface EsBulkCallback {
   void apply(List failList);
}
