package org.unsane.spirit.planningweb.lecturemanagement.impl

import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.Dozent

/**
 * This class is the representation of a dozentinformation object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

case class DozentInformation (dozent: Dozent, giveLecture: Boolean, giveTutorial: Boolean)