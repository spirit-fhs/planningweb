package org.unsane.spirit.planningweb.transform.impl

import org.unsane.spirit.planningweb._
import transform.{ITransform, TransformFactory}
import model.CourseMongo
import coursemanagement.impl.{Course, Semester}
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.JObject

/**
 * This class is the implementation of ITransform interface for a Course object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class TransformCourseMongo extends ITransform {

  /**
   * stores a course object in MongoDB
   *
   * @param obj is a oject of type Course
   */
  def store (obj: AnyRef) {
    val course = obj.asInstanceOf[Course]
    CourseMongo.createRecord.name(course.name)
                            .shortcut(course.shortcut)
                            .semesters(course.semesters).save
  }

  /**
   * loads a list of Course objects from MongoDB
   *
   * @return a list of objects of type Course
   */
  def load: List[Course] = {
    val fromDB = CourseMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (course => Course(course.name.value,
                                   course.shortcut.value,
                                   course.semesters.value))
    }
  }

  /**
   * deletes a course object from MongoDB
   *
   * @param obj is a oject of type Course
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[Course]
    val toDelete = CourseMongo.findAll(("name" -> objToDel.name) ~
                                       ("shortcut" -> objToDel.shortcut))

    toDelete map (course => if (!course.delete_!) error("Could not delete: " + course.toString))
  }

  /**
   * changes a course object in MongoDB
   *
   * @param before is the couorse obejct to change
   * @param after is representation of the course object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[Course]
    val objAfter = after.asInstanceOf[Course]
    val toChange = CourseMongo.findAll(("name" -> objToChange.name) ~
                                       ("shortcut" -> objToChange.shortcut))

    toChange map(course => if (!course.delete_!) error("Could not update: " + course.toString))

    toChange map( _ => CourseMongo.createRecord.name(objAfter.name)
                                                             .shortcut(objAfter.shortcut)
                                                             .semesters(objAfter.semesters).save)
  }
}