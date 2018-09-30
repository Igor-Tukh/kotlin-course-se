package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TestSource {
    @Test
    fun testGetNameFirstSample() {
        assertNull(getName(3, "a?c"))
    }

    @Test
    fun testGetNameSecondSample() {
        assertEquals("abba", getName(2, "a??a"))
    }

    @Test
    fun testGetNameThirdSample() {
        assertEquals("abba", getName(2, "?b?a"))
    }

    @Test
    fun testGetNameFourthSample() {
        assertEquals("adbeccebda", getName(5, "a?b?c?????"))
    }

    @Test
    fun testFifthSample() {
        assertEquals("aaabaaa", getName(2, "????aaa"))
    }
}