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


class LecturesCreate extends LecturesCreateHelper with LecturesCreateName
                                                  with LecturesCreateCourses
                                                  with LecturesCreateSemesters
                                                  with LecturesCreateDozents
                                                  with LecturesCreateHoures
                                                  with LecturesCreateSave {
 /**
  * we have to call this function, because we need a differentiation between
  * a update process and a normal create process
  */
  checkWhereWasTheUserBefore(true)

  /**
  * this function represents the different screens to create a lecture
  */
  def create() = {
    Status.is match {
      case LecturesCreateHelper.InitialStatus => addName
      case LecturesCreateHelper.AddedName => addCourses
      case LecturesCreateHelper.AddedCourse => addSemesters
      case LecturesCreateHelper.AddedSemester => addDozents
      case LecturesCreateHelper.AddedDozent => addHoures
      case LecturesCreateHelper.AddedHoure => saveLecture
    }
  }

  def render = {
    "#create *" #> create
  }
}