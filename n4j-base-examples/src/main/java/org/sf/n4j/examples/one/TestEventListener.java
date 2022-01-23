package org.sf.n4j.examples.one;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEventListener {

    @Subscribe
    public void onEvent(String event){
        log.info("Received Event : {}",event);
    }
}
