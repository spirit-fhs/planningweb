package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This class is the representation of the view to manage the Lectures
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml._
import net.liftweb.util.Props
import scala.collection.mutable.Set
import org.unsane.spirit.planningweb
import planningweb.transform._
import planningweb.persistence._
import planningweb.lecturemanagement.impl._
import planningweb.coursemanagement.impl.Course
import planningweb.dozentmanagement.impl.Dozent


class LecturesCreate extends LecturesCreateNavigator {
 /**
  * we have to call this function, because we need a differentiation between
  * a update process and a normal create process
  */
  checkWhereWasTheUserBefore(true)

  /**
  * this function represents the different screens to create a lecture
  */
  def create() = {
    navigation
  }

  def render = {
    "#create *" #> create
  }
}