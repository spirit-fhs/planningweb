package org.unsane.spirit.planningweb.transform

/**
 * This interface decouple the Oject-World form the DB-World
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

trait ITransform {

  /**
   * stores a object in persistence
   *
   * @param obj is a oject of type AnyRef
   */

  def store (obj: AnyRef)

  /**
   * loads a list of objects from persistence
   *
   * @return a list of objects of type AnyRef
   */

  def load : List[AnyRef]

  /**
   * deletes a object from persistence
   *
   * @param obj is a oject of type AnyRef
   */

  def del (obj: AnyRef)

  /**
   * changes a object in persistence
   *
   * @param before is the obejct to change
   * @param after is representation of the object after change
   */

  def change (before: AnyRef, after: AnyRef)
}