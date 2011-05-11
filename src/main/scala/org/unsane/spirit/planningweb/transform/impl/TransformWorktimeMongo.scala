package org.unsane.spirit.planningweb.transform.impl

/**
 * This class is the implementation of ITransform interface for a Worktime object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb._
import transform.{ITransform, TransformFactory}
import model.WorktimeMongo
import dozentmanagement.impl.{Dozent, DozentType}
//import roommanagement.impl.Room
import worktimemanagement.impl.{Worktime,TimeSlot}
import net.liftweb.json.JsonDSL._

class TransformWorktimeMongo extends ITransform {

  /**
   * stores a Worktime object in MongoDB
   *
   * @param obj is a oject of type Worktime
   */
  def store (obj: AnyRef) {
    val worktime = obj.asInstanceOf[Worktime]
    WorktimeMongo.createRecord.dozent(worktime.dozent)
                              .timeSlots(worktime.timeSlots)
                              .notes(worktime.notes).save
  }

  /**
   * loads a list of Worktime objects from MongoDB
   *
   * @return a list of objects of type AnyRef
   */
  def load : List[Worktime] = {
    val fromDB = WorktimeMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (worktime => Worktime(worktime.dozent.value,
                                   worktime.timeSlots.value,
                                   worktime.notes.value))
    }
  }

  /**
   * deletes a Worktime object from MongoDB
   *
   * @param obj is a oject of type Worktime
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[Worktime]
    val toDelete = WorktimeMongo.findAll(("dozent" -> ("name" -> objToDel.dozent.name) ~
                                                      ("reasonSelfManagement" -> objToDel.dozent.reasonSelfManagement) ~
                                                      ("timeSelfManagement" -> objToDel.dozent.timeSelfManagement) ~
                                                      ("typeD" -> ("name" -> objToDel.dozent.typeD.name) ~
                                                                  ("requiredTime" -> objToDel.dozent.typeD.requiredTime) ~
                                                                  ("hasLectureship" -> objToDel.dozent.typeD.hasLectureship)
                                                      )
                                         )
                                        )

    toDelete map (worktime => if (!worktime.delete_!) error("Could not delete worktime of: " + worktime.toString))
  }

  /**
   * changes a Worktime object in MongoDB
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[Worktime]
    val objAfter = after.asInstanceOf[Worktime]
    val toChange = WorktimeMongo.findAll(("dozent" -> ("name" -> objToChange.dozent.name) ~
                                                      ("reasonSelfManagement" -> objToChange.dozent.reasonSelfManagement) ~
                                                      ("timeSelfManagement" -> objToChange.dozent.timeSelfManagement) ~
                                                      ("typeD" -> ("name" -> objToChange.dozent.typeD.name) ~
                                                                  ("requiredTime" -> objToChange.dozent.typeD.requiredTime) ~
                                                                  ("hasLectureship" -> objToChange.dozent.typeD.hasLectureship)
                                                      )
                                         )
                                        )

    toChange map(worktime => if (!worktime.delete_!) error("Could not update worktime of: " + worktime.toString))

    toChange map( _ => WorktimeMongo.createRecord.dozent(objAfter.dozent)
                                                 .timeSlots(objAfter.timeSlots)
                                                 .notes(objAfter.notes).save)
  }
}