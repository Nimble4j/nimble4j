package org.sf.n4j.examples.two;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SomeServiceImpl implements SomeService{

    @Override
    public void start() {
      log.info("Started SomeService...");
    }
}
