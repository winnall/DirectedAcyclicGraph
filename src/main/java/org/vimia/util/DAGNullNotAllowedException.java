/*
 * Copyright 2009 Stephen Winnall, CH-8143 Stallikon. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the Licence.
 * You may obtain a copy of the Licence at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 */

/*
 * All matters arising in law from the use of this software are subject
 * exclusively to the jurisdiction and venue of the courts located in
 * Zurich, Switzerland. See
 *
 *	http://www.vimia.org/licences/JURISDICTION-1.0
 *
 */



package org.vimia.util;

/**
 * 
DOCUMENT ME!
 *
 * @author steve
*/
public class DAGNullNotAllowedException
    extends RuntimeException {
    //~ Constructors ===========================================================

    /**
     * Creates a new instance of
     * <code>DAGNullNotAllowedException</code> without detail message.
     */
    public DAGNullNotAllowedException() {
    }

    /**
     * Constructs an instance of
     * <code>DAGNullNotAllowedException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public DAGNullNotAllowedException(String msg) {
        super(msg);
    }
}
