package org.unsane.spirit.planningweb.model

import net.liftweb.mongodb._
import record.{MongoRecord, MongoId, MongoMetaRecord}
import net.liftweb.record.field._
import record.field.{MongoCaseClassListField, MongoCaseClassField}

import org.unsane.spirit.planningweb
import planningweb.lecturemanagement.impl._

object LectureMongo extends LectureMongo with MongoMetaRecord[LectureMongo] {

}

/**
 * This class is the representation of a lecture object in a MongoDB
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class LectureMongo extends MongoRecord[LectureMongo] with MongoId[LectureMongo] {
  def meta = LectureMongo
  object name extends StringField(this, 100)
  object lectureType extends MongoCaseClassField[LectureMongo, LectureType](this)
  object courseInfos extends MongoCaseClassListField[LectureMongo, CourseInformation](this)
  object hasLectureTogetherWith extends MongoCaseClassListField[LectureMongo, LectureRelationship](this)
  object hasTutorialTogetherWith extends MongoCaseClassListField[LectureMongo, LectureRelationship](this)
  object hoursOfLecture extends IntField(this)
  object hoursOfTutorial extends IntField(this)
  object inSummerSemester extends BooleanField(this)
  object inWinterSemester extends BooleanField(this)
}