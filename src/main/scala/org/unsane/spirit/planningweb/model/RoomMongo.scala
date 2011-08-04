package org.unsane.spirit.planningweb.model

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._
import record.field.MongoListField

object RoomMongo extends RoomMongo with MongoMetaRecord[RoomMongo] {

}

/**
 * This class is the representation of a room object in a MongoDB
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class RoomMongo extends MongoRecord[RoomMongo] with MongoId[RoomMongo] {
  def meta = RoomMongo
  object building extends StringField(this, 100)
  object number extends StringField(this, 100)
  object roomSize extends IntField(this)
  object roomEquipment extends MongoListField[RoomMongo,String](this)
}