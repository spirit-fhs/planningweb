package org.unsane.spirit.planningweb.persistence

/**
 * This object is the representation of the PersistenceFactory
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import impl.Persistence
import org.unsane.spirit.planningweb.transform.ITransform

object PersistenceFactory {

  /**
   * Create a new Persistence object
   *
   * @param transform is a transform object which is a implementation of ITransform interface
   * @return new Persistence Object
   */

  def createPersistence (transform: ITransform) : IPersistence = {
    new Persistence(transform)
  }
}