package org.unsane.spirit.planningweb.persistence

/**
 * This triat is the representation of the IPersistence interface
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

trait IPersistence {
  def read : List[AnyRef]
  def create(obj: AnyRef)
  def delete(obj: AnyRef)
  def update(before: AnyRef, after: AnyRef)
}