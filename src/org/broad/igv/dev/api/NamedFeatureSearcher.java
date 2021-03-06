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

package org.broad.igv.dev.api;

import org.broad.igv.feature.NamedFeature;

import java.util.Collection;

/**
 * Service for finding features by their name.
 *
 * @author jacob
 * @date 2013-Oct-02
 * @api
 */
public interface NamedFeatureSearcher {

    /**
     *
     * @param name
     * @limit Maximum number of results to return. 0 (or less) = no limit
     * @return Search results. Should be an empty collection if not found,
     * null means the search could not be performed
     */
    public Collection<? extends NamedFeature> search(String name, int limit);
}
