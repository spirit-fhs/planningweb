package org.unsane.spirit.planningweb.transform.impl

/**
 * This class is the implementation of ITransform interface for a Dozent object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb._
import transform.{ITransform, TransformFactory}
import model.DozentMongo
import dozentmanagement.impl.{Dozent, DozentType}
import net.liftweb.json.JsonDSL._


class TransformDozentMongo extends ITransform {

  /**
   * stores a dozent object in MongoDB
   *
   * @param obj is a dozent oject of type Dozent
   */
  def store (obj: AnyRef) {
    val dozent = obj.asInstanceOf[Dozent]
    DozentMongo.createRecord.name(dozent.name)
                            .reasonSelfManagement(dozent.reasonSelfManagement)
                            .timeSelfManagement(dozent.timeSelfManagement)
                            .typeD(dozent.typeD).save
  }

  /**
   * loads a list of dozent objects from MongoDB
   *
   * @return a list of objects of type Dozent
   */
  def load : List[Dozent] = {
    val fromDB = DozentMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (dozent => Dozent(dozent.name.value,
                                   dozent.reasonSelfManagement.value,
                                   dozent.timeSelfManagement.value,
                                   dozent.typeD.value))
    }
  }


  /**
   * deletes a dozent object from MongoDB
   *
   * @param obj is a oject of type Dozent
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[Dozent]
    val toDelete = DozentMongo.findAll(("name" -> objToDel.name) ~
                                       ("reasonSelfManagement" -> objToDel.reasonSelfManagement) ~
                                       ("timeSelfManagement" -> objToDel.timeSelfManagement) ~
                                       ("typeD" -> ("name" -> objToDel.typeD.name) ~
                                                   ("requiredTime" -> objToDel.typeD.requiredTime) ~
                                                   ("hasLectureship" -> objToDel.typeD.hasLectureship)
                                       )
                                      )

    toDelete map (dozent => if (!dozent.delete_!) error("Could not delete: " + dozent.toString))
  }

  /**
   * changes a dozent object in MongoDB
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[Dozent]
    val objAfter = after.asInstanceOf[Dozent]
    val dozentTypes = TransformFactory.createTransformDozentType("mongoDB")
                                      .load
                                      .asInstanceOf[List[DozentType]]


    val toChange = DozentMongo.findAll(("name" -> objToChange.name) ~
                                       ("reasonSelfManagement" -> objToChange.reasonSelfManagement) ~
                                       ("timeSelfManagement" -> objToChange.timeSelfManagement) ~
                                       ("typeD" -> ("name" -> objToChange.typeD.name) ~
                                                   ("requiredTime" -> objToChange.typeD.requiredTime) ~
                                                   ("hasLectureship" -> objToChange.typeD.hasLectureship)
                                       )
                                      )

    if(dozentTypes contains(DozentType(objAfter.typeD.name,
                                       objAfter.typeD.requiredTime,
                                       objAfter.typeD.hasLectureship))) {
      toChange map (dozent => DozentMongo.update(("name" -> dozent.name.value) ~
                                                 ("reasonSelfManagement" -> dozent.reasonSelfManagement.value) ~
                                                 ("timeSelfManagement" -> dozent.timeSelfManagement.value) ~
                                                 ("typeD" -> ("name" -> dozent.typeD.value.name) ~
                                                             ("requiredTime" -> dozent.typeD.value.requiredTime) ~
                                                             ("hasLectureship" -> dozent.typeD.value.hasLectureship)),
                                                 ("name" -> objAfter.name) ~
                                                 ("reasonSelfManagement" -> objAfter.reasonSelfManagement) ~
                                                 ("timeSelfManagement" -> objAfter.timeSelfManagement) ~
                                                 ("typeD" -> ("name" -> objAfter.typeD.name) ~
                                                             ("requiredTime" -> objAfter.typeD.requiredTime) ~
                                                             ("hasLectureship" -> objAfter.typeD.hasLectureship)
                                                 )
                                                )
      )
    }
    else {
      error("Could not update dozent, because type of dozent is unknown!")
    }
  }
}