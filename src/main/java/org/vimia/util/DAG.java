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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * DAG is an implementation of a <a href="http://en.wikipedia.org/wiki/Directed_acyclic_graph">directed acyclic graph</a>
 * that conforms to {@link Collection}.
 *
 * @param <B> DOCUMENT ME!
 */
public class DAG<B>
    extends HashSet<B>
    implements DirectedAcyclicGraph<B> {
    //~ Instance fields ========================================================

    private NodeMap     nodeMap;
    private NodePathSet nodePathSet;

    //~ Constructors ===========================================================

    /**
     * Creates a new DAG object.
     */
    public DAG() {
        nodeMap = new NodeMap();
        nodePathSet = new NodePathSet();
    }

    /**
     * Creates a new DAG object, which initially contains the members specified
     * its parameter list.
     *
     * @param member list of members, which may be empty, which are initially contained
     * by the DAG object.
     */
    public DAG(B... member) {
        this();

        for (B m : member) {
            add(m);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param collection
     */
    public DAG(Collection<?extends B> collection) {
        this();
        addAll(collection);
    }

    //~ Methods ================================================================

    /**
     * DOCUMENT ME!
     *
     * @param before
     * @param after
     *
     * @throws DAGCycleException
     */
    public void setBefore(B before, B after)
                   throws DAGCycleException {
        assertNoLoop(before, after);
        nodeMap.get(before).addAfter(after);
        nodeMap.get(after).addBefore(before);
    }

    /**
     * DOCUMENT ME!
     *
     * @param member
     *
     * @return
     */
    public Set<B> getBefore(B member) {
        return nodeMap.get(member).getBeforeSet();
    }

    /**
     * DOCUMENT ME!
     *
     * @param before
     * @param after
     *
     * @return
     *
     * @throws DAGNotAMemberException DOCUMENT ME!
     */
    public boolean isBefore(B before, B after) {
        // after doesn't change during recursion, so check it once here
        if (!nodeMap.containsKey(after)) {
            throw new DAGNotAMemberException(after);
        }

        return isBeforeWorker(before, after);
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public List<B> getSortedList() {
        return new TopologicalSort(nodeMap).sort();
    }

    @Override
    public boolean add(B member) {
        if (member == null) {
            throw new DAGNullNotAllowedException();
        }

        if (!contains(member)) {
            return (nodeMap.put(member, new Node(member)) == null) &
                   super.add(member);
        }

        return false;
    }

    @Override
    public boolean remove(Object member) {
        if (contains((B) member)) {
            return (nodeMap.remove((B) member) != null) &
                   super.remove((B) member);
        }

        return false;
    }

    private boolean isBeforeWorker(B before, final B after) {
        // before does change during recursion, os check it every time
        if (!nodeMap.containsKey(before)) {
            throw new DAGNotAMemberException(before);
        }

        Set<B> followers = nodeMap.get(before).getAfterSet();

        if (followers.contains(after)) {
            return true;
        } else {
            for (B follower : followers) {
                if (isBeforeWorker(follower, after)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void assertNoLoop(B before, B after)
                       throws DAGCycleException {
        if (before != after) {
            // recursion terminates when afterSet.isEmpty(), i.e. the following loop
            // has zero iterations
            for (B follower : nodeMap.get(after).getAfterSet()) {
                assertNoLoop(before, follower);
            }

            return;
        }

        throw new DAGCycleException(before, after);
    }

    //~ Inner Classes ==========================================================

    /**
     * Node contains the beforeSet and afterSet of a set member, i.e.
     * the set of other members which are immediate predecessors of the
     * member, and the set of other members which are immediate followers of
     * the member.
    */
    protected class Node {
        //~ Instance fields ====================================================

        B              self;
        private Set<B> afterSet;
        private Set<B> beforeSet;

        //~ Constructors =======================================================

        /**
         * Make a copy of an initialNode
         *
         * @param node
         */
        protected Node(Node node) {
            beforeSet = node.beforeSet;
            afterSet = node.afterSet;
            self = node.self;
        }

        Node() {
        }

        Node(B member) {
            beforeSet = new HashSet<B>();
            afterSet = new HashSet<B>();
            self = member;
            notifyChange();
        }

        //~ Methods ============================================================

        /**
         * DOCUMENT ME!
         *
         * @return
         */
        public Set<B> getAfterSet() {
            return afterSet;
        }

        /**
         * DOCUMENT ME!
         *
         * @return
         */
        public Set<B> getBeforeSet() {
            return beforeSet;
        }

        /**
         * DOCUMENT ME!
         *
         * @return
         */
        public B getSelf() {
            return self;
        }

        /**
         * DOCUMENT ME!
         *
         * @param member
         *
         * @return
         */
        public boolean addAfter(B member) {
            boolean result = afterSet.add(member);

            if (result) {
                notifyChange();
            }

            return result;
        }

        /**
         * DOCUMENT ME!
         *
         * @param member
         *
         * @return
         */
        public boolean addBefore(B member) {
            boolean result = beforeSet.add(member);

            if (result) {
                notifyChange();
            }

            return result;
        }

        /**
         * DOCUMENT ME!
         *
         * @param member
         *
         * @return
         */
        public boolean removeAfter(B member) {
            boolean result = afterSet.remove(member);

            if (result) {
                notifyChange();
            }

            return result;
        }

        /**
         * DOCUMENT ME!
         *
         * @param member
         *
         * @return
         */
        public boolean removeBefore(B member) {
            boolean result = beforeSet.remove(member);

            if (result) {
                notifyChange();
            }

            return result;
        }

        private void notifyChange() {
            nodePathSet.update(this);
        }
    }

    /**
     * NodeMap contains all the members of the PartiallyOrderedSetX.
    */
    protected class NodeMap
        extends HashMap<B, Node> {
        //~ Methods ============================================================

        @Override
        public Node get(Object key) {
            Node result = super.get((B) key);

            if (result == null) {
                throw new DAGNotAMemberException(key);
            }

            return result;
        }

        @Override
        public Node remove(Object key) {
            Node member = get((B) key);

            // remove any references to this key
            // remove it from afterSet of antecedents
            for (B before : member.getBeforeSet()) {
                get(before).removeAfter((B) key);
            }

            // remove it from the beforeSet of followers
            for (B after : member.getAfterSet()) {
                get(after).removeBefore((B) key);
            }

            return super.remove((B) key);
        }
    }

    /**
     * NodePathSet contains all elements of DAG where
     * beforeSet.isEmpty(), i.e. the first elements of all the sublists of
     * DAG.
    */
    protected class NodePathSet
        extends HashSet<B> {
        //~ Constructors =======================================================

        NodePathSet() {
        }

        //~ Methods ============================================================

        /**
         * DOCUMENT ME!
         *
         * @param node
         */
        public void update(Node node) {
            if (node.getBeforeSet().isEmpty()) {
                add(node.self);
            } else {
                remove(node.self);
            }
        }
    }

    /**
     * 
    */
    protected class TopologicalSort
        extends HashMap<B, Node> {
        //~ Constructors =======================================================

        TopologicalSort(NodeMap nodeMap) {
            for (B member : nodeMap.keySet()) {
                // create a copy to preserve the user's original
                put(member, new Node(nodeMap.get(member)));
            }
        }

        //~ Methods ============================================================

        List<B> sort() {
            return sort(nodePathSet);
        }

        List<B> sort(NodePathSet nodePathSet) {
            List<B> result       = new ArrayList<B>();
            List<B> initialNodes = new LinkedList<B>();
            initialNodes.addAll(nodePathSet);

            while (!initialNodes.isEmpty()) {
                B initialNode = initialNodes.remove(0);
                result.add(initialNode);

                Iterator<B> iterator = get(initialNode).getAfterSet().iterator();

                while (iterator.hasNext()) {
                    B after = iterator.next();
                    //get(initialNode).removeAfter(after);
                    iterator.remove();
                    get(after).getBeforeSet().remove(initialNode);

                    if (get(after).getBeforeSet().isEmpty()) {
                        initialNodes.add(after);
                    }
                }
            }

            return result;
        }
    }
}
