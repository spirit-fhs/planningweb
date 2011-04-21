package org.unsane.spirit.planningweb.model

/**
 * This class is the representation of a course object in a MongoDB
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
import record.field.MongoCaseClassListField

import org.unsane.spirit.planningweb
import planningweb.coursemanagement.impl._


object CourseMongo extends CourseMongo with MongoMetaRecord[CourseMongo] {

}

class CourseMongo extends MongoRecord[CourseMongo] with MongoId[CourseMongo] {
  def meta = CourseMongo
  object name extends StringField(this, 100)
  object shortcut extends StringField(this, 100)
  object semesters extends MongoCaseClassListField[CourseMongo, Semester](this)
}