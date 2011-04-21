package org.unsane.spirit.planningweb.model

/**
 * This class is the representation of a dozenttype object in a MongoDB
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


object DozentTypeMongo extends DozentTypeMongo with MongoMetaRecord[DozentTypeMongo] {

}

class DozentTypeMongo extends MongoRecord[DozentTypeMongo] with MongoId[DozentTypeMongo] {
  def meta = DozentTypeMongo
  object name extends StringField(this, 100)
  object requiredTime extends IntField(this)
  object hasLectureship extends BooleanField(this)
}