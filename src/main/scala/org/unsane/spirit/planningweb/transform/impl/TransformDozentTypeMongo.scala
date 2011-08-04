package org.unsane.spirit.planningweb.transform.impl

import org.unsane.spirit.planningweb._
import transform.ITransform
import model.{DozentTypeMongo, DozentMongo}
import dozentmanagement.impl.DozentType
import net.liftweb.json.JsonDSL._
import net.liftweb.http.S

/**
 * This class is the implementation of ITransform interface for a DozentType object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class TransformDozentTypeMongo extends ITransform {

  /**
   * stores a DozentType object in MongoDB
   *
   * @param obj is a oject of type DozentType
   */
  def store (obj: AnyRef) {
    val dozentType = obj.asInstanceOf[DozentType]
    DozentTypeMongo.createRecord.name(dozentType.name)
                                .requiredTime(dozentType.requiredTime)
                                .hasLectureship(dozentType.hasLectureship).save
  }

  /**
   * loads a list of DozentType objects from MongoDB
   *
   * @return a list of objects of type DozentType
   */
  def load : List[DozentType] = {
    val fromDB = DozentTypeMongo.findAll
    if (fromDB.isEmpty) {
      List()
    }
    else {
      fromDB map (typeD => DozentType(typeD.name.value,
                                      typeD.requiredTime.value,
                                      typeD.hasLectureship.value))
    }
  }

  /**
   * deletes a DozentType object from MongoDB
   *
   * @param obj is a oject of type DozentType
   */
  def del (obj: AnyRef) {
    val objToDel = obj.asInstanceOf[DozentType]
    val toDelete = DozentTypeMongo.findAll(("name" -> objToDel.name) ~
                                           ("requiredTime" -> objToDel.requiredTime) ~
                                           ("hasLectureship" -> objToDel.hasLectureship))

    val dozents = DozentMongo.findAll(("typeD" -> ("name" -> objToDel.name) ~
                                                  ("requiredTime" -> objToDel.requiredTime) ~
                                                  ("hasLectureship" -> objToDel.hasLectureship)
                                      )
                                     )

    dozents match {
      case List() =>
        toDelete map (typeD => if (!typeD.delete_!) error("Could not delete: " + typeD.toString))
      case _ =>
        S.warning("Could not delete, because typ is already in use!")
    }
  }

  /**
   * changes a DozentType object in MongoDB
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */
  def change (before: AnyRef, after: AnyRef) {
    val objToChange = before.asInstanceOf[DozentType]
    val objAfter = after.asInstanceOf[DozentType]
    val toChange = DozentTypeMongo.findAll(("name" -> objToChange.name) ~
                                           ("requiredTime" -> objToChange.requiredTime) ~
                                           ("hasLectureship" -> objToChange.hasLectureship))

    toChange map (typeD => DozentTypeMongo.update(("name" -> typeD.name.value) ~
                                                  ("requiredTime" -> typeD.requiredTime.value) ~
                                                  ("hasLectureship" -> typeD.hasLectureship.value),
                                                  ("name" -> objAfter.name) ~
                                                  ("requiredTime" -> objAfter.requiredTime) ~
                                                  ("hasLectureship" -> objAfter.hasLectureship)))
  }
}