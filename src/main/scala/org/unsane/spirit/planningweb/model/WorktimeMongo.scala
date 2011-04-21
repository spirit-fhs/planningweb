package org.unsane.spirit.planningweb.model

/**
 * This class is the representation of a worktime object in a MongoDB
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._
import record.field.{MongoCaseClassField, MongoCaseClassListField}
import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.Dozent
import planningweb.worktimemanagement.impl.TimeSlot
import planningweb.roommanagement.impl.Room


object WorktimeMongo extends WorktimeMongo with MongoMetaRecord[WorktimeMongo] {

}

class WorktimeMongo extends MongoRecord[WorktimeMongo] with MongoId[WorktimeMongo] {
  def meta = WorktimeMongo
  object dozent extends MongoCaseClassField[WorktimeMongo,Dozent](this)
  object timeSlots extends MongoCaseClassListField[WorktimeMongo,TimeSlot](this)
  object rooms extends MongoCaseClassListField[WorktimeMongo,Room](this)
}