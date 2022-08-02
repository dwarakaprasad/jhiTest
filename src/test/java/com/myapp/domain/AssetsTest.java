package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assets.class);
        Assets assets1 = new Assets();
        assets1.setId(1L);
        Assets assets2 = new Assets();
        assets2.setId(assets1.getId());
        assertThat(assets1).isEqualTo(assets2);
        assets2.setId(2L);
        assertThat(assets1).isNotEqualTo(assets2);
        assets1.setId(null);
        assertThat(assets1).isNotEqualTo(assets2);
    }
}
