package org.unsane.spirit.planningweb.model

/**
 * This class is the representation of a worktimemanager object in a MongoDB
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


object WorktimeManagerMongo extends WorktimeManagerMongo with MongoMetaRecord[WorktimeManagerMongo] {

}

class WorktimeManagerMongo extends MongoRecord[WorktimeManagerMongo] with MongoId[WorktimeManagerMongo] {
  def meta = WorktimeManagerMongo
  object status extends BooleanField(this)
}