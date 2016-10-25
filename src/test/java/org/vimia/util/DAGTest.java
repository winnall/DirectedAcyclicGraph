/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.vimia.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author steve
 */
public class DAGTest {

    enum Base { A, B, C, D, E, F, G, H };
    DAG<Base> setABCDEFG;

    public DAGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        setABCDEFG = new DAG<Base>();
        setABCDEFG.add(Base.A);
        setABCDEFG.add(Base.B);
        setABCDEFG.add(Base.C);
        setABCDEFG.add(Base.D);
        setABCDEFG.add(Base.E);
        setABCDEFG.add(Base.F);
        setABCDEFG.add(Base.G);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getBefore method, of class DAG<Base>.
     */
    @Test
    public void testGetBefore() {
        try {
            System.out.println("getBefore");
            DAG<Base> instance = new DAG<Base>(setABCDEFG);
            // Test 1
            instance.setBefore(Base.A, Base.B);
            instance.setBefore(Base.F, Base.B);
            instance.setBefore(Base.G, Base.B);
            instance.setBefore(Base.B, Base.C);
            Set<Base> expResult = new HashSet<Base>();
            expResult.add(Base.A);
            expResult.add(Base.F);
            expResult.add(Base.G);
            Set<Base> result = instance.getBefore(Base.B);
            assertEquals(expResult, result);
            // Test 2
            try {
                instance.setBefore(Base.B, Base.B);
                fail("expecting PartialOrderingLoopException");
            } catch (DAGCycleException partialOrderingLoopException) {
                assertEquals(expResult, result);
            }
            // Test 3
            try {
                instance.setBefore(Base.C, Base.B);
                fail("expecting PartialOrderingLoopException");
            } catch (DAGCycleException partialOrderingLoopException) {
                assertEquals(expResult, result);
            }
            // Test 4
            try {
                instance.setBefore(Base.H, Base.A);
                expResult = new HashSet<Base>();
                expResult.add(Base.H);
                result = instance.getBefore(Base.A);
                fail("should generate a PartialOrderingNullPointerException");
            } catch (DAGNotAMemberException ex) {
                assertTrue(true);
            }
        } catch (DAGCycleException ex) {
            fail(ex.getMessage());
        }
    }


