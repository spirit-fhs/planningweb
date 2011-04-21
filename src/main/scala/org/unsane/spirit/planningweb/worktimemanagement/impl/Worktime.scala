package org.unsane.spirit.planningweb.worktimemanagement.impl

/**
 * This class is the representation of a worktime object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.Dozent
import planningweb.roommanagement.impl.Room

case class Worktime(dozent: Dozent,
                    timeSlots: List[TimeSlot],
                    rooms: List[Room])