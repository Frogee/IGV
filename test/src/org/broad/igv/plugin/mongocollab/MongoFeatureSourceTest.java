/*
 * Copyright (c) 2007-2013 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

package org.broad.igv.plugin.mongocollab;

import com.google.common.collect.Lists;
import com.mongodb.DBCollection;
import org.broad.igv.AbstractHeadlessTest;
import org.broad.igv.feature.BasicFeature;
import org.broad.igv.feature.NamedFeature;
import org.broad.igv.track.Track;
import org.broad.igv.ui.action.SearchCommand;
import org.broad.igv.util.TestUtils;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Starts a Mongo server instance, host must have mongo installed
 * See {@link MongoCollabPluginTest}
 * @author jacob
 * @date 2013-Sep-17
 */
public class MongoFeatureSourceTest extends AbstractHeadlessTest{

    private MongoCollabPlugin.Locator locator;
    private MongoFeatureSource source;
    private DBCollection collection;

    @BeforeClass
    public static void setUpClass() throws Exception{
        MongoCollabPluginTest.setUpClass();
    }

    @AfterClass
    public static void tearDownClass() throws Exception{
        MongoCollabPluginTest.tearDownClass();
    }

    @Before
    public void setUp() throws Exception {
        MongoCollabPluginTest.assumeTestDBRunning();
        super.setUp();
        this.locator = MongoCollabPluginTest.getTestLocator();
        this.collection = MongoCollabPluginTest.emptyTestCollection();
        this.source = new MongoFeatureSource(this.collection, true);
    }

    @After
    public void tearDown() throws Exception{
        super.tearDown();
        MongoCollabPlugin.closeMongo(locator.host, locator.port);
    }

    @Test
    public void testHasIndex() throws Exception{
        assertTrue(this.source.hasLocusIndex());

        this.source = new MongoFeatureSource(this.collection, false);
        assertTrue(this.source.hasLocusIndex());

        this.collection.dropIndexes();
        this.source = new MongoFeatureSource(this.collection, false);
        assertFalse(this.source.hasLocusIndex());
    }

    @Test
    public void testGetFeatures_chr() throws Exception{
        int inserted = MongoCollabPlugin.insertFeaturesFromFile(this.collection, TestUtils.DATA_DIR + "bed/test.bed");

        Iterator<DBFeature.IGVFeat> features = this.source.getFeatures("chr1", 0, Integer.MAX_VALUE);
        List<DBFeature.IGVFeat> chr1List = Lists.newArrayList(features);

        features = this.source.getFeatures("chr2", 0, Integer.MAX_VALUE);
        List<DBFeature.IGVFeat> chr2List = Lists.newArrayList(features);

        assertEquals(inserted, chr1List.size() + chr2List.size());

        TestUtils.assertFeatureIteratorSorted(chr1List.iterator());

        TestUtils.assertFeatureIteratorSorted(chr2List.iterator());
    }

    @Test
    public void testGetFeatures_start_01() throws Exception{
        int inserted = MongoCollabPlugin.insertFeaturesFromFile(this.collection, TestUtils.DATA_DIR + "bed/test.bed");

        Iterator<DBFeature.IGVFeat> features = this.source.getFeatures("chr1", 250, 100005);
        List<DBFeature.IGVFeat> list = Lists.newArrayList(features);

        assertEquals(2, list.size());
        TestUtils.assertFeatureIteratorSorted(list.iterator());
    }

    @Test
    public void testGetFeatures_start_02() throws Exception{
        int inserted = MongoCollabPlugin.insertFeaturesFromFile(this.collection, TestUtils.DATA_DIR + "bed/test.bed");

        Iterator<DBFeature.IGVFeat> features = this.source.getFeatures("chr1", 100005, 100008);
        List<DBFeature.IGVFeat> list_00 = Lists.newArrayList(features);

        assertEquals(1, list_00.size());
        assertEquals(100000, list_00.get(0).getStart());

        features = this.source.getFeatures("chr1", 100005, 200008);
        List<DBFeature.IGVFeat> list_01 = Lists.newArrayList(features);

        assertEquals(2, list_01.size());
        assertEquals(100000, list_01.get(0).getStart());
    }

    private void setupUnigene(){
        collection.drop();
        int inserted = MongoCollabPlugin.insertFeaturesFromFile(this.collection, TestUtils.DATA_DIR + "bed/Unigene.sample.bed");
        assert inserted > 0;

    }

    private NamedFeature getUnigeneTestFeature(){
        //Note the cases are incorrect, want to make sure matching is case-insensitive
        BasicFeature testFeat = new BasicFeature("chr2", 179908392, 179909870);
        testFeat.setName("hs.516555");
        return testFeat;
    }

    @Test
    public void testGetFeaturesByName() throws Exception{

        setupUnigene();
        NamedFeature testFeat = getUnigeneTestFeature();

        Collection<? extends NamedFeature> features = this.source.search(testFeat.getName(), 0);
        List<? extends NamedFeature> list = Lists.newArrayList(features);

        assertEquals(1, list.size());
        NamedFeature resFeat = list.get(0);

        TestUtils.assertNamedFeaturesEqual(testFeat, resFeat);
    }

    @Test
    public void testSearchCommand() throws Exception{
        setupUnigene();
        NamedFeature testFeat = getUnigeneTestFeature();

        //Need to call this to attach listener
        MongoFeatureSource.loadFeatureTrack(MongoCollabPluginTest.getTestLocator(), new ArrayList<Track>());

        String searchStr = testFeat.getName();
        SearchCommand cmd = new SearchCommand(null, searchStr, false);
        List<SearchCommand.SearchResult> list = cmd.runSearch(searchStr);

        assertEquals(1, list.size());
        SearchCommand.SearchResult result = list.get(0);

        assertEquals(SearchCommand.ResultType.FEATURE, result.getType());
        NamedFeature resFeat = result.getFeature();

        TestUtils.assertNamedFeaturesEqual(testFeat, resFeat);
    }
}
