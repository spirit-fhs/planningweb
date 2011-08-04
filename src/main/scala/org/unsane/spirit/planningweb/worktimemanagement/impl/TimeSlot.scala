package org.unsane.spirit.planningweb.worktimemanagement.impl

/**
 * This class is the representation of a timeslot object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

case class TimeSlot(day:String, time: Int, isWishtime: Boolean, isAvailableTime: Boolean)
