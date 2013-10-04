/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

package org.broad.igv.util;

import org.broad.igv.util.collections.IntArrayList;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: jrobinso
 * Date: Jun 18, 2010
 * Time: 7:55:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArrayMemoryTest {
    private static final int size = 10000000;

    @Before
    public void setUp() throws Exception {
        System.gc();
        Runtime.getRuntime().gc();
    }

    //GetObjectSize is not particularly accurate, because it's not recursive
    //TODO 3rd party implementations of things like IntArrayList exist. Use them?
    /**NOTE:
     * JavaAgent has to be put in a jar and included as instrumentation in the
       junit tests, as in:
       <jvmarg value="-javaagent:${testlib.dir}/JavaAgent.jar"/>
      for this to work.
    **/
    @Ignore
    @Test
    public void compareMemory() throws Exception {

        IntArrayList tmp = makeIntArrayList();
        ArrayList<Integer> tmp2 = makeArrayList();
        long memIntArrList = 0;
        long memArrList = 0;

        for (int ii = 0; ii < size; ii++) {
            memArrList += JavaAgent.getObjectSize(tmp2.get(ii));
            memIntArrList += JavaAgent.getObjectSize(tmp.get(ii));
        }
        assertTrue(memIntArrList < memArrList);

    }


    public static IntArrayList makeIntArrayList() {
        IntArrayList list = new IntArrayList();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    public static ArrayList<Integer> makeArrayList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

}
