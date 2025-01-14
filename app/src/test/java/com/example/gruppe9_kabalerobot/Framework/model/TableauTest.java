package com.example.gruppe9_kabalerobot.Framework.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TableauTest {

    @Test
    public void testGetVisibleCards() {
        Tableau stack = new Tableau(6, null);
        stack.addCardToStack(new Card(0,1));
        Card[] expected = {new Card(0,1)};
        assertEquals(expected[0].toString(), stack.getVisibleCards().get(0).toString());
    }

    @Test
    public void testIsEmpty() {
        Tableau stack = new Tableau(0, null);
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testAddCardToStack() {
        Tableau stack = new Tableau(3, null);
        Card testcard1 = new Card(1, 2);
        Card testcard2 = new Card(0, 1);
        Card testcard3 = new Card(0, 3);
        Card testcard4 = new Card(1, 1);
        stack.addCardToStack(testcard1);
        assertEquals("Spar 2", stack.getVisibleCards().get(0).toString());
        stack.addCardToStack(testcard3);
        assertEquals(1, stack.getVisibleCards().size());
        assertEquals("Spar 2", stack.getVisibleCards().get(0).toString());
        stack.addCardToStack(testcard4);
        assertEquals(1, stack.getVisibleCards().size());
        assertEquals("Spar 2", stack.getVisibleCards().get(0).toString());
        stack.addCardToStack(testcard2);
        assertEquals(2, stack.getVisibleCards().size());
        assertEquals("Spar 2", stack.getVisibleCards().get(0).toString());
        assertEquals("Hjerter Es", stack.getVisibleCards().get(1).toString());

    }
}