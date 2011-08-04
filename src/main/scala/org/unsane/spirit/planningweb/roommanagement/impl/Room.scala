package org.unsane.spirit.planningweb.roommanagement.impl

/**
 * This class is the representation of a room object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

case class Room(building: String,
                number: String,
                roomSize: Int,
                roomEquipment: List[String])