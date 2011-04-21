package org.unsane.spirit.planningweb.transform.impl

/**
 * This class is the implementation of ITransform interface for a Lecture object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb._
import transform.{ITransform, TransformFactory}
import model.LectureMongo
import lecturemanagement.impl._
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.JObject


class TransformLectureMongo extends ITransform {

  /**
   * stores a Lecture object in MongoDB
   *
   * @param obj is a oject of type Lecture
   */
  def store (obj: AnyRef) {
    val lecture = obj.asInstanceOf[Lecture]
    LectureMongo.createRecord.name(lecture.name)
                             .lectureType(lecture.lectureType)
                             .courseInfos(lecture.courseInfos)
                             .hasLectureTogetherWith(lecture.hasLectureTogetherWith)
                             .hasTutorialTogetherWith(lecture.hasTutorialTogetherWith)
                             .hoursOfLecture(lecture.hoursOfLecture)
                             .hoursOfTutorial(lecture.hoursOfTutorial)
                             .inSummerSemester(lecture.inSummerSemester)
                             .inWinterSemester(lecture.inWinterSemester).save
  }

  /**
   * loads a list of Lecture objects from MongoDB
   *
   * @return a list of objects of type Lecture
   */
  def load : List[Lecture] = {
    val fromDB = LectureMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (lecture => Lecture(lecture.name.value,
                                     lecture.lectureType.value,
                                     lecture.courseInfos.value,
                                     lecture.hasLectureTogetherWith.value,
                                     lecture.hasTutorialTogetherWith.value,
                                     lecture.hoursOfLecture.value,
                                     lecture.hoursOfTutorial.value,
                                     lecture.inSummerSemester.value,
                                     lecture.inWinterSemester.value))
    }
  }

  /**
   * deletes a Lecture object from MongoDB
   *
   * @param obj is a oject of type Lecture
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[Lecture]
    val toDelete = LectureMongo.findAll(("name" -> objToDel.name) ~
                                        ("lectureType" -> ("name" -> objToDel.lectureType.name)) ~
                                        ("hoursOfLecture" -> objToDel.hoursOfLecture) ~
                                        ("hoursOfTutorial" -> objToDel.hoursOfTutorial) ~
                                        ("inSummerSemester" -> objToDel.inSummerSemester) ~
                                        ("inWinterSemester" -> objToDel.inWinterSemester))

    toDelete map (lecture => if (!lecture.delete_!) error("Could not delete: " + lecture.toString))
  }

  /**
   * changes a Lecture object in MongoDB
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[Lecture]
    val objAfter = after.asInstanceOf[Lecture]
    val toChange = LectureMongo.findAll(("name" -> objToChange.name) ~
                                        ("lectureType" -> ("name" -> objToChange.lectureType.name)) ~
                                        ("hoursOfLecture" -> objToChange.hoursOfLecture) ~
                                        ("hoursOfTutorial" -> objToChange.hoursOfTutorial) ~
                                        ("inSummerSemester" -> objToChange.inSummerSemester) ~
                                        ("inWinterSemester" -> objToChange.inWinterSemester))

    toChange map(lecture => if (!lecture.delete_!) error("Could not update: " + lecture.toString))

    toChange map( _ => LectureMongo.createRecord.name(objAfter.name)
                                                .lectureType(objAfter.lectureType)
                                                .courseInfos(objAfter.courseInfos)
                                                .hasLectureTogetherWith(objAfter.hasLectureTogetherWith)
                                                .hasTutorialTogetherWith(objAfter.hasTutorialTogetherWith)
                                                .hoursOfLecture(objAfter.hoursOfLecture)
                                                .hoursOfTutorial(objAfter.hoursOfTutorial)
                                                .inSummerSemester(objAfter.inSummerSemester)
                                                .inWinterSemester(objAfter.inWinterSemester).save)
  }
}