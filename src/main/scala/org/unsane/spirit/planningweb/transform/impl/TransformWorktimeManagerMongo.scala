package org.unsane.spirit.planningweb.transform.impl

/**
 * This class is the implementation of ITransform interface for a WorktimeManager object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb._
import transform.ITransform
import model.WorktimeManagerMongo
import worktimemanagement.impl.WorktimeManager
import net.liftweb.json.JsonDSL._

class TransformWorktimeManagerMongo extends ITransform {

  /**
   * stores a WorktimeManager object in MongoDB
   *
   * @param obj is a oject of type WorktimeManager
   */
  def store (obj: AnyRef) {
    val worktimeManager = obj.asInstanceOf[WorktimeManager]
    WorktimeManagerMongo.createRecord.status(worktimeManager.status).save
  }

  /**
   * loads a list of WorktimeManager objects from MongoDB
   *
   * @return a list of objects of type WorktimeManger
   */
  def load : List[WorktimeManager] = {
    val fromDB = WorktimeManagerMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (worktimeManager => WorktimeManager(worktimeManager.status.value))
    }
  }

  /**
   * deletes a WorktimeManager object from MongoDB
   *
   * @param obj is a oject of type WorktimeManager
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[WorktimeManager]
    val toDelete = WorktimeManagerMongo.findAll(("status" -> objToDel.status))

    toDelete map (worktimeManager => if (!worktimeManager.delete_!) error("Could not delete: " + worktimeManager.toString))
  }

   /**
   * changes a WorktimeManager object in MongoDB
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[WorktimeManager]
    val objAfter = after.asInstanceOf[WorktimeManager]
    val toChange = WorktimeManagerMongo.findAll(("status" -> objToChange.status))

    toChange map (worktimeManager => WorktimeManagerMongo.update(("status" -> worktimeManager.status.value),
                                                                 ("status" -> objAfter.status)))
  }
}