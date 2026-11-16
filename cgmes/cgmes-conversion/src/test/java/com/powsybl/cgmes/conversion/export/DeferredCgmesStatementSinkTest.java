/**
 * Copyright (c) 2026, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * SPDX-License-Identifier: MPL-2.0
 */
package com.powsybl.cgmes.conversion.export;

import com.powsybl.cgmes.conversion.export.CgmesRecords.SwitchSsh;
import com.powsybl.cgmes.conversion.export.CgmesRecords.TerminalSsh;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Marcos de Miguel {@literal <demiguelm at aia.es>}
 */
class DeferredCgmesStatementSinkTest {

    @Test
    void keepsOneStatementForEachObject() throws XMLStreamException {
        DeferredCgmesStatementSink<SshStatement> sink = new DeferredCgmesStatementSink<>();
        sink.accept(new TerminalSsh("T1", true));
        sink.accept(new SwitchSsh("Breaker", "SW1", false));
        sink.accept(new TerminalSsh("T1", false));

        // The second statement of T1 replaces the first one, and T1 keeps its first position
        List<SshStatement> expected = List.of(new TerminalSsh("T1", false), new SwitchSsh("Breaker", "SW1", false));
        assertEquals(expected, List.copyOf(sink.getStatements()));

        // The collected statements are sent in the same order
        List<SshStatement> sent = new ArrayList<>();
        sink.sendAll(sent::add);
        assertEquals(expected, sent);
    }
}
