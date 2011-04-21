package org.unsane.spirit.planningweb.dozentmanagement.impl

/**
 * This class is the representation of a dozenttype object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb.model
import model.DozentTypeMongo

case class DozentType (name: String, requiredTime: Int, hasLectureship: Boolean)
