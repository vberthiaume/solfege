package com.example.solfege;

import static org.junit.Assert.*;

import org.junit.Test;

public class PieceTest {

   public Piece mapiece;
   
    
    @Test
    public void testPiece() {
        mapiece = new Piece();
    }

    @Test
    public void testGenerePiece() {
        mapiece = new Piece();
        String test = mapiece.generePiece();
        System.out.print(test);
    }

    @Test
    public void testGetTempo() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetTempo() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetMode() {
        fail("Not yet implemented");
    }

    @Test
    public void testSetMode() {
        fail("Not yet implemented");
    }

}
