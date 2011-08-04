package org.unsane.spirit.planningweb.model

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._
import record.field.MongoCaseClassField

import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl._

object FHSDozentMongo extends FHSDozentMongo with MongoMetaRecord[FHSDozentMongo] {

}

/**
 * This class is the representation of a fhsdozent object in a MongoDB
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class FHSDozentMongo extends MongoRecord[FHSDozentMongo] with MongoId[FHSDozentMongo] {
  def meta = FHSDozentMongo
  object fhsId extends StringField(this, 100)
  object dozent extends MongoCaseClassField[FHSDozentMongo, Dozent](this)
}