package org.unsane.spirit.planningweb.dozentmanagement.impl

/**
 * This class is the representation of a dozenttype object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

import org.unsane.spirit.planningweb.model
import model.DozentTypeMongo

case class DozentType (name: String, requiredTime: Int, hasLectureship: Boolean)
