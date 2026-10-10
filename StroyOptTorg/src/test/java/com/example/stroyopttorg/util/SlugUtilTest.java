package com.example.stroyopttorg.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SlugUtilTest {
    @Test
    void transliteratesCyrillic() {
        assertEquals("drel-shurupovert-makita-18v", SlugUtil.slugify("Дрель-шуруповёрт Makita 18V"));
    }

    @Test
    void trimsSeparators() {
        assertEquals("a-b", SlugUtil.slugify("  A  &  B !"));
    }
}
