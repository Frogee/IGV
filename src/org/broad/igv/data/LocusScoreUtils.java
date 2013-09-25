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

package org.broad.igv.data;

import org.broad.igv.feature.FeatureUtils;
import org.broad.igv.feature.LocusScore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @deprecated Use {@link FeatureUtils}
 * @author jrobinso
 */
public class LocusScoreUtils {


    /**
     * Segregate a list of possibly overlapping features into a list of
     * non-overlapping lists of features.
     */
    public static List<List<LocusScore>> segreateFeatures(List<LocusScore> features, double scale) {

        // Create a list to hold the lists of non-overlapping features
        List<List<LocusScore>> segmentedLists = new ArrayList();

        // Make a working copy of the original list.
        List<LocusScore> workingList = new LinkedList(features);
        FeatureUtils.sortFeatureList(workingList);

        // Loop until all features have been allocated to non-overlapping lists
        while (workingList.size() > 0) {

            List<LocusScore> nonOverlappingFeatures = new LinkedList();
            List<LocusScore> overlappingFeatures = new LinkedList();

            // Prime the loop with the first feature, it can't overlap itself
            LocusScore f1 = workingList.remove(0);
            nonOverlappingFeatures.add(f1);
            while (workingList.size() > 0) {
                LocusScore f2 = workingList.remove(0);
                int scaledStart = (int) (f2.getStart() / scale);
                int scaledEnd = (int) (f1.getEnd() / scale);
                if (scaledStart > scaledEnd) {
                    nonOverlappingFeatures.add(f2);
                    f1 = f2;
                } else {
                    overlappingFeatures.add(f2);
                }
            }

            // Add the list of non-overlapping features and start again with whats left
            segmentedLists.add(nonOverlappingFeatures);
            workingList = overlappingFeatures;
        }
        return segmentedLists;
    }

    /**
     * Get the index of the feature just to the right of the given position.
     * If there is no feature to the right return -1;
     *
     * @param position
     * @param features
     * @return
     */
    public static LocusScore getFeatureAfter(double position, List<? extends LocusScore> features) {

        if (features.size() == 0 ||
                features.get(features.size() - 1).getStart() <= position) {
            return null;
        }

        int startIdx = 0;
        int endIdx = features.size();

        // Narrow the list to ~ 10
        while (startIdx != endIdx) {
            int idx = (startIdx + endIdx) / 2;
            double distance = features.get(idx).getStart() - position;
            if (distance <= 0) {
                startIdx = idx;
            } else {
                endIdx = idx;
            }
            if (endIdx - startIdx < 10) {
                break;
            }
        }

        // Now find feature
        for (int idx = startIdx; idx < features.size(); idx++) {
            if (features.get(idx).getStart() > position) {
                return features.get(idx);
            }
        }

        return null;

    }

    public static LocusScore getFeatureBefore(double position, List<? extends LocusScore> features) {

        if (features.size() == 0 ||
                features.get(features.size() - 1).getStart() <= position) {
            return null;
        }

        int startIdx = 0;
        int endIdx = features.size() - 1;

        while (startIdx != endIdx) {
            int idx = (startIdx + endIdx) / 2;
            double distance = features.get(idx).getStart() - position;
            if (distance <= 0) {
                startIdx = idx;
            } else {
                endIdx = idx;
            }
            if (endIdx - startIdx < 10) {
                break;
            }
        }

        if (features.get(endIdx).getStart() >= position) {
            for (int idx = endIdx; idx >= 0; idx--) {
                if (features.get(idx).getStart() < position) {
                    return features.get(idx);
                }
            }
        } else {
            for (int idx = endIdx + 1; idx < features.size(); idx++) {
                if (features.get(idx).getStart() >= position) {
                    return features.get(idx - 1);
                }

            }
        }
        return null;


    }

}
