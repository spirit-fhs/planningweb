package org.unsane.spirit.planningweb.persistence

import impl.Persistence
import org.unsane.spirit.planningweb.transform.ITransform

/**
 * This object is the representation of the PersistenceFactory
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

object PersistenceFactory {

  /**
   * creates a new Persistence object
   *
   * @param transform is a transform object which is a implementation of ITransform interface
   * @return new Persistence Object
   */

  def createPersistence (transform: ITransform) : IPersistence = {
    new Persistence(transform)
  }
}