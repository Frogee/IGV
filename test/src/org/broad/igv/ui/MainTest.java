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

package org.broad.igv.ui;

import org.broad.igv.feature.genome.GenomeListItem;
import org.broad.igv.feature.genome.GenomeManager;
import org.broad.igv.ui.panel.FrameManager;
import org.broad.igv.util.StringUtils;
import org.broad.igv.util.TestUtils;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import java.io.File;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Test of class main. In general will use this to see that IGV
 * starts properly, given startup parameters. Since we will be starting
 * IGV in a non-standard way, do NOT inherit from AbstractHeadedTest
 * User: jacob
 * Date: 2012/05/04
 */
public class MainTest {

    @Rule
    public TestRule testTimeout = new Timeout((int) 1e5);

    @BeforeClass
    public static void setUpClass() throws Exception {
        AbstractHeadedTest.assumeNotHeadless();
        TestUtils.setUpTestEnvironment();
    }

    @Before
    public void setUp() throws Exception{
        AbstractHeadedTest.stopGUI();
    }


    @After
    public void tearDown() throws Exception {
        AbstractHeadedTest.stopGUI();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        AbstractHeadedTest.tearDownClass();
    }


    /**
     * Test that loading IGV with a startup file and
     * locus loads that file and locus
     *
     * @throws Exception
     */
    @Test
    public void testFileLocusArgs() throws Exception {

        String trackName = "NA12878.SLX.sample.bam";
        String filePath = TestUtils.DATA_DIR + "bam/" + trackName;
        String genome = "hg18";
        String locus = "chr1:9,703,210-9,727,225";

        String[] args = new String[]{filePath, locus, "-g", genome};

        //Need to wait for IGV to load file, genome, and move to locus
        IGV igv = startMain(args, 60000);

        //System.out.println(IGV.getInstance());

        assertEquals(genome, igv.getGenomeManager().getGenomeId());
        TestUtils.assertTrackLoaded(igv, trackName);

        String actLocus = FrameManager.getDefaultFrame().getFormattedLocusString();
        assertEquals(locus, actLocus);
    }

    @Test
    public void testFileWithSpaces() throws Exception{
        String trackName = "test.wig";
        String filePath = TestUtils.DATA_DIR + "folder with spaces/" + trackName;
        String[] args = new String[]{filePath};

        //Need to wait for IGV to start and load file
        IGV igv = startMain(args, 10000);

        TestUtils.assertTrackLoaded(igv, trackName);
    }

    @Test
    public void testFileURLWithSpaces() throws Exception{
        String trackName = "test.wig";
        String dir = StringUtils.encodeURL("folder with spaces");
        String absFilePath = (new File(TestUtils.DATA_DIR)).getAbsolutePath();
        absFilePath = absFilePath.replace("\\", "/");
        String filePath = "file://" + absFilePath + "/" + dir + "/" + trackName;
        String[] args = new String[]{filePath};

        //Need to wait for IGV to start and load file
        IGV igv = startMain(args, 10000);

        TestUtils.assertTrackLoaded(igv, trackName);
    }

    @Test
    public void testRemoteURLWithSpaces() throws Exception{
        String trackName = "test.wig";
        String dir = StringUtils.encodeURL("folder with spaces");
        String absFilePath = "www.broadinstitute.org/igvdata/unit_test_files";
        String filePath = String.format("http://%s/%s/%s", absFilePath, dir, trackName);
        String[] args = new String[]{filePath};

        //Need to wait for IGV to start and load file
        IGV igv = startMain(args, 10000);

        TestUtils.assertTrackLoaded(igv, trackName);
    }

    /**
     * Test loading a genome not in the display list
     *
     * @throws Exception
     */
    @Test
    public void testLoadGenomeById() throws Exception {
        String genomeId = "mm7";
        Collection<GenomeListItem> genomeListItems = GenomeManager.getInstance().getGenomes();
        for (GenomeListItem gen : genomeListItems) {
            assertNotSame("Bad test setup, test genome in display list", gen.getId(), genomeId);
        }

        String[] args = new String[]{"-g", genomeId};
        IGV igv = startMain(args, 10000);

        assertEquals(igv.getGenomeManager().getGenomeId(), genomeId);
    }

    /**
     * Test loading a genome not in the display list, by full path
     *
     * @throws Exception
     */
    @Test
    public void testLoadGenomeByPath() throws Exception {
        String genomePath = TestUtils.DATA_DIR + "genomes/canFam2.unittest.genome";
        String genomeId = "canFam2.unittest";

        String[] args = new String[]{"-g", genomePath};
        IGV igv = startMain(args, 10000);

        assertEquals(igv.getGenomeManager().getGenomeId(), genomeId);
    }

    /**
     * Test loading a genome not in the display list, by full path
     *
     * @throws Exception
     */
    @Test
    public void testLoadFastaByPath() throws Exception {
        String genomePath = TestUtils.DATA_DIR + "fasta/ecoli_out.padded.fasta";
        String genomeId = genomePath;

        String[] args = new String[]{"-g", genomePath};
        IGV igv = startMain(args, 5000);

        assertEquals(igv.getGenomeManager().getGenomeId(), genomeId);
    }

    private IGV startMain(String[] args, long timeout) {
        Main.main(args);
        IGV igv = IGV.getInstance();
        assertTrue(igv.waitForNotify(timeout));
        return igv;
    }


}
