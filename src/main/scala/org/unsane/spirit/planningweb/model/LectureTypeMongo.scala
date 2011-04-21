package org.unsane.spirit.planningweb.model

/**
 * This class is the representation of a lecturetype object in a MongoDB
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


object LectureTypeMongo extends LectureTypeMongo with MongoMetaRecord[LectureTypeMongo] {

}
class LectureTypeMongo extends MongoRecord[LectureTypeMongo] with MongoId[LectureTypeMongo] {
  def meta = LectureTypeMongo
  object name extends StringField(this, 100)
}