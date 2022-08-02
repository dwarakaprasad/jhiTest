package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncomeSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomeSource.class);
        IncomeSource incomeSource1 = new IncomeSource();
        incomeSource1.setId(1L);
        IncomeSource incomeSource2 = new IncomeSource();
        incomeSource2.setId(incomeSource1.getId());
        assertThat(incomeSource1).isEqualTo(incomeSource2);
        incomeSource2.setId(2L);
        assertThat(incomeSource1).isNotEqualTo(incomeSource2);
        incomeSource1.setId(null);
        assertThat(incomeSource1).isNotEqualTo(incomeSource2);
    }
}
