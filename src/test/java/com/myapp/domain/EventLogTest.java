package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventLog.class);
        EventLog eventLog1 = new EventLog();
        eventLog1.setId(1L);
        EventLog eventLog2 = new EventLog();
        eventLog2.setId(eventLog1.getId());
        assertThat(eventLog1).isEqualTo(eventLog2);
        eventLog2.setId(2L);
        assertThat(eventLog1).isNotEqualTo(eventLog2);
        eventLog1.setId(null);
        assertThat(eventLog1).isNotEqualTo(eventLog2);
    }
}
