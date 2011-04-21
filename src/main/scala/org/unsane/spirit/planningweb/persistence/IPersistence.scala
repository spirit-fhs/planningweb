package org.unsane.spirit.planningweb.persistence

/**
 * This triat is the representation of the IPersistence interface
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */


trait IPersistence {
  def read : List[AnyRef]
  def create(obj: AnyRef)
  def delete(obj: AnyRef)
  def update(before: AnyRef, after: AnyRef)
}