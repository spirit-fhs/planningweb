package org.unsane.spirit.planningweb.worktimemanagement.impl

/**
 * This class is the representation of a timeslot object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

case class TimeSlot(day:String, time: Int, isWishtime: Boolean, isAvailableTime: Boolean)
