package org.unsane.spirit.planningweb.model

/**
 * This class is the representation of a dozent object in a MongoDB
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
import record.field.MongoCaseClassField
import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.DozentType


object DozentMongo extends DozentMongo with MongoMetaRecord[DozentMongo] {

}

class DozentMongo extends MongoRecord[DozentMongo] with MongoId[DozentMongo] {
  def meta = DozentMongo
  object name extends StringField(this, 100)
  object reasonSelfManagement extends StringField(this, 200)
  object timeSelfManagement extends DoubleField(this)
  object typeD extends MongoCaseClassField[DozentMongo,DozentType](this)
}