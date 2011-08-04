package org.unsane.spirit.planningweb.model

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._
import record.field.{MongoCaseClassField, MongoCaseClassListField}
import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.Dozent
import planningweb.worktimemanagement.impl.TimeSlot
//import planningweb.roommanagement.impl.Room

object WorktimeMongo extends WorktimeMongo with MongoMetaRecord[WorktimeMongo] {

}

/**
 * This class is the representation of a worktime object in a MongoDB
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class WorktimeMongo extends MongoRecord[WorktimeMongo] with MongoId[WorktimeMongo] {
  def meta = WorktimeMongo
  object dozent extends MongoCaseClassField[WorktimeMongo,Dozent](this)
  object timeSlots extends MongoCaseClassListField[WorktimeMongo,TimeSlot](this)
  //object rooms extends MongoCaseClassListField[WorktimeMongo,Room](this)
  object notes extends StringField(this, 1000)
}