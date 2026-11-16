/**
 * Copyright (c) 2026, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * SPDX-License-Identifier: MPL-2.0
 */
package com.powsybl.cgmes.conversion.export;

import javax.xml.stream.XMLStreamException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A sink that collects the statements and sends them later to another sink.
 * It keeps one statement for each CGMES object: when a statement arrives for an object that it already has,
 * the new statement replaces the previous one, and the object keeps its first position.
 *
 * @param <T> the type of the statements
 * @author Marcos de Miguel {@literal <demiguelm at aia.es>}
 */
public final class DeferredCgmesStatementSink<T extends CgmesStatement> implements CgmesStatementSink<T> {

    private final Map<String, T> statements = new LinkedHashMap<>();

    @Override
    public void accept(T statement) {
        statements.put(statement.rdfId(), statement);
    }

    /**
     * The collected statements, one for each CGMES object, in the order of their first arrival.
     */
    public Collection<T> getStatements() {
        return statements.values();
    }

    /**
     * Send all the collected statements to the given sink, in the order of their first arrival.
     */
    public void sendAll(CgmesStatementSink<T> sink) throws XMLStreamException {
        for (T statement : statements.values()) {
            sink.accept(statement);
        }
    }
}
