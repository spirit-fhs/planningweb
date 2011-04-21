package org.unsane.spirit.planningweb.roommanagement.impl

/**
 * This class is the representation of a room object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

case class Room(building: String,
                number: String,
                roomSize: Int,
                roomEquipment: List[String])