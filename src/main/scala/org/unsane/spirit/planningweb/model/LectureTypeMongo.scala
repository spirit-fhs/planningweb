package org.unsane.spirit.planningweb.model

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._


object LectureTypeMongo extends LectureTypeMongo with MongoMetaRecord[LectureTypeMongo] {

}

/**
 * This class is the representation of a lecturetype object in a MongoDB
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class LectureTypeMongo extends MongoRecord[LectureTypeMongo] with MongoId[LectureTypeMongo] {
  def meta = LectureTypeMongo
  object name extends StringField(this, 100)
}