    /**
     * Test of isBefore method, of class DAG<Base>.
     */
    @Test
    public void testIsBefore() {
        try {
            System.out.println("isBefore");
            DAG<Base> instance = new DAG<Base>(setABCDEFG);
            instance.setBefore(Base.A, Base.B);
            instance.setBefore(Base.F, Base.B);
            instance.setBefore(Base.G, Base.B);
            instance.setBefore(Base.B, Base.C);
            // Test 1
            boolean expResult = true;
            boolean result = instance.isBefore(Base.A, Base.B);
            assertEquals(expResult, result);
            // Test 2
            expResult = false;
            result = instance.isBefore(Base.B, Base.A);
            assertEquals(expResult, result);
            // Test 3
            expResult = false;
            result = instance.isBefore(Base.A, Base.A);
            assertEquals(expResult, result);
            // Test 4
            expResult = false;
            result = instance.isBefore(Base.A, Base.D);
            assertEquals(expResult, result);
            // Test 5
            expResult = false;
            result = instance.isBefore(Base.D, Base.A);
            assertEquals(expResult, result);
            // Test 6
            expResult = false;
            result = instance.isBefore(Base.D, Base.D);
            assertEquals(expResult, result);
            // Test 5
            expResult = true;
            result = instance.isBefore(Base.A, Base.C);
            assertEquals(expResult, result);
            // Test 6
            try {
                instance.isBefore(Base.A, Base.H);
                fail("should generate PartialOrderingNotAMemberException");
            } catch (DAGNotAMemberException e) {
                assertTrue(true);
            }
            // Test 7
            try {
                instance.isBefore(Base.H, Base.A);
                fail("should generate PartialOrderingNotAMemberException");
            } catch (DAGNotAMemberException e) {
                assertTrue(true);
            }
        } catch (DAGCycleException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of setBefore method, of class DAG<Base>.
     */
    @Test
    public void testSetBefore() {
        try {
            System.out.println("setBefore");
            DAG<Base> instance = new DAG<Base>(setABCDEFG);
            // Test 1
            instance.setBefore(Base.C, Base.A);
            assertEquals(true, instance.isBefore(Base.C, Base.A));
            // Test 2
            try {
                instance.setBefore(Base.A, Base.C);
                fail("should generate PartialOrderingLoopException");
            } catch (DAGCycleException partialOrderingLoopException) {
                assertTrue(true);
            }
            // Test 3
            try {
                instance.setBefore(Base.A, Base.D);
                instance.setBefore(Base.D, Base.C);
                fail("should generate PartialOrderingLoopException");
            } catch (DAGCycleException partialOrderingLoopException) {
                assertTrue(true);
            }
            // Test 4
            try {
                instance.setBefore(Base.A, Base.H);
                fail("should generate PartialOrderingNotAMemberException");
            } catch (DAGNotAMemberException ex) {
                assertTrue(true);
            }
            // Test 5
            try {
                instance.setBefore(Base.H, Base.A);
                fail("should generate PartialOrderingNotAMemberException");
            } catch (DAGNotAMemberException ex) {
                assertTrue(true);
            }
        } catch (DAGCycleException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of size method, of class DAG<Base>.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        DAG<Base> instance = new DAG<Base>(setABCDEFG);
        int expResult = 7;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class DAG<Base>.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        // Test 1
        DAG<Base> instance = new DAG<Base>();
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);
        // Test 2
        instance = new DAG<Base>(setABCDEFG);
        expResult = false;
        result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of contains method, of class DAG<Base>.
     */
    @Test
    public void testContains() {
        System.out.println("contains");
        DAG<Base> instance = new DAG<Base>();
        // Test 1
        boolean expResult = false;
        Object o = null;
        boolean result = instance.contains(o);
        assertEquals(expResult, result);
        // Test 2
        expResult = false;
        result = instance.contains(Base.C);
        assertEquals(expResult, result);

        instance = new DAG<Base>(setABCDEFG);
         // Test 3
        expResult = false;
        result = instance.contains(o);
        assertEquals(expResult, result);
        // Test 4
        expResult = true;
        result = instance.contains(Base.C);
        assertEquals(expResult, result);
        // Test 5
        expResult = false;
        result = instance.contains(Base.H);
        assertEquals(expResult, result);
   }

    /**
     * Test of iterator method, of class DAG<Base>.
     */
    @Test
    public void testIterator() {
        System.out.println("iterator");
        DAG<Base> instance = new DAG<Base>(setABCDEFG);
        HashSet<Base> used = new HashSet<Base>();
        Iterator<Base> iterator = instance.iterator();
        assertEquals(true, iterator.hasNext());
        Base next = iterator.next();
        iterator.remove();
        assertEquals(false, used.contains(next));
        used.add(next);
        assertEquals(true, iterator.hasNext());
        next = iterator.next();
        iterator.remove();
        assertEquals(false, used.contains(next));
        used.add(next);
        assertEquals(true, iterator.hasNext());
        next = iterator.next();
        iterator.remove();
        assertEquals(false, used.contains(next));
        used.add(next);
        assertEquals(true, iterator.hasNext());
        next = iterator.next();
        iterator.remove();
        assertEquals(false, used.contains(next));
        used.add(next);
        assertEquals(true, iterator.hasNext());
        next = iterator.next();
        iterator.remove();
        assertEquals(false, used.contains(next));
        used.add(next);
        assertEquals(true, iterator.hasNext());
        next = iterator.next();
        iterator.remove();
        assertEquals(false, used.contains(next));
        used.add(next);
        assertEquals(true, iterator.hasNext());
        next = iterator.next();
        iterator.remove();
        assertEquals(false, used.contains(next));
        used.add(next);
        assertEquals(false, iterator.hasNext());
        assertEquals(new DAG<Base>(), instance);
    }

    /**
     * Test of toArray method, of class DAG<Base>.
     */
    @Test
    public void testToArray_0args() {
        System.out.println("toArray");
        DAG<Base> instance = new DAG<Base>(Base.A, Base.D, Base.H, Base.C);
        DAG<Base> duplicate = new DAG<Base>();
        Object[] result = instance.toArray();
        assertTrue(result.length==4);
        for ( Object obj : result ) {
            assertTrue(instance.contains((Base)obj));
            assertTrue(duplicate.add((Base)obj));
        }
        assertEquals(instance, duplicate);
    }

    /**
     * Test of toArray method, of class DAG<Base>.
     */
    @Test
    public void testToArray_null() {
        System.out.println("toArray");
        Base[] prototype = new Base[0];
        DAG<Base> instance = new DAG<Base>(Base.A, Base.D, Base.H, Base.C);
        DAG<Base> duplicate = new DAG<Base>();
        Object[] result = instance.toArray(prototype);
        assertTrue(result.length==4);
        for ( Object obj : result ) {
            assertTrue(instance.contains((Base)obj));
            assertTrue(duplicate.add((Base)obj));
        }
        assertEquals(instance, duplicate);
    }

    /**
     * Test of add method, of class DAG<Base>.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Base element = null;
        DAG<Base> instance = new DAG<Base>();
        assertTrue(instance.isEmpty());
        assertTrue(instance.add(Base.B));
        assertTrue(instance.add(Base.D));
        assertTrue(instance.add(Base.H));
        assertFalse(instance.add(Base.H));
        assertTrue(instance.contains(Base.B));
        assertTrue(instance.contains(Base.D));
        assertTrue(instance.contains(Base.H));
        assertFalse(instance.contains(Base.A));
        assertFalse(instance.contains(Base.C));
        assertFalse(instance.contains(Base.E));
        assertFalse(instance.contains(Base.F));
        assertFalse(instance.contains(Base.G));
        try {
            instance.add(null);
            fail("should generate PartialOrderingNullNotAllowedException");
        } catch ( DAGNullNotAllowedException ex ) {
            assertTrue(true);
        }
    }

    /**
     * Test of remove method, of class DAG<Base>.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        DAG<Base> instance = new DAG<Base>(setABCDEFG);
        assertTrue(instance.remove(Base.B));
        assertTrue(instance.remove(Base.D));
        assertFalse(instance.remove(Base.D));
        assertFalse(instance.remove(Base.H));
        assertFalse(instance.contains(Base.B));
        assertFalse(instance.contains(Base.D));
        assertFalse(instance.contains(Base.H));
        assertTrue(instance.contains(Base.A));
        assertTrue(instance.contains(Base.C));
        assertTrue(instance.contains(Base.E));
        assertTrue(instance.contains(Base.F));
        assertTrue(instance.contains(Base.G));
    }

    /**
     * Test of containsAll method, of class DAG<Base>.
     */
    @Test
    public void testContainsAll() {
        System.out.println("containsAll");
        Collection<Base> collection = new HashSet<Base>();
        DAG<Base> instance = new DAG<Base>(setABCDEFG);
        // Test 1
        assertTrue(instance.containsAll(collection));
        // Test 2
        collection.add(Base.B);
        collection.add(Base.D);
        collection.add(Base.E);
        assertTrue(instance.containsAll(collection));
        // Test 3
        collection.add(Base.H);
        assertFalse(instance.containsAll(collection));
    }

    /**
     * Test of addAll method, of class DAG<Base>.
     */
    @Test
    public void testAddAll() {
        System.out.println("addAll");
        // Test 1
        DAG<Base> instance = new DAG<Base>(Base.A, Base.B, Base.C);
        assertTrue(instance.addAll(new DAG<Base>(Base.G, Base.H)));
        assertFalse(instance.addAll(new DAG<Base>(Base.A, Base.B)));
        assertTrue(instance.equals(
                new DAG<Base>(Base.A, Base.B, Base.C, Base.G, Base.H)));
    }

    /**
     * Test of removeAll method, of class DAG<Base>.
     */
    @Test
    public void testRemoveAll() {
        System.out.println("removeAll");
        // Test 1
        DAG<Base> instance = new DAG<Base>(Base.A, Base.B, Base.C, Base.G);
        assertTrue(instance.removeAll(new DAG<Base>(Base.G, Base.H)));
        assertTrue(instance.equals(
                new DAG<Base>(Base.A, Base.B, Base.C)));
        // Test 2
        instance = new DAG<Base>(Base.A, Base.B, Base.C, Base.G);
        assertFalse(instance.removeAll(new DAG<Base>(Base.H)));
    }

    /**
     * Test of retainAll method, of class DAG<Base>.
     */
    @Test
    public void testRetainAll() {
        System.out.println("retainAll");
        DAG<Base> instance = new DAG<Base>(Base.A, Base.B, Base.C, Base.D);
        assertTrue(instance.retainAll(new DAG<Base>(Base.B, Base.C, Base.G, Base.H)));
        assertEquals(new DAG<Base>(Base.B, Base.C), instance);
    }

    /**
     * Test of clear method, of class DAG<Base>.
     */
    @Test
    public void testClear() {
        System.out.println("clear");
        DAG<Base> instance = new DAG<Base>(setABCDEFG);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

}