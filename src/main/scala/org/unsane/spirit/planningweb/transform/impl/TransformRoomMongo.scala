package org.unsane.spirit.planningweb.transform.impl

/**
 * This class is the implementation of ITransform interface for a Room object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb._
import transform.{ITransform, TransformFactory}
import roommanagement.impl.Room
import model.RoomMongo
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.JObject


class TransformRoomMongo extends ITransform {
  /**
   * stores a Room object in MongoDB
   *
   * @param obj is a oject of type Room
   */
  def store (obj: AnyRef) {
    val room = obj.asInstanceOf[Room]
    RoomMongo.createRecord.building(room.building)
                          .number(room.number)
                          .roomSize(room.roomSize)
                          .roomEquipment(room.roomEquipment).save
  }

  /**
   * loads a list of Room objects from MongoDB
   *
   * @return a list of objects of type Mongo
   */
  def load : List[Room] = {
    val fromDB = RoomMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (room => Room(room.building.value,
                               room.number.value,
                               room.roomSize.value,
                               room.roomEquipment.value))
    }
  }

  /**
   * deletes a Room object from MongoDB
   *
   * @param obj is a oject of type Room
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[Room]
    val toDelete = RoomMongo.findAll(("building" -> objToDel.building) ~
                                     ("number" -> objToDel.number) ~
                                     ("roomSize" -> objToDel.roomSize))

    toDelete map (course => if (!course.delete_!) error("Could not delete: " + course.toString))
  }

  /**
   * changes a Room object in MongoDB
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[Room]
    val objAfter = after.asInstanceOf[Room]
    val toChange = RoomMongo.findAll(("building" -> objToChange.building) ~
                                     ("number" -> objToChange.number) ~
                                     ("roomSize" -> objToChange.roomSize))

    toChange map(room => if (!room.delete_!) error("Could not update: " + room.toString))

    toChange map( _ => RoomMongo.createRecord.building(objAfter.building)
                                             .number(objAfter.number)
                                             .roomSize(objAfter.roomSize)
                                             .roomEquipment(objAfter.roomEquipment).save)
  }
}