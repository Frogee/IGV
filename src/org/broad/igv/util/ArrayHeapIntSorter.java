/*
 * Copyright (c) 2007-2011 by The Broad Institute of MIT and Harvard.  All Rights Reserved.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 *
 * THE SOFTWARE IS PROVIDED "AS IS." THE BROAD AND MIT MAKE NO REPRESENTATIONS OR
 * WARRANTES OF ANY KIND CONCERNING THE SOFTWARE, EXPRESS OR IMPLIED, INCLUDING,
 * WITHOUT LIMITATION, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, NONINFRINGEMENT, OR THE ABSENCE OF LATENT OR OTHER DEFECTS, WHETHER
 * OR NOT DISCOVERABLE.  IN NO EVENT SHALL THE BROAD OR MIT, OR THEIR RESPECTIVE
 * TRUSTEES, DIRECTORS, OFFICERS, EMPLOYEES, AND AFFILIATES BE LIABLE FOR ANY DAMAGES
 * OF ANY KIND, INCLUDING, WITHOUT LIMITATION, INCIDENTAL OR CONSEQUENTIAL DAMAGES,
 * ECONOMIC DAMAGES OR INJURY TO PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER
 * THE BROAD OR MIT SHALL BE ADVISED, SHALL HAVE OTHER REASON TO KNOW, OR IN FACT
 * SHALL KNOW OF THE POSSIBILITY OF THE FOREGOING.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.broad.igv.util;

public class ArrayHeapIntSorter {

    private int[] a;
    private int n;
    IntComparator c;

    public void sort(int[] a0) {
        a = a0;
        n = a.length;
        this.c = null;
        heapsort();
    }

    public void sort(int[] a0, IntComparator c) {
        a = a0;
        n = a.length;
        this.c = c;
        heapsort();
    }

    private void heapsort() {
        buildheap();
        while (n > 1) {
            n--;
            exchange(0, n);
            downheap(0);
        }
    }

    private void buildheap() {
        for (int v = n / 2 - 1; v >= 0; v--) {
            downheap(v);
        }
    }

    private void downheap(int v) {
        int w = 2 * v + 1;    // first descendant of v
        while (w < n) {
            if (w + 1 < n) // is there a second descendant?
            {
                if (c == null) {
                    if (a[w + 1] > a[w]) {
                        w++;
                    }
                } else {
                    if (c.compare(a[w + 1], a[w]) > 0) {
                        w++;
                    }
                }
            }
            // w is the descendant of v with maximum label

            if (c == null) {
                if (a[v] >= a[w]) {
                    return;
                }
            } else {
                if (c.compare(a[v], a[w]) >= 0) {
                    return;  // v has heap property
                }            // otherwise
            }
            exchange(v, w);  // exchange labels of v and w
            v = w;        // continue
            w = 2 * v + 1;
        }
    }

    private void exchange(int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }
} 
