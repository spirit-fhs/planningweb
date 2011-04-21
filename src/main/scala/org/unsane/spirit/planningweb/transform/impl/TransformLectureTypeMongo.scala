package org.unsane.spirit.planningweb.transform.impl

/**
 * This class is the implementation of ITransform interface for a LectureType object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb._
import transform.ITransform
import model.{LectureTypeMongo,LectureMongo}
import lecturemanagement.impl.{LectureType,Lecture}
import net.liftweb.json.JsonDSL._
import net.liftweb.http.S


class TransformLectureTypeMongo extends ITransform {

  /**
   * stores a LectureType object in MongoDB
   *
   * @param obj is a oject of type LectureType
   */
  def store (obj: AnyRef) {
    val lectureType = obj.asInstanceOf[LectureType]
    LectureTypeMongo.createRecord.name(lectureType.name).save
  }

  /**
   * loads a list of LectureType objects from MongoDB
   *
   * @return a list of objects of type LectureType
   */
  def load : List[LectureType] = {
    val fromDB = LectureTypeMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (typeL => LectureType(typeL.name.value))
    }
  }

  /**
   * deletes a LectureType object from MongoDB
   *
   * @param obj is a oject of type LectureType
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[LectureType]
    val toDelete = LectureTypeMongo.findAll(("name" -> objToDel.name))

    val lectures =  LectureMongo.findAll(("lectureType" -> ("name" -> objToDel.name)))

    lectures match {
      case List() =>
        toDelete map (typeL => if (!typeL.delete_!) error("Could not delete: " + typeL.toString))
      case _ =>
        S.warning("Could not delete, because typ is already in use!")
    }
  }

  /**
   * changes a LectureType object in MongoDB
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[LectureType]
    val objAfter = after.asInstanceOf[LectureType]
    val toChange = LectureTypeMongo.findAll(("name" -> objToChange.name))

    toChange map (typeL => LectureTypeMongo.update(("name" -> typeL.name.value),
                                                   ("name" -> objAfter.name)))
  }
}