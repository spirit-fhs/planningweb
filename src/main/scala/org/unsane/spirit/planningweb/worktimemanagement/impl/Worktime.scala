package org.unsane.spirit.planningweb.worktimemanagement.impl

import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.Dozent
import planningweb.roommanagement.impl.Room

/**
 * This class is the representation of a worktime object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
case class Worktime(dozent: Dozent,
                    timeSlots: List[TimeSlot],
                    notes: String)