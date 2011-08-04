package org.unsane.spirit.planningweb.model

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._
import record.field.MongoCaseClassField
import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.DozentType

object DozentMongo extends DozentMongo with MongoMetaRecord[DozentMongo] {

}

/**
 * This class is the representation of a dozent object in a MongoDB
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class DozentMongo extends MongoRecord[DozentMongo] with MongoId[DozentMongo] {
  def meta = DozentMongo
  object name extends StringField(this, 100)
  object reasonSelfManagement extends StringField(this, 200)
  object timeSelfManagement extends DoubleField(this)
  object typeD extends MongoCaseClassField[DozentMongo,DozentType](this)
}