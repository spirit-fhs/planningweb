package org.unsane.spirit.planningweb.model

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._

object WorktimeManagerMongo extends WorktimeManagerMongo with MongoMetaRecord[WorktimeManagerMongo] {

}

/**
 * This class is the representation of a worktimemanager object in a MongoDB
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class WorktimeManagerMongo extends MongoRecord[WorktimeManagerMongo] with MongoId[WorktimeManagerMongo] {
  def meta = WorktimeManagerMongo
  object status extends BooleanField(this)
}