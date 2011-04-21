package org.unsane.spirit.planningweb.persistence.impl

/**
 * This class is a implementation of a IPersistence interface
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb.persistence.IPersistence
import org.unsane.spirit.planningweb.transform.ITransform

class Persistence (trf: ITransform) extends IPersistence {
  private val transform = trf

  // to read objects from persistence
  def read : List[AnyRef] = {
    transform.load
  }

  // to create objects persistently
  def create(obj: AnyRef) {
    transform.store(obj)
  }

  // to delete objects from persistence
  def delete(obj: AnyRef) {
    transform.del(obj)
  }

  // to update objects in persistence
  def update(before: AnyRef, after: AnyRef) {
    transform.change(before, after)
  }
}