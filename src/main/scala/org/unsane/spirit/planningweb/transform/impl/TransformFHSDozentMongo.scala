package org.unsane.spirit.planningweb.transform.impl

/**
 * This class is the implementation of ITransform interface for a FHSDozent object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb._
import transform.{ITransform, TransformFactory}
import model.{FHSDozentMongo,WorktimeMongo}
import dozentmanagement.impl._
import net.liftweb.json.JsonDSL._
import net.liftweb.http.S


class TransformFHSDozentMongo extends ITransform {

  /**
   * stores a FHSDozent object in MongoDB
   *
   * @param obj is a oject of type FHSDozent
   */
  def store (obj: AnyRef) {
    val fhsdozent = obj.asInstanceOf[FHSDozent]
    FHSDozentMongo.createRecord.fhsId(fhsdozent.fhsId)
                               .dozent(fhsdozent.dozent).save
  }

  /**
   * loads a list of FHSDozent objects from MongoDB
   *
   * @return a list of objects of type FHSDozent
   */
  def load : List[FHSDozent] = {
    val fromDB = FHSDozentMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (fhsdozent => FHSDozent(fhsdozent.fhsId.value,
                                         fhsdozent.dozent.value))
    }
  }

  /**
   * deletes a FHSDozent object from MongoDB
   *
   * @param obj is a oject of type FHSDozent
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[FHSDozent]
    val toDelete = FHSDozentMongo.findAll(("fhsId" -> objToDel.fhsId))
    val worktimes = WorktimeMongo.findAll(("dozent" -> ("name" -> objToDel.dozent.name) ~
                                                       ("reasonSelfManagement" -> objToDel.dozent.reasonSelfManagement) ~
                                                       ("timeSelfManagement" -> objToDel.dozent.timeSelfManagement) ~
                                                       ("typeD" -> ("name" -> objToDel.dozent.typeD.name) ~
                                                                   ("requiredTime" -> objToDel.dozent.typeD.requiredTime) ~
                                                                   ("hasLectureship" -> objToDel.dozent.typeD.hasLectureship)
                                                       )
                                           )
                                          )

    val toDeleteWorktimes = worktimes.filter(_.dozent.value.name == objToDel.dozent.name)

    toDelete map (fhsdozent => if (!fhsdozent.delete_!) error("Could not delete: " + fhsdozent.toString))
    toDeleteWorktimes map (worktime => if (!worktime.delete_!) error("Could not delete: " + worktime.toString))
  }

  /**
   * changes a FHSDozent object in MongoDB
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[FHSDozent]
    val objAfter = after.asInstanceOf[FHSDozent]

    val toChange = FHSDozentMongo.findAll(("fhsId" -> objToChange.fhsId))

    toChange map (fd => FHSDozentMongo.update(("fhsId" -> fd.fhsId.value) ~
                                              ("dozent" -> ("name" -> fd.dozent.value.name) ~
                                                           ("reasonSelfManagement" -> fd.dozent.value.reasonSelfManagement) ~
                                                           ("timeSelfManagement" -> fd.dozent.value.timeSelfManagement) ~
                                                           ("typeD" -> ("name" -> fd.dozent.value.typeD.name) ~
                                                                       ("requiredTime" -> fd.dozent.value.typeD.requiredTime) ~
                                                                       ("hasLectureship" -> fd.dozent.value.typeD.hasLectureship)
                                                           )
                                              ),
                                              ("fhsId" -> objAfter.fhsId) ~
                                              ("dozent" -> ("name" -> objAfter.dozent.name) ~
                                                           ("reasonSelfManagement" -> objAfter.dozent.reasonSelfManagement) ~
                                                           ("timeSelfManagement" -> objAfter.dozent.timeSelfManagement) ~
                                                           ("typeD" -> ("name" -> objAfter.dozent.typeD.name) ~
                                                                       ("requiredTime" -> objAfter.dozent.typeD.requiredTime) ~
                                                                       ("hasLectureship" -> objAfter.dozent.typeD.hasLectureship)
                                                           )
                                              )
                                             )
                 )
  }
